package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteAddressDto {

    @JsonProperty(value = "addressId")
    private Long addressId;
}
