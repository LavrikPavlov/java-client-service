package ru.kazan.clientservice.unit.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import ru.kazan.clientservice.dto.jwt.JwtSessionToken;
import ru.kazan.clientservice.dto.session.EmailWithCodeDtoImpl;
import ru.kazan.clientservice.dto.session.MobilePhoneCodeDtoImpl;
import ru.kazan.clientservice.dto.session.PasswordDto;
import ru.kazan.clientservice.dto.session.TypeCodeSendDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SessionControllerTest extends AbstractControllerTest {


    @ParameterizedTest
    @ValueSource(strings = {"email", "mobile_phone"})
    @DisplayName("SessionController: Should to send new verify code on email/mobile code")
    void shouldSendNewVerifyCode(String type) throws Exception {
        String contact = null;
        if (type.equals("email"))
            contact = "alisa.akopova2000@yandex.ru";
        if (type.equals("mobile_phone"))
            contact = "89139229100";

        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type(type)
                .contact(contact)
                .build();

        mockMvc.perform(post("/session/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(200));

        ArgumentCaptor<TypeCodeSendDto> dtoCaptor
                = ArgumentCaptor.forClass(TypeCodeSendDto.class);

        verify(sessionService, times(1)).verifyCode(dtoCaptor.capture());
    }

    @Test
    @DisplayName("SessionController: Should to send new session token of email code")
    void shouldSendNewSessionTokenEmail() throws Exception {
        EmailWithCodeDtoImpl dto = EmailWithCodeDtoImpl.builder()
                .contact("alisa.akopova2000@yandex.ru")
                .verifyCode("123456")
                .build();

        var jwtSessionToken = JwtSessionToken.builder()
                .sessionToken(sessionTokenEmail)
                .build();

        when(sessionService.getSessionToken(any(EmailWithCodeDtoImpl.class), eq("email")))
                .thenReturn(jwtSessionToken);

        mockMvc.perform(post("/session/verify/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtSessionToken)));

        ArgumentCaptor<EmailWithCodeDtoImpl> dtoCaptor
                = ArgumentCaptor.forClass(EmailWithCodeDtoImpl.class);

        verify(sessionService, times(1))
                .getSessionToken(dtoCaptor.capture(), eq("email"));

        assertEquals(dto.getContact(), dtoCaptor.getValue().getContact());
        assertEquals(dto.getVerifyCode(), dtoCaptor.getValue().getVerifyCode());
    }

    @Test
    @DisplayName("SessionController: Should to send new session token of mobile code")
    void shouldSendNewSessionTokenMobile() throws Exception {
        MobilePhoneCodeDtoImpl dto = MobilePhoneCodeDtoImpl.builder()
                .contact("89139229100")
                .verifyCode("123456")
                .build();

        var jwtSessionToken = JwtSessionToken.builder()
                .sessionToken(sessionTokenEmail)
                .build();

        when(sessionService.getSessionToken(any(MobilePhoneCodeDtoImpl.class), eq("mobile")))
                .thenReturn(jwtSessionToken);

        mockMvc.perform(post("/session/verify/mobile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtSessionToken)));

        ArgumentCaptor<MobilePhoneCodeDtoImpl> dtoCaptor
                = ArgumentCaptor.forClass(MobilePhoneCodeDtoImpl.class);

        verify(sessionService, times(1))
                .getSessionToken(dtoCaptor.capture(), eq("mobile"));

        assertEquals(dto.getContact(), dtoCaptor.getValue().getContact());
        assertEquals(dto.getVerifyCode(), dtoCaptor.getValue().getVerifyCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "mobile"})
    @DisplayName("SessionController: Should to set new password with Email/Mobile tokens for User")
    void shouldSetNewPasswordForUser(String type) throws Exception {
        String sessionToken = null;

        if(type.equals("email"))
            sessionToken = sessionTokenEmail;
        if(type.equals("mobile"))
            sessionToken = sessionTokenMobile;

        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        mockMvc.perform(patch("/session/password/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Session", sessionToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        ArgumentCaptor<PasswordDto> dtoCaptor
                = ArgumentCaptor.forClass(PasswordDto.class);

        verify(sessionService, times(1))
                .setNewPasswordClient(dtoCaptor.capture(), eq(sessionToken));

        assertEquals(dto.getNewPassword(), dtoCaptor.getValue().getNewPassword());

    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "mobile"})
    @DisplayName("SessionController: Should to change password with Email/Mobile tokens for User")
    void shouldChangePasswordForUser(String type) throws Exception {
        String sessionToken = null;

        if(type.equals("email"))
            sessionToken = sessionTokenEmail;
        if(type.equals("mobile"))
            sessionToken = sessionTokenMobile;

        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        mockMvc.perform(patch("/session/password/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .header("Session", sessionToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        ArgumentCaptor<PasswordDto> dtoCaptor
                = ArgumentCaptor.forClass(PasswordDto.class);

        verify(sessionService, times(1))
                .changePasswordClient(dtoCaptor.capture(), eq(accessToken) , eq(sessionToken));

        assertEquals(dto.getNewPassword(), dtoCaptor.getValue().getNewPassword());

    }

}
