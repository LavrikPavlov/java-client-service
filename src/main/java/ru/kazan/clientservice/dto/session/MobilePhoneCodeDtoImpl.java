package ru.kazan.clientservice.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobilePhoneCodeDtoImpl implements CodeDto{

    @NotNull
    @JsonProperty("mobile_phone")
    private String contact;

    @NotNull
    @JsonProperty("verify_code")
    private String verifyCode;

}
