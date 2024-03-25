package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEditEmailDto {

    @JsonProperty(value = "clientId")
    String id;

    @Email
    @JsonProperty(value = "newEmail")
    String email;
}
