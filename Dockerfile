FROM openjdk:8
ADD target/todo-app.jar todo-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "todo-app.jar"]

