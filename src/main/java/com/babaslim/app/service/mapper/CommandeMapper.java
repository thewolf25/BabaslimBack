package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Commande;
import com.babaslim.app.service.dto.CommandeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commande} and its DTO {@link CommandeDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddresseMapper.class })
public interface CommandeMapper extends EntityMapper<CommandeDTO, Commande> {
    @Mapping(target = "addresse", source = "addresse", qualifiedByName = "id")
    CommandeDTO toDto(Commande s);
}
