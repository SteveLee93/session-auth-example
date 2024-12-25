FROM openjdk:21-slim

WORKDIR /app

# JAR 파일 복사
COPY target/*.jar app.jar

# 컨테이너 실행 시 실행될 명령어
ENTRYPOINT ["java", "-jar", "app.jar"] 