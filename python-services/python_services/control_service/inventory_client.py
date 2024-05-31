import logging

import requests

from python_services.common.config import INVENTORY_SERVICE_URL
from python_services.common.exception.ClientException import ClientException
from python_services.common.models import CarBase

logger = logging.getLogger(__name__)

def get_car_base_data(vin):
    """
    Get the base data for a given vin
    :param vin: vin of the car
    :return: basedata
    """
    url = f"{INVENTORY_SERVICE_URL}/inventory/car/{vin}"

    # Make the GET request
    response = requests.get(url)

    # Check if the request was successful
    if response.status_code == 200:
        # Parse the JSON response into a CarsInReach object
        data = response.json()
        car = CarBase(**data)
        logger.debug(f"car data: {car}")
        return car
    else:
        raise ClientException(f"Request to {url} failed with status code {response.status_code}")