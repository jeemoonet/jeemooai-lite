import os
from elasticsearch import Elasticsearch

es = Elasticsearch("https://elasticsearch:9200",basic_auth=(os.getenv('ELASTIC_USER'), os.getenv('ELASTIC_PASSWORD')))


def has_index(index_name):
    """
    检查索引是否存在
    :param index_name: 索引名称
    :return: 布尔值，如果索引存在返回True，否则返回False
    """
    try:
        return es.indices.exists(index=index_name)
    except Exception as e:
        print(f"Error checking index existence: {e}")
        return False


def create_index(index_name, in_mapping=None):
    """
    创建索引并设置映射
    :param index_name: 索引名称
    :param in_mapping: 索引映射
    :return: 创建成功返回True，否则返回False
    """
    try:
        if in_mapping:
            mapping = in_mapping
        else:
            mapping = {
                "mappings": {
                    "properties": {
                        "content": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "search_analyzer": "ik_smart"
                        },
                        "document_id": {
                            "type": "long",
                        },
                        "category_id": {
                            "type": "long",
                        },
                        "content_vector": {
                            "type": "dense_vector",
                            "dims": 1024
                        }
                    }
                }
            }
        response = es.indices.create(index=index_name, body=mapping)
        return response.get('acknowledged')
    except Exception as e:
        print(f"Error creating index: {e}")
        return False


def insert_data(index_name, data):
    """
       向索引中插入数据
       :param index_name: 索引名称
       :param document_id: 文档ID
       :param data: 要插入的数据
       :return: 插入成功返回True，否则返回False
       """
    try:
        actions = []
        for item in data:
            actions.append({"index": {"_index": index_name}})
            actions.append(item)
        response = es.bulk(index=index_name, body=actions)
        if response['errors']:
            print("发生错误：", response)
        else:
            print("批量操作成功完成。")
            return True
    except Exception as e:
        print(f"Error inserting data: {e}")
        return False


def full_text_search(text, document_ids, index_name, top_k):
    body = {
        "query": {
            "bool": {
                "must": [
                    {
                        "terms": {
                            "category_id": document_ids
                        }
                    },
                    {
                        "match": {
                            "content": text
                        }
                    }
                ]
            }
        },
        "size": top_k,
        "_source": [
            "content",
            "document_id",
            "category_id"
        ]
    }

    response = es.search(index=index_name, body=body)
    hits = response.body['hits']['hits']
    result = []
    for item in hits:
        source = item['_source']
        source['id'] = item['_id']
        result.append(source)

    return result


def long_history_vector_search(vector, window_id, top_k, score):
    if not top_k:
        top_k = 5

    body = {
        "query": {
            "term": {
                "window_id": {
                    "value": window_id
                }
            }
        }
    }
    knn = {
        "field": "content_vector",
        "query_vector": vector,
        "num_candidates": 100,
        "k": top_k
    }
    if score:
        knn["similarity"] = score

    body['knn'] = knn
    response = es.search(index="long_history_index", body=body)
    hits = response.body['hits']['hits']
    result = []
    for item in hits:
        source = item['_source']
        source['id'] = item['_id']
        result.append(source)

    return result


def vector_search(vector, document_ids, index_name, top_k, score):
    if not top_k:
        top_k = 5

    body = {
        "query": {
            "terms": {
                "category_id": document_ids
            }
        }
    }
    knn = {
        "field": "content_vector",
        "query_vector": vector,
        "num_candidates": 100,
        "k": top_k
    }
    if score:
        knn["similarity"] = score

    body['knn'] = knn
    response = es.search(index=index_name,body=body)
    hits = response.body['hits']['hits']
    result = []
    for item in hits:
        source = item['_source']
        source['id'] = item['_id']
        result.append(source)

    return result


def clean_index(index_name):
    body = {
        "query": {
            "match_all": {}
        }
    }
    es.delete_by_query(index=index_name, body=body)
