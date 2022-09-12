package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Produit} entity.
 */
public class ProduitDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    @NotNull
    private String libelle;

    @NotNull
    private String composition;

    private String unite;

    private Double prixUnitaireHT;

    private Set<TailleDTO> tailles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Double getPrixUnitaireHT() {
        return prixUnitaireHT;
    }

    public void setPrixUnitaireHT(Double prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public Set<TailleDTO> getTailles() {
        return tailles;
    }

    public void setTailles(Set<TailleDTO> tailles) {
        this.tailles = tailles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProduitDTO)) {
            return false;
        }

        ProduitDTO produitDTO = (ProduitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, produitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProduitDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", composition='" + getComposition() + "'" +
            ", unite='" + getUnite() + "'" +
            ", prixUnitaireHT=" + getPrixUnitaireHT() +
            ", tailles=" + getTailles() +
            "}";
    }
}
