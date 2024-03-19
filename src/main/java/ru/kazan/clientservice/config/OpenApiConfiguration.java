package ru.kazan.clientservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Client API",
                description = "Client Service", version = "0.0.1"
        )
)
public class OpenApiConfiguration {

}

