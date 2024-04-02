package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestEditMobilePhoneDto {

    @Email
    @JsonProperty(value = "newMobilePhone")
    String mobilePhone;
}
