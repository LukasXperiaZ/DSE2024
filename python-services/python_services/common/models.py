from pydantic import BaseModel


class CarBase(BaseModel):
    oem: str
    model: str
    vin: str
    is_self_driving: bool = False


class RegisterCar(CarBase):
    pass