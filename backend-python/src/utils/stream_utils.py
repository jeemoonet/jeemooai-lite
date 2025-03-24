import json, time
from src.client import activemq


def send_finish_data(messageId, log, fulltext, timestamp, e=''):
    log.receiveMessage = fulltext
    log.wordCount = len(fulltext)
    log.requestTime = time.time() - timestamp
    log.errorMessage = str(e)
    activemq.send_request_log_data(log.to_dict())
    isError = 0
    if e:
        isError = 1
    activemq.send_record_data(
        {"messageId": messageId, "content": fulltext, "createTimestamp": time.time() * 1000, "isError": isError})


def app_send_finish_data(log, fulltext, timestamp, e=''):
    log.receiveMessage = fulltext
    log.wordCount = len(fulltext)
    log.requestTime = time.time() - timestamp
    log.errorMessage = str(e)
    activemq.send_app_request_log_data(log.to_dict())


def fail_event_stream(error):
    yield parse_fail_data(error)
    yield parse_stop_data()


def parse_event_data(message_id, model, content, master_uuid, doc_info):
    data = {
        "messageId": message_id,
        "model": None,
        "content": content,
        "finish": None,
        "type": "text",
        "masterUuid": master_uuid,
        "docInfo": doc_info,
        "error": ""
    }
    return 'event: message\ndata: {}\n\n'.format(json.dumps(data))


def parse_fail_data(content):
    data = {
        "messageId": None,
        "model": None,
        "content": "非常抱歉，我刚才的回应似乎超时了或者遇到了一些问题。由于复杂的计算和查询，这次可能无法给您答案。请再次发送您的问题，我会尽快为您提供相关答案。谢谢您的理解和支持！",
        "finish": None,
        "type": "text",
        "masterUuid": None,
        "docInfo": [],
        "error": content
    }
    return 'event: message\ndata: {}\n\n'.format(json.dumps(data))


def parse_stop_data(finish_label='stop'):
    data = {
        "messageId": None,
        "model": None,
        "content": '',
        "finish": finish_label,
        "type": "text",
        "docInfo": [],
        "masterUuid": None,
        "error": None
    }
    return 'event: message\ndata: {}\n\n'.format(json.dumps(data))


def parse_app_data(message_id, model, content):
    return {
        "messageId": message_id,
        "model": None,
        "content": content,
        "finish": '',
        "type": "text",
        "error": None
    }


def parse_miniapp_data(message_id, task_id, model, content, audio_data):
    return {
        "messageId": message_id,
        "taskId": task_id,
        "model": None,
        "content": content,
        "audioData": audio_data,
        "finish": '',
        "type": "audio",
        "error": None
    }


def parse_miniapp_stop_data(finish_label='stop', taskId=None):
    return {
        "messageId": None,
        "taskId": taskId,
        "model": None,
        "content": "",
        "audioData": "",
        "finish": finish_label,
        "type": "audio",
        "error": None
    }


def parse_miniapp_fail_data(content, audio_data, error):
    return {
        "messageId": None,
        "model": None,
        "content": content,
        "audioData": audio_data,
        "finish": None,
        "type": "audio",
        "error": error
    }
