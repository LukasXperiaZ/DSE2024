import datetime
import json
import logging
import threading
import time

import schedule

from python_services.common.config import FOLLOWME_END_TIME, FOLLOWME_SPEED_WARN_TOLERANCE, FOLLOWME_SPEED_END_TOLERANCE
from python_services.control_service import database
from python_services.control_service.beachcomb_client import get_cars_in_reach, get_vehicle_data
from python_services.control_service.inventory_client import get_car_base_data
from python_services.control_service.rabbitmq import channel

logger = logging.getLogger(__name__)


def run_continuously(interval=1):
    """
    Run a function continuously in a separate thread.
    :param interval: interval in seconds that is used to check whether jobs should be run
    """
    cease_continuous_run = threading.Event()

    class ScheduleThread(threading.Thread):
        @classmethod
        def run(cls):
            while not cease_continuous_run.is_set():
                schedule.run_pending()
                time.sleep(interval)

    continuous_thread = ScheduleThread()
    continuous_thread.start()
    return cease_continuous_run


def check_nearing_cars():
    """
    Check if there are cars in reach and start a follow me session if possible.
    """
    try:
        in_reach = get_cars_in_reach()
    except Exception as e:
        logger.warning(f"Could not get cars in reach: {e}")
        return

    # check all candidate cars
    for car, near_cars in in_reach.cars.items():
        car_data = get_car_base_data(car)
        if car_data is None or car_data.is_self_driving is False:
            logger.info(f"Car {car} is not a self driving car. Skipping it.")
            continue

        # check if leading car is already i an active follow me session
        if database.state.find_one({"lv": car}) is not None or database.state.find_one({"fv": car}) is not None:
            logger.info(f"Car {car} is already in use. Skipping it as leading vehicle.")
            continue

        # check if following car is already in an active follow me session
        fv = None
        for near_car in near_cars:
            if database.state.find_one({"lv": near_car}) is not None or database.state.find_one(
                    {"fv": near_car}) is not None:
                logger.info(f"Car {car} is already in use. Skipping it as following vehicle.")
                continue
            fv = near_car
            break
        if fv is None:
            logger.info(f"No available following vehicle for car {car}.")
            continue

        # found pair of cars that is available to start a follow me session
        # start a new follow me session
        logger.info(f"Starting follow me session between {car} and {fv}")
        try:
            lv_data = get_vehicle_data(car)
        except Exception as e:
            logger.warning(f"Could not get vehicle data for {car}: {e}")
            continue

        # store the follow me session in the database
        database.state.insert_one({"lv": car,
                                   "fv": fv,
                                   "followme_start": datetime.datetime.now(),
                                   "target_lane": lv_data.targetLane,
                                   "target_speed": lv_data.targetSpeed,
                                   "successive_check_fails": 0
                                   })

        # send info to the cars
        channel.basic_publish(exchange='control',
                              routing_key=car,
                              body=json.dumps({"isLeadingVehicle": True,
                                               "vinFV": fv,
                                               }))

        channel.basic_publish(exchange='control',
                              routing_key=fv,
                              body=json.dumps({
                                  "usesFM": True,
                                  "vinLV": car,
                                  "targetLane": lv_data.targetLane,
                                  "targetSpeed": lv_data.targetSpeed
                              }))

        # store event in eventlog
        database.eventlog.insert_one({
            "type": "followme_start",
            "timestamp": datetime.datetime.now(),
            "lv": car,
            "fv": fv
        })


def check_end_followme():
    """
    check for all follow me sessions if they should be ended due to timeout
    """
    for state in database.state.find():
        if state["followme_start"] + datetime.timedelta(seconds=FOLLOWME_END_TIME) < datetime.datetime.now():
            # end follow me session
            end_followme(state["lv"], state["fv"], state["_id"])


def end_followme(lv, fv, state_id):
    """
    End a follow me session between two cars.
    :param lv: vin of the leading vehicle
    :param fv: vin of the following vehicle
    :param state_id: id of the state in the database
    """
    logger.info(f"Ending follow me session between {lv} and {fv}")

    # send info to the cars
    channel.basic_publish(exchange='control',
                          routing_key=lv,
                          body=json.dumps({"isLeadingVehicle": False,
                                           "vinFV": None,
                                           }))
    channel.basic_publish(exchange='control',
                          routing_key=fv,
                          body=json.dumps({
                              "usesFM": False,
                              "vinLV": None,
                              "targetLane": None,
                              "targetSpeed": None
                          }))

    # store event in eventlog
    database.eventlog.insert_one({
        "type": "followme_end",
        "timestamp": datetime.datetime.now(),
        "lv": lv,
        "fv": fv
    })
    database.state.delete_one({"_id": state_id})


def check_followme_speeds():
    """
    Check if the speed and lane of the following car are within tolerance.
    :return:
    """

    # check for all current follow me sessions
    for state in database.state.find():
        try:
            lv_data = get_vehicle_data(state["lv"])
            fv_data = get_vehicle_data(state["fv"])
        except Exception as e:
            logger.warning(f"Could not get vehicle data for {state['lv']} or {state['fv']}: {e}")
            continue

        target_speed = lv_data.targetSpeed
        target_lane = lv_data.targetLane
        speed = fv_data.speed
        lane = fv_data.lane
        # check if a warning should be issued
        message = ""
        if ((speed < target_speed - FOLLOWME_SPEED_WARN_TOLERANCE
            or speed > target_speed + FOLLOWME_SPEED_WARN_TOLERANCE)
                and lane != target_lane):
            logger.info(f"Speed and lane of {fv_data.vin} are not within tolerance.")
            message = message + ("Total Missmatch")
        else:
            if speed < target_speed - FOLLOWME_SPEED_WARN_TOLERANCE or speed > target_speed + FOLLOWME_SPEED_WARN_TOLERANCE:
                logger.info(f"Speed of {fv_data.vin} is not within tolerance.")
                message = message + ("Speed Missmatch")
            if lane != target_lane:
                logger.info(f"Lane of {fv_data.vin} is not the same as the target lane.")
                message = message + ("Lane Missmatch")

        # check if the follow me session should be ended due to big mismatch
        if speed < target_speed - FOLLOWME_SPEED_END_TOLERANCE or speed > target_speed + FOLLOWME_SPEED_END_TOLERANCE:
            message = "Speed difference too high. Ending follow me session."
            end_followme(state["lv"], state["fv"], state["_id"])

        # issue a warning to eventlog
        if message != "":
            state["successive_check_fails"] += 1
            database.eventlog.insert_one({
                "type": "followme_check_fail",
                "timestamp": datetime.datetime.now(),
                "lv": state["lv"],
                "fv": state["fv"],
                "message": message,
                "successive_check_fails": state["successive_check_fails"],
                "speed_mismatch": abs(speed - target_speed),
                "lane_mismatch": f"{lane} != {target_lane}" if lane != target_lane else "OK"
            })
            database.state.update_one({"_id": state["_id"]},
                                      {"$set": {"successive_check_fails": state["successive_check_fails"]}})
        else:
            database.state.update_one({"_id": state["_id"]}, {"$set": {"successive_check_fails": 0}})

# schedule the jobs
schedule.every(5).seconds.do(check_nearing_cars)
schedule.every(5).seconds.do(check_end_followme)
schedule.every(5).seconds.do(check_followme_speeds)
