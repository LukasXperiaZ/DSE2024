from pymongo import MongoClient

from python_services.common.config import MONGO_URI

client = MongoClient(MONGO_URI)
# databse used by the control service
db = client["control"]
# state collection for current follow me state
state = db["state"]
# eventlog collection for logging events
eventlog = db["eventlog"]

