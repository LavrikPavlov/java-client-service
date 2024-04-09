package ru.kazan.clientservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.repository.ClientRepository;
import ru.kazan.clientservice.repository.UserProfileRepository;
import ru.kazan.clientservice.service.UserService;
import ru.kazan.clientservice.utils.enums.ClientStatus;
import ru.kazan.clientservice.utils.enums.RoleEnum;
import ru.kazan.clientservice.utils.security.JwtProvider;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserService userService;

    private UUID clientId;
    private String refreshToken;
    private UserProfile userProfile;
    private Client client;

    @BeforeEach
    public void setUp() {
        clientId = UUID.fromString(TestClientConstants.CLIENT_ID_FOR_SESSION);
        refreshToken = "refresh_token";
        userProfile = UserProfile.builder()
                .role(RoleEnum.CLIENT)
                .client(TestClientConstants.CLIENT_DEFAULT_FOR_SESSION)
                .clientId(UUID.fromString(TestClientConstants.CLIENT_ID_FOR_SESSION))
                .lastCodeEmail("123456")
                .lastCodeMobile("654321")
                .password("password")
                .refreshToken("refresh_token")
                .build();

        client = TestClientConstants.CLIENT_DEFAULT_FOR_SESSION;
    }

    @Test
    @DisplayName("UserService: Refresh token should return JwtResponse")
    void refreshToken_ExistUser_returnResponseDto() {
        when(jwtProvider.getClientIdFromToken(refreshToken)).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(jwtProvider.genAccessToken(userProfile)).thenReturn("new_access_token");
        when(jwtProvider.genRefreshToken(userProfile)).thenReturn("new_refresh_token");

        JwtResponse result = userService.refreshToken(refreshToken);

        verify(jwtProvider,times(1)).getClientIdFromToken(refreshToken);
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(jwtProvider,times(1)).genAccessToken(userProfile);
        verify(jwtProvider,times(1)).genRefreshToken(userProfile);
        verify(userProfileRepository,times(1)).save(userProfile);

        assertEquals("new_access_token", result.getAccessToken());
        assertEquals("new_refresh_token", result.getRefreshToken());

    }

    @Test
    @DisplayName("UserService: Refresh token with notCorrectToken should return Exception")
    void refreshToken_NotCorrectToken_returnException() {

        when(jwtProvider.getClientIdFromToken(refreshToken)).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.refreshToken(refreshToken));

        verify(jwtProvider,times(1)).getClientIdFromToken(refreshToken);
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Not verify client", exception.getErrorMessage());

    }

    @Test
    @DisplayName("UserService: Refresh token which not equel with token in DB should return Exception")
    void refreshToken_tokenNotEqualInDB_returnException() {
        userProfile.setRefreshToken("another_token");

        when(jwtProvider.getClientIdFromToken(refreshToken)).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId))
                .thenReturn(Optional.of(userProfile));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.refreshToken(refreshToken));

        verify(jwtProvider,times(1)).getClientIdFromToken(refreshToken);
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.UNAUTHORIZED, exception.getExceptionEnum());
        assertEquals("Refresh token not is invalid", exception.getErrorMessage());

    }


    @Test
    @DisplayName("UserService: Login with correct data should return JwtResponse")
    void authUserEmailAndMobile_withCorrectData_thenReturnResponseDto() {
        String login = "test";
        String password = "password";

        when(clientRepository.findClientByEmailOrMobilePhone(login)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(password, userProfile.getPassword())).thenReturn(true);
        when(jwtProvider.genAccessToken(userProfile)).thenReturn("new_access_token");
        when(jwtProvider.genRefreshToken(userProfile)).thenReturn("new_refresh_token");

        JwtResponse result = userService.loginWithEmailOrMobilePhone(login, password);

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(login);
        verify(userProfileRepository,times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).matches(password, userProfile.getPassword());
        verify(jwtProvider,times(1)).genAccessToken(userProfile);
        verify(jwtProvider,times(1)).genRefreshToken(userProfile);
        verify(userProfileRepository,times(1)).save(userProfile);

        assertEquals("new_access_token", result.getAccessToken());
        assertEquals("new_refresh_token", result.getRefreshToken());

    }

    @Test
    @DisplayName("UserService: Login with not correct user's login should return Exception")
    void authUserEmailAndMobile_notCorrectLogin_thenReturnException() {
        String login = "test";
        String password = "password";

        when(clientRepository.findClientByEmailOrMobilePhone(login)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.loginWithEmailOrMobilePhone(login, password));

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(login);


        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Not correct login", exception.getErrorMessage());
    }

    @Test
    @DisplayName("UserService: Login with not correct user's password should return Exception")
    void authUserEmailAndMobile_notCorrectPassword_thenReturnException() {
        String login = "test";
        String password = "password";

        when(clientRepository.findClientByEmailOrMobilePhone(login)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(password, userProfile.getPassword())).thenReturn(false);


        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.loginWithEmailOrMobilePhone(login, password));

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(login);
        verify(userProfileRepository,times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).matches(password, userProfile.getPassword());


        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Password is INCORRECT", exception.getErrorMessage());
    }

    @Test
    @DisplayName("UserService: Login with user's password dont set in DB should return Exception")
    void authUserEmailAndMobile_passwordUserNotSetUp_thenReturnException() {
        String login = "test";
        String password = "password";
        userProfile.setPassword("");

        when(clientRepository.findClientByEmailOrMobilePhone(login)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));


        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.loginWithEmailOrMobilePhone(login, password));

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(login);
        verify(userProfileRepository,times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.FORBIDDEN, exception.getExceptionEnum());
        assertEquals("Password is NULL", exception.getErrorMessage());
    }

    @Test
    @DisplayName("UserService: Passport login with correct data should return JwtResponse")
    void authUserPassport_withCorrectData_thenReturnResponseDto() {
        String login = "1111222333";
        String password = "password";

        String serial = login.substring(0, 4);
        String number = login.substring(4, 10);

        when(clientRepository.findClientByPassportSerialNumber(serial, number)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(password, userProfile.getPassword())).thenReturn(true);
        when(jwtProvider.genAccessToken(userProfile)).thenReturn("new_access_token");
        when(jwtProvider.genRefreshToken(userProfile)).thenReturn("new_refresh_token");

        JwtResponse result = userService.loginWithPassport(login, password);

        verify(clientRepository, times(1)).findClientByPassportSerialNumber(serial, number);
        verify(userProfileRepository,times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).matches(password, userProfile.getPassword());
        verify(jwtProvider,times(1)).genAccessToken(userProfile);
        verify(jwtProvider,times(1)).genRefreshToken(userProfile);
        verify(userProfileRepository,times(1)).save(userProfile);

        assertEquals("new_access_token", result.getAccessToken());
        assertEquals("new_refresh_token", result.getRefreshToken());
    }

    @Test
    @DisplayName("UserService: Passport login with not correct user's passport data should return Exception")
    void authUserPassport_withNotCorrectPassportData_thenReturnException() {
        String login = "1111222333";
        String password = "password";

        String serial = login.substring(0, 4);
        String number = login.substring(4, 10);

        when(clientRepository.findClientByPassportSerialNumber(serial, number)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.loginWithPassport(login, password));

        verify(clientRepository, times(1)).findClientByPassportSerialNumber(serial, number);

        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Not Correct login", exception.getErrorMessage());
    }

    @Test
    @DisplayName("UserService: Passport login with user's password dont set in DB should return Exception")
    void authUserPassport_passwordUserNotSetUp_thenReturnException() {
        String login = "1111222333";
        String password = "password";
        String serial = login.substring(0, 4);
        String number = login.substring(4, 10);

        when(clientRepository.findClientByPassportSerialNumber(serial, number)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(password, userProfile.getPassword())).thenReturn(false);


        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.loginWithPassport(login, password));

        verify(clientRepository, times(1)).findClientByPassportSerialNumber(serial, number);
        verify(userProfileRepository,times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).matches(password, userProfile.getPassword());


        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Password is INCORRECT", exception.getErrorMessage());
    }

    @Test
    @DisplayName("UserService: Passport login with user's password which dont match return Exception")
    void authUserPassport_passwordIsNotMatch_thenReturnException() {
        String login = "1111222333";
        String password = "password";
        String serial = login.substring(0, 4);
        String number = login.substring(4, 10);

        userProfile.setPassword("");

        when(clientRepository.findClientByPassportSerialNumber(serial, number)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));


        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.loginWithPassport(login, password));

        verify(clientRepository, times(1)).findClientByPassportSerialNumber(serial, number);
        verify(userProfileRepository,times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.FORBIDDEN, exception.getExceptionEnum());
        assertEquals("Password is NULL", exception.getErrorMessage());
    }

    @Test
    @DisplayName("UserService: Create new Client in DB")
    void createNewClient() {
        RegistrationClientDto dto = new RegistrationClientDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPatronymic("Smith");
        dto.setEmail("john.doe@example.com");
        dto.setMobilePhone("1234567890");
        dto.setAge(30);

        userService.createNewClient(dto);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository, times(1)).save(clientCaptor.capture());

        Client savedClient = clientCaptor.getValue();
        assertEquals(dto.getFirstName(), savedClient.getFirstName());
        assertEquals(dto.getLastName(), savedClient.getLastName());
        assertEquals(dto.getPatronymic(), savedClient.getPatronymic());
        assertEquals(dto.getEmail(), savedClient.getEmail());
        assertEquals(dto.getMobilePhone(), savedClient.getMobilePhone());
        assertEquals(dto.getAge(), savedClient.getAge());
        assertEquals(ClientStatus.NOT_ACCEPT, savedClient.getStatus());
        assertEquals(Date.valueOf(LocalDate.now()), savedClient.getDateRegistration());

        UUID id = savedClient.getId();
        assertNotNull(id);
        assertNotEquals(0L, id.getMostSignificantBits());
        assertNotEquals(0L, id.getLeastSignificantBits());
    }
}
