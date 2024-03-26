package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
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

    public ResponseShortInfoDtoImpl getShortInfoClient(String clientId){
        return clientRepository.findById(UUID.fromString(clientId))
                .map(clientMapper::toShortInfoDto)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));
    }

    public ResponseFullInfoDtoImpl getFullInfoClient(String clientId){
        return clientRepository.findById(UUID.fromString(clientId))
                .map(clientMapper::toFullInfoDto)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));
    }

    @Transactional
    public void changeEmail(RequestEditEmailDto dto){
        Client client = getClient(UUID.fromString(dto.getId()));

        if(dto.getEmail().equals(client.getEmail()))
            throw new ApplicationException(ExceptionEnum.CONFLICT);

        if(dto.getEmail().isEmpty())
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        client.setEmail(dto.getEmail());
        clientRepository.save(client);
    }

    @Transactional
    public void changeMobilePhone(RequestEditMobilePhoneDto dto){
        Client client = getClient(UUID.fromString(dto.getId()));

        if(dto.getMobilePhone().equals(client.getMobilePhone()))
            throw new ApplicationException(ExceptionEnum.CONFLICT);

        if(dto.getMobilePhone().isEmpty())
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        client.setMobilePhone(dto.getMobilePhone());
        clientRepository.save(client);
    }

    @Transactional
    public void addNewAddress(NewAddressDto dto){
        Client client = getClient(UUID.fromString(dto.getId()));

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

    @Transactional
    public void deleteAddress(DeleteAddressDto dto){
        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));

        Client client = getClient(UUID.fromString(dto.getId()));

        if(!checkClientHaveAddress(address, client))
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        if(!checkCountClientWithAddress(address)) {
            client.getAddress().remove(address);
            addressRepository.delete(address);
        }

        client.getAddress().remove(address);
        clientRepository.save(client);
    }

    private boolean checkCountClientWithAddress(Address address){
        int clientsCount = clientRepository.findClientByAddressContains(address)
                .size();

        return clientsCount > 1;
    }

    private boolean checkClientHaveAddress(Address address, Client client){
        return client.getAddress().contains(address);
    }

    private Client getClient(UUID id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));
    }

}
