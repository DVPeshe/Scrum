FROM openjdk:17-oracle
ADD target/email-service-2.6.6.jar docker-email-service-2.6.6.jar
EXPOSE 8193
ENTRYPOINT ["java","-jar","/docker-email-service-2.6.6.jar"]