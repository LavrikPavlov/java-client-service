package ru.kazan.clientservice.unit.utils.convector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kazan.clientservice.utils.convector.RoleEnumConverter;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoleEnumConverterTest {

    private RoleEnumConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RoleEnumConverter();
    }

    @Test
    @DisplayName("Convert GenderEnum to Database Column")
    void convertToDatabaseColumn() {
        assertEquals(RoleEnum.ADMIN.getRoleText(), converter.convertToDatabaseColumn(RoleEnum.ADMIN));
        assertEquals(RoleEnum.NOT_CLIENT.getRoleText(), converter.convertToDatabaseColumn(RoleEnum.NOT_CLIENT));
        assertEquals(RoleEnum.CLIENT.getRoleText(), converter.convertToDatabaseColumn(RoleEnum.CLIENT));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Convert Database Column to GenderEnum")
    void convertToEntityAttribute() {
        assertEquals(RoleEnum.ADMIN, converter.convertToEntityAttribute(RoleEnum.ADMIN.getText()));
        assertEquals(RoleEnum.NOT_CLIENT, converter.convertToEntityAttribute(RoleEnum.NOT_CLIENT.getText()));
        assertEquals(RoleEnum.CLIENT, converter.convertToEntityAttribute(RoleEnum.CLIENT.getText()));
    }

    @Test
    @DisplayName("Convert invalid Database Column to GenderEnum should throw IllegalArgumentException")
    void convertInvalidStringToEntityAttribute() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
    }

    @Test
    @DisplayName("Convert null Database Column to GenderEnum should return null")
    void convertNullToEntityAttribute() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
    }

}
