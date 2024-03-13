package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEditMobilePhoneDto {
    @JsonProperty(value = "clientId")
    String id;

    @Email
    @JsonProperty(value = "newMobilePhone")
    String mobilePhone;
}
