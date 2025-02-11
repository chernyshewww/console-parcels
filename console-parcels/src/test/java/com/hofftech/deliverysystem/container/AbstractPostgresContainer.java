package com.hofftech.deliverysystem.container;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(SpringExtension.class)
public abstract class AbstractPostgresContainer {

    final static String POSTGRES_VERSION = "postgres:latest";
    final static String DATABASE_NAME = "your db";
    final static String DATABASE_USER = "your user";
    final static String DATABASE_PASSWORD = "your password";

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(POSTGRES_VERSION)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USER)
            .withPassword(DATABASE_PASSWORD);

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = "jdbc:postgresql://"
                + postgresContainer.getHost()
                + ":" + postgresContainer.getMappedPort(5432)
                + "/" + postgresContainer.getDatabaseName();

        registry.add("spring.datasource.url", () -> jdbcUrl);
    }
}