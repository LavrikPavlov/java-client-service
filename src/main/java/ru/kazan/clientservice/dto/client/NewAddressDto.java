package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewAddressDto {

    @JsonProperty(value = "country", required = true)
    private String country;

    @JsonProperty(value = "city", required = true)
    private String city;

    @JsonProperty(value = "street", required = true)
    private String street;

    @JsonProperty(value = "house", required = true)
    private Integer house;

    @JsonProperty(value = "apartment")
    private Integer apartment;

}
