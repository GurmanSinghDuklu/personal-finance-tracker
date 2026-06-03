package com.gurman.financetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 *
 * @SpringBootApplication is a convenience annotation that combines:
 *   @Configuration       — marks this class as a source of Spring bean definitions
 *   @EnableAutoConfiguration — tells Spring Boot to automatically configure itself
 *                             based on the dependencies on the classpath
 *                             (e.g. seeing spring-data-mongodb → configure MongoDB)
 *   @ComponentScan       — tells Spring to scan this package and all sub-packages
 *                         for @Component, @Service, @Repository, @RestController etc.
 *
 * The main() method launches the embedded Tomcat server and starts the Spring context.
 */
@SpringBootApplication
public class FinanceTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceTrackerApplication.class, args);
    }
}
