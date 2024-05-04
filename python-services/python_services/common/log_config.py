LOGGING_CONFIG = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "colored": {
            "()": "colorlog.ColoredFormatter",
            "format": "%(asctime)s - %(log_color)s%(levelname)s%(reset)s - %(name)s - %(funcName)s:%(lineno)d - %(message)s",  # pylint: disable=line-too-long
            "log_colors": {
                "DEBUG": "cyan",
                "INFO": "green",
                "WARNING": "yellow",
                "ERROR": "red",
                "CRITICAL": "red,bg_white",
            },
        },
    },
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "level": "DEBUG",
            "formatter": "colored",  # Use the colored formatter
        },
    },
    "root": {
        "level": "DEBUG",
        "handlers": ["console"],
    },
}
