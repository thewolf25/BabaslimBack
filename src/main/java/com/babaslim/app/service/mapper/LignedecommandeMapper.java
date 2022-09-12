package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Lignedecommande;
import com.babaslim.app.service.dto.LignedecommandeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lignedecommande} and its DTO {@link LignedecommandeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProduitMapper.class })
public interface LignedecommandeMapper extends EntityMapper<LignedecommandeDTO, Lignedecommande> {
    @Mapping(target = "produit", source = "produit", qualifiedByName = "id")
    LignedecommandeDTO toDto(Lignedecommande s);
}
