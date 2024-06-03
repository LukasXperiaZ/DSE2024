import datetime
from typing import List, Dict, Optional

from pydantic import BaseModel


class CarBase(BaseModel):
    """
    Base class for car data
    """
    oem: str
    model: str
    vin: str
    is_self_driving: bool = False


class RegisterCar(CarBase):
    """
    Class for registering a car
    """
    pass

class VehicleData(BaseModel):
    """
    Class for vehicle data
    """
    vin: str
    coordinates: dict
    speed: float
    lane: int
    targetSpeed: Optional[float]
    targetLane: Optional[int]
    timestamp: Optional[datetime.datetime] = None


class CarsInReach(BaseModel):
    """
    Class for cars in reach
    return value of the beachcomb services get_cars_in_reach method
    """
    # key is the car's vin, value is a list of other cars' vins that are in reach
    # e.g.:
    # {
    #     'vin1': ['vin2', 'vin3'],
    #     'vin4': ['vin5'],
    # }
    cars: Dict[str, List[str]]