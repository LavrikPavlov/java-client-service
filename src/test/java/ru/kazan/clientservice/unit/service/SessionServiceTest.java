package ru.kazan.clientservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.dto.jwt.JwtSessionToken;
import ru.kazan.clientservice.dto.session.*;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.repository.ClientRepository;
import ru.kazan.clientservice.repository.UserProfileRepository;
import ru.kazan.clientservice.service.SessionService;
import ru.kazan.clientservice.utils.enums.RoleEnum;
import ru.kazan.clientservice.utils.security.JwtProvider;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    private UUID clientId;
    private String accessToken;
    private String sessionTokenEmail;
    private String sessionTokenMobile;
    private UserProfile userProfile;
    private Client client;

    @BeforeEach
    void setUp() {
        clientId = UUID.fromString(TestClientConstants.CLIENT_ID_FOR_SESSION);
        sessionTokenEmail = "session_token_email";
        sessionTokenMobile = "session_token_mobile";
        accessToken = "Bearer access_token";
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

    @ParameterizedTest
    @ValueSource(strings = {"email", "mobile"})
    @DisplayName("SessionService: Send verify code with correct data and return OK")
    void sendVerifyCode_withCorrectData_thenReturnOK(String type) {
        String contact = null;

        if (type.equals("email"))
            contact = "test@test.ru";
        if (type.equals("mobile"))
            contact = "89112223344";

        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type(type)
                .contact(contact)
                .build();

        when(clientRepository.findClientByEmailOrMobilePhone(dto.getContact())).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));

        sessionService.verifyCode(dto);

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(dto.getContact());
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(userProfileRepository, times(1)).save(userProfile);

    }

    @Test
    @DisplayName("SessionService: Send verify code with not exist client and return Exception")
    void sendVerifyCode_withNotCorrectData_thenReturnException() {
        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type("type")
                .contact("contact")
                .build();

        when(clientRepository.findClientByEmailOrMobilePhone(dto.getContact())).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.verifyCode(dto));

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(dto.getContact());
        assertEquals(ExceptionEnum.NOT_FOUND, exception.getExceptionEnum());
        assertEquals("Don't have user with this contact " + dto.getContact(),
                exception.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "mobile"})
    @DisplayName("SessionService: Send verify code with client who dont have UserProfile and return new UserProfile")
    void sendVerifyCode_withClientWithoutUserProfile_thenNewUserProfile(String type) {
        String contact = null;

        if (type.equals("email"))
            contact = "test@test.ru";
        if (type.equals("mobile"))
            contact = "89112223344";

        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type(type)
                .contact(contact)
                .build();

        when(clientRepository.findClientByEmailOrMobilePhone(dto.getContact())).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        sessionService.verifyCode(dto);

        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

        verify(clientRepository, times(1)).findClientByEmailOrMobilePhone(dto.getContact());
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(userProfileRepository, times(1)).save(userProfileCaptor.capture());

        assertNotNull(userProfileCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "mobile"})
    @DisplayName("SessionService: Check verify with correct code and return JwtSessionToken")
    void getSessionToken_withCorrectCode_thenReturnJwtSessionToken(String type) {
        CodeDto codeDto = null;
        String code = "123456";
        String token = null;

        if (type.equals("email")) {
            codeDto = EmailWithCodeDtoImpl.builder()
                    .verifyCode(code)
                    .contact("test@test.ru")
                    .build();
            token = sessionTokenEmail;
        }

        if (type.equals("mobile")) {
            codeDto = MobilePhoneCodeDtoImpl.builder()
                    .contact("89112223344")
                    .verifyCode(code)
                    .build();
            token = sessionTokenMobile;
        }

        when(clientRepository.findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact()))
                .thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(jwtProvider.genSessionTokenType(userProfile, type)).thenReturn(token);

        JwtSessionToken jwtSessionToken = sessionService.getSessionToken(codeDto, type);

        verify(clientRepository, times(1))
                .findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact());
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(jwtProvider, times(1)).genSessionTokenType(userProfile, type);

        assertEquals(jwtSessionToken.getSessionToken(), token);

    }

    @Test
    @DisplayName("SessionService: Check verify with client who not exist and return Exception")
    void getSessionToken_clientDontHaveContact_thenReturnException() {
        String type = "type";
        CodeDto codeDto = EmailWithCodeDtoImpl.builder()
                .verifyCode("123456")
                .contact("test@test.ru")
                .build();


        when(clientRepository.findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact()))
                .thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.getSessionToken(codeDto, type));

        verify(clientRepository, times(1))
                .findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact());

        assertEquals(ExceptionEnum.NOT_FOUND, exception.getExceptionEnum());
        assertEquals("Don't have user with this contact " + codeDto.getContact(),
                exception.getErrorMessage());
    }

    @Test
    @DisplayName("SessionService: Check verify when client don't have UserProfile and return Exception")
    void getSessionToken_clientDontHaveUserProfile_thenReturnException() {
        String type = "type";
        CodeDto codeDto = EmailWithCodeDtoImpl.builder()
                .verifyCode("123456")
                .contact("test@test.ru")
                .build();

        when(clientRepository.findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact()))
                .thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.getSessionToken(codeDto, type));

        verify(clientRepository, times(1))
                .findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact());

        verify(userProfileRepository, times(1)).findByClientId(clientId);


        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals(ExceptionEnum.BAD_REQUEST.getErrorMessage(), exception.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"000000", "111111"})
    @DisplayName("SessionService: Check verify when verify code not correct and return Exception")
    void getSessionToken_verifyCodeNotCorrect_thenReturnException(String code) {
        String type = "type";

        EmailWithCodeDtoImpl codeDto = EmailWithCodeDtoImpl.builder()
                .verifyCode(code)
                .contact("test@test.ru")
                .build();


        when(clientRepository.findClientByEmailOrMobilePhone(codeDto.getContact()))
                .thenReturn(Optional.of(client));
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.getSessionToken(codeDto, type));

        verify(clientRepository, times(1))
                .findClientByEmailOrMobilePhone(Objects.requireNonNull(codeDto).getContact());
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Not correct verification code", exception.getErrorMessage());
    }

    @Test
    @DisplayName("SessionService: Set password for new client and return OK")
    void setPasswordForNewClient_thenReturnOK() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();
        String encodePassword = "EncodePassword";

        when(jwtProvider.validateToken(sessionTokenEmail)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken(sessionTokenEmail)).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.encode(dto.getNewPassword())).thenReturn(encodePassword);

        sessionService.setNewPasswordClient(dto, sessionTokenEmail);

        verify(jwtProvider, times(1)).validateToken(sessionTokenEmail);
        verify(jwtProvider, times(1)).getClientIdFromToken(sessionTokenEmail);
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).encode(dto.getNewPassword());

        assertEquals(encodePassword, userProfile.getPassword());
    }

    @Test
    @DisplayName("SessionService: Set password when session token not valid and return Exception")
    void setPasswordForNewClient_whenTokenNotValid_thenReturnException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        when(jwtProvider.validateToken(sessionTokenEmail)).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.setNewPasswordClient(dto, sessionTokenEmail));

        verify(jwtProvider, times(1)).validateToken(sessionTokenEmail);
        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Invalid session token", exception.getErrorMessage());

    }

    @Test
    @DisplayName("SessionService: Set password when client dont have UserProfile and return Exception")
    void setPasswordForNewClient_whenClientDontHaveUserProfile_thenReturnException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        when(jwtProvider.validateToken(sessionTokenEmail)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken(sessionTokenEmail)).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.setNewPasswordClient(dto, sessionTokenEmail));

        verify(jwtProvider, times(1)).validateToken(sessionTokenEmail);
        verify(jwtProvider, times(1)).getClientIdFromToken(sessionTokenEmail);
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Don't have user profile from client", exception.getErrorMessage());
    }

    @Test
    @DisplayName("SessionService: Set password when password is empty and return Exception")
    void setPasswordForNewClient_whenPasswordEmpty_thenReturnException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("")
                .build();

        when(jwtProvider.validateToken(sessionTokenEmail)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken(sessionTokenEmail)).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.setNewPasswordClient(dto, sessionTokenEmail));

        verify(jwtProvider, times(1)).validateToken(sessionTokenEmail);
        verify(jwtProvider, times(1)).getClientIdFromToken(sessionTokenEmail);
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.FORBIDDEN, exception.getExceptionEnum());
        assertEquals("Password is EMPTY", exception.getErrorMessage());
    }

    @Test
    @DisplayName("SessionService: Change password for valid client then return OK")
    void changePasswordForValidClient_thenReturnOK() {
        String encodePassword = "encodePassword";

        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        when(jwtProvider.validateToken(sessionTokenMobile)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken("access_token")).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        doReturn(false).when(passwordEncoder).matches(dto.getNewPassword(), "password");
        when(passwordEncoder.encode(dto.getNewPassword())).thenReturn(encodePassword);

        sessionService.changePasswordClient(dto, accessToken, sessionTokenMobile);

        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

        verify(jwtProvider, times(1)).validateToken(sessionTokenMobile);
        verify(jwtProvider, times(1)).getClientIdFromToken("access_token");
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).matches(dto.getNewPassword(), "password");
        verify(passwordEncoder, times(1)).encode(dto.getNewPassword());
        verify(userProfileRepository, times(1)).save(userProfileCaptor.capture());

        assertEquals(userProfileCaptor.getValue().getPassword(), encodePassword);
    }

    @Test
    @DisplayName("SessionService: Change password when session token not valid and return Exception")
    void changePasswordForValidClient_whenTokenNotValid_thenException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        when(jwtProvider.validateToken(sessionTokenMobile)).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.changePasswordClient(dto, accessToken, sessionTokenMobile));

        verify(jwtProvider, times(1)).validateToken(sessionTokenMobile);

        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Invalid session token", exception.getErrorMessage());

    }

    @Test
    @DisplayName("SessionService: Change password when client dont have UserProfile and return Exception")
    void changePasswordForValidClient_whenClientDontHaveUserProfile_thenException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        when(jwtProvider.validateToken(sessionTokenMobile)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken("access_token")).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.changePasswordClient(dto, accessToken, sessionTokenMobile));

        verify(jwtProvider, times(1)).validateToken(sessionTokenMobile);
        verify(jwtProvider, times(1)).getClientIdFromToken("access_token");
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.BAD_REQUEST, exception.getExceptionEnum());
        assertEquals("Don't have user profile from client", exception.getErrorMessage());
    }

    @Test
    @DisplayName("SessionService: Change password when password is empty and return Exception")
    void changePasswordForValidClient_whenPasswordEmpty_thenException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("")
                .build();

        when(jwtProvider.validateToken(sessionTokenMobile)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken("access_token")).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.changePasswordClient(dto, accessToken, sessionTokenMobile));

        verify(jwtProvider, times(1)).validateToken(sessionTokenMobile);
        verify(jwtProvider, times(1)).getClientIdFromToken("access_token");
        verify(userProfileRepository, times(1)).findByClientId(clientId);

        assertEquals(ExceptionEnum.FORBIDDEN, exception.getExceptionEnum());
        assertEquals("Password is EMPTY", exception.getErrorMessage());
    }

    @Test
    @DisplayName("SessionService: Change password when password is same and return Exception")
    void changePasswordForValidClient_whenPasswordSame_thenException() {
        PasswordDto dto = PasswordDto.builder()
                .newPassword("password")
                .build();

        when(jwtProvider.validateToken(sessionTokenMobile)).thenReturn(true);
        when(jwtProvider.getClientIdFromToken("access_token")).thenReturn(clientId);
        when(userProfileRepository.findByClientId(clientId)).thenReturn(Optional.of(userProfile));
        doReturn(true).when(passwordEncoder).matches(dto.getNewPassword(), "password");

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> sessionService.changePasswordClient(dto, accessToken, sessionTokenMobile));

        verify(jwtProvider, times(1)).validateToken(sessionTokenMobile);
        verify(jwtProvider, times(1)).getClientIdFromToken("access_token");
        verify(userProfileRepository, times(1)).findByClientId(clientId);
        verify(passwordEncoder, times(1)).matches(dto.getNewPassword(), "password");

        assertEquals(ExceptionEnum.CONFLICT, exception.getExceptionEnum());
        assertEquals("Password is SAME", exception.getErrorMessage());
    }

}
