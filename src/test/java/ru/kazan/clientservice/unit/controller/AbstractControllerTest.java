package ru.kazan.clientservice.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.kazan.clientservice.config.SecurityConfiguration;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.controller.ClientController;
import ru.kazan.clientservice.handler.GlobalExceptionHandler;
import ru.kazan.clientservice.service.ClientService;
import ru.kazan.clientservice.service.SessionService;
import ru.kazan.clientservice.service.UserDetail;
import ru.kazan.clientservice.service.UserService;
import ru.kazan.clientservice.utils.filter.JwtFilter;
import ru.kazan.clientservice.utils.security.JwtProvider;


@Import(value = {
        SecurityConfiguration.class,
        JwtFilter.class,
        UserDetail.class,
        GlobalExceptionHandler.class
})
@WebMvcTest(controllers = ClientController.class)
@EnableWebSecurity
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ClientService clientService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected SessionService sessionService;

    @SpyBean
    protected ObjectMapper objectMapper;

    @SpyBean
    protected JwtProvider jwtProvider;

    protected String sessionTokenMobile;
    protected String sessionTokenEmail;
    protected String accessToken;
    protected String refreshToken;

    @BeforeEach
    void setUp(){
        sessionTokenMobile = jwtProvider.genSessionTokenType(TestClientConstants.USER_PROFILE_DEFAULT, "mobile");
        sessionTokenEmail = jwtProvider.genSessionTokenType(TestClientConstants.USER_PROFILE_DEFAULT, "email");
        accessToken =  jwtProvider.genAccessToken(TestClientConstants.USER_PROFILE_DEFAULT);
        refreshToken = jwtProvider.genRefreshToken(TestClientConstants.USER_PROFILE_DEFAULT);
    }
}
