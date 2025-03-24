import json
import logging
import random
import re
import string
import subprocess
import time
import uuid
from datetime import datetime
from decimal import Decimal
from hashlib import sha256
from typing import Mapping, Any
from zoneinfo import available_timezones
import hashlib

import requests
from bs4 import BeautifulSoup
from langdetect import LangDetectException, detect_langs
from genson import SchemaBuilder


def run(script):
    return subprocess.getstatusoutput('source /root/.bashrc && ' + script)


# class TimestampField(fields.Raw):
#     def format(self, value) -> int:
#         return int(value.timestamp())


def email(email):
    # Define a regex pattern for email addresses
    pattern = r"^[\w\.!#$%&'*+\-/=?^_`{|}~]+@([\w-]+\.)+[\w-]{2,}$"
    # Check if the email matches the pattern
    if re.match(pattern, email) is not None:
        return email

    error = ('{email} is not a valid email.'
             .format(email=email))
    raise ValueError(error)


def uuid_value(value):
    if value == '':
        return str(value)

    try:
        uuid_obj = uuid.UUID(value)
        return str(uuid_obj)
    except ValueError:
        error = ('{value} is not a valid uuid.'
                 .format(value=value))
        raise ValueError(error)


def alphanumeric(value: str):
    # check if the value is alphanumeric and underlined
    if re.match(r'^[a-zA-Z0-9_]+$', value):
        return value

    raise ValueError(f'{value} is not a valid alphanumeric value')


def timestamp_value(timestamp):
    try:
        int_timestamp = int(timestamp)
        if int_timestamp < 0:
            raise ValueError
        return int_timestamp
    except ValueError:
        error = ('{timestamp} is not a valid timestamp.'
                 .format(timestamp=timestamp))
        raise ValueError(error)


class str_len:
    """ Restrict input to an integer in a range (inclusive) """

    def __init__(self, max_length, argument='argument'):
        self.max_length = max_length
        self.argument = argument

    def __call__(self, value):
        length = len(value)
        if length > self.max_length:
            error = ('Invalid {arg}: {val}. {arg} cannot exceed length {length}'
                     .format(arg=self.argument, val=value, length=self.max_length))
            raise ValueError(error)

        return value


class float_range:
    """ Restrict input to an float in a range (inclusive) """

    def __init__(self, low, high, argument='argument'):
        self.low = low
        self.high = high
        self.argument = argument

    def __call__(self, value):
        value = _get_float(value)
        if value < self.low or value > self.high:
            error = ('Invalid {arg}: {val}. {arg} must be within the range {lo} - {hi}'
                     .format(arg=self.argument, val=value, lo=self.low, hi=self.high))
            raise ValueError(error)

        return value


class datetime_string:
    def __init__(self, format, argument='argument'):
        self.format = format
        self.argument = argument

    def __call__(self, value):
        try:
            datetime.strptime(value, self.format)
        except ValueError:
            error = ('Invalid {arg}: {val}. {arg} must be conform to the format {format}'
                     .format(arg=self.argument, val=value, format=self.format))
            raise ValueError(error)

        return value


def patten_loading_text(text: str):
    # 正则表达式模式
    pattern = r'###loading###(.*?)!!'

    # 使用re.search查找匹配项
    match = re.search(pattern, text)
    result = ""
    if match:
        # 匹配到的字符串
        result = match.group(1)

    return result


def _get_float(value):
    try:
        return float(value)
    except (TypeError, ValueError):
        raise ValueError('{} is not a valid float'.format(value))


def timezone(timezone_string):
    if timezone_string and timezone_string in available_timezones():
        return timezone_string

    error = ('{timezone_string} is not a valid timezone.'
             .format(timezone_string=timezone_string))
    raise ValueError(error)


def generate_string(n):
    letters_digits = string.ascii_letters + string.digits
    result = ""
    for i in range(n):
        result += random.choice(letters_digits)

    return result


