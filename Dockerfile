#Build
FROM maven:3.9-eclipse-temurin-21 as build
COPY src /docker/src
COPY uploads /docker/uploads
COPY pom.xml /docker/pom.xml
WORKDIR /docker
RUN mvn clean && mvn compile && mvn package

FROM eclipse-temurin:21
COPY --from=build /docker/target/*.jar /app/
RUN mkdir /uploads
CMD ["sh", "-c", "java -jar /app/*.jar"]