from src.llms.CustomOneApi import CustomOneApi
from src.llms.ErrorLLM import ErrorLLM


class Factory:
    @staticmethod
    def instance(record, param, log, platform, is_app, prompt=None):
        if platform == "oneapi":
            return CustomOneApi(record, log, param, is_app)
        else:
            return ErrorLLM(record, log, param)