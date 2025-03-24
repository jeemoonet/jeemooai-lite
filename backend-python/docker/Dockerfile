# 设置基础镜像
FROM zhana0/jeemoo_python:latest

# 创建工作目录
WORKDIR /app

# 更换源地址
COPY ./docker/sources.list /etc/apt/sources.list
RUN rm -rf /etc/apt/sources.list.d/*

# 安装中文语言包
RUN apt-get update \
    && apt-get install -y locales vim \
    && rm -rf /var/lib/apt/lists/* \
    && localedef -i zh_CN -c -f UTF-8 -A /usr/share/locale/locale.alias zh_CN.UTF-8

# 设置时区
RUN ln -fs /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && dpkg-reconfigure -f noninteractive tzdata

# 设置环境变量
ENV LANG zh_CN.UTF-8
ENV LANGUAGE zh_CN:zh
ENV LC_ALL zh_CN.UTF-8

# 复制项目文件
COPY . /app

# 安装项目依赖
RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple --no-cache-dir -r requirements.txt

# 配置环境变量
ENV FLASK_APP=app.py

# 启动Gunicorn服务器
ENTRYPOINT ["/bin/bash","run.sh"]
