package ru.kazan.clientservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.kazan.clientservice.utils.enums.TypeAuthEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginWithPasswordDto {

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
