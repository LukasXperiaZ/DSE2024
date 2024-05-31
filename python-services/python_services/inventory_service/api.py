import logging
from typing import List

from fastapi import APIRouter, Depends
import pymongo

from python_services.common.models import RegisterCar
from python_services.common.config import MONGO_URI

logger = logging.getLogger(__name__)

router = APIRouter(tags=["inventory"], prefix="/inventory")


client = pymongo.MongoClient(MONGO_URI)
db = client["inventory"]
cars = db["cars"]


@router.get("/health")
async def health():
    return {"status": "ok"}

@router.post("/register")
async def register(register_request: RegisterCar) -> RegisterCar:
    logger.info(f"Registering car: {register_request}")
    car = register_request.dict()
    car["_id"] = car["vin"]

    existing_car = cars.find_one({"_id": car["_id"]})
    if existing_car:
        cars.update_one({"_id": car["_id"]}, {"$set": car})
    else:
        cars.insert_one(car)

    return register_request

@router.get("/cars")
async def list_cars() -> List[RegisterCar]:
    logger.info("Listing cars")
    return [RegisterCar(**car) for car in cars.find({})]

@router.get("/car/{vin}")
async def get_car(vin: str) -> RegisterCar:
    logger.info(f"Getting car with vin: {vin}")
    car = cars.find_one({"_id": vin})
    if not car:
        return None

    return RegisterCar(**car)

@router.delete("/delete/{vin}")
async def delete_car(vin: str):
    logger.info(f"Deleting car with vin: {vin}")
    cars.delete_one({"_id": vin})

