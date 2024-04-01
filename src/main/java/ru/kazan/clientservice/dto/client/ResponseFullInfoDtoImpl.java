package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.kazan.clientservice.model.Address;
import ru.kazan.clientservice.model.Passport;
import ru.kazan.clientservice.utils.enums.ClientStatus;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFullInfoDtoImpl implements ResponseInfoDto {

    @JsonProperty(value = "first_name")
    String firstName;

    @JsonProperty(value = "last_name")
    String lastName;

    @JsonProperty(value = "patronymic")
    String patronymic;

    @JsonProperty(value = "mobile_phone")
    String mobilePhone;

    @JsonProperty(value = "email")
    String email;

    @JsonProperty(value = "age")
    Integer age;

    @JsonProperty(value = "status")
    ClientStatus status;

    @JsonProperty(value = "date_registration")
    String dateRegistration;

    @JsonProperty(value = "passport")
    Passport passport;

    @JsonProperty(value = "addresses")
    Set<Address> address;

}
