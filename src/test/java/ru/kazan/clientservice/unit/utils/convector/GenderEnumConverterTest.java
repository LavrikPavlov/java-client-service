package ru.kazan.clientservice.unit.utils.convector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kazan.clientservice.utils.convector.GenderEnumConverter;
import ru.kazan.clientservice.utils.enums.GenderEnum;

import static org.junit.jupiter.api.Assertions.*;


class GenderEnumConverterTest {

    private GenderEnumConverter converter;

    @BeforeEach
    void setUp() {
        converter = new GenderEnumConverter();
    }

    @Test
    @DisplayName("Convert GenderEnum to Database Column")
    void convertToDatabaseColumn() {
        assertEquals(GenderEnum.MALE.getText(), converter.convertToDatabaseColumn(GenderEnum.MALE));
        assertEquals(GenderEnum.FEMALE.getText(), converter.convertToDatabaseColumn(GenderEnum.FEMALE));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Convert Database Column to GenderEnum")
    void convertToEntityAttribute() {
        assertEquals(GenderEnum.MALE, converter.convertToEntityAttribute(GenderEnum.MALE.getText()));
        assertEquals(GenderEnum.FEMALE, converter.convertToEntityAttribute(GenderEnum.FEMALE.getText()));
    }

    @Test
    @DisplayName("Convert invalid Database Column to GenderEnum should throw IllegalArgumentException")
    void convertInvalidStringToEntityAttribute() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
    }

    @Test
    @DisplayName("Convert null Database Column to GenderEnum should return null")
    void convertNullToEntityAttribute() {
        assertNull(converter.convertToEntityAttribute(null));
    }
}