package ru.kazan.clientservice.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.dto.session.EmailWithCodeDtoImpl;
import ru.kazan.clientservice.dto.session.MobilePhoneCodeDtoImpl;
import ru.kazan.clientservice.dto.session.PasswordDto;
import ru.kazan.clientservice.dto.session.TypeCodeSendDto;

import java.util.UUID;

import static io.restassured.RestAssured.given;

class SessionControllerIT extends AbstractIntegrationTest{

    private String sessionToken;

    @BeforeEach
    void setUpSessionController(){
        accessToken = "Bearer " + jwtProvider.genAccessToken(TestClientConstants.USER_PROFILE_FOR_SESSION);
        sessionToken = jwtProvider.genSessionToken(UUID.fromString(TestClientConstants.CLIENT_ID_FOR_SESSION));
    }

    @ParameterizedTest
    @ValueSource(strings = {"mobile_phone", "email"})
    @DisplayName("SessionController: Send verify code with correct contact then return 200")
    void sendVerifyCone_thenReturn_200(String type){
        String contact = null;
        if(type.equals("mobile_phone"))
            contact = "89139229100";
        if(type.equals("email"))
            contact = "alisa.akopova2000@yandex.ru";

        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type(type)
                .contact(contact)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify")
                .then()
                .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {"mobile_phone", "email"})
    @DisplayName("SessionController: Send verify code with not correct contact then return 404")
    void sendVerifyCone_thenReturn_BadRequest(String type){
        String contact = "notCorrect";
        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type(type)
                .contact(contact)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("SessionController: Send verify code without type then return 400")
    void sendVerifyCone_thenReturn_BadRequest(){
        TypeCodeSendDto dto = TypeCodeSendDto.builder()
                .type("")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Check verify code on email then return 200")
    void verifyCodeFromEmail_thenReturn_200(){
        EmailWithCodeDtoImpl dto = EmailWithCodeDtoImpl.builder()
                .verifyCode(TestClientConstants.VALID_CODE_FROM_DB)
                .contact("evegiy_mail@mail.ru")
                .build();


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify/email")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("SessionController: Check verify code on email without contact then return 400")
    void verifyCodeFromEmail_withoutContact_thenReturn_400(){
        EmailWithCodeDtoImpl dto = EmailWithCodeDtoImpl.builder()
                .verifyCode(TestClientConstants.VALID_CODE_FROM_DB)
                .build();


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify/email")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Check verify code on email without code then return 400")
    void verifyCodeFromEmail_withoutCode_thenReturn_400(){
        EmailWithCodeDtoImpl dto = EmailWithCodeDtoImpl.builder()
                .verifyCode(TestClientConstants.NOT_VALID_CODE)
                .contact("evegiy_mail@mail.ru")
                .build();


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify/email")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Check verify code on mobile phone then return 200")
    void verifyCodeFromMobilePhone_thenReturn_200(){
        MobilePhoneCodeDtoImpl dto = MobilePhoneCodeDtoImpl.builder()
                .verifyCode(TestClientConstants.VALID_CODE_FROM_DB)
                .contact("89119931023")
                .build();


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify/mobile")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("SessionController: Check verify code on mobile phone without contact then return 400")
    void verifyCodeFromMobilePhone_withoutContact_thenReturn_400(){
        MobilePhoneCodeDtoImpl dto = MobilePhoneCodeDtoImpl.builder()
                .verifyCode(TestClientConstants.VALID_CODE_FROM_DB)
                .build();


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify/mobile")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Check verify code on mobile phone without code then return 400")
    void verifyCodeFromMobilePhone_withoutCode_thenReturn_400(){
        MobilePhoneCodeDtoImpl dto = MobilePhoneCodeDtoImpl.builder()
                .verifyCode(TestClientConstants.NOT_VALID_CODE)
                .contact("89119931023")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/session/verify/mobile")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Set new password with correct dto and valid Session token, return 200")
    void setNewPassword_thenReturn_200(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("TestPassword123")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/session/password/new")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("SessionController: Set new password with not correct dto, return 400")
    void setNewPassword_withoutPassword_thenReturn_400(){
        PasswordDto dto = PasswordDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/session/password/new")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Set new password without Session token, return 401")
    void setNewPassword_withoutSessionToken_thenReturn_401(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("TestPassword123")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Session", "NOT_CORRECT_TOKEN")
                .when()
                .patch("/session/password/new")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("SessionController: Set new password with null, return 403")
    void setNewPassword_withNullPassword_thenReturn_403(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/session/password/new")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("SessionController: Change password with correct data, return 200")
    void changePassword_withCorrectDto_thenReturn_200(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("TestPassword123")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Session", sessionToken)
                .when()
                .patch("/session/password/new");

        dto.setNewPassword("TestPassword");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Authorization", accessToken)
                .header("Session", sessionToken)
                .when()
                .patch("/session/password/change")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("SessionController: Change password without tokens, return 401")
    void setNewPassword_withoutTokens_thenReturn_401(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("Test")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Authorization", "NOT_CORRECT_TOKEN_ACCESS")
                .header("Session", "NOT_CORRECT_TOKEN_SESSION")
                .when()
                .patch("/session/password/change")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("SessionController: Change password without dto, return 400")
    void setNewPassword_withoutTokens_thenReturn_400(){
        PasswordDto dto = new PasswordDto();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Authorization", accessToken)
                .header("Session", sessionToken)
                .when()
                .patch("/session/password/change")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("SessionController: Change password with same password, return 409")
    void setNewPassword_withSamePassword_thenReturn_409(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("TestPassword123")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Session", sessionToken)
                .when()
                .patch("/session/password/new");

        dto.setNewPassword("TestPassword123");


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Authorization", accessToken)
                .header("Session", sessionToken)
                .when()
                .patch("/session/password/change")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("SessionController: Change password is empty, return 403")
    void setNewPassword_changePasswordIsEmpty_thenReturn_403(){
        PasswordDto dto = PasswordDto.builder()
                .newPassword("")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .header("Authorization", accessToken)
                .header("Session", sessionToken)
                .when()
                .patch("/session/password/change")
                .then()
                .statusCode(403);
    }


}
