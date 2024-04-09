package ru.kazan.clientservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.service.ClientService;
import ru.kazan.clientservice.service.SessionService;
import ru.kazan.clientservice.utils.security.JwtProvider;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private ClientService clientService;

    @Mock
    private UserProfile userProfile;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("SessionService: Test")
    void test() {
        assertTrue(true);
    }
}
