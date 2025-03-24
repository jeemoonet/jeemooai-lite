import json
import logging
import sys
import os

from openai import OpenAI
from flask import Flask, Response, request
from src import chat_stream
from src.chat_stream import model_id_to_label
from src.client.mysql import get_company, get_prompt, get_model, get_models_config, get_models
from src.llms.FactoryLLM import Factory
from src.plugin.FactoryPlugin import Factory as FactoryPlugin
from src.utils import stream_utils, token_utils
from src.client import activemq, mysql
from src.entity.entity import ai_request_log
from src.client.redis_client import getRedisClient

app = Flask(__name__)

# 设置日志记录级别
app.logger.setLevel(logging.DEBUG)

# 创建文件处理器
os.makedirs('flask_logs', exist_ok=True)
file_handler = logging.FileHandler('flask_logs/app.log')
file_handler.setLevel(logging.DEBUG)

# 创建格式化器
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

# 添加文件处理器到日志记录器
app.logger.addHandler(file_handler)


@app.get('/api/py/test/stream')
def models_test_stream():
    app.logger.debug('receive stream request')
    authorization = request.headers.get("Authorization")
    isAudio = request.args.get("isAudio")
    windowId = request.args.get("windowId")
    if authorization is None:
        authorization = request.args.get("Authorization")

    loginData = token_utils.hasAuth(authorization)
    if not loginData:
        return Response(stream_utils.fail_event_stream("token验证失败"), mimetype="text/event-stream")

    app.logger.debug('starting get redis data')
    r = getRedisClient()
    redis_value = r.get(loginData['loginId'] + '_' + windowId)
    if redis_value is None:
        return Response(stream_utils.fail_event_stream("redis数据为空"), mimetype="text/event-stream")

    redis_data = json.loads(json.loads(redis_value.decode('utf-8')))
    record = redis_data['record']
    param = redis_data['requestBody']
    model = param.get('model')
    if model.isdigit():
        model = model_id_to_label(model)
        param['model'] = model
    platform = chat_stream.switch_stream(param.get('model'))

    log = ai_request_log()
    log.userId = loginData['userId']
    log.promptId = record.get('promptId')
    log.model = param.get('model')
    log.sendMessage = param
    log.question = record.get('content')
    log.companyId = record.get('companyId')
    log.ip = request.headers.get('X-Real-IP')
    log.userAgent = request.headers.get('User-Agent')
    activemq.send_request_log_data(log.to_dict())
    llm = Factory.instance(record, param, log, platform, 0)
    if isAudio:
        voice = redis_data.get('voice')
        return Response(llm.chat_stream_audio(voice), mimetype="text/event-stream")
    else:
        return Response(llm.chat_stream(), mimetype="text/event-stream")


@app.get('/api/chat/stream')
def stream():
    app.logger.debug('receive stream request')
    authorization = request.headers.get("Authorization")
    isAudio = request.args.get("isAudio")
    if authorization is None:
        authorization = request.args.get("Authorization")

    loginData = token_utils.hasAuth(authorization)
    if not loginData:
        return Response(stream_utils.fail_event_stream("token验证失败"), mimetype="text/event-stream")

    app.logger.debug('starting get redis data')
    r = getRedisClient()
    redis_value = r.get(loginData['loginId'])
    if redis_value is None:
        return Response(stream_utils.fail_event_stream("redis数据为空"), mimetype="text/event-stream")

    redis_data = json.loads(json.loads(redis_value.decode('utf-8')))
    record = redis_data['record']
    param = redis_data['requestBody']
    window = redis_data['window']
    app.logger.debug(window)
    model = param.get('model')
    if model.isdigit():
        model = model_id_to_label(model)
        param['model'] = model
    platform = chat_stream.switch_stream(param.get('model'))
    prompt = None
    if window:
        prompt = get_prompt(window.get('promptId'))

    app.logger.debug(param)
    log = ai_request_log()
    log.userId = loginData['userId']
    log.promptId = record.get('promptId')
    log.model = param.get('model')
    log.sendMessage = param
    log.question = record.get('content')
    log.companyId = record.get('companyId')
    log.ip = request.headers.get('X-Real-IP')
    log.userAgent = request.headers.get('User-Agent')
    activemq.send_request_log_data(log.to_dict())
    llm = Factory.instance(record, param, log, platform, 0, prompt)
    if isAudio:
        voice = redis_data.get('voice')
        return Response(llm.chat_stream_audio(voice), mimetype="text/event-stream")
    else:
        return Response(llm.chat_stream(), mimetype="text/event-stream")


@app.post('/api/prompt/init')
def promptInit():
    authorization = request.headers.get("Authorization")
    if authorization is None:
        authorization = request.args.get("Authorization")

    loginData = token_utils.hasAuth(authorization)
    if not loginData:
        return {"code": 1, "msg": "token验证失败"}

    post_data = request.get_json()
    prompt = post_data.get("prompt")
    if not prompt:
        return {"code": 1, "msg": "角色设置不能为空"}

    promptTemplate = mysql.get_prompt_template()
    result = {}
    # result['promptDesc'] = promptTemplate['promptDescTemplate'].replace('${name}', promptName)
    # result['initMessage'] = promptTemplate['initMessageTemplate'].replace('${name}', promptName)

    model_config = get_models_config()
    client = OpenAI(base_url=model_config['oneapi']['url'], api_key=model_config['oneapi']['key'])
    models = get_models()
    model = None
    for key in models:
        if models[key]['is_default']:
            model = key

    response = client.chat.completions.create(
        model=model,
        messages=[
            {'role': 'system', 'content': promptTemplate['initPromptTemplate']},
            {'role': 'user', 'content': prompt}
        ],
        max_tokens=promptTemplate['max_tokens'],
        stream=False
    )

    content = response.choices[0].message.content
    result['initPrompt'] = content
    return {'code': 200, 'msg': '生成成功', 'data': result}


@app.get('/api/login/healthCheck')
def healthCheck():
    return 'success'


def log_exception(exc_type, exc_value, exc_traceback):
    # 创建日志记录器
    logger = logging.getLogger('global_exception_logger')
    logger.setLevel(logging.ERROR)

    # 创建文件处理器
    file_handler = logging.FileHandler('flask_logs/exceptions.log')
    file_handler.setLevel(logging.ERROR)

    # 创建格式化器
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
    file_handler.setFormatter(formatter)

    # 添加文件处理器到日志记录器
    logger.addHandler(file_handler)

    # 记录异常信息到日志
    logger.error("Uncaught exception", exc_info=(exc_type, exc_value, exc_traceback))


# 注册全局异常处理函数
sys.excepthook = log_exception

if __name__ == "__main__":
    app.run(debug=True)
