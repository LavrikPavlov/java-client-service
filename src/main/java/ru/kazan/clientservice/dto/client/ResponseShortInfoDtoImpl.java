package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.kazan.clientservice.utils.enums.ClientStatus;
import ru.kazan.clientservice.utils.enums.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseShortInfoDtoImpl implements ResponseInfoDto {

    @JsonProperty(value = "first_name")
    String firstName;

    @JsonProperty(value = "last_name")
    String lastName;

    @JsonProperty(value = "patronymic")
    String patronymic;

    @JsonProperty(value = "gender")
    GenderEnum gender;

    @JsonProperty(value = "mobile_phone")
    String mobilePhone;

    @JsonProperty(value = "email")
    String email;

    @JsonProperty(value = "age")
    Integer age;

    @JsonProperty(value = "status")
    ClientStatus status;
}
