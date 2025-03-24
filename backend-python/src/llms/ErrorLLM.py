from src.llms.AbstractLLM import AbstractLLM


class ErrorLLM(AbstractLLM):

    def __init__(self, record, log, param):
        super().__init__(record, log, param)
        self.error_msg = '模型错误'

    def _chat(self):
        self.exception = Exception(self.error_msg)
        return self.error_msg

    def _chat_stream(self):
        self.exception = Exception(self.error_msg)
        yield self.error_msg
        self.finish_label = 'stop'

    def set_error_msg(self, error_msg):
        self.error_msg = error_msg