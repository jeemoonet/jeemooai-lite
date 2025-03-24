from langchain.text_splitter import CharacterTextSplitter
from src.client.qianfan_client import emb, count_token
from src.document import document_loader
from src.client.mysql import get_company


def do_embedding(document):
    fulltext = document_loader.loader(document)
    if not fulltext:
        raise Exception("未检测到文字")

    document['fileSize'] = len(fulltext)
    document['documentDesc'] = fulltext[:2000]

    splitChar = document.get('splitChar')
    if not splitChar:
        splitChar = "\n"
    chunk_size = document.get('length')
    if not chunk_size:
        chunk_size = 1000

    text_splitter = CharacterTextSplitter(
        separator=splitChar,
        chunk_size=chunk_size,
        chunk_overlap=100,
        length_function=len,
    )

    splitted_documents = text_splitter.create_documents([fulltext])
    texts = []
    for item in splitted_documents:
        if count_token(item.page_content) > 500:
            text_splitter1 = CharacterTextSplitter(
                separator="",
                chunk_size=500,
                chunk_overlap=50,
                length_function=len,
            )
            splitted_documents1 = text_splitter1.create_documents([item.page_content])
            for item1 in splitted_documents1:
                texts.append(item1.page_content)
        else:
            texts.append(item.page_content)

    group_size = 16
    embedding_results = []

    for i in range(0, len(texts), group_size):
        group = texts[i:i + group_size]
        response_group = emb.do(texts=group, model='bge-large-zh')
        for j, vector in enumerate(response_group.body['data']):
            embedding_results.append(vector['embedding'])

    return [{'document_id': str(document['documentId']), 'category_id': str(document['categoryId']), 'content': text,
             'content_word_count': len(text), 'content_vector': v} for text, v in
            zip(texts, embedding_results)]
