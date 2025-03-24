import datetime
import json

from sqlalchemy import create_engine, QueuePool, text
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import sessionmaker
from urllib.parse import quote
import os
import logging

logger = logging.getLogger('app.mysql')

MYSQL_DB_HOST = 'mysql'
MYSQL_DB_USER = os.getenv('MYSQL_USER')
MYSQL_DB_PASSWORD = quote(os.getenv('MYSQL_PASSWORD'))
MYSQL_DB = os.getenv('MYSQL_DATABASE')

engine = create_engine(f"mysql://{MYSQL_DB_USER}:{MYSQL_DB_PASSWORD}@{MYSQL_DB_HOST}:3306/{MYSQL_DB}?charset=utf8",
                       poolclass=QueuePool, pool_recycle=3600, echo=True, echo_pool=True)

engine.connect().close()  # 先创建一个连接，然后关闭，以便访问连接池的参数
engine.pool.timeout = 30  # 设置连接超时时间为30秒
engine.pool.recycle = True  # 开启连接重连

# 创建会话工厂
Session = sessionmaker(bind=engine)


def get_user_by_id(user_id):
    # 使用 with 语句确保会话正确关闭
    with engine.connect() as connection:
        # 这里执行你的查询
        result = connection.execute(
            text("select * from sys_user where user_id = :user_id"), {"user_id": user_id})
        data = result.fetchone()
        if data is not None:
            return dict(data._mapping)
        else:
            return None


def get_one_task():
    # 使用 with 语句确保会话正确关闭
    with engine.connect() as connection:
        # 这里执行你的查询
        result = connection.execute(
            text("select a.document_id as documentId, a.company_id as companyId, a.category_id as categoryId, "
                 "a.user_id as userId, split_char as splitChar, length,"
                 " a.document_type as documentType, a.document_name as documentName, a.document_desc as "
                 "documentDesc, a.url, a.file_size as fileSize, a.status, a.page_number as pageNumber, a.remark, "
                 "b.company_name as companyName from "
                 "ai_document a left join ai_company b on a.company_id = b.company_id where a.status = 0 and "
                 "a.deleted = 0 order by a.create_time limit 1"))
        data = result.fetchone()
        if data is not None:
            return dict(data._mapping)
        else:
            return None


def get_document(company_id):
    # 使用 with 语句确保会话正确关闭
    with engine.connect() as connection:
        # 这里执行你的查询
        result = connection.execute(
            text("select * from ai_document where company_id = :company_id and status = 2 and deleted = 0"),
            {'company_id': company_id})
        list = result.fetchall()

        # Use _mapping to get a dict-like view of the Row objects for SQLAlchemy 1.4+
        return [{key: value for key, value in row._mapping.items()} for row in list]


def get_company(company_id):
    with engine.connect() as connection:
        result = connection.execute(
            text("SELECT * FROM ai_company WHERE company_id = :company_id"),
            {'company_id': company_id}
        )
        company_data = result.fetchone()
        # 如果查询结果不为空，则转换为字典
        if company_data is not None:
            # SQLAlchemy 1.4+ 使用 ._mapping
            company_dict = dict(company_data._mapping)
            return company_dict
        else:
            # 如果没有找到数据，可以返回一个空的 JSON 对象或者其他你认为合适的值
            return None


def get_models():
    with engine.connect() as connection:
        result = connection.execute(text("SELECT * FROM ai_models"))
        models_data = result.fetchall()

        # Use _mapping to get a dict-like view of the Row objects for SQLAlchemy 1.4+
        models_list = [{key: value for key, value in row._mapping.items()} for row in models_data]
        resultMap = {}
        for item in models_list:
            resultMap[item['model_label']] = item
        return resultMap


def get_model(model_label):
    with engine.connect() as connection:
        result = connection.execute(text("SELECT * FROM ai_models where model_label = :model_label"),
                                    {"model_label": model_label})
        data = result.fetchone()
        if data is not None:
            _dict = dict(data._mapping)
            return _dict
        else:
            return None


def get_models_config():
    print('get_models_config')
    with engine.connect() as connection:
        result = connection.execute(text("SELECT * FROM ai_models_config"))
        data = result.fetchall()

        # Use _mapping to get a dict-like view of the Row objects for SQLAlchemy 1.4+
        list = [{key: value for key, value in row._mapping.items()} for row in data]
        resultMap = {}
        for item in list:
            resultMap[item['platform']] = item
        return resultMap


def get_prompt_template():
    with engine.connect() as connection:
        result = connection.execute(text("select config_value from sys_config where config_key = 'prompt_template'"))
        data = result.fetchone()
        return json.loads(data[0])


def get_sys_config(config_key):
    with engine.connect() as connection:
        result = connection.execute(text("select config_value from sys_config where config_key = :config_key"),
                                    {"config_key": config_key})
        data = result.fetchone()
        return data[0]


def get_prompt(prompt_id):
    with engine.connect() as connection:
        result = connection.execute(
            text("SELECT * FROM ai_prompt WHERE prompt_id = :prompt_id"),
            {'prompt_id': prompt_id}
        )
        data = result.fetchone()
        # 如果查询结果不为空，则转换为字典
        if data is not None:
            # SQLAlchemy 1.4+ 使用 ._mapping
            _dict = dict(data._mapping)
            _dict['temperature'] = float(_dict['temperature'])
            return _dict
        else:
            # 如果没有找到数据，可以返回一个空的 JSON 对象或者其他你认为合适的值
            return None


def list_category(ids, company_id):
    with engine.connect() as connection:
        result = connection.execute(
            text(
                "select * from ai_document_category where deleted = 0 and company_id = :company_id and category_id in :ids"),
            {'ids': tuple(ids), 'company_id': company_id})
        data = result.fetchall()

        # Use _mapping to get a dict-like view of the Row objects for SQLAlchemy 1.4+
        return [{key: value for key, value in row._mapping.items()} for row in data]
