FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/rightchain-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Duser.timezone=\"Asia/Seoul\"", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]