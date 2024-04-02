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

    @Email
    @JsonProperty(value = "newEmail")
    String email;
}
