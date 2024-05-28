from unittest.mock import patch

import pytest
from fastapi.testclient import TestClient
from pymongo_inmemory import MongoClient

from python_services.inventory_service.main import app


class TestInventoryModule:

    # fixture that sets up the testclient
    @pytest.fixture
    def client_fixture(self):
        with TestClient(app) as test_client:
            yield test_client


    @pytest.fixture
    def mongo_fixture(self):
        # create a mongomock client
        mongo_client = MongoClient()
        # clear the mongodb
        mongo_client.inventory.cars.delete_many({})
        yield mongo_client.inventory.cars

        del mongo_client


    def test_health(self, client_fixture):
        """
        Test the health endpoint
        :param client_fixture:
        :return:
        """
        response = client_fixture.get("/inventory/health")
        assert response.status_code == 200
        assert response.json() == {"status": "ok"}


    def test_register_car(self, client_fixture, mongo_fixture):
        """
        Test the register endpoint
        :param client_fixture:
        :return:
        """

        with patch("python_services.inventory_service.api.cars", mongo_fixture):
            response = client_fixture.post("/inventory/register", json={"vin": "1234", "oem": "Toyota", "model": "Camry", "is_self_driving": True})
            assert response.status_code == 200
            assert response.json() == {"vin": "1234", "oem": "Toyota", "model": "Camry", "is_self_driving": True}
            # check if the car was added to the database
            response = client_fixture.get("/inventory/cars")
            assert response.status_code == 200
            assert len(response.json()) == 1
            assert response.json()[0] == {"vin": "1234", "oem": "Toyota", "model": "Camry", "is_self_driving": True}


    def test_remove_car(self, client_fixture, mongo_fixture):
        """
        Test the remove car endpoint
        :param client_fixture:
        :return:
        """
        with patch("python_services.inventory_service.api.cars", mongo_fixture):
            response = client_fixture.post("/inventory/register", json={"vin": "1234", "oem": "Toyota", "model": "Camry", "is_self_driving": True})
            assert response.status_code == 200
            response = client_fixture.delete("/inventory/delete/1234")
            assert response.status_code == 200
            response = client_fixture.get("/inventory/cars")
            assert response.status_code == 200
            assert len(response.json()) == 0
            assert response.json() == []


    def test_get_car_by_id(self, client_fixture, mongo_fixture):
        """
        Test the get car by id endpoint
        :param client_fixture:
        :return:
        """
        with patch("python_services.inventory_service.api.cars", mongo_fixture):
            response = client_fixture.post("/inventory/register", json={"vin": "1234", "oem": "Toyota", "model": "Camry", "is_self_driving": True})
            assert response.status_code == 200
            response = client_fixture.get("/inventory/car/1234")
            assert response.status_code == 200
            assert response.json() == {"vin": "1234", "oem": "Toyota", "model": "Camry", "is_self_driving": True}




