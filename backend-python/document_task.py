import json
import logging
import traceback

from src.document import embedding
from src.client import activemq, mysql
from src.client.es_client import has_index, create_index, insert_data

def job():
    data = mysql.get_one_task()
    print('Received a message "%s"' % json.dumps(data))
    if data:
        document = data
        document['status'] = 1
        activemq.send_document_update_data(document)
        logging.info(document)
        try:
            index_name = "company_" + str(document['companyId'])
            if not has_index(index_name):
                create_index(index_name)

            embedding_data = embedding.do_embedding(document)
            insert_data(index_name, embedding_data)
            document['status'] = 2
            activemq.send_document_update_data(document)
        except Exception as e:
            traceback.print_exc()
            document['status'] = 3
            document['remark'] = str(e)
            activemq.send_document_update_data(document)