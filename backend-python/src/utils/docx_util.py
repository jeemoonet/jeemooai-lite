# 使用的插件 python-docx
import os
import uuid

import docx
from docx import Document
import xml.etree.ElementTree as ET
import io
from PIL import Image
from urllib.parse import urlparse
import urllib.request

from docx.opc.exceptions import PackageNotFoundError

namespaces = {
    'a': 'http://schemas.openxmlformats.org/drawingml/2006/main',
    'r': 'http://schemas.openxmlformats.org/officeDocument/2006/relationships',
    # 其他可能需要的命名空间可在这里添加
}


# 文件路径

def parse_docx_image(url):
    if not os.path.exists('tmp'):
        os.mkdir('tmp')
    # 解析URL
    parsed_url = urlparse(url)

    # 从解析后的URL中提取路径部分
    path = parsed_url.path

    # 从路径中提取最后的文件名
    filename = os.path.basename(path)

    urllib.request.urlretrieve(url, os.path.join('tmp', filename))

    # 加载文档
    try:
        doc = Document(os.path.join('tmp', filename))
    except PackageNotFoundError:
        os.remove(os.path.join('tmp', filename))
        raise Exception('文档解析错误，请另存为docx格式再试。')

    text = []
    for para in doc.paragraphs:
        text.append(para.text)

    os.remove(os.path.join('tmp', filename))
    return text
