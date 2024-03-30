package ru.kazan.clientservice.utils.convector;

import jakarta.persistence.AttributeConverter;
import ru.kazan.clientservice.utils.enums.RoleEnum;

public class RoleEnumConverter implements AttributeConverter<RoleEnum, String> {
    @Override
    public String convertToDatabaseColumn(RoleEnum roleEnum) {
        if(roleEnum == null)
            return null;

        return roleEnum.getRoleText();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String string) {

        for(RoleEnum role : RoleEnum.values()){
            if(role.getRoleText().equalsIgnoreCase(string))
                return role;
        }

        throw new IllegalArgumentException("Роль отсутвует: " + string);
    }
}
