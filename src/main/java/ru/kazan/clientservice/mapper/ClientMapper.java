package ru.kazan.clientservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kazan.clientservice.dto.client.ResponseFullInfoDtoImpl;
import ru.kazan.clientservice.dto.client.ResponseShortInfoDtoImpl;
import ru.kazan.clientservice.model.Client;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClientMapper {

    @Mapping(target = "gender", source = "passport.gender")
    ResponseShortInfoDtoImpl toShortInfoDto (Client client);

    ResponseFullInfoDtoImpl toFullInfoDto (Client client);


}
