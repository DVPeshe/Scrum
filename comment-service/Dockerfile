FROM openjdk:17-oracle
ADD target/comment-service-2.6.6.jar docker-comment-service-2.6.6.jar
EXPOSE 8195
ENTRYPOINT ["java","-jar","/docker-comment-service-2.6.6.jar"]