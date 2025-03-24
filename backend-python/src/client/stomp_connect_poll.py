import os
import stomp
import logging
from queue import Queue
from threading import Lock

# 创建一个logger
logger = logging.getLogger('app.activemq')


# 连接池类
class StompConnectionPool:
    def __init__(self, host, port, user, password, pool_size=10, heartbeats=(4000, 4000)):
        self.host = host
        self.port = port
        self.user = user
        self.password = password
        self.pool_size = pool_size
        self.heartbeats = heartbeats
        self.connections = Queue()
        self.lock = Lock()
        self.initialize_pool()

    def initialize_pool(self):
        with self.lock:
            for _ in range(self.pool_size):
                conn = stomp.Connection([(self.host, self.port)], heartbeats=self.heartbeats)
                self.connections.put(conn)

    def get_connection(self):
        with self.lock:
            conn = self.connections.get(block=False)
            if not conn or not conn.is_connected():
                conn = stomp.Connection([(self.host, self.port)], heartbeats=self.heartbeats)
                conn.connect(self.user, self.password, wait=True)
                self.connections.put(conn)  # Return old connection back to pool
            return conn

    def release_connection(self, conn):
        with self.lock:
            if not conn.is_connected():
                conn = stomp.Connection([(self.host, self.port)], heartbeats=self.heartbeats)
                conn.connect(self.user, self.password, wait=True)
            self.connections.put(conn)
