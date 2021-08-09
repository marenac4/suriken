#### Stage 1: Build the application
FROM adoptopenjdk/openjdk11:alpine as build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .

COPY pom.xml .

COPY src src

RUN ./mvnw package -DskipTests

#### Stage 2: A minimal docker image with command to run the app
FROM adoptopenjdk/openjdk11:alpine-jre
# Copy project dependencies from the build stage
COPY --from=build /app/target/*.jar /app/suriken.jar

EXPOSE 8081

ENTRYPOINT [ \
   "java", \
   "-jar", \
   "/app/suriken.jar" \
   ]
