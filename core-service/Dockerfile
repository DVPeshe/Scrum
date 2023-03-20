FROM openjdk:17-oracle
ADD target/core-service-2.6.6.jar docker-core-service-2.6.6.jar
EXPOSE 8189
ENTRYPOINT ["java","-jar","/docker-core-service-2.6.6.jar"]