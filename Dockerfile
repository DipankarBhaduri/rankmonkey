# # Use a base image with JDK 21
# FROM eclipse-temurin:21-jdk-alpine
#
# # Set the working directory inside the container
# WORKDIR /app
#
# # Copy the JAR file from the target directory to the working directory
# COPY target/authsvc-0.0.1-SNAPSHOT.jar app.jar
#
# # Expose the port your Spring Boot app runs on
# EXPOSE 9990
#
# # Command to run the JAR file
# ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 9999
ARG JAR_FILE=target/rankmonkeysvc-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


# mvn clean package -> create a new jar file
# java -jar target/jarfilename  -> run in termianl
# docker buildx build -t authsvc . -> build docker image
# docker run -d -p 8080:8080 --name authsvc authsvc -> run the docker container
# docker logs authsvc --> for see logs
# docker logs <container_id> --> see logs in container
# docker logs --tail 10 authsvc  --> log last 10 line
# docker logs --since "2024-06-01T00:00:00" authsvc --> since here
# docker stop authsvc --> stop the running container
# docker rm authsvc -> removed the container