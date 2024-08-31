FROM openjdk:17

MAINTAINER LJCzzzzZ

WORKDIR /w

COPY target/gzhuoj-gateway-1.0.0.jar /w/gzhuoj-gateway

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gzhuoj-gateway.jar"]

