package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Lignedecommande} entity.
 */
public class LignedecommandeDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer quantity;

    private Double prixUnitaireHT;

    private ProduitDTO produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrixUnitaireHT() {
        return prixUnitaireHT;
    }

    public void setPrixUnitaireHT(Double prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LignedecommandeDTO)) {
            return false;
        }

        LignedecommandeDTO lignedecommandeDTO = (LignedecommandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lignedecommandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LignedecommandeDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", prixUnitaireHT=" + getPrixUnitaireHT() +
            ", produit=" + getProduit() +
            "}";
    }
}
