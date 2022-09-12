package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Pointdevente;
import com.babaslim.app.service.dto.PointdeventeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pointdevente} and its DTO {@link PointdeventeDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddresseMapper.class })
public interface PointdeventeMapper extends EntityMapper<PointdeventeDTO, Pointdevente> {
    @Mapping(target = "addresse", source = "addresse", qualifiedByName = "id")
    PointdeventeDTO toDto(Pointdevente s);
}
