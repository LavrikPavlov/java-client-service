package ru.kazan.clientservice.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.model.Address;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
class ClientControllerTest extends AbstractControllerTest {

    private final UUID clientId = UUID.fromString(TestClientConstants.CLIENT_ID_FOR_CLIENT);

    @BeforeEach
    void setUpClientController(){
        accessToken = "Bearer " + accessToken;
    }

    @Test
    @DisplayName("ClientController Unit: Should return SHORT info client")
    void getShortInfo() throws Exception {
        ResponseShortInfoDtoImpl response = TestClientConstants.RESPONSE_SHORT_INFO_DTO;

        when(clientService.getShortInfoClient(accessToken)).thenReturn(response);

        mockMvc.perform(get("/client/info")
                        .header("Authorization", accessToken)
                        .param("type", ""))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response), true));
    }

    @Test
    @DisplayName("ClientController Unit: Should return FULL info client")
    void getFullInfo() throws Exception {
        ResponseFullInfoDtoImpl response = TestClientConstants.RESPONSE_FULL_INFO_DTO;

        when(clientService.getFullInfoClient(accessToken)).thenReturn(response);

        mockMvc.perform(get("/client/info")
                        .header("Authorization", accessToken)
                        .param("type", "full"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response), true));
    }

    @Test
    @DisplayName("ClientController Unit: Should update email's client")
    void editEmail() throws Exception {
        String newEmail = "test@test.ru";
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .email(newEmail)
                .build();

        mockMvc.perform(patch("/client/edit/email")
                        .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(200));

        ArgumentCaptor<RequestEditEmailDto> dto
                = ArgumentCaptor.forClass(RequestEditEmailDto.class);

        verify(clientService, times(1))
                .changeEmail(dto.capture(), accessToken, sessionTokenMobile);
        assertEquals(newEmail, dto.getValue().getEmail());
        assertEquals(clientId, jwtProvider.getClientIdFromToken(accessToken));

    }

    @Test
    @DisplayName("ClientController Unit: Should update mobile phone's client")
    void editMobilePhone() throws Exception {
        String newMobilePhone = "89998887766";
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .mobilePhone(newMobilePhone)
                .build();

        mockMvc.perform(patch("/client/edit/mobile-phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(200));

        ArgumentCaptor<RequestEditMobilePhoneDto> dto
                = ArgumentCaptor.forClass(RequestEditMobilePhoneDto.class);

        verify(clientService, times(1))
                .changeMobilePhone(dto.capture(), accessToken, sessionTokenMobile);
        assertEquals(newMobilePhone, dto.getValue().getMobilePhone());
        assertEquals(clientId, jwtProvider.getClientIdFromToken(accessToken));
    }

    @Test
    @DisplayName("ClientController Unit: Should add new address for client")
    void addNewAddress() throws Exception{
        Address newAddress
                = new Address(2L,"TestNew","TestNew","TestNew", 1, 1);
        NewAddressDto request = NewAddressDto.builder()
                .country(newAddress.getCountry())
                .city(newAddress.getCity())
                .street(newAddress.getStreet())
                .house(newAddress.getHouse())
                .apartment(newAddress.getApartment())
                .build();

        mockMvc.perform(put("/client/edit/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(200));

        ArgumentCaptor<NewAddressDto> dto
                = ArgumentCaptor.forClass(NewAddressDto.class);

        verify(clientService, times(1)).addNewAddress(dto.capture(), accessToken);
        assertEquals(newAddress.getCountry(), dto.getValue().getCountry());
        assertEquals(newAddress.getCity(), dto.getValue().getCity());
        assertEquals(newAddress.getStreet(), dto.getValue().getStreet());
        assertEquals(newAddress.getHouse(), dto.getValue().getHouse());
        assertEquals(newAddress.getApartment(), dto.getValue().getApartment());
        assertEquals(clientId, jwtProvider.getClientIdFromToken(accessToken));
    }

    @Test
    @DisplayName("ClientController Unit: Should delete address for client")
    void deleteAddress() throws Exception{

        DeleteAddressDto request = DeleteAddressDto.builder()
                .addressId(2L)
                .build();

        mockMvc.perform(delete("/client/edit/delete/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(200));

        ArgumentCaptor<DeleteAddressDto> dto
                = ArgumentCaptor.forClass(DeleteAddressDto.class);

        verify(clientService, times(1)).deleteAddress(dto.capture(), anyString());
        assertEquals(request.getAddressId(), dto.getValue().getAddressId());
        assertEquals(clientId, jwtProvider.getClientIdFromToken(accessToken));
    }
}