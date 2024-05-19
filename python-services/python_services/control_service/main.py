import logging
from contextlib import asynccontextmanager

import uvicorn
from fastapi import FastAPI

from python_services.common.log_config import LOGGING_CONFIG
from python_services.control_service import api
from python_services.control_service.jobs import run_continuously
from python_services.control_service.rabbitmq import consumer

logger = logging.getLogger(__name__)
logging.config.dictConfig(LOGGING_CONFIG)

@asynccontextmanager
async def lifespan(app: FastAPI):
    stop_run_continuously = run_continuously()
    consumer.start()
    yield
    stop_run_continuously.set()
    consumer.stop()

app = FastAPI(lifespan=lifespan)
app.include_router(api.router)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8002, log_config=LOGGING_CONFIG)
