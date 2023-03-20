FROM openjdk:17-oracle
ADD target/gateway-service-0.0.1-SNAPSHOT.jar docker-gateway-service-0.0.1-SNAPSHOT.jar
EXPOSE 5555
ENTRYPOINT ["java","-jar","/docker-gateway-service-0.0.1-SNAPSHOT.jar"]