# Rank Monkey Service

## Overview
Rank Monkey Service is a Spring Boot application designed to manage user authentication, authorization, and skill management. It includes features such as JWT-based authentication, password management, and skill creation.

## Technologies Used
- Java
- Spring Boot
- Maven
- JWT (JSON Web Tokens)
- Jakarta Servlet
- SLF4J (Simple Logging Facade for Java)

## Project Structure
- `src/main/java/com/rankmonkeysvc/auth/impl/JwtSvcImpl.java`: Implementation of JWT service for token generation and validation.
- `src/main/java/com/rankmonkeysvc/services/impl/AuthSvcImpl.java`: Implementation of authentication services including password management and token exchange.
- `src/main/java/com/rankmonkeysvc/services/impl/SkillSvcImpl.java`: Implementation of skill management services.
- `src/main/java/com/rankmonkeysvc/services/EventLogSvc.java`: Interface for event logging service.

## Setup and Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/rank_monkey_svc.git
    cd rank_monkey_svc
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Configuration
- Ensure you have the necessary environment variables set for database connection and JWT secret key.
- Update the `application.properties` file with your database configuration.

## Usage
- The application provides endpoints for user authentication, password management, and skill management.
- Use tools like Postman to test the API endpoints.

## Logging
- The application uses SLF4J for logging. Logs are configured in the `logback.xml` file.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License
This project is licensed under the MIT License.