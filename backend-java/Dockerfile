# 二开推荐阅读[如何提高项目构建效率](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/scene/build/speed.html)
# 选择构建用基础镜像。如需更换，请到[dockerhub官方仓库](https://hub.docker.com/_/java?tab=tags)自行选择后替换。
FROM maven:3.6.0-jdk-8-alpine as build
#
#RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys AA8E81B4331F7F50
#
#RUN echo "deb [check-valid-until=no] http://cdn-fastly.deb.debian.org/debian jessie main" > /etc/apt/sources.list.d/jessie.list
#RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
#RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list
##RUN apt-get -o Acquire::Check-Valid-Until=false update
##RUN apt-get -o Acquire::Check-Valid-Until=false update
#RUN #echo "deb https://mirrors.tuna.tsinghua.edu.cn/debian/ sid main contrib non-free" > /etc/apt/sources.list
#RUN apt-get update && apt-get install -y apt-transport-https ca-certificates apt-utils libgdiplus libc6-dev && apt-get install -y libfreetype6 && apt-get install -y libfontconfig1 && apt-get install -y fontconfig
#

# 将src目录下所有文件，拷贝到工作目录中src目录下（.gitignore/.dockerignore中文件除外）
COPY ./ai-admin ai-admin/
COPY ./ai-api ai-api/
COPY ./ai-common ai-common/
COPY ./ai-framework ai-framework/
COPY ./ai-system ai-system/

# 将pom.xml文件，拷贝到工作目录下
COPY ./pom.xml pom.xml
COPY ./settings.xml settings.xml

# 执行代码编译命令
# 自定义settings.xml, 选用国内镜像源以提高下载速度
RUN mvn -s settings.xml -f pom.xml clean package -Dmaven.test.skip=true
#RUN mvn -f pom.xml clean package

#辅助jar
#RUN mvn clean package
# 选择运行时基础镜像
FROM alpine:3.13

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tencent.com/g' /etc/apk/repositories

RUN apk add openjdk8-jre fontconfig ttf-dejavu

#captcher 字体包
RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig

# 容器默认时区为UTC，如需使用上海时间请启用以下时区设置命令
RUN apk add tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone

# 使用 HTTPS 协议访问容器云调用证书安装
# RUN apk add ca-certificates

# 安装依赖包，如需其他依赖包，请到alpine依赖包管理(https://pkgs.alpinelinux.org/packages?name=php8*imagick*&branch=v3.13)查找。
# 选用国内镜像源以提高下载速度
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tencent.com/g' /etc/apk/repositories \
    && apk add --update --no-cache openjdk8-jre-base \
    && rm -f /var/cache/apk/*

# 将构建产物jar包拷贝到运行时目录中
COPY --from=build /ai-admin/target/ai-admin.jar ai-admin.jar

# 暴露端口
# 此处端口必须与「服务设置」-「流水线」以及「手动上传代码包」部署时填写的端口一致，否则会部署失败。
EXPOSE 8080
ENV ACTIVE=dev
# 执行启动命令.
# 写多行独立的CMD命令是错误写法！只有最后一行CMD命令会被执行，之前的都会被忽略，导致业务报错。
# 请参考[Docker官方文档之CMD命令](https://docs.docker.com/engine/reference/builder/#cmd)

CMD ["java","-Djava.awt.headless=true", "-jar", "ai-admin.jar", "--spring.profiles.active=${ACTIVE}"]
