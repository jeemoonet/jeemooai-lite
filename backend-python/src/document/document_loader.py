from langchain.document_loaders import PyPDFLoader
from langchain.document_loaders import Docx2txtLoader
from langchain.document_loaders import WebBaseLoader
from langchain.document_loaders import SeleniumURLLoader
from src.document.url_spider import crawl
from src.utils.docx_util import parse_docx_image
from urllib.parse import urlparse
import chardet
import re


def loader(document):
    filetype = document['documentType']
    filepath = document['url']

    if filetype == 'pdf':
        return pdf_loader(filepath)

    if filetype == 'txt':
        return web_loader([filepath])

    if filetype == 'docx':
        data = docx_loader_parse_image(filepath)
        return data

    if filetype == 'url':
        data = spider_loader(filepath, document)
        return data

    if filetype == 'wechat':
        data = web_loader([filepath])
        return data

    return ''


def find_encoding(fname):
    r_file = open(fname, 'rb').read()
    result = chardet.detect(r_file)
    charenc = result['encoding']
    return charenc


def doc_loader(filepath):
    loader = Docx2txtLoader(filepath)
    documents = loader.load()
    data = documents[0].page_content
    return re.sub('\n+', '\n', data)

def docx_loader_parse_image(filepath):
    text = parse_docx_image(filepath)
    fulltext = ''
    for line in text:
        fulltext += (line + '\n')
    return re.sub('\n+', '\n', fulltext)

def pdf_loader(filepath):
    loader = PyPDFLoader(filepath)
    documents = loader.load()

    fulltext = ''
    for page in documents:
        fulltext += page.page_content
    return re.sub('\n+', '\n', fulltext)


def web_loader(url):
    header_template = {
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Language": "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "Referer": "https://www.baidu.com/",
        "DNT": "1",
        "Connection": "keep-alive",
        "Upgrade-Insecure-Requests": "1",
    }
    loader = WebBaseLoader(web_path=url, header_template=header_template)
    documents = loader.load()

    fulltext = ''
    for page in documents:
        page.page_content = page.page_content
        fulltext += page.page_content
    return re.sub('\n+', '\n', fulltext)


def url_loader(urls, document):
    loader = SeleniumURLLoader(urls=urls)
    documents = loader.load()

    fulltext = ''
    for page in documents:
        page.page_content = page.page_content
        fulltext += page.page_content
    return re.sub('\n+', '\n', fulltext)


def spider_loader(url, document):
    base_url = urlparse(url).netloc
    visited_urls = set()
    page = []
    out_put = {"title": None, "remark": []}
    crawl(url, visited_urls, base_url, page, out_put)
    document['remark'] = "\n".join(out_put['remark'])
    document['pageNumber'] = len(visited_urls)
    document['documentName'] = out_put['title']
    return '\n'.join(page)