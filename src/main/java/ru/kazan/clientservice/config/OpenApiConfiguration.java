package ru.kazan.clientservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Client Service API",
                version = "1.0.0",
                description = "API for interacting with client information",
                contact = @Contact(
                        name = "Mikhail Kazan",
                        email = "misha-kazan2013@yandex.ru",
                        url = "https://github.com/lavrikpavlov"
                )
        )
)
public class OpenApiConfiguration {

}