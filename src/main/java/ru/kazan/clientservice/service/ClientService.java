package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.mapper.AddressMapper;
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

    private final ClientMapper clientMapper;

    private final AddressMapper addressMapper;

    public ClientService(ClientRepository clientRepository, AddressRepository addressRepository,
                         ClientMapper clientMapper, AddressMapper addressMapper) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.clientMapper = clientMapper;
        this.addressMapper = addressMapper;
    }

    public ResponseShortInfoDto getShortInfoClient(RequestInfoDto dto){
        log.info("Get short info about Client");

        return clientRepository.findById(UUID.fromString(dto.getId()))
                .map(clientMapper::toShrotInfoDto)
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

    @Transactional
    public void addNewAddress(NewAddressDto dto){
        Client client = clientRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Пользователя нет"));

        Address address = addressMapper.toAddress(dto);

        addressRepository.findLikeAddress(address.getCountry(),
                        address.getCity(), address.getStreet(),
                        address.getHouse(), address.getApartment())
                .ifPresentOrElse(existAddress -> client.getAddress().add(existAddress),
                        () -> {
                    Address newAddress = addressRepository.save(address);
                    client.getAddress().add(newAddress);
                });

        clientRepository.save(client);
        log.info("Successful save address's client");

    }


}
