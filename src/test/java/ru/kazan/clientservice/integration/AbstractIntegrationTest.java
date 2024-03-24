package ru.kazan.clientservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.kazan.clientservice.config.TestConfig;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = TestConfig.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    private PostgreSQLContainer<?> postgres;

    @BeforeEach
    void setUp(){

    }

    @Test
    @DisplayName("Postgres test container start")
    public void checkPostgresContainerIsRunning(){
        assertThat(postgres.isRunning()).isTrue();
    }
}
