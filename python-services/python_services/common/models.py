import datetime
from typing import List, Dict, Optional

from pydantic import BaseModel


class CarBase(BaseModel):
    oem: str
    model: str
    vin: str
    is_self_driving: bool = False


class RegisterCar(CarBase):
    pass

class VehicleData(BaseModel):
    vin: str
    coordinates: dict
    speed: float
    lane: int
    timestamp: Optional[datetime.datetime]
    targetSpeed: Optional[float]
    targetLane: Optional[int]


class CarsInReach(BaseModel):
    # key is the car's vin, value is a list of other cars' vins that are in reach
    # e.g.:
    # {
    #     'vin1': ['vin2', 'vin3'],
    #     'vin4': ['vin5'],
    # }
    cars: Dict[str, List[str]]