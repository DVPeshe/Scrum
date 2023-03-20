FROM openjdk:17-oracle
ADD target/front-service-2.6.6.jar docker-front-service-2.6.6.jar
EXPOSE 3000
ENTRYPOINT ["java","-jar","/docker-front-service-2.6.6.jar"]