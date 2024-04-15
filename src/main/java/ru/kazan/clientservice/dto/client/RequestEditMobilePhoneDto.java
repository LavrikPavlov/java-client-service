package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestEditMobilePhoneDto {

    @Pattern(regexp = "\\+7[0-9]{10}")
    @JsonProperty(value = "newMobilePhone")
    String mobilePhone;
}
