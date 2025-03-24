import os
from celery import Celery

from document_task import job

redis_host = 'redis'
redis_db = 0
app = Celery('workflow_tasks', broker=f'redis://{redis_host}:6379/{redis_db}')

app.conf.beat_schedule = {
    'document_tasks': {
        'task': 'workflow_task.run_document_task',
        'schedule': 3.0,
    }
}


@app.task
def run_document_task():
    job()
    # pass
