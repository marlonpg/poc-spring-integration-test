# Spring Integration Testing Example

This project demonstrates Spring MVC integration testing using MockMvc framework based on the Baeldung tutorial.

## Reference
This implementation is based on the tutorial: [Integration Testing in Spring](https://www.baeldung.com/integration-testing-in-spring)

## Project Structure
```
src/
├── main/java/com/example/
│   ├── Application.java              # Main Spring Boot application
│   ├── config/ApplicationConfig.java # Spring configuration
│   ├── controller/
│   │   ├── GreetController.java      # REST controller with endpoints
│   │   └── UserController.java       # User CRUD controller
│   ├── entity/User.java              # JPA entity
│   ├── repository/UserRepository.java # JPA repository
│   ├── service/UserService.java      # Business logic service
│   └── model/Greeting.java           # Response model
└── test/java/com/example/
    ├── GreetControllerIntegrationTest.java        # MockMvc integration tests
    └── UserControllerDatabaseIntegrationTest.java # Full database integration tests
```

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

## Step-by-Step Setup and Execution

### 1. Clone or Download the Project
```bash
git clone <repository-url>
cd poc-spring-integration-test
```

### 2. Build the Project
```bash
./mvnw clean compile
# or on Windows:
mvnw.cmd clean compile
```

### 3. Run the Tests
```bash
./mvnw test
# or on Windows:
mvnw.cmd test
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
# or on Windows:
mvnw.cmd spring-boot:run
```

### 5. Quick Build and Run (Windows)
```bash
build-and-run.bat
```

### 5. Test Endpoints Manually (Optional)
Once the application is running, you can test the endpoints:

**Greeting Endpoints:**
- **Home Page**: `GET http://localhost:8080/` or `http://localhost:8080/homePage`
- **Simple Greeting**: `GET http://localhost:8080/greet`
- **Greeting with Path Variable**: `GET http://localhost:8080/greetWithPathVariable/John`
- **Greeting with Query Parameter**: `GET http://localhost:8080/greetWithQueryVariable?name=John%20Doe`
- **POST Greeting**: `POST http://localhost:8080/greetWithPost`
- **POST with Form Data**: `POST http://localhost:8080/greetWithPostAndFormData` (with form data: id=1&name=John%20Doe)

**User CRUD Endpoints:**
- **Create User**: `POST http://localhost:8080/users` (JSON body: {"name":"John","email":"john@example.com"})
- **Get User by ID**: `GET http://localhost:8080/users/1`
- **Get All Users**: `GET http://localhost:8080/users`
- **Get User by Name**: `GET http://localhost:8080/users/name/John`
- **H2 Console**: `GET http://localhost:8080/h2-console` (JDBC URL: jdbc:h2:mem:testdb)

## Test Cases Covered

### MockMvc Integration Tests (GreetControllerIntegrationTest)
The integration tests demonstrate:

1. **Context Loading**: Verifying WebApplicationContext and ServletContext setup
2. **View Name Testing**: Testing view resolution for JSP pages
3. **JSON Response Testing**: Validating JSON response content and structure
4. **Path Variables**: Testing endpoints with dynamic path parameters
5. **Query Parameters**: Testing endpoints with query string parameters
6. **POST Requests**: Testing POST endpoints
7. **Form Data**: Testing POST endpoints with form parameters

### Full Database Integration Tests (UserControllerDatabaseIntegrationTest)
The database integration tests demonstrate:

1. **End-to-End Testing**: From HTTP request through controller, service, repository to H2 database
2. **CRUD Operations**: Create, Read, Update, Delete operations with database persistence
3. **Database State Verification**: Asserting data is correctly saved and retrieved from database
4. **Transaction Management**: Using @Transactional for test isolation
5. **H2 In-Memory Database**: Using H2 for fast, isolated testing
6. **JSON Serialization**: Testing JSON request/response with database entities

## Key Dependencies

- **Spring Boot Starter Web**: For web application support
- **Spring Boot Starter Data JPA**: For database operations
- **H2 Database**: In-memory database for testing
- **JUnit 5**: For testing framework
- **Spring Test**: For Spring-specific testing utilities
- **MockMvc**: For testing web layer without starting full server

## Testing Features Demonstrated

- `@ExtendWith(SpringExtension.class)`: Enables Spring support in JUnit 5
- `@ContextConfiguration`: Loads Spring application context
- `@WebAppConfiguration`: Loads web application context
- `MockMvc`: Simulates HTTP requests without starting servlet container
- JSON path assertions using `jsonPath()`
- HTTP status and content type validation
- Request parameter and path variable testing

## Running Individual Tests

To run specific test methods:
```bash
./mvnw test -Dtest=GreetControllerIntegrationTest#givenGreetURI_whenMockMVC_thenVerifyResponse
# or on Windows:
mvnw.cmd test -Dtest=GreetControllerIntegrationTest#givenGreetURI_whenMockMVC_thenVerifyResponse
```

## Notes

- Tests use MockMvc which doesn't start a real servlet container
- All endpoints return JSON responses except the home page
- The project uses Spring Boot for simplified configuration
- Integration tests verify the complete web layer functionality