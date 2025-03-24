import uuid

import stomp, json, os
import logging

from src.client.stomp_connect_poll import StompConnectionPool

logger = logging.getLogger('app.activemq')
activemq_url = 'activemq'
activemq_port = 61613
activemq_user = os.getenv('ACTIVEMQ_ADMIN_USER')
activemq_password = os.getenv('ACTIVEMQ_ADMIN_PASSWORD')

pool = StompConnectionPool(activemq_url, activemq_port, activemq_user, activemq_password)

def send_request_log_data(message):
    conn = pool.get_connection()
    try:
        destination = '/queue/ai_request_log_demo'
        conn.send(destination, json.dumps(message))
        logger.info("push" + destination)
    finally:
        pool.release_connection(conn)

def send_record_data(message):
    conn = pool.get_connection()
    try:
        destination = '/queue/ai_record_demo'
        conn.send(destination, json.dumps(message))
        logger.info("push" + destination)
    finally:
        pool.release_connection(conn)


def send_document_entity_data(message):
    conn = pool.get_connection()
    try:
        destination = '/queue/ai_document_entity_demo'
        conn.send(destination, json.dumps(message))
        logger.info("push" + destination)
    finally:
        pool.release_connection(conn)


def send_document_update_data(message):
    conn = pool.get_connection()
    try:
        destination = '/queue/ai_document_demo'
        conn.send(destination, json.dumps(message))
        logger.info("push" + destination)
    finally:
        pool.release_connection(conn)


def is_connect():
    try:
        conn = pool.get_connection()
        logger.info('Check ActiveMQ connect status')
        if not conn.is_connected():
            logger.warning('ActiveMQ is not connected, trying to reconnect')
            conn.connect(os.getenv('ACTIVEMQ_ADMIN_USER'), os.getenv('ACTIVE_MQ_PASSWORD'), wait=True)
            logger.info('ActiveMQ is connected')
    except Exception as e:
        logger.error("Failed to connect to ActiveMQ: " + str(e))
        conn.connect(os.getenv('ACTIVEMQ_ADMIN_USER'), os.getenv('ACTIVE_MQ_PASSWORD'), wait=True)
        logger.info('ActiveMQ is connected')
