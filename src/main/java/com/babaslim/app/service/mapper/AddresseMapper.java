package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Addresse;
import com.babaslim.app.service.dto.AddresseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Addresse} and its DTO {@link AddresseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AddresseMapper extends EntityMapper<AddresseDTO, Addresse> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddresseDTO toDtoId(Addresse addresse);
}
