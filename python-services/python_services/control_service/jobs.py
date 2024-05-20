import logging
import threading
import time
import datetime

import schedule

from python_services.common.config import FOLLOWME_END_TIME
from python_services.control_service import database
from python_services.control_service.rabbitmq import channel
from python_services.control_service.requests import get_cars_in_reach, get_vehicle_data

logger = logging.getLogger(__name__)

def run_continuously(interval=1):
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
    in_reach = get_cars_in_reach()
    for car, near_cars in in_reach.cars.items():
        if database.state.find_one({"lv": car}) is not None or database.state.find_one({"fv": car}) is not None:
            logger.info(f"Car {car} is already in use. Skipping it as leading vehicle.")
            continue
        fv = None
        for near_car in near_cars:
            if database.state.find_one({"lv": near_car}) is not None or database.state.find_one({"fv": near_car}) is not None:
                logger.info(f"Car {car} is already in use. Skipping it as following vehicle.")
                continue
            fv = near_car
            break
        if fv is None:
            logger.info(f"No available following vehicle for car {car}.")
            continue
        # found pair of cars that is available
        # start a new follow me session
        logger.info(f"Starting follow me session between {car} and {fv}")
        lv_data = get_vehicle_data(car)

        database.state.insert_one({"lv": car,
                                   "fv": fv,
                                   "followme_start": datetime.datetime.now(),
                                   "target_lane": lv_data.target_lane,
                                    "target_speed": lv_data.target_speed,
                                   "successive_check_fails": 0
                                   })

        # send info to the cars
        channel.basic_publish(exchange='control',
                              routing_key=car,
                              body=str({"leadingVehicle": True,
                                    "vinFV": fv,
                                    }))

        channel.basic_publish(exchange='control',
                              routing_key=fv,
                              body=str({
                                  "usesFM": True,
                                    "vinLV": car,
                                    "targetLane": lv_data.target_lane,
                                    "targetSpeed": lv_data.target_speed
                              }))

        database.eventlog.insert_one({
            "type": "followme_start",
            "timestamp": datetime.datetime.now(),
            "lv": car,
            "fv": fv
        })



def check_end_followme():
    # check for all follow me sessions if they should be ended due to timeout
    for state in database.state.find():
        if state["followme_start"] + datetime.timedelta(seconds=FOLLOWME_END_TIME) < datetime.datetime.now():
            # end follow me session
            logger.info(f"Ending follow me session between {state['lv']} and {state['fv']}")

            channel.basic_publish(exchange='control',
                                  routing_key=state["lv"],
                                  body=str({"leadingVehicle": False,
                                        "vinFV": None,
                                        }))
            channel.basic_publish(exchange='control',
                                  routing_key=state["fv"],
                                  body=str({
                                      "usesFM": False,
                                        "vinLV": None,
                                        "targetLane": None,
                                        "targetSpeed": None
                                  }))

            database.eventlog.insert_one({
                "type": "followme_end",
                "timestamp": datetime.datetime.now(),
                "lv": state["lv"],
                "fv": state["fv"]
            })
            database.state.delete_one({"_id": state["_id"]})


def check_followme_speeds():
    for state in database.state.find():
        lv_data = get_vehicle_data(state["lv"])
        fv_data = get_vehicle_data(state["fv"])
        target_speed = lv_data.target_speed
        target_lane = lv_data.target_lane
        speed = fv_data.speed
        lane = fv_data.lane
        message = ""
        if (speed < target_speed - 5 or speed > target_speed + 5) and lane != target_lane:
            logger.info(f"Speed and lane of {fv_data.vin} are not within tolerance.")
            message = message + ("Total Missmatch")
        else:
            if speed < target_speed - 5 or speed > target_speed + 5:
                logger.info(f"Speed of {fv_data.vin} is not within tolerance.")
                message = message + ("Speed Missmatch")
            if lane != target_lane:
                logger.info(f"Lane of {fv_data.vin} is not the same as the target lane.")
                message = message + ("Lane Missmatch")
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
            database.state.update_one({"_id": state["_id"]}, {"$set": {"successive_check_fails": state["successive_check_fails"]}})
        else:
            database.state.update_one({"_id": state["_id"]}, {"$set": {"successive_check_fails": 0}})


schedule.every(5).seconds.do(check_nearing_cars)
schedule.every(5).seconds.do(check_end_followme)
schedule.every(5).seconds.do(check_followme_speeds)

