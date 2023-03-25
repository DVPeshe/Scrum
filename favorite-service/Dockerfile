FROM openjdk:17-oracle
ADD target/favorite-service-2.6.6.jar docker-favorite-service-2.6.6.jar
EXPOSE 8192
ENTRYPOINT ["java","-jar","/docker-favorite-service-2.6.6.jar"]