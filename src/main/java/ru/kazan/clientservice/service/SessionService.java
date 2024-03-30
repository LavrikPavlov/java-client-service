package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kazan.clientservice.dto.jwt.JwtSessionToken;
import ru.kazan.clientservice.dto.session.*;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.repository.ClientRepository;
import ru.kazan.clientservice.repository.UserProfileRepository;
import ru.kazan.clientservice.utils.enums.RoleEnum;
import ru.kazan.clientservice.utils.security.JwtProvider;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {

    private static final Integer MIN_CODE_VALUE = 100_000;
    private static final Integer MAX_CODE_VALUE = 999_999;
    private static final Random RANDOM_VALUE = new SecureRandom();

    private final ClientRepository clientRepository;

    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    @Transactional
    public void verifyCode(TypeCodeSendDto dto) {
        Client client = getClientContact(dto.getContact());
        updateOrCreateUserProfile(client);
    }

    public JwtSessionToken getSessionToken(CodeDto dto){
        UUID clientId = getClientContact(dto.getContact()).getId();

        UserProfile user = userProfileRepository.findByClientId(clientId)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));

        if(user.getLastCode().equals(dto.getVerifyCode()))
            return new JwtSessionToken(jwtProvider.genSessionToken(user));
        else
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);
    }

    @Transactional
    public void setNewPasswordClient(NewPasswordDto dto, String sessionToken){
        jwtProvider.validateToken(sessionToken);
        UUID clientId = jwtProvider.getClientIdFromToken(sessionToken);
        UserProfile user = getUserByClientId(clientId);
        setPasswordWithEncoder(user, dto.getNewPassword());

    }

    private void setPasswordWithEncoder(UserProfile user, String password){
        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        userProfileRepository.save(user);
    }

    private void updateOrCreateUserProfile(Client client) {
        String code = genNewCode();

        UserProfile user = userProfileRepository.findByClientId(client.getId())
                        .orElse(new UserProfile(
                                client.getId(),
                                client,
                                null,
                                RoleEnum.NOT_CLIENT,
                                null
                        ));

        user.setLastCode(code);
        userProfileRepository.save(user);
    }

    private UserProfile getUserByClientId(UUID clientId){
        return  userProfileRepository.findByClientId(clientId)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST,
                        "Don't have user profile from client"));
    }

    private Client getClientContact(String contact){
        return clientRepository.findClientByEmailOrMobilePhone(contact)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.NOT_FOUND,
                        "Don't have user with this contact " + contact));
    }

    private String genNewCode() {
        Integer code;
        do {
            code = RANDOM_VALUE.nextInt(MIN_CODE_VALUE, MAX_CODE_VALUE);
        } while (isRepeatedDigits(code));

        return String.valueOf(code);
    }

    private boolean isRepeatedDigits(int number) {
        String numberStr = String.valueOf(number);
        char firstDigit = numberStr.charAt(0);
        for (int i = 1; i < numberStr.length(); i++) {
            if (numberStr.charAt(i) != firstDigit) {
                return false;
            }
        }
        return true;
    }

}
