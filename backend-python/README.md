### 使用说明

本项目为企业大脑相关功能

运行包括2部分，api接口(app.py)，定时任务(task.py)

### docker启动

```shell
docker build -t jeemooai -f docker/Dockerfile .
```

```shell
docker run --env-file .env.test -p 5001:5001 --name jeemooai_c jeemooai
```