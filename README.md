# Holiday-service
A Spring Boot application that retrieves public holidays using the [Nager.Date API](https://date.nager.at/Api) and exposes REST endpoints for:

- Fetching the last 3 celebrated holidays for a country
- Counting non-weekend public holidays per country
- Finding common holidays between two countries

### Prerequisites
Ensure you have the following installed:

- Java 21
- Maven (or use mvnw Maven Wrapper)

### Build the application

```shell
./mvnw clean install
```

### Run the Application Locally

```shell
./mvnw spring-boot:run
```

or using java once you build the app

```shell
java -jar target/holiday-service-0.0.1-SNAPSHOT.jar
```

You can access the API documentation once you successfully run the application [swagger](http://localhost:8080/swagger-ui/index.html)

### Technologies Used 
- Spring Boot 3.3.9
- Java 21
- RestClient
- CompletableFuture
- Junit


