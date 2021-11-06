#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/action/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/action/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/action/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/action/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi
DEPLOY_PATH=/home/ec2-user/action/
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/action/deploy.log
sudo chmod +x $DEPLOY_JAR
nohup java -jar $DEPLOY_JAR -Dspring.config.location=classpath:/application.yml , /home/ec2-user/action/application-prod.yml -Dspring.profiles.active=prod >> /home/ec2-user/action/deploy.log 2>/home/ec2-user/action/deploy_err.log &
