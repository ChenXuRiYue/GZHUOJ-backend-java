FROM openjdk:17

MAINTAINER LJCzzzzZ

WORKDIR /w

COPY target/judge-server-1.0.0.jar /w/judge-server.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "judge-server.jar"]

