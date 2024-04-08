package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.repository.ClientRepository;
import ru.kazan.clientservice.repository.UserProfileRepository;
import ru.kazan.clientservice.utils.enums.ClientStatus;
import ru.kazan.clientservice.utils.security.JwtProvider;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ClientRepository clientRepository;

    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public JwtResponse refreshToken(String token) {

        UUID clientId = jwtProvider.getClientIdFromToken(token);
        UserProfile user = getUserProfile(clientId);

        if(!user.getRefreshToken().equals(token))
            throw new ApplicationException(ExceptionEnum.UNAUTHORIZED, "Refresh token not is invalid");

        JwtResponse response = createResponseJwtWithTokens(user);

        user.setRefreshToken(response.getRefreshToken());
        userProfileRepository.save(user);

        return response;
    }

    @Transactional()
    public void createNewClient(RegistrationClientDto dto) {
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patronymic(dto.getPatronymic())
                .email(dto.getEmail())
                .mobilePhone(dto.getMobilePhone())
                .age(dto.getAge())
                .dateRegistration(Date.valueOf(LocalDate.now()))
                .status(ClientStatus.NOT_ACCEPT)
                .build();

        clientRepository.save(client);
    }


    @Transactional
    public JwtResponse loginWithEmailOrMobilePhone(String login, String password) {
        Client client = clientRepository.findClientByEmailOrMobilePhone(login)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST, "Not correct login"));

        UserProfile user = getUserProfile(client.getId());

        checkPasswordUser(user, password);

       return createResponseJwtWithTokens(user);

    }

    @Transactional
    public JwtResponse loginWithPassport(String login, String password) {
        String serial = login.substring(0, 4);
        String number = login.substring(4, 10);

        Client client = clientRepository.findClientByPassportSerialNumber(serial, number)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST, "Not Correct login"));

        UserProfile user = getUserProfile(client.getId());

        checkPasswordUser(user, password);

        return createResponseJwtWithTokens(user);
    }

    private void checkPasswordUser(UserProfile user, String password) {
        if (user.getPassword().isEmpty()) {
            throw new ApplicationException(ExceptionEnum.FORBIDDEN, "Password is NULL");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST, "Password is INCORRECT");
        }
    }

    private UserProfile getUserProfile(UUID clientId) {
        return userProfileRepository.findByClientId(clientId)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST, "Not verify client"));
    }

    private JwtResponse createResponseJwtWithTokens(UserProfile user) {
        String accessToken = jwtProvider.genAccessToken(user);
        String refreshToken = jwtProvider.genRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userProfileRepository.save(user);

        return new JwtResponse(
                accessToken,
                refreshToken
        );
    }

}
