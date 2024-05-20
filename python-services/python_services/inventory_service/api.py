import logging
from typing import List

from fastapi import APIRouter, Depends
from pymongo import MongoClient
from pymongo.client_session import ClientSession

from python_services.common.models import RegisterCar
from python_services.common.config import MONGO_URI

logger = logging.getLogger(__name__)

router = APIRouter(tags=["inventory"], prefix="/inventory")


client = MongoClient(MONGO_URI)
db = client["inventory"]
cars = db["cars"]


def get_session():
    with client.start_session() as session:
        yield session


@router.get("/health")
async def health():
    return {"status": "ok"}

@router.post("/register")
async def register(register_request: RegisterCar, session: ClientSession = Depends(get_session)) -> RegisterCar:
    logger.info(f"Registering car: {register_request}")
    car = register_request.dict()
    car["_id"] = car["vin"]

    existing_car = cars.find_one({"_id": car["_id"]}, session=session)
    if existing_car:
        cars.update_one({"_id": car["_id"]}, {"$set": car}, session=session)
    else:
        cars.insert_one(car, session=session)

    return register_request

@router.get("/cars")
async def list_cars(session: ClientSession = Depends(get_session)) -> List[RegisterCar]:
    logger.info("Listing cars")
    return [RegisterCar(**car) for car in cars.find({}, session=session)]

@router.get("/car/{vin}")
async def get_car(vin: str, session: ClientSession = Depends(get_session)) -> RegisterCar:
    logger.info(f"Getting car with vin: {vin}")
    car = cars.find_one({"_id": vin}, session=session)
    if not car:
        return None

    return RegisterCar(**car)

@router.delete("/delete/{vin}")
async def delete_car(vin: str, session: ClientSession = Depends(get_session)):
    logger.info(f"Deleting car with vin: {vin}")
    cars.delete_one({"_id": vin}, session=session)

