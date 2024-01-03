package com.binno.dominio;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(properties = "spring.profiles.active:test")
@AutoConfigureMockMvc
public abstract class ApplicationConfigIT {
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void pgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }
//    @AfterAll
//    static void afterAll() {
//        postgreSQLContainer.stop();
//    }
}
