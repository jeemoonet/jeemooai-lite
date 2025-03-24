from src.client.es_client import full_text_search, vector_search, long_history_vector_search
from src.client.mysql import list_category
from src.client.qianfan_client import emb


def long_history_search(question, window_id, top_k, score):
    if not top_k:
        top_k = 4
    content = ''
    vector = embedding(question)
    documents = long_history_vector_search(vector, window_id, top_k, score)
    for document in documents:
        content += document['content']

    return content


def search_document_by_category_ids(question, search_type, company_id, category_ids, top_k, score):
    if not top_k:
        top_k = 4
    ids = find_all_category_ids(category_ids, company_id)
    content = ''
    if ids and len(ids) > 0:
        documents_dict = {}
        if search_type == 'full_text_search' or search_type == 'hybrid_search':
            documents = full_text_search(question, ids, 'company_' + str(company_id), top_k)
            for document in documents:
                documents_dict[document['id']] = document
                content += document['content']
        if search_type == 'vector_search' or search_type == 'hybrid_search':
            vector = embedding(question)
            documents = vector_search(vector, ids, 'company_' + str(company_id), top_k, score)
            for document in documents:
                documents_dict[document['id']] = document
                content += document['content']

        return content, documents_dict.values()
    else:
        raise Exception("知识库分类不能为空")


def embedding(question):
    response_group = emb.do(texts=[question], model='bge-large-zh')
    data = response_group.body['data']
    return data[0]['embedding']


def find_all_category_ids(category_ids, company_id):
    category_list = list_category(category_ids, company_id)
    result = []
    for category in category_list:
        result.append(category['category_id'])

    return list(set(int(category_id) for category_id in result))
