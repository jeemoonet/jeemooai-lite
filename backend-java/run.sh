#!/bin/bash

mvn clean
mvn package -DskipTests

# 使用 pgrep 找到 ai-admin.jar 进程的 PID
pid=$(pgrep -f ai-admin.jar)

# 如果找到了进程
if [ ! -z "$pid" ]; then
    echo "Killing ai-admin.jar process with PID: $pid"
    # 使用 kill 命令结束进程
    kill -9 $pid
    echo "Process killed."
else
    echo "No ai-admin.jar process found."
fi

#nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 ai-admin/target/ai-admin.jar --spring.profiles.active=test &
nohup java -jar ai-admin/target/ai-admin.jar --spring.profiles.active=test &