package ru.kazan.clientservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.kazan.clientservice.config.TestConfig;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.utils.security.JwtProvider;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = TestConfig.class)
@Rollback
@Transactional
public abstract class AbstractIntegrationTest {

    @Autowired
    protected PostgreSQLContainer<?> postgres;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtProvider jwtProvider;

    @LocalServerPort
    protected Integer port;

    protected String accessToken;
    protected String sessionTokenMobile;
    protected String sessionTokenEmail;
    protected String refreshToken;

    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "http://localhost:" + port;

        accessToken = jwtProvider.genAccessToken(TestClientConstants.USER_PROFILE_DEFAULT);
        sessionTokenMobile = jwtProvider.genSessionToken(TestClientConstants.USER_PROFILE_DEFAULT, "mobile");
        sessionTokenEmail = jwtProvider.genSessionToken(TestClientConstants.USER_PROFILE_DEFAULT, "email");
        refreshToken = jwtProvider.genRefreshToken(TestClientConstants.USER_PROFILE_DEFAULT);
    }

    @Test
    @DisplayName("Postgres test container start")
    public void checkPostgresContainerIsRunning(){
        assertThat(postgres.isRunning()).isTrue();
    }

    protected <T> String getJsonFromObject(T dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
