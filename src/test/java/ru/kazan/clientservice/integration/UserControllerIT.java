package ru.kazan.clientservice.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.LoginWithPasswordDto;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.utils.enums.TypeAuthEnum;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerIT extends AbstractIntegrationTest{

    private JwtResponse response;

    @BeforeEach
    void setUpUserController(){

    }

    @Test
    @DisplayName("UserService: Generate new Refresh/Access token then return 200")
    public void generateNewTokens_thenReturn_200(){
        LoginWithPasswordDto dto = new LoginWithPasswordDto(
                "misha-kazan2013@yandex.ru",
                "TestPassword123",
                TypeAuthEnum.EMAIL
        );

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("user/auth")
                .then()
                .statusCode(200)
                        .extract()
                                .as(JwtResponse.class);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Refresh", response.getRefreshToken())
                .when()
                .patch("/user/token")
                .then()
                .statusCode(200);

    }

    @Test
    @DisplayName("UserService: Generate new Refresh/Access token then return 401")
    public void generateNewTokens_thenReturn_401(){
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Refresh", "Not correct token")
                .when()
                .patch("/user/token")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("UserService: Registration new Client then return 200")
    public void regNewClient_thenReturn_200(){
        RegistrationClientDto dto = RegistrationClientDto.builder()
                .firstName("Test")
                .lastName("Test")
                .mobilePhone("89001119981")
                .email("test@test.ru")
                .age(18)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/user/reg")
                .then()
                .statusCode(200);
    }

    @ParameterizedTest
    @EnumSource(TypeAuthEnum.class)
    @DisplayName("UserService: Authentication with correct data then return 200")
    public void authWithData_thenReturn_200(TypeAuthEnum type){
        String login = null;

        if(type.getText().equals("email"))
            login = "misha-kazan2013@yandex.ru";
        if(type.getText().equals("passport"))
            login = "5050111000";
        if(type.getText().equals("mobile_phone"))
            login = "89069438723";

        LoginWithPasswordDto dto = LoginWithPasswordDto.builder()
                .login(login)
                .password("TestPassword123")
                .type(type)
                .build();

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/user/auth")
                .then()
                .statusCode(200)
                .extract()
                .as(JwtResponse.class);

        assertEquals(UUID.fromString("b3f62160-c084-41b1-8189-306d1906e2fb"),
                jwtProvider.getClientIdFromToken(response.getAccessToken()));

    }

    @ParameterizedTest
    @EnumSource(TypeAuthEnum.class)
    @DisplayName("UserService: Authentication with not correct data then return 400")
    public void authWithData_notCorrectData_thenReturn_400(TypeAuthEnum type){

        LoginWithPasswordDto dto = LoginWithPasswordDto.builder()
                .type(type)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/user/auth")
                .then()
                .statusCode(400);
    }

    @ParameterizedTest
    @EnumSource(TypeAuthEnum.class)
    @DisplayName("UserService: Authentication if password is NULL in DB then return 403")
    public void authWithData_withPasswordNullInBD_thenReturn_403(TypeAuthEnum type){
        String login = null;

        if(type.getText().equals("email"))
            login = "anas.lomach@gmail.com";
        if(type.getText().equals("passport"))
            login = "8754910021";
        if(type.getText().equals("mobile_phone"))
            login = "89992171176";

        LoginWithPasswordDto dto = LoginWithPasswordDto.builder()
                .login(login)
                .password("Test")
                .type(type)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/user/auth")
                .then()
                .statusCode(403);
    }

    @ParameterizedTest
    @EnumSource(TypeAuthEnum.class)
    @DisplayName("UserService: Authentication if password is not match then return 400")
    public void authWithData_withNotCorrectPassword_thenReturn_400(TypeAuthEnum type){
        String login = null;

        if(type.getText().equals("email"))
            login = "misha-kazan2013@yandex.ru";
        if(type.getText().equals("passport"))
            login = "5050111000";
        if(type.getText().equals("mobile_phone"))
            login = "89069438723";

        LoginWithPasswordDto dto = LoginWithPasswordDto.builder()
                .login(login)
                .password("Test")
                .type(type)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(dto)
                .when()
                .post("/user/auth")
                .then()
                .statusCode(400);
    }
}
