package ru.kazan.clientservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.kazan.clientservice.dto.client.NewAddressDto;
import ru.kazan.clientservice.model.Address;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    Address toAddress(NewAddressDto dto);
}
