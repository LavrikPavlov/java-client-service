package ru.kazan.clientservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import ru.kazan.clientservice.utils.security.JwtProvider;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final AddressRepository addressRepository;

    private final ClientMapper clientMapper;

    private final AddressMapper addressMapper;

    private final JwtProvider jwtProvider;


    public ResponseShortInfoDtoImpl getShortInfoClient(String token){
        UUID clientId = getIdFromToken(token);

        return clientRepository.findById(clientId)
                .map(clientMapper::toShortInfoDto)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));
    }

    public ResponseFullInfoDtoImpl getFullInfoClient(String token){
        UUID clientId = getIdFromToken(token);

        return clientRepository.findFullInfoClientById(clientId)
                .map(clientMapper::toFullInfoDto)
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));
    }

    @Transactional
    public void changeEmail(RequestEditEmailDto dto, String token, String sessionToken){
        if(validSessionToken(sessionToken))
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST, "Not valid session token");

        UUID clientId = getIdFromToken(token);
        Client client = getClient(clientId);

        if(dto.getEmail().equals(client.getEmail()))
            throw new ApplicationException(ExceptionEnum.CONFLICT);

        if(dto.getEmail().isEmpty())
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        client.setEmail(dto.getEmail());
        clientRepository.save(client);
    }

    @Transactional
    public void changeMobilePhone(RequestEditMobilePhoneDto dto, String token, String sessionToken){
        if(validSessionToken(sessionToken))
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST, "Not valid session token");

        UUID clientId = getIdFromToken(token);
        Client client = getClient(clientId);

        if(dto.getMobilePhone().equals(client.getMobilePhone()))
            throw new ApplicationException(ExceptionEnum.CONFLICT);

        if(dto.getMobilePhone().isEmpty())
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        client.setMobilePhone(dto.getMobilePhone());
        clientRepository.save(client);
    }

    @Transactional
    public void addNewAddress(NewAddressDto dto, String token){
        UUID clientId = getIdFromToken(token);
        Client client = getClient(clientId);

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
    public void deleteAddress(DeleteAddressDto dto, String token){
        UUID clientId = getIdFromToken(token);

        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new ApplicationException(ExceptionEnum.BAD_REQUEST));

        Client client = getClient(clientId);

        if(!checkClientHaveAddress(address, client))
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        if(!checkCountClientWithAddress(address)) {
            client.getAddress().remove(address);
            addressRepository.delete(address);
        }

        client.getAddress().remove(address);
        clientRepository.save(client);
    }

    private boolean validSessionToken(String token){
        return !jwtProvider.validateToken(token);
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

    private UUID getIdFromToken(String token){
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtProvider.getClientIdFromToken(token);
    }

}
