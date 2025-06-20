import logging

import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from python_services.common.log_config import LOGGING_CONFIG
from python_services.inventory_service import api


logger = logging.getLogger(__name__)
logging.config.dictConfig(LOGGING_CONFIG)

app = FastAPI()
app.include_router(api.router)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001, log_config=LOGGING_CONFIG)
