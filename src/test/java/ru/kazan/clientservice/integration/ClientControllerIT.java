package ru.kazan.clientservice.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.kazan.clientservice.constants.TestClientConstants;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.service.ClientService;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ClientControllerIT extends AbstractIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Test
    @DisplayName("ClientController: Get client FULL info and should return response with body ClientInfo")
    void getClientFullInfo_thenReturnResponse_200(){

        Map<String, String> params = new HashMap<>();

        params.put("type", "full");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .header("Authorization", accessToken)
                .when()
                .get("/client/info")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("ClientController: Get client SHORT info and should return response with body ClientInfo")
    void getClientShortInfo_thenReturnResponse_200(){

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get("/client/info")
                .then()
                .statusCode(200)
                .body(equalTo(getJsonFromObject(TestClientConstants.RESPONSE_SHORT_INFO_DTO)));
    }

    @Test
    @DisplayName("ClientController: Get client info without request params and should be error 400")
    void getClientInfo_WithoutParams_thenReturnResponse_400(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "wrongType");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .header("Authorization",  accessToken)
                .when()
                .get("/client/info")
                .then()
                .statusCode(400);
    }


    @ParameterizedTest
    @ValueSource(strings = {"full", "short"})
    @DisplayName("ClientController: Get client info without token and should be error 401")
    void getClientInfo_thenReturnResponse_401(String type){
        Map<String, String> params = new HashMap<>();

        params.put("type", type);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .when()
                .get("/client/info")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("ClientController: Edit client email and should return answer OK")
    void editClientEmail_thenReturnResponse_200(){
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .email("new@mail.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(200);

        assertEquals(clientService.getShortInfoClient(accessToken).getEmail(),
                request.getEmail());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("ClientController: Edit client email with identity and should return 409")
    void editClientEmail_thenReturnResponse_409(){
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .email("alisa.akopova2000@yandex.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(409);

        assertEquals(clientService.getShortInfoClient(accessToken).getEmail(),
                request.getEmail());
    }

    @Test
    @DisplayName("ClientController: Edit client email without token and should return answer 401")
    void editClientEmail_thenReturnResponse_401() {
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .email("new@mail.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("ClientController: Edit client email with another session token and should return answer 401")
    void editClientEmail_withSessionTokenMobile_thenReturnResponse_401() {
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .email("new@mail.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenMobile)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("ClientController: Edit client email without session token and should return answer 400")
    void editClientEmail_WithoutSessionToken_thenReturnResponse_400() {
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Edit client email with not correct body and should return answer 400")
    void editClientEmail_WithoutBody_thenReturnResponse_400() {
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Edit client mobile phone and should return answer OK")
    void editClientMobilePhone_thenReturnResponse_200(){
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .mobilePhone("89111231234")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenMobile)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(200);

        assertEquals(clientService.getShortInfoClient(accessToken).getMobilePhone(),
                request.getMobilePhone());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("ClientController: Edit client with identity mobile phone and should return answer 409")
    void editClientMobilePhone_thenReturnResponse_409(){
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .mobilePhone("89139229100")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenMobile)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(409);

    }


    @Test
    @DisplayName("ClientController: Edit client mobile phone without token and should return answer 401")
    void editClientMobilePhone_thenReturnResponse_401() {
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .mobilePhone("89111231234")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Session", sessionTokenMobile)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("ClientController: Edit client mobile phone with not correct body and should return answer 400")
    void editClientMobilePhone_WithoutBody_thenReturnResponse_400() {
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenMobile)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Edit client mobile phone with another session token and should return answer 401")
    void editClientMobilePhone_withSessionTokenEmail_thenReturnResponse_401(){
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .mobilePhone("89111231234")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .header("Session", sessionTokenEmail)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(401);

    }

    @Test
    @DisplayName("ClientController: Edit client mobile phone without session token and should return answer 400")
    void editClientMobilePhone_WithoutSessionToken_thenReturnResponse_400() {
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Add new address and should return answer 200")
    void addNewAddress_thenReturnResponse_200(){
        NewAddressDto request = NewAddressDto.builder()
                .country("Test")
                .city("Test")
                .street("Test")
                .house(1)
                .apartment(1)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .when()
                .put("/client/edit/address")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("ClientController: Add new address without token and should return answer 401")
    void addNewAddress_WithoutBody_thenReturnResponse_401(){
        NewAddressDto request = NewAddressDto.builder()
                .country("Test")
                .city("Test")
                .street("Test")
                .house(1)
                .apartment(1)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .put("/client/edit/address")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("ClientController: Add new address with not correct body and should return answer 400")
    void addNewAddress_WithoutBody_thenReturnResponse_400(){
        NewAddressDto request = NewAddressDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .when()
                .put("/client/edit/address")
                .then()
                .statusCode(400);
    }


    @Test
    @DisplayName("ClientController: Delete address and should return answer 200")
    void deleteNewAddress_thenReturnResponse_200(){
        DeleteAddressDto request = DeleteAddressDto.builder()
                .addressId(1L)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .when()
                .delete("/client/edit/delete/address")
                .then()
                .statusCode(200);

    }

    @Test
    @DisplayName("ClientController: Delete address without token and should return answer 401")
    void deleteNewAddress_thenReturnResponse_401(){
        DeleteAddressDto request = DeleteAddressDto.builder()
                .addressId(1L)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .delete("/client/edit/delete/address")
                .then()
                .statusCode(401);

    }

    @Test
    @DisplayName("ClientController: Delete address with not correct body and should return answer 400")
    void deleteNewAddress_WithoutBody_thenReturnResponse_400(){
        DeleteAddressDto request = DeleteAddressDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .header("Authorization", accessToken)
                .when()
                .delete("/client/edit/delete/address")
                .then()
                .statusCode(400);
    }
}
