package ru.kazan.clientservice.unit.utils.convector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kazan.clientservice.utils.convector.TypeAuthEnumConverter;
import ru.kazan.clientservice.utils.enums.TypeAuthEnum;

import static org.junit.jupiter.api.Assertions.*;


class TypeAuthEnumConverterTest {

    private TypeAuthEnumConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TypeAuthEnumConverter();
    }

    @Test
    @DisplayName("Convert TypeAuthEnum to Database Column")
    void convertToDatabaseColumn() {
        assertEquals(TypeAuthEnum.EMAIL.getText(), converter.convertToDatabaseColumn(TypeAuthEnum.EMAIL));
        assertEquals(TypeAuthEnum.MOBILE_PHONE.getText(), converter.convertToDatabaseColumn(TypeAuthEnum.MOBILE_PHONE));
        assertEquals(TypeAuthEnum.PASSPORT.getText(), converter.convertToDatabaseColumn(TypeAuthEnum.PASSPORT));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Convert Database Column to TypeAuthEnum")
    void convertToEntityAttribute() {
        assertEquals(TypeAuthEnum.EMAIL, converter.convertToEntityAttribute(TypeAuthEnum.EMAIL.getText()));
        assertEquals(TypeAuthEnum.PASSPORT, converter.convertToEntityAttribute(TypeAuthEnum.PASSPORT.getText()));
        assertEquals(TypeAuthEnum.MOBILE_PHONE,
                converter.convertToEntityAttribute(TypeAuthEnum.MOBILE_PHONE.getText()));
    }

    @Test
    @DisplayName("Convert invalid Database Column to TypeAuthEnum should throw IllegalArgumentException")
    void convertInvalidStringToEntityAttribute() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
    }

    @Test
    @DisplayName("Convert null Database Column to TypeAuthEnum should return null")
    void convertNullToEntityAttribute() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));    }
}