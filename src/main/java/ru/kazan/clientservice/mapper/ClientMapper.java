package ru.kazan.clientservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kazan.clientservice.dto.client.ResponseShortInfoDto;
import ru.kazan.clientservice.model.Client;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClientMapper {

    @Mapping(target = "gender", source = "passport.gender")
    ResponseShortInfoDto toShrotInfoDto (Client client);



}
