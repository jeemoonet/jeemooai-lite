import requests
import re
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse
import logging

logging.basicConfig(format='%(asctime)s - %(pathname)s[line:%(lineno)d] - %(levelname)s: %(message)s',
                    level=logging.DEBUG)

# 定义爬虫函数
def crawl(url, visited_urls, base_url, page, out_put,level=0):
    headers = {
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Language": "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "Referer": "https://www.baidu.com/",
        "DNT": "1",
        "Connection": "keep-alive",
        "Upgrade-Insecure-Requests": "1",
    }

    if url in visited_urls:
        return

    # 发送 GET 请求获取页面内容
    response = requests.get(url, headers=headers, timeout=10)
    response.encoding = 'utf-8'
    visited_urls.add(url)
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        if soup.title:
            title = soup.title.text
        else:
            title = "no found title"

        if out_put.get('title') is None:
            out_put['title'] = title

        out_put['remark'].append(title)
        out_put['remark'].append(url)
        # 提取页面文本内容并进行格式处理
        page_content = soup.get_text().strip()
        page_content = re.sub(r'\s+', ' ', page_content)  # 将多个空格替换为一个
        page_content = re.sub(r'\n+', ' ', page_content)  # 将多个换行替换为一个

        page.append(page_content)
        page.append(' ')

        # 提取页面中的链接并继续爬取
        links = soup.find_all('a')
        for link in links:
            href = link.get('href')
            if href and href != 'javascript:;':
                absolute_url = urljoin(url, href)
                parsed_url = urlparse(absolute_url)
                if parsed_url.netloc == base_url and absolute_url not in visited_urls and level < 50 and len(visited_urls) < 100:
                    logging.info(str(level) + ":" + absolute_url)
                    crawl(absolute_url, visited_urls, base_url, page, out_put, level+1)
