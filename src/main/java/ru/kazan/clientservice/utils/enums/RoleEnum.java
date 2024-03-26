package ru.kazan.clientservice.utils.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN(0,"Администратор"),
    CLIENT(1, "Клиент");

    private final Integer id;
    private final String roleText;

    @JsonValue
    public String getText() {
        return roleText;
    }

}
