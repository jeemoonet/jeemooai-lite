import os
import time
import qianfan
from qianfan.resources.tools import tokenizer
from qianfan.resources import Reranker

from src.client.mysql import get_models_config

config = get_models_config()
qianfan.get_config().ACCESS_KEY = config['qian_fan']['key']
qianfan.get_config().SECRET_KEY = config['qian_fan']['secret']
chat_comp = qianfan.ChatCompletion()
emb = qianfan.Embedding()


def count_token(text):
    time.sleep(0.2)
    token_cnt = tokenizer.Tokenizer().count_tokens(
        text=text,
        mode='remote'
    )
    print(token_cnt)
    return token_cnt


def rerank(question, knowledge, top_k):
    # 默认模型
    r = Reranker()

    # 使用指定reranker模型，固定值为bce-reranker-base_v1
    # r = Reranker(model="bce-reranker-base_v1")

    res = r.do(question, knowledge)
    results = res.body['results']
    rerank_list = []
    for item in results:
        rerank_list.append(item['document'])
    if len(rerank_list) > top_k:
        return rerank_list[:top_k]
    else:
        return rerank_list
