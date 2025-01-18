
# Flight Price

## Overview

The **Flight Price System** is a Spring Boot application designed to retrieve and aggregate flight information from multiple partners. It provides functionality for querying flights based on origin, destination, and date, while ensuring optimal performance with caching and asynchronous processing.

## Features

- **Partner Integration**: Aggregates flight data from multiple partners.
- **Asynchronous Processing**: Uses `CompletableFuture` for concurrent calls to partner APIs.
- **Caching**: Configured with Caffeine cache for efficient data retrieval.
- **H2 Database**: An in-memory database for development and testing.
- **Spring Data JPA**: Simplifies database operations with repository support.

---

## Project Structure

```plaintext
src/
├── main/
│   ├── java/com/mhdi/flightPrice/
│   │   ├── controller/       # REST controllers
│   │   ├── model/            # Domain models (e.g., Flight)
│   │   ├── repository/       # Data access layer (JPA repositories)
│   │   ├── service/          # Business logic (e.g., PartnerService)
│   │   └── FlightPriceApplication.java # Main application class
│   └── resources/
│       ├── application.yml   # Spring configuration
│       ├── static/           # Static resources (if applicable)
│       └── templates/        # Templates for web views (if applicable)
├── test/
│   └── java/com/mhdi/flightPrice/
│       ├── controller/
│       ├── repository/       # Repository unit tests
│       └── service/          # Service unit tests
│   └── resources/
│       └── test-data.sql     # Test data for H2 database
├── Dockerfile                # Dockerfile for containerization
└── docker-compose.yml        # Compose file for running services
```

---

## Prerequisites

- Java 21
- Maven 3.6+
- Docker (optional, for containerization)

---

## Getting Started

### Running the Application Locally

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/mhdi-esmi/flightPrice.git
   cd flightprice
   ```

2. **Build the Project**:

   ```bash
   mvn clean install
   ```

3. **Run the Application**:

   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**:

   - API Base URL: [http://localhost:8080](http://localhost:8080)
   - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (username: `sa`, no password)

### Running with Docker

1. **Build the Docker Image**:

   ```bash
   docker build -t flightprice .
   ```

2. **Run with Docker Compose**:

   ```bash
   docker-compose up
   ```

3. **Access the Application**:

   - API Base URL: [http://localhost:8080](http://localhost:8080)

---

## Configuration

### Application Properties (`application.yml`)

Key configurations:

- **Database**:
  ```yaml
  spring:
    datasource:
      url: jdbc:h2:mem:flightdb
      driver-class-name: org.h2.Driver
    jpa:
      hibernate:
        ddl-auto: create
  ```

- **H2 Console**:
  ```yaml
  spring:
    h2:
      console:
        enabled: true
        path: /h2-console
  ```

---

## Testing

- **Run Unit Tests**:
  ```bash
  mvn test
  ```
- Test data is loaded from `src/test/resources/test-data.sql`.

---

## API Endpoints

### Flight Endpoints

1. **Get Flights**:
   - `GET /flights?origin={origin}&destination={destination}&date={date}`
   - Example:
     ```http
     GET /flights?origin=TEH&destination=LON&date=2025-01-11
     ```

### Partner Service

Simulated partner APIs:

- Partner A, B, C: Returns mock flight data for testing.

---

## Caching

- Caffeine cache is used for caching frequently accessed data.
- Configurations can be adjusted in `application.yml`.

---

## Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/flightPrice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
```

---

## Logs

Logs are stored in `logs/flight-price-system.log`. Adjust logging levels in `application.yml`.

---

## Future Enhancements

- Add real-time partner API integrations.
- Implement rate-limiting for API calls.
- Enhance test coverage with integration tests.

---



