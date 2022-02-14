FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/pokedex
COPY . .
RUN ./gradlew build -x test

FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY --from=build /workspace/pokedex/build/libs/*.jar pokedex.jar

ENTRYPOINT ["java","-jar","pokedex.jar"]