"""
collection of settings that can be set via environment variables
"""

import os

MONGO_URI = os.environ.get("MONGO_URI", "mongodb://root:example@localhost:27017")

RABBITMQ_HOST = os.environ.get("RABBITMQ_HOST", "localhost")
RABBITMQ_PORT = os.environ.get("RABBITMQ_PORT", 5672)
RABBITMQ_USER = os.environ.get("RABBITMQ_USER", "guest")
RABBITMQ_PASS = os.environ.get("RABBITMQ_PASS", "guest")

INVENTORY_SERVICE_URL = os.environ.get("INVENTORY_SERVICE_URL", "http://inventoryservice:8001")
CONTROL_SERVICE_URL = os.environ.get("CONTROL_SERVICE_URL", "http://controlservice:8002")
BEACHCOMB_SERVICE_URL = os.environ.get("BEACHCOMB_SERVICE_URL", "http://beachcombservice:8080")


FOLLOWME_END_TIME = os.environ.get("FOLLOWME_END_TIME", 180)
FOLLOWME_SPEED_WARN_TOLERANCE = os.environ.get("FOLLOWME_SPEED_TOLERANCE", 5)
FOLLOWME_SPEED_END_TOLERANCE = os.environ.get("FOLLOWME_SPEED_END_TOLERANCE", 12)