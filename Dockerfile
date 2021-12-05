FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=target/todo-app.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} todo-app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","todo-app.jar"]