# Demographic Microservice


## Description

The Demographic Services module provides endpoints for managing demographic data, such as persons and person names.

## Endpoints

- `/demographic/person`: Retrieves all persons
- `/demographic/person/{id}`: Retrieves a person by ID
- `/demographic/person-name`: Retrieves all person names
- `/demographic/person-name/{id}`: Retrieves a person name by ID

## Requirements

- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Lombok

## Configuration

- `spring.datasource.url`: The URL of the MySQL database
- `spring.datasource.username`: The username for the MySQL database
- `spring.datasource.password`: The password for the MySQL database

## Usage

1. Clone the repository
2. Run `mvn clean install` to build the project
3. Run `mvn spring-boot:run` to start the application

## Testing

- The application is tested using JUnit and Mockito
- To run the tests, execute `mvn test`
- To generate the Javadoc, execute `mvn javadoc:javadoc`
- To generate the JaCoCo report, execute `mvn jacoco:report`
- To generate the SonarQube report, execute `mvn sonar:sonar`

## Deployment

- The application is deployed using Docker
- To build the Docker image, execute `docker build -t demographic .`
- To run the Docker container, execute `docker run -p 8050:8050 demographic`
- To push the Docker image to Docker Hub, execute `docker push enumahin/demographic`
- To pull the Docker image from Docker Hub, execute `docker pull enumahin/demographic`
- To run the Docker container, execute `docker run -p 8050:8050 enumahin/demographic`
- To stop the Docker container, execute `docker stop demographic`
- To remove the Docker container, execute `docker rm demographic`
- To remove the Docker image, execute `docker rmi demographic`

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for more information.