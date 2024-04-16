package ru.kazan.clientservice.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobilePhoneCodeDtoImpl implements CodeDto{

    @NotNull
    @Pattern(regexp = "8[0-9]{10}")
    @JsonProperty("mobile_phone")
    private String contact;

    @NotNull
    @JsonProperty("verify_code")
    private String verifyCode;

}
