import json
import logging
from typing import Optional

from bson import json_util
from fastapi import APIRouter

from python_services.control_service import database
from python_services.control_service.database import eventlog

logger = logging.getLogger(__name__)

router = APIRouter(tags=["control"], prefix="/control")


@router.get("/eventlog")
async def get_eventlog(max_length: Optional[int] = None):
    logger.info("Listing eventlog")
    if max_length is None:
        max_length = 100000
    events = [event for event in eventlog.find({}, limit=max_length).sort("_id", -1)]
    return json.loads(json_util.dumps(events))

@router.get("/follow_me_status")
async def get_follow_me_status():
    follow_me_vehicles = []
    for state in database.state.find():
        follow_me_vehicles.append({
            "lv": state["lv"],
            "fv": state["fv"],
            "start_time": state["followme_start"],
        })
    return follow_me_vehicles

@router.get("/health")
async def health():
    return {"status": "ok"}
