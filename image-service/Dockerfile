FROM openjdk:17-oracle
ADD target/image-service-2.6.6.jar docker-image-service-2.6.6.jar
EXPOSE 8192
ENTRYPOINT ["java","-jar","/docker-image-service-2.6.6.jar"]