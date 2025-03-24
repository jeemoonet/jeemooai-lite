import concurrent
import json
import time

from libs.helper import fetch_content
from src.client.mysql import get_models_config
from src.plugin.AbstractPlugin import AbstractPlugin
import requests


class BingSearchPlugin(AbstractPlugin):

    def run(self, param):
        configs = get_models_config()
        config = configs['bing']
        # Add your Bing Search V7 subscription key and endpoint to your environment variables.
        subscription_key = config['secret']
        endpoint = config['url']

        # Query term(s) to search for.
        query = param.get("query")

        # Construct a request
        mkt = 'zh-CN'
        params = {'q': query, 'mkt': mkt, 'safeSearch': 'Strict', 'responseFilter': 'webPages'}
        headers = {'Ocp-Apim-Subscription-Key': subscription_key}

        response = requests.get(endpoint, headers=headers, params=params)
        response.raise_for_status()
        body = response.json()
        results = body.get('webPages').get('value')

        text = ""
        content = ""
        result_list = []
        # for result in results:
        #     print(result)
            # content = f"{content}{result['snippet']}"
            # result_list.append(result)

        with concurrent.futures.ThreadPoolExecutor() as executor:
            # 提交抓取任务
            future_to_result = {executor.submit(fetch_content, result['url']): result for result in results[:5]}

            # 3秒后统计结果
            # start_time = time.time()
            # while time.time() - start_time < 3:
            #     time.sleep(0.1)  # 避免忙等待
            index = 1
            for future in concurrent.futures.as_completed(future_to_result):
                result = future_to_result[future]
                text = result['name'] + "\n"
                text = text + result['snippet'] + "\n"
                try:
                    fetched_content = future.result()
                    content += f"[webpage {index} begin]{fetched_content[:2000]}[webpage {index} end]\n"
                except Exception as exc:
                    print(result['url'] + f' generated an exception: {exc}')
                    content += f"[webpage {index} begin]{text}[webpage {index} end]\n"

                index += 1
                result_list.append({"title": result['name'], "url": result['url'], "snippet": result['snippet']})

        return content, result_list
