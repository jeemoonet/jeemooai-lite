from src.client.mysql import get_models
import logging

logger = logging.getLogger('app.chat_stream')

def switch_stream(model):
    logger.debug('starting get models data')
    models = get_models()
    if model not in models:
        return 'error'
    return models[model]['platform']


def switch_stream_id(modelId):
    models = get_models()
    for key in models:
        model = models[key]
        if str(modelId) == str(model['id']):
            return model['platform'], models


def model_id_to_label(modelId):
    models = get_models()
    for key in models:
        model = models[key]
        if str(modelId) == str(model['id']):
            return model['model_label']


def parse_request_param(request_body, out_prompt, messages, models):
    prompt = out_prompt
    for key in models:
        model = models[key]
        if str(model['id']) == str(prompt['model']):
            prompt['model'] = model['model_label']
            if 'max_tokens' in prompt and prompt['max_tokens'] and model['max_tokens'] <= prompt['max_tokens']:
                prompt['max_tokens'] = model['max_tokens']
            break
    request_body = openai_parser(request_body, prompt, messages)
    return request_body

def openai_parser(request_body, prompt, _messages):
    if 'model' in prompt and prompt['model']:
        request_body['model'] = prompt['model']

    if 'max_tokens' in prompt and prompt['max_tokens'] is not None:
        request_body['max_tokens'] = prompt['max_tokens']

    if 'temperature' in prompt and prompt['temperature'] is not None:
        request_body['temperature'] = prompt['temperature']

    messages = []
    for message in _messages:
        if message['content']:
            messages.append(message)
    request_body['messages'] = messages
    return request_body
