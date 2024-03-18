package ru.kazan.clientservice.utils.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {

    MALE(1, "муж"),
    FEMALE(2, "жен");

    private final Integer code;
    private final String text;

    @JsonValue
    public String getText() {
        return text;
    }
}
