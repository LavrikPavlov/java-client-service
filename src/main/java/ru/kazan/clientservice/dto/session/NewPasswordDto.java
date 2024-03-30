package ru.kazan.clientservice.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordDto {

    @NotNull
    @JsonProperty("new_password")
    private String newPassword;
}
