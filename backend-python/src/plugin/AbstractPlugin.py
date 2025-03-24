from abc import abstractmethod


class AbstractPlugin:

    @abstractmethod
    def run(self, param) -> (str, list):
        pass


