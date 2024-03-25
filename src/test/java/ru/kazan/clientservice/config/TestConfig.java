package ru.kazan.clientservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry reg){
        PostgreSQLContainer<?> postgres
                = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

        reg.add("spring.datasource.url", postgres::getJdbcUrl);
        return postgres;
    }
}
