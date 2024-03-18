package ru.kazan.clientservice.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @RequestInfoDto not used
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfoDto {

    @JsonProperty("clientId")
    String id;

    @JsonProperty(required = true, value = "type")
    String type;
}
