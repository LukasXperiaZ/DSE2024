import logging

import requests

from python_services.common.config import BEACHCOMB_SERVICE_URL
from python_services.common.exception.ClientException import ClientException
from python_services.common.models import CarsInReach, VehicleData

logger = logging.getLogger(__name__)

def get_cars_in_reach():
    """
    Get the cars in reach.
    This are the potential matches for starting a follow me session.
    :return: CarsInReach object
    """
    # get request to beachcomb service /beachcomb/follow_me
    # Format the URL
    url = f"{BEACHCOMB_SERVICE_URL}/beachcomb/follow_me"

    # Make the GET request
    response = requests.get(url)

    # Check if the request was successful
    if response.status_code == 200:
        # Parse the JSON response into a CarsInReach object
        data = response.json()
        cars_in_reach = CarsInReach(cars=data)
        logger.info(f"cars in reach: {cars_in_reach}")
        return cars_in_reach
    else:
        raise ClientException(f"Request to {url} failed with status code {response.status_code}")

def get_vehicle_data(vin: str) -> VehicleData:
    """
    get the vehicle data for a given vin
    :param vin: vin of the vehicle
    :return: data of the car
    """
    # get request to beachcomb service /beachcomb/vehicles/{vin}
    # Format the URL with the vin
    url = f"{BEACHCOMB_SERVICE_URL}/beachcomb/vehicles/{vin}"

    # Make the GET request
    response = requests.get(url)

    # Check if the request was successful
    if response.status_code == 200:
        # Parse the JSON response into a VehicleData object
        data = response.json()
        logger.info(f"Vehicle data for {vin}: {data}")
        return VehicleData(**data)
    else:
        raise ClientException(f"Request to {url} failed with status code {response.status_code}")

