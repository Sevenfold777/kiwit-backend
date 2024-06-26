FROM openjdk:17-alpine
EXPOSE 8080
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","-Dspring.profiles.active=prod","/app.jar"]