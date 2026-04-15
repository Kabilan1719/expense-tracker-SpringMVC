# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Copy jar
COPY target/expense-tracker-0.0.1-SNAPSHOT.jar app.jar

# Run app
ENTRYPOINT ["java","-jar","/app.jar"]