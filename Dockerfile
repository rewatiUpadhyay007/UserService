FROM openjdk:8
ADD target/UserService.jar UserService.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","UserService.jar"]