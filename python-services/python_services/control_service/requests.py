import datetime
import random

from python_services.common.models import CarsInReach, VehicleData


def get_cars_in_reach():
    # TODO: add interface to beachcomb service
    return CarsInReach(cars={"vin1": ["vin2", "vin3"], "vin4": ["vin5"]})

def get_vehicle_data(vin: str) -> VehicleData:
    # TODO: add interface to beachcomb service
    if vin == "vin2" or vin == "vin5":
        chance = random.randint(0, 4)
        if chance == 0:
            return VehicleData(**{
                "vin": vin,
                "coordinates": [0.0, 0.0],
                "speed": 40.0,
                "lane": 2,
                "timestamp": datetime.datetime.now() - datetime.timedelta(seconds=10),
                "target_speed": 50.0,
                "target_lane": 1,
            })

    return VehicleData(**{
        "vin": vin,
        "coordinates": [0.0, 0.0],
        "speed": 50.0,
        "lane": 1,
        "timestamp": datetime.datetime.now() - datetime.timedelta(seconds=10),
        "target_speed": 50.0,
        "target_lane": 1,
    })

