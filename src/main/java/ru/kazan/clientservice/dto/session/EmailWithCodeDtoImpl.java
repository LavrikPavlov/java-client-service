package ru.kazan.clientservice.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailWithCodeDtoImpl implements CodeDto {

    @NotNull
    @Email
    @JsonProperty("email")
    private String contact;

    @NotNull
    @JsonProperty("verify_code")
    private String verifyCode;

}
