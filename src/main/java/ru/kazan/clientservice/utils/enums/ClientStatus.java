package ru.kazan.clientservice.utils.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientStatus {
    NOT_ACCEPT(0, "Не зарегестрирован"),
    ACCEPT(1, "Потвержеденный");

    private final Integer code;
    private final String text;

    @JsonValue
    public String getText() {
        return text;
    }
}
