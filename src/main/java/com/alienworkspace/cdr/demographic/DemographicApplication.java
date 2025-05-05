package com.alienworkspace.cdr.demographic;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the Demographic module.
 * This is the entry point of the Spring Boot application.
 *
 * <p>The application is configured with {@link SpringBootApplication}, which
 * enables auto-configuration, component scanning, and configuration properties support.</p>
 *
 * <p>To run the application, execute the {@link #main(String[])} method.</p>
 *
 * <p>Author: Codeium Engineering Team</p>
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Demographic Microservice",
                version = "v1.0",
                description = "API for Demographic microservice",
                contact = @Contact(
                        name = "Ikechukwu Enumah",
                        email = "enumahin@gmail.com",
                        url = "https://enumahin.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://enumahin.com/cdr-license"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Patients Central Data Repository",
                url = "https://enumahin.com/cdr-page"
        )
)
public class DemographicApplication {

    /**
     * The main entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DemographicApplication.class, args);
    }

}
