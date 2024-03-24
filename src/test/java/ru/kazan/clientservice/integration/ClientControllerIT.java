package ru.kazan.clientservice.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.*;


//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientControllerIT extends AbstractIntegrationTest {

    @Autowired
    private ClientService clientService;

    @BeforeEach
    void setUpClientController(){

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("ClientController: Get client FULL info and should return response with body ClientInfo")
    void getClientFullInfo_thenReturnResponse_200(){

        Map<String, String> params = new HashMap<>();

        params.put("clientId", TestClientConstants.CLIENT_ID_CORRECT);
        params.put("type", "full");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .when()
                .get("/client/info")
                .then()
                .statusCode(200)
                .body(equalTo(getJsonFromObject(TestClientConstants.RESPONSE_FULL_INFO_DTO)));
    }

    @Test
    @DisplayName("ClientController: Get client SHORT info and should return response with body ClientInfo")
    void getClientShortInfo_thenReturnResponse_200(){

        Map<String, String> params = new HashMap<>();

        params.put("clientId", TestClientConstants.CLIENT_ID_CORRECT);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .when()
                .get("/client/info")
                .then()
                .statusCode(200)
                .body(equalTo(getJsonFromObject(TestClientConstants.RESPONSE_SHORT_INFO_DTO)));
    }

    @Test
    @DisplayName("ClientController: Get client info without request params and should be error 415")
    void getClientInfo_WithoutParams_thenReturnResponse_400(){
        Map<String, String> params = new HashMap<>();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .when()
                .get("/client/info")
                .then()
                .statusCode(415);
    }


    @ParameterizedTest
    @ValueSource(strings = {"full", "short"})
    @DisplayName("ClientController: Get client info with not correct data and should be error 400")
    void getClientInfo_thenReturnResponse_400(String type){
        Map<String, String> params = new HashMap<>();

        params.put("clientId", TestClientConstants.CLIENT_ID_NOT_CORRECT);
        params.put("type", type);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .params(params)
                .when()
                .get("/client/info")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Edit client email and should return answer OK")
    void editClientEmail_thenReturnResponse_200(){
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .id(TestClientConstants.CLIENT_ID_CORRECT)
                .email("new@mail.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(200);

        assertEquals(clientService.getShortInfoClient(
                TestClientConstants.CLIENT_ID_CORRECT).getEmail(),
                request.getEmail());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("ClientController: Edit client email with identity and should return 409")
    void editClientEmail_thenReturnResponse_409(){
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .id(TestClientConstants.CLIENT_ID_CORRECT)
                .email("alisa.akopova2000@yandex.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(409);

        assertEquals(clientService.getShortInfoClient(
                        TestClientConstants.CLIENT_ID_CORRECT).getEmail(),
                request.getEmail());
    }

    @Test
    @DisplayName("ClientController: Edit client email with not correct ClientId and should return answer 400")
    void editClientEmail_thenReturnResponse_400() {
        RequestEditEmailDto request = RequestEditEmailDto.builder()
                .id(TestClientConstants.CLIENT_ID_NOT_CORRECT)
                .email("new@mail.ru")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
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
                .when()
                .patch("/client/edit/email")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Edit client mobile phone and should return answer OK")
    void editClientMobilePhone_thenReturnResponse_200(){
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .id(TestClientConstants.CLIENT_ID_CORRECT)
                .mobilePhone("89111231234")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(200);

        assertEquals(clientService.getShortInfoClient(
                        TestClientConstants.CLIENT_ID_CORRECT).getMobilePhone(),
                request.getMobilePhone());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("ClientController: Edit client with identity mobile phone and should return answer 409")
    void editClientMobilePhone_thenReturnResponse_409(){
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .id(TestClientConstants.CLIENT_ID_CORRECT)
                .mobilePhone("89139229100")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(409);

    }

    @Test
    @DisplayName("ClientController: Edit client mobile phone with not correct ClientId and should return answer 400")
    void editClientMobilePhone_thenReturnResponse_400() {
        RequestEditMobilePhoneDto request = RequestEditMobilePhoneDto.builder()
                .id(TestClientConstants.CLIENT_ID_NOT_CORRECT)
                .mobilePhone("89111231234")
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(400);
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
                .when()
                .patch("/client/edit/mobile-phone")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ClientController: Add new address and should return answer 200")
    void addNewAddress_thenReturnResponse_200(){
        NewAddressDto request = NewAddressDto.builder()
                .id(TestClientConstants.CLIENT_ID_CORRECT)
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
                .statusCode(200);
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
                .when()
                .put("/client/edit/address")
                .then()
                .statusCode(400);
    }


    @Test
    @DisplayName("ClientController: Delete address and should return answer 200")
    void deleteNewAddress_thenReturnResponse_200(){
        DeleteAddressDto request = DeleteAddressDto.builder()
                .id(TestClientConstants.CLIENT_ID_CORRECT)
                .addressId(1L)
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .delete("/client/edit/delete/address")
                .then()
                .statusCode(200);

    }

    @Test
    @DisplayName("ClientController: Delete address with not correct body and should return answer 415")
    void deleteNewAddress_WithoutBody_thenReturnResponse_400(){
        DeleteAddressDto request = DeleteAddressDto.builder()
                .build();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .delete("/client/edit/delete/address")
                .then()
                .statusCode(400);
    }
}
