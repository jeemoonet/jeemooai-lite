class StopSendMessage(Exception):
    def __init__(self, message):
        self.message = message
        super().__init__(self.message)


class ServiceMessage(Exception):
    def __init__(self, message):
        self.message = message
        super().__init__(self.message)
