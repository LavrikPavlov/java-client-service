package ru.kazan.clientservice.utils.convector;

import jakarta.persistence.AttributeConverter;
import ru.kazan.clientservice.utils.enums.TypeAuthEnum;

public class TypeAuthEnumConverter implements AttributeConverter<TypeAuthEnum, String> {
    @Override
    public String convertToDatabaseColumn(TypeAuthEnum typeAuthEnum) {
        if(typeAuthEnum == null)
            return null;

        return typeAuthEnum.getText();
    }

    @Override
    public TypeAuthEnum convertToEntityAttribute(String string) {
        for(TypeAuthEnum text : TypeAuthEnum.values()){
            if(text.getText().equalsIgnoreCase(string))
                return text;
        }

        throw new IllegalArgumentException("Отсутсвует тип логина: " + string);
    }
}
