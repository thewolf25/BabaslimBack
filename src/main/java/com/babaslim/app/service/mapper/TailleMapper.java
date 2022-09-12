package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Taille;
import com.babaslim.app.service.dto.TailleDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Taille} and its DTO {@link TailleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TailleMapper extends EntityMapper<TailleDTO, Taille> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<TailleDTO> toDtoIdSet(Set<Taille> taille);
}
