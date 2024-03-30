package ru.kazan.clientservice.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationClientDto {

    private String email;
    private String mobilePhone;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Integer age;
}
