FROM openjdk:17-oracle
ADD target/cart-service-2.6.6.jar docker-cart-service-2.6.6.jar
EXPOSE 8190
ENTRYPOINT ["java","-jar","/docker-cart-service-2.6.6.jar"]