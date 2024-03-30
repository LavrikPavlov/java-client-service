package ru.kazan.clientservice.utils.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TypeAuthEnum {

    PASSPORT("passport"),
    MOBILE_PHONE("mobile_phone"),
    EMAIL("email");

    private final String text;

    @JsonValue
    public String getText(){ return text; }

}
