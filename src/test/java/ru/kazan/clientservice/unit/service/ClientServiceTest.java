package ru.kazan.clientservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.mapper.AddressMapper;
import ru.kazan.clientservice.mapper.ClientMapper;
import ru.kazan.clientservice.model.Address;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.repository.AddressRepository;
import ru.kazan.clientservice.repository.ClientRepository;
import ru.kazan.clientservice.service.ClientService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private AddressMapper addressMapper;

    private String clientId;

    private Client client;

    private Address address;

    private String notCorrectId;

    @BeforeEach
    void setUp(){
        clientId = TestClientConstants.CLIENT_ID_CORRECT;
        client = TestClientConstants.CLIENT_DEFAULT;
        address = TestClientConstants.ADDRESS_DEFAULT;
        notCorrectId = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("ClientService Unit: Client exist and return DTO (Short)")
    void getShortInfoClient_ExistClient_ReturnDto() {
        ResponseShortInfoDtoImpl responseShortInfoDto
                = TestClientConstants.RESPONSE_SHORT_INFO_DTO;

        when(clientRepository.findById(UUID.fromString(clientId)))
                .thenReturn(Optional.of(client));
        when(clientMapper.toShortInfoDto(any(Client.class)))
                .thenReturn(responseShortInfoDto);

        ResponseShortInfoDtoImpl response = clientService.getShortInfoClient(clientId);

        assertEquals(response.getId(), responseShortInfoDto.getId());
        assertEquals(response.getFirstName(), responseShortInfoDto.getFirstName());
        assertEquals(response.getLastName(), responseShortInfoDto.getLastName());
        assertEquals(response.getPatronymic(), responseShortInfoDto.getPatronymic());
        assertEquals(response.getGender(), responseShortInfoDto.getGender());
        assertEquals(response.getEmail(), responseShortInfoDto.getEmail());
        assertEquals(response.getMobilePhone(), responseShortInfoDto.getMobilePhone());
        assertEquals(response.getStatus(), responseShortInfoDto.getStatus());

    }

    @Test
    @DisplayName("ClientService Unit: UUID not correct or Client not exist (Short)")
    void getShortInfoClient_NotExistClient_ReturnException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> clientService
                .getShortInfoClient(notCorrectId));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Client exist and return DTO (Full)")
    void getFullInfoClient_ExistClient_ReturnDt0() {
        ResponseFullInfoDtoImpl responseFullInfoDto
                = TestClientConstants.RESPONSE_FULL_INFO_DTO;

        when(clientRepository.findById(UUID.fromString(clientId)))
                .thenReturn(Optional.of(client));
        when(clientMapper.toFullInfoDto(any(Client.class)))
                .thenReturn(responseFullInfoDto);

        ResponseFullInfoDtoImpl response
                = clientService.getFullInfoClient(clientId);

        assertEquals(responseFullInfoDto.getId(), response.getId());
        assertEquals(responseFullInfoDto.getFirstName(), response.getFirstName());
        assertEquals(responseFullInfoDto.getLastName(), response.getLastName());
        assertEquals(responseFullInfoDto.getPatronymic(), response.getPatronymic());
        assertEquals(responseFullInfoDto.getMobilePhone(), response.getMobilePhone());
        assertEquals(responseFullInfoDto.getEmail(), response.getEmail());
        assertEquals(responseFullInfoDto.getAge(), response.getAge());
        assertEquals(responseFullInfoDto.getStatus(), response.getStatus());
        assertEquals(responseFullInfoDto.getDateRegistration(), response.getDateRegistration());
        assertEquals(responseFullInfoDto.getPassport(), response.getPassport());
        assertEquals(responseFullInfoDto.getAddress(), response.getAddress());
    }

    @Test
    @DisplayName("ClientService Unit: UUID not correct or Client not exist (Full)")
    void getFullInfoClient_NotExistClient_ReturnException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> clientService
                .getFullInfoClient(notCorrectId));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Change email with correct DTO")
    void changeEmail_withCorrectDto() {
        String newEmail = "test@new.ru";
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .id(clientId)
                .email(newEmail)
                .build();

        when(clientRepository.findById(UUID.fromString(clientId))).thenReturn(Optional.of(client));

        clientService.changeEmail(request);

        assertEquals(newEmail, client.getEmail());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("ClientService Unit: Change email with not correct DTO and return exception")
    void changeEmail_withNotCorrectDto_ReturnException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .id(clientId)
                .email("")
                .build();

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(client));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.changeEmail(request));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Change email with identity email and return exception")
    void changeEmail_withIdentityEmail_ReturnException() {
                String newEmail = "test@test.ru";

        client = new Client();
        client.setId(UUID.fromString(clientId));
        client.setEmail(newEmail);

        ExceptionEnum exceptionEnum = ExceptionEnum.CONFLICT;

        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .id(clientId)
                .email(newEmail)
                .build();

        when(clientRepository.findById(UUID.fromString(clientId))).thenReturn(Optional.of(client));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.changeEmail(request));

        assertEquals(newEmail, client.getEmail());
        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Change mobile phone with correct DTO")
    void changeMobilePhone_withCorrectDto() {
        String newMobilePhone = "89998887766";
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .id(clientId)
                .mobilePhone(newMobilePhone)
                .build();

        when(clientRepository.findById(UUID.fromString(clientId))).thenReturn(Optional.of(client));

        clientService.changeMobilePhone(request);

        assertEquals(newMobilePhone, client.getMobilePhone());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("ClientService Unit: Change mobile phone with identity mobile phone and return exception")
    void changeMobilePhone_withIdentityMobilePhone_ReturnException() {
        String newMobilePhone = client.getMobilePhone();

        client = new Client();
        client.setId(UUID.fromString(clientId));
        client.setMobilePhone(newMobilePhone);

        ExceptionEnum exceptionEnum = ExceptionEnum.CONFLICT;

        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .id(clientId)
                .mobilePhone(newMobilePhone)
                .build();

        when(clientRepository.findById(UUID.fromString(clientId))).thenReturn(Optional.of(client));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.changeMobilePhone(request));

        assertEquals(newMobilePhone, client.getMobilePhone());
        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Change mobile phone with not correct DTO and return exception")
    void changeMobilePhone_withNotCorrectDto_ReturnException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;

        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .id(clientId)
                .mobilePhone("")
                .build();

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.changeMobilePhone(request));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Add new Address with correct DTO and duplicate exist address")
    void addNewAddress_withCorrectDto() {
        NewAddressDto dto = new NewAddressDto();
        dto.setId(clientId);
        dto.setCountry(address.getCountry());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setHouse(address.getHouse());
        dto.setHouse(address.getApartment());


        when(clientRepository.findById(UUID.fromString(clientId))).thenReturn(Optional.of(client));
        when(addressMapper.toAddress(dto)).thenReturn(address);

        when(addressRepository.findLikeAddress(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment()))
                .thenReturn(Optional.of(address));

        clientService.addNewAddress(dto);

        assertTrue(client.getAddress().contains(address));
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("ClientService Unit: Add new Address with correct DTO and dont duplicate exist address")
    void addNewAddress_withAddressExists_SaveAddressClient(){
        NewAddressDto dto = new NewAddressDto();
        dto.setId(clientId);
        dto.setCountry(address.getCountry());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setHouse(address.getHouse());
        dto.setHouse(address.getApartment());


        when(clientRepository.findById(UUID.fromString(clientId))).thenReturn(Optional.of(client));
        when(addressMapper.toAddress(dto)).thenReturn(address);

        when(addressRepository.findLikeAddress(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment()))
                .thenReturn(Optional.empty());

        when(addressRepository.save(address)).thenReturn(address);

        clientService.addNewAddress(dto);

        assertTrue(client.getAddress().contains(address));
        verify(clientRepository, times(1)).save(client);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    @DisplayName("ClientService Unit: Add new Address with correct DTO and duplicate exist address")
    void addNewAddress_withNotCorrectDto_ReturnException(){
        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;

        NewAddressDto dto = new NewAddressDto();
        dto.setId(clientId);

        when(clientRepository.findById(UUID.fromString(dto.getId())))
                .thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.addNewAddress(dto));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Delete address, " +
            "and if this address is associated with only one client," +
            " then remove this address")
    void deleteAddress_successfulDeleteAddress_withOneClient() {
        Address deleteAddress = TestClientConstants.ADDRESS_DEFAULT;
        DeleteAddressDto dto = DeleteAddressDto.builder()
                .addressId(deleteAddress.getId())
                .id(clientId)
                .build();

        when(addressRepository.findById(deleteAddress.getId()))
                .thenReturn(Optional.of(address));

        when(clientRepository.findById(UUID.fromString(clientId)))
                .thenReturn(Optional.of(client));

        when(clientRepository.findClientByAddressContains(address))
                .thenReturn(Collections.singletonList(client));

        clientService.deleteAddress(dto);

        assertFalse(client.getAddress().contains(address));
        verify(addressRepository, times(1)).delete(address);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("ClientService Unit: Delete address, " +
            "and if this address is associated with two client," +
            " then dont remove this address")
    void deleteAddress_successfulDeleteAddress_withTwoClient() {
        Address deleteAddress = TestClientConstants.ADDRESS_DEFAULT;

        Client newClient = TestClientConstants.CLIENT_DEFAULT;
        newClient.setId(UUID.randomUUID());
        newClient.getAddress().add(deleteAddress);

        DeleteAddressDto dto = DeleteAddressDto.builder()
                .addressId(deleteAddress.getId())
                .id(clientId)
                .build();

        when(addressRepository.findById(deleteAddress.getId()))
                .thenReturn(Optional.of(address));

        when(clientRepository.findById(UUID.fromString(clientId)))
                .thenReturn(Optional.of(client));

        when(clientRepository.findClientByAddressContains(address))
                .thenReturn(Arrays.asList(client, newClient));

        clientService.deleteAddress(dto);

        assertFalse(client.getAddress().contains(address));

        verify(addressRepository, times(0)).delete(address);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("ClientService Unit: Dont delete address, because addressId not correct")
    void deleteAddress_withNotCorrectAddressId_ThenReturnException() {

        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;
        Address deleteAddress = TestClientConstants.ADDRESS_DEFAULT;
        DeleteAddressDto dto = DeleteAddressDto.builder()
                .addressId(deleteAddress.getId())
                .id(clientId)
                .build();

        when(addressRepository.findById(deleteAddress.getId()))
                .thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.deleteAddress(dto));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Dont delete address, because clientId not correct")
    void deleteAddress_withNotCorrectClientId_ThenReturnException() {

        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;
        Address deleteAddress = TestClientConstants.ADDRESS_DEFAULT;
        DeleteAddressDto dto = DeleteAddressDto.builder()
                .addressId(deleteAddress.getId())
                .id(clientId)
                .build();

        when(addressRepository.findById(deleteAddress.getId()))
                .thenReturn(Optional.of(address));

        when(clientRepository.findById(UUID.fromString(clientId)))
                .thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.deleteAddress(dto));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }

    @Test
    @DisplayName("ClientService Unit: Dont delete address, because client dont have this address")
    void deleteAddress_clientNotHaveAddress_ThenReturnException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.BAD_REQUEST;

        Address deleteAddress = TestClientConstants.ADDRESS_DEFAULT;
        client.getAddress().remove(deleteAddress);

        DeleteAddressDto dto = DeleteAddressDto.builder()
                .addressId(deleteAddress.getId())
                .id(clientId)
                .build();

        when(addressRepository.findById(deleteAddress.getId()))
                .thenReturn(Optional.of(address));

        when(clientRepository.findById(UUID.fromString(clientId)))
                .thenReturn(Optional.of(client));

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> clientService.deleteAddress(dto));

        assertEquals(exception.getExceptionEnum(), exceptionEnum);
    }
}