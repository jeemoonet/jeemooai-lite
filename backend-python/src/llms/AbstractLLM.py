import json
import logging
import os
import time
import uuid
from datetime import datetime

import requests
from abc import abstractmethod

from libs.helper import patten_loading_text
from src.client import activemq
from src.client.mysql import get_sys_config
from src.client.redis_client import getRedisClient
from src.plugin.BingSearchPlugin import BingSearchPlugin

logger = logging.getLogger('app.AbstractLLM')

class AbstractLLM:
    def __init__(self, record, log, param, is_app=0):
        self.key = None
        self.log = log
        self.param = param
        self.record = record
        self.start_time = time.time()
        self.end_time = None
        self.content = ""
        self.reasoning_content = ""
        self.exception = None
        self.finish_label = None
        self.is_app = is_app
        self.is_show_loading = 1

    def chat(self):
        response = self._chat()
        self.content = response['content']
        self.reasoning_content = response['reasoning_content']
        self._finish_event()
        if self.exception:
            return {"code": 1, "msg": response}
        else:
            return {"code": 0, "msg": "请求成功", "data": {
                "messageId": self.record.get('nextId') if self.record else None,
                "model": None,
                "content": response['content'],
                "reasoningContent": response['reasoning_content'],
                "finish": '',
                "type": "text",
                "error": None
            }}

    def chat_stream(self):
        messageId = uuid.uuid4()
        response = self._chat_stream()
        while True:
            try:
                if self.finish_label:
                    raise StopIteration("stop")
                content_data = next(response)
                content = content_data['content'] if content_data['content'] else ''
                reasoning_content = content_data['reasoning_content']
                if not self.is_show_loading and content.startswith("!!###loading###"):
                    loading_text = patten_loading_text(content)
                    content = content.replace(f'!!###loading###{loading_text}!!', '')
                if not content.startswith("!!###loading###"):
                    self.content += content
                if reasoning_content:
                    self.reasoning_content += reasoning_content
                data = {
                    "messageId": self.record.get('nextId') if self.record else str(messageId),
                    "model": None,
                    "content": content,
                    "reasoningContent": reasoning_content,
                    "finish": None,
                    "type": "text",
                    "masterUuid": self.record.get('masterUuid') if self.record else None,
                    "error": ""
                }
                if self.record and self.record.get('docInfo') and not self.exception:
                    data['docInfo'] = json.loads(self.record.get('docInfo'))
                if self.record and self.record.get('searchInfo') and not self.exception:
                    data['searchInfoList'] = json.loads(self.record.get('searchInfo'))
                yield 'event: message\ndata: {}\n\n'.format(json.dumps(data))
            except (StopIteration, GeneratorExit) as e:
                logger.debug(type(e))
                data = {
                    "messageId": self.record.get('nextId') if self.record else str(messageId),
                    "model": None,
                    "content": "",
                    "reasoningContent": "",
                    "finish": 'stop',
                    "type": "text",
                    "masterUuid": self.record.get('masterUuid') if self.record else None,
                    "error": None
                }
                if self.record and self.record.get('docInfo') and not self.exception:
                    data['docInfo'] = json.loads(self.record.get('docInfo'))
                if self.exception:
                    data['error'] = str(self.exception)
                self._finish_event()
                yield 'event: message\ndata: {}\n\n'.format(json.dumps(data))
                break

    def _finish_event(self):
        is_error = 0
        self.log.receiveMessage = self.content
        self.log.wordCount = len(self.content)
        self.log.requestTime = time.time() - self.start_time
        self.log.key = self.key
        if self.exception:
            is_error = 1
            self.log.errorMessage = str(self.exception)
            self.log.wordCount = 0
        if self.is_app:
            activemq.send_app_request_log_data(self.log.to_dict())
        else:
            activemq.send_request_log_data(self.log.to_dict())
            if self.record:
                activemq.send_record_data(
                    {"messageId": self.record.get('nextId'),
                     "content": self.content,
                     "reasoningContent": self.reasoning_content,
                     "createTimestamp": time.time() * 1000,
                     "searchInfo": self.record.get('searchInfo'),
                     "masterUuid": self.record.get('masterUuid'),
                    "isError": is_error})

    def _search(self):
        system_prompt = ""
        is_have_system_prompt = False
        messages = self.param.get("messages")
        search_prompt = get_sys_config('search_prompt')
        for message in messages:
            if message['role'] == 'system':
                system_prompt = message['content']
                is_have_system_prompt = True

        user_question = messages[-1]['content']

        plugin = BingSearchPlugin()
        search_content, search_list = plugin.run({"query": user_question[:100]})
        now = datetime.now()
        formatted_date = now.strftime("%Y-%m-%d %H:%M:%S")
        search_prompt = search_prompt.replace("{search_results}", search_content).replace("{cur_date}", formatted_date).replace("{question}", user_question)
        system_prompt = f"{system_prompt}\n\n{search_prompt}"
        if is_have_system_prompt:
            for message in messages:
                if message['role'] == 'system':
                    message['content'] += system_prompt
        else:
            message = {
                "role": "system",
                "content": system_prompt
            }
            messages.insert(0, message)

        self.param["messages"] = messages
        self.log.sendMessage = self.param
        if self.record:
            self.record['searchInfo'] = json.dumps(search_list)

    @abstractmethod
    def _chat(self):
        pass

    @abstractmethod
    def _chat_stream(self):
        pass

    @abstractmethod
    def _generator_stream_response(self):
        pass
