import redis
import os
import time
import logging

logger = logging.getLogger('app.redis')

pool = redis.ConnectionPool(
    host='redis',
    port=6379,
    db=0,
    max_connections=10,
    socket_timeout=60
)

def getRedisClient():
    redis_client = redis.Redis(connection_pool=pool, client_name='python_client')
    try:
        if redis_client.ping():
            logger.info("Redis connection is healthy.")
            return redis_client
        else:
            logger.warning("Redis connection is not healthy. Reconnecting...")
            return reconnectRedis()
    except redis.exceptions.ConnectionError:
        logger.error("Redis connection error. Reconnecting...")
        return reconnectRedis()


def reconnectRedis():
    global pool
    pool.disconnect()
    time.sleep(1)  # 可以适当增加延迟时间
    pool = redis.ConnectionPool(
        host=os.getenv('REDIS_HOST', 'localhost'),
        port=6379,
        db=os.getenv('REDIS_DB', 8),
        max_connections=10,
        socket_timeout=5
    )
    logger.info("Redis reconnected.")
    redis_client = redis.Redis(connection_pool=pool)
    return redis_client


class RedisQueue:
    def __init__(self, name, namespace='queue', redis_client=None):
        self.__db = redis_client
        self.key = f'{namespace}:{name}'
        self.task_id = name

    def qsize(self):
        return self.__db.llen(self.key)

    def put(self, item):
        self.__db.rpush(self.key, item)

    def get(self, block=True, timeout=None):
        if block:
            item = self.__db.blpop(self.key, timeout=timeout)
        else:
            item = self.__db.lpop(self.key)

        if item:
            item = item[1]
        return item

    def get_nowait(self):
        return self.get(False)

    def stop_task(self):
        self.__db.delete(f"workflow_queue_task_{self.task_id}")
