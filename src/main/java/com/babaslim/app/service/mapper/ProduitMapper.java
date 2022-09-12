package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Produit;
import com.babaslim.app.service.dto.ProduitDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produit} and its DTO {@link ProduitDTO}.
 */
@Mapper(componentModel = "spring", uses = { TailleMapper.class })
public interface ProduitMapper extends EntityMapper<ProduitDTO, Produit> {
    @Mapping(target = "tailles", source = "tailles", qualifiedByName = "idSet")
    ProduitDTO toDto(Produit s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProduitDTO toDtoId(Produit produit);

    @Mapping(target = "removeTaille", ignore = true)
    Produit toEntity(ProduitDTO produitDTO);
}
