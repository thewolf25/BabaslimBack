package com.babaslim.app.repository;

import com.babaslim.app.domain.Produit;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProduitRepositoryWithBagRelationshipsImpl implements ProduitRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Produit> fetchBagRelationships(Optional<Produit> produit) {
        return produit.map(this::fetchTailles);
    }

    @Override
    public Page<Produit> fetchBagRelationships(Page<Produit> produits) {
        return new PageImpl<>(fetchBagRelationships(produits.getContent()), produits.getPageable(), produits.getTotalElements());
    }

    @Override
    public List<Produit> fetchBagRelationships(List<Produit> produits) {
        return Optional.of(produits).map(this::fetchTailles).get();
    }

    Produit fetchTailles(Produit result) {
        return entityManager
            .createQuery("select produit from Produit produit left join fetch produit.tailles where produit is :produit", Produit.class)
            .setParameter("produit", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Produit> fetchTailles(List<Produit> produits) {
        return entityManager
            .createQuery(
                "select distinct produit from Produit produit left join fetch produit.tailles where produit in :produits",
                Produit.class
            )
            .setParameter("produits", produits)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
