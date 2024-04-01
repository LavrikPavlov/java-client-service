package ru.kazan.clientservice.utils.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN(0,"Администратор"),
    CLIENT(1, "Клиент"),
    NOT_CLIENT(2, "Не потвержеднный");

    private final Integer id;
    private final String roleText;

    @JsonValue
    public String getText() {
        return roleText;
    }

    public static RoleEnum fromText(String text) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.roleText.equals(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + text);
    }
}
