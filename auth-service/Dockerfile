FROM openjdk:17-oracle
ADD target/auth-service-2.6.6.jar docker-auth-service-2.6.6.jar
EXPOSE 8187
ENTRYPOINT ["java","-jar","/docker-auth-service-2.6.6.jar"]