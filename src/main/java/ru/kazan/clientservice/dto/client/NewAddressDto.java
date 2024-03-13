package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewAddressDto {

    @JsonProperty(value = "clientId")
    private String id;

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
