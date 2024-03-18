package ru.kazan.clientservice.utils.convector;

import jakarta.persistence.AttributeConverter;
import ru.kazan.clientservice.utils.enums.GenderEnum;

public class GenderEnumConverter implements AttributeConverter<GenderEnum, String> {


    @Override
    public String convertToDatabaseColumn(GenderEnum genderEnum) {
        if(genderEnum == null)
            return null;

        return genderEnum.getText();
    }

    @Override
    public GenderEnum convertToEntityAttribute(String s) {
        if(s == null){
            return null;
        }

        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getText().equalsIgnoreCase(s)) {
                return gender;
            }
        }

        throw new IllegalArgumentException("Данный пол отсутвует: " + s);
    }
}
