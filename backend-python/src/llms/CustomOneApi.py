import json
import traceback

from openai.types.chat import ChatCompletionChunk
from openai.types.chat.chat_completion_chunk import Choice, ChoiceDelta

from src.llms.AbstractLLM import AbstractLLM
from src.client.mysql import get_models_config
from openai import OpenAI, BadRequestError
from src.plugin.FactoryPlugin import Factory as FactoryPlugin


class CustomOneApi(AbstractLLM):

    def __init__(self, record, log, param, is_app):
        super().__init__(record, log, param, is_app)
        config = get_models_config()
        self.key = config['oneapi']['key']
        self.client = OpenAI(
            api_key=config['oneapi']['key'],
            base_url=config['oneapi']['url']
        )

    def _chat(self):
        try:
            response = self.client.chat.completions.create(
                model=self.param.get('model'),
                messages=self.param.get('messages'),
                temperature=self.param.get('temperature'),
                max_tokens=self.param.get('max_tokens'),
                tools=self.param.get('functions'),
                response_format=self.param.get('response_format'),
                stream=False
            )

            message = response.choices[0].message
            content = getattr(message, "content", "")
            reasoning_content = getattr(message, "reasoning_content", "")
            if message.tool_calls:
                tool_calls = message.tool_calls
                messages = self.param.get('messages')
                for tool in tool_calls:
                    arguments = json.loads(tool.function.arguments)
                    function_name = tool.function.name
                    function_call = super()._function_call(function_name, arguments, self.param.get('functionsConfig'))
                    messages.append(
                        {'role': 'function', 'content': function_call, 'tool_call_id': tool.id, 'name': function_name})
                return self._chat()
            else:
                return {"content": content, "reasoning_content": reasoning_content}

        except Exception as e:
            self.exception = e
            return {"content":'非常抱歉，我刚才的回应似乎超时了或者遇到了一些问题。由于复杂的计算和查询，这次可能无法给您答案。请再次发送您的问题，我会尽快为您提供相关答案。谢谢您的理解和支持！', "reasoning_content": ""}

    def _chat_stream(self):
        try:
            response = self._generator_stream_response()
            functions_arguments = {}
            function_names = {}
            function_call_id = {}
            for chunk in response:
                print(chunk.choices)
                finish = chunk.choices[0].finish_reason
                delta = chunk.choices[0].delta
                content = getattr(delta, "content", "")
                reasoning_content = getattr(delta, "reasoning_content", "")
                if not finish and delta.tool_calls:
                    for tool in delta.tool_calls:
                        index = str(tool.index)
                        if tool.id:
                            function_call_id[index] = tool.id
                        if index not in functions_arguments:
                            functions_arguments[index] = ''
                        if tool.function.arguments:
                            functions_arguments[index] += tool.function.arguments
                        if tool.function.name:
                            function_names[index] = tool.function.name
                else:
                    if finish == 'tool_calls':
                        messages = self.param.get('messages')
                        yield {"content": '!!###loading###正在思考中...!!', "reasoning_content": ""}
                        factoryPlugin = FactoryPlugin()
                        for index in function_names.keys():
                            function_name = function_names[index]
                            function_result, result_list = factoryPlugin.run_plugin(function_name, json.loads(
                                functions_arguments[index]),
                                                                                    self.param['toolConfig'])
                            self.record['searchInfo'] = json.dumps(result_list)
                            messages.append({'role': 'assistant', 'tool_calls': [{
                                "function": {
                                    "arguments": functions_arguments[index],
                                    "name": function_name
                                },
                                "id": function_call_id[index],
                                "type": "function"
                            }]})
                            messages.append(
                                {'role': 'tool', 'tool_call_id': function_call_id[index], 'content': function_result,
                                 'name': function_name})
                            self.param['messages'] = messages
                            self.param['tools'] = None
                        response = self._chat_stream()
                        for content in response:
                            yield {"content": content, "reasoning_content": ""}
                    elif not finish and (content or reasoning_content):
                        yield {"content": content, "reasoning_content": reasoning_content}
                    else:
                        self.finish_label = finish
        except Exception as e:
            print(traceback.format_exc())
            self.exception = traceback.format_exc()
            if isinstance(e, BadRequestError):
                if e.code == 'context_length_exceeded':
                    yield {"content": '上下文超出限制，请删除一些会话，或者清空上下文再试。', "reasoning_content":""}
                else:
                    yield {"content": '非常抱歉，我刚才的回应似乎超时了或者遇到了一些问题。由于复杂的计算和查询，这次可能无法给您答案。请再次发送您的问题，我会尽快为您提供相关答案。谢谢您的理解和支持！', "reasoning_content":""}
            else:
                yield {"content": '非常抱歉，我刚才的回应似乎超时了或者遇到了一些问题。由于复杂的计算和查询，这次可能无法给您答案。请再次发送您的问题，我会尽快为您提供相关答案。谢谢您的理解和支持！', "reasoning_content": ""}

    def _generator_stream_response(self):
        is_need_search = self.param.get('isNeedSearch', 0)
        if is_need_search:
            chunk = ChatCompletionChunk(id="",
                                        choices=[Choice(index=0, delta=ChoiceDelta(content="!!###loading###正在搜索中...!!"))],
                                        created=0, model='', object="chat.completion.chunk")
            yield chunk
            super()._search()
        for chunk in self.client.chat.completions.create(
            model=self.param.get('model'),
            messages=self.param.get('messages'),
            temperature=self.param.get('temperature'),
            max_tokens=self.param.get('max_tokens'),
            tools=self.param.get('tools'),
            stream=True
        ):
            yield chunk
