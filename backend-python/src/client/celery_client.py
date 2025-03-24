from celery import Celery
import os

config = {'CELERY_BROKER_URL': f"redis://redis:6379/0",
          'CELERY_RESULT_BACKEND': f"redis://redis:6379/0"}

celery = Celery("jeemooai-task", broker=config['CELERY_BROKER_URL'])
celery.conf.update(config)