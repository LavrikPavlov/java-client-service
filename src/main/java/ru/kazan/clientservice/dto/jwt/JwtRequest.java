package ru.kazan.clientservice.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.kazan.clientservice.utils.enums.TypeAuthEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {

    @NotNull
    @JsonProperty("login")
    private String login;

    @NotNull
    @JsonProperty("password")
    private String password;

    @NotNull
    @JsonProperty("type")
    private TypeAuthEnum type;

}