def get_remote_ip(request) -> str:
    if request.headers.get('CF-Connecting-IP'):
        return request.headers.get('Cf-Connecting-Ip')
    elif request.headers.getlist("X-Forwarded-For"):
        return request.headers.getlist("X-Forwarded-For")[0]
    else:
        return request.remote_addr


def generate_text_hash(text: str) -> str:
    hash_text = str(text) + 'None'
    return sha256(hash_text.encode()).hexdigest()


def cover_to_graph(cells_str: str) -> json:
    cells = json.loads(cells_str)

    _nodes = []
    _edges = []
    nodes = []
    edges = []
    _map = {}
    for item in cells['cells']:
        if item['shape'] == 'edge':
            if 'cell' in item['target']:
                _edges.append(item)
        else:
            _nodes.append(item)

    for node in _nodes:
        _map[node['id']] = node['data']['type']
        nodes.append({
            "id": node["id"],
            "type": "custom",
            "data": node.get("data"),
            "position": node['position'],
            "targetPosition": "left",
            "sourcePosition": "right"
        })

    for edge in _edges:
        source_id = edge['source']['cell']
        target_id = edge['target']['cell']
        edges.append({
            "id": edge['id'],
            "type": "custom",
            "source": source_id,
            "target": target_id,
            "sourceHandle": edge['source']['port'],
            "targetHandle": "target",
            "data": {
                "sourceType": _map[source_id],
                "targetType": _map[target_id],
                "isInIteration": False
            }
        })

    return {"nodes": nodes, "edges": edges}


def get_variable_text(s) -> list[str]:
    # 正则表达式匹配{{和}}之间的内容
    pattern = r'\{\{(.*?)\}\}'
    matches = re.findall(pattern, s)
    return matches


def generator_request_config(plugin: dict, request_param: list[dict]):
    url = plugin['base_url'] + plugin['url_path']
    method = plugin['method'].lower()
    query_var = []
    headers_var = [] + json.loads(plugin['headers'])
    if plugin['auth_type'].lower() == 'service':
        service_config = json.loads(plugin['service_auth_config'])
        if service_config['type'].lower() == 'header':
            headers_var.append({"name": service_config['parameterName'], "value": service_config['serviceToken']})
        if service_config['type'].lower() == 'query':
            query_var.append({"name": service_config['parameterName'], "value": service_config['serviceToken']})
    path_var = []
    body_var = []

    request_parameters = json.loads(plugin['request_parameter'])
    for parameter in request_parameters:
        _param = None
        for param in request_param:
            if param['name'] == parameter['name']:
                _param = param
                break
        if parameter['parameter_method'].lower() == 'body':
            body_var.append(
                {"name": parameter['name'], "value": _param['value'] if _param else parameter['default_value']})
        elif parameter['parameter_method'].lower() == 'header':
            headers_var.append(
                {"name": parameter['name'], "value": _param['value'] if _param else parameter['default_value']})
        elif parameter['parameter_method'].lower() == 'path':
            path_var.append(
                {"name": parameter['name'], "value": _param['value'] if _param else parameter['default_value']})
        elif parameter['parameter_method'].lower() == 'query':
            query_var.append(
                {"name": parameter['name'], "value": _param['value'] if _param else parameter['default_value']})

    headers = {}
    for header in headers_var:
        headers[header['name']] = header['value']
    bodies = {}
    for body in body_var:
        bodies[body['name']] = body['value']
    query = {}
    for var in query_var:
        query[var['name']] = var['value']

    for var in path_var:
        url = url.replace("{" + var['name'] + "}", var['value'])

    return {
        "url": url,
        "method": method,
        "body": bodies,
        "params": query,
        "headers": headers
    }


