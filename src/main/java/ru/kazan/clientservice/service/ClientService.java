package ru.kazan.clientservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.clientservice.dto.client.RequestInfoDto;
import ru.kazan.clientservice.dto.client.ResponseShortInfoDto;
import ru.kazan.clientservice.mapper.ClientMapper;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.repository.ClientRepository;

import java.util.UUID;

@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper mapper;

    public ClientService(ClientRepository clientRepository, ClientMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    public ResponseShortInfoDto getShortInfoClient(RequestInfoDto dto){
        return clientRepository.findById(UUID.fromString(dto.getId()))
                .map(mapper::toShrotInfoDto)
                .orElseThrow(() -> new RuntimeException("Пользователя нет"));
    }

    public Client getFullInfoClient(RequestInfoDto dto){
        return clientRepository.findFullInfoClientById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Пользователя нет"));
    }

}
