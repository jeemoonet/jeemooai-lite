import json

from openai import OpenAI
from src.client.mysql import get_models_config
from src.plugin.BingSearchPlugin import BingSearchPlugin


class Factory:

    def __init__(self):
        config = get_models_config()
        self.client = OpenAI(
            api_key=config['openai']['key'],
            base_url=config['openai']['url']
        )

    def run_plugin(self, function_name, arguments, function_configs):
        config = function_configs[function_name]
        class_name = config['class_name']
        if class_name == 'com.jeemoo.plugin.BingSearch':
            instance = BingSearchPlugin()
        else:
            instance = None

        if instance:
            return instance.run(arguments)
        else:
            return ''