def fetch_content(url):
    headers = {
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
        "Accept": "text/html",
        "Accept-Language": "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "Referer": "https://www.baidu.com/",
        "DNT": "1",
        "Connection": "keep-alive",
        "Upgrade-Insecure-Requests": "1",
        "Accept-Charset": "utf-8;q=0.5"
    }

    try:
        response = requests.get(url, headers=headers, timeout=(3, 3))
        if response.status_code == 200:
            soup = BeautifulSoup(response.content, 'html.parser')
            soup_text = soup.get_text()
            cleaned_string = re.sub(r'\n+', ' ', soup_text)
            print(f'url:{url}')
            print(f'content:{cleaned_string}')
            return cleaned_string
    except Exception as e:
        print(f"Error fetching {url}: {e}")
        return None



def is_garbled(text, min_confidence=0.7):
    try:
        # Detect the languages of the text with their probabilities
        detected_languages = detect_langs(text)
        print(f"Detected languages: {detected_languages}")

        # Check if the highest confidence language has a confidence above the threshold
        if detected_languages and detected_languages[0].prob >= min_confidence:
            return False
        else:
            return True
    except LangDetectException:
        # If language detection fails, consider it as garbled
        return True



class SnowflakeIdWorker:
    twepoch = 1288834974657  # Twitter 雪花算法的起始时间戳
    machine_id = 1  # 机器标识
    datacenter_id = 1  # 数据中心标识
    sequence = 0  # 序列号

    worker_id_bits = 5  # 机器ID长度
    datacenter_id_bits = 5  # 数据中心ID长度
    sequence_bits = 12  # 序列号长度
    max_sequence = -1 ^ (-1 << sequence_bits)  # 最大序列号

    epoch_bits = 29  # 时间戳长度
    machine_id_shift = sequence_bits
    datacenter_id_shift = sequence_bits + worker_id_bits
    timestamp_shift = sequence_bits + worker_id_bits + datacenter_id_bits

    def __init__(self, machine_id, datacenter_id):
        self.machine_id = machine_id
        self.datacenter_id = datacenter_id
        self.sequence = 0
        self.last_timestamp = -1

    def _now(self):
        return int(time.time() * 1000)

    def get_id(self):
        timestamp = self._now()
        if timestamp < self.last_timestamp:
            raise Exception("Clock moved backwards. Refusing to generate id")

        if self.last_timestamp == timestamp:
            self.sequence = (self.sequence + 1) & self.max_sequence
            if self.sequence == 0:
                timestamp = self._till_next_millis(self.last_timestamp)
        else:
            self.sequence = 0

        self.last_timestamp = timestamp

        return ((timestamp - self.twepoch) << self.timestamp_shift) | \
            (self.datacenter_id << self.datacenter_id_shift) | \
            (self.machine_id << self.machine_id_shift) | \
            self.sequence

    def _till_next_millis(self, last_timestamp):
        timestamp = self._now()
        while timestamp <= last_timestamp:
            timestamp = self._now()
        return timestamp



def decimal_default(obj):
    if isinstance(obj, Decimal):
        return float(obj)  # 或者 return str(obj) 如果需要字符串格式
    raise TypeError("Object of type '%s' is not JSON serializable" % type(obj).__name__)


class CustomSchemaBuilder(SchemaBuilder):
    def add_object(self, obj, **kwargs):
        super().add_object(obj, **kwargs)
        schema = self.to_schema()
        self._add_descriptions(schema)
        return schema
        # self.add_schema(schema)

    def _add_descriptions(self, schema):
        # 递归函数，为schema中的每个字段添加描述
        if 'properties' in schema:
            for key, value in schema['properties'].items():
                value['id'] = str(uuid.uuid4())
                if 'type' in value and value['type'] == 'null':
                    value['type'] = 'string'
                if 'description' not in value:
                    value['description'] = f'{key}说明'
                if 'items' in value:  # 处理数组类型
                    self._add_descriptions(value['items'])
                elif 'properties' in value:  # 处理对象类型
                    self._add_descriptions(value)


def from_dict_to_schema(data):
    # 创建SchemaBuilder实例
    builder = CustomSchemaBuilder()
    return json.dumps(builder.add_object(data), ensure_ascii=False)

    # 生成JSON Schema
    # schema = builder.to_json()
    # return schema
