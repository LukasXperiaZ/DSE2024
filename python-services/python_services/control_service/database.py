from pymongo import MongoClient

from python_services.common.config import MONGO_URI

client = MongoClient(MONGO_URI)
db = client["control"]
state = db["state"]
eventlog = db["eventlog"]

