package ru.kazan.clientservice.unit.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.LoginWithPasswordDto;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.utils.enums.TypeAuthEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractControllerTest {

    @Test
    @DisplayName("UserController: Should to update refresh token for User")
    void shouldToUpdateRefreshTokenForUser() throws Exception {

        JwtResponse jwtResponse
                = new JwtResponse("new_access_token", "new_refresh_token");

        doReturn(jwtResponse)
                .when(userService)
                .refreshToken(refreshToken);

        MvcResult result = mockMvc.perform(patch("/user/token")
                        .header("Refresh", refreshToken))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse newResponse
                = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponse.class);

        verify(userService, times(1)).refreshToken(refreshToken);
        assertEquals(jwtResponse.getRefreshToken(), newResponse.getRefreshToken());
        assertEquals(jwtResponse.getAccessToken(), newResponse.getAccessToken());
    }

    @ParameterizedTest
    @EnumSource(value = TypeAuthEnum.class)
    @DisplayName("UserController: Should to return new AccessToken and RefreshToken for User")
    void shouldToReturnAccessTokenAndRefreshTokenForUser(TypeAuthEnum type) throws Exception {

        LoginWithPasswordDto dto = LoginWithPasswordDto.builder()
                .login("login")
                .password("password")
                .type(type)
                .build();

        JwtResponse jwtResponse
                = new JwtResponse("new_access_token", "new_refresh_token");
        if(type.equals(TypeAuthEnum.PASSPORT))
            doReturn(jwtResponse)
                    .when(userService)
                    .loginWithPassport(dto.getLogin(), dto.getPassword());
        else
            doReturn(jwtResponse)
                    .when(userService)
                    .loginWithEmailOrMobilePhone(dto.getLogin(), dto.getPassword());

        MvcResult result = mockMvc.perform(post("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse newResponse
                = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponse.class);

        if(type.equals(TypeAuthEnum.PASSPORT))
            verify(userService, times(1)).loginWithPassport(dto.getLogin(), dto.getPassword());
        else
            verify(userService, times(1)).loginWithEmailOrMobilePhone(dto.getLogin(), dto.getPassword());

        assertEquals(jwtResponse.getRefreshToken(), newResponse.getRefreshToken());
        assertEquals(jwtResponse.getAccessToken(), newResponse.getAccessToken());
    }

    @Test
    @DisplayName("UserController: Should to registration new person in DataBase")
    void shouldToReturnRegistrationNewPersonInDataBase() throws Exception {
        RegistrationClientDto dto = RegistrationClientDto.builder()
                .firstName("Test")
                .lastName("Test")
                .mobilePhone("89001119981")
                .email("test@test.ru")
                .age(18)
                .build();

        mockMvc.perform(post("/user/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}
