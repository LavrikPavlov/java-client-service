package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.mapper.ClientMapper;
import ru.kazan.clientservice.model.Address;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.repository.AddressRepository;
import ru.kazan.clientservice.repository.ClientRepository;

import java.util.UUID;

@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final AddressRepository addressRepository;

    private final ClientMapper mapper;

    public ClientService(ClientRepository clientRepository, AddressRepository addressRepository,
                         ClientMapper mapper) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.mapper = mapper;
    }

    public ResponseShortInfoDto getShortInfoClient(RequestInfoDto dto){
        log.info("Get short info about Client");

        return clientRepository.findById(UUID.fromString(dto.getId()))
                .map(mapper::toShrotInfoDto)
                .orElseThrow(() -> new RuntimeException("Пользователя нет"));
    }

    public Client getFullInfoClient(RequestInfoDto dto){
        log.info("Get full info about Client");

        return clientRepository.findFullInfoClientById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Пользователя нет"));
    }

    @Transactional
    public void changeEmail(RequestEditEmailDto dto){
        Client client = clientRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow( () -> new RuntimeException("Пользователя нет"));

        client.setEmail(dto.getEmail());
        clientRepository.save(client);
        log.info("Successful change client's email");
    }

    @Transactional
    public void changeMobilePhone(RequestEditMobilePhoneDto dto){
        Client client = clientRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow( () -> new RuntimeException("Пользователя нет"));

        client.setMobilePhone(dto.getMobilePhone());
        clientRepository.save(client);
        log.info("Successful change client's mobile phone");
    }

//    @Transactional
//    public void addNewAddress(NewAddressDto dto){
//
//        Address address = addressRepository.findLikeAddress(dto.getCountry(), dto.getCity(), dto.getStreet(), dto.getHouse(), dto.getApartment())
//                .ifPresentOrElse(presentAddress -> {
//                    clientRepository.save(clientRepository.findById(UUID.fromString(dto.getId()))
//                            .orElseThrow(() -> new RuntimeException("Пользователя нет")));
//        }
//        )
//    }

}
