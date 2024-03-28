package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.repository.ClientRepository;
import ru.kazan.clientservice.repository.UserProfileRepository;
import ru.kazan.clientservice.utils.enums.ClientStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ClientRepository clientRepository;

    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public JwtResponse loginWithPassword(){
        return null;
    }

    @Transactional()
    public void createNewClient(RegistrationClientDto dto){
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

}
