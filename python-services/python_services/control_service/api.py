import json
import logging
from typing import Optional

from bson import json_util
from fastapi import APIRouter

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
