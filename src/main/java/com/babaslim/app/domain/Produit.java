package com.babaslim.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "composition", nullable = false)
    private String composition;

    @Column(name = "unite")
    private String unite;

    @Column(name = "prix_unitaire_ht")
    private Double prixUnitaireHT;

    @ManyToMany
    @JoinTable(
        name = "rel_produit__taille",
        joinColumns = @JoinColumn(name = "produit_id"),
        inverseJoinColumns = @JoinColumn(name = "taille_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "produits" }, allowSetters = true)
    private Set<Taille> tailles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Produit reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Produit libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getComposition() {
        return this.composition;
    }

    public Produit composition(String composition) {
        this.setComposition(composition);
        return this;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getUnite() {
        return this.unite;
    }

    public Produit unite(String unite) {
        this.setUnite(unite);
        return this;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Double getPrixUnitaireHT() {
        return this.prixUnitaireHT;
    }

    public Produit prixUnitaireHT(Double prixUnitaireHT) {
        this.setPrixUnitaireHT(prixUnitaireHT);
        return this;
    }

    public void setPrixUnitaireHT(Double prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public Set<Taille> getTailles() {
        return this.tailles;
    }

    public void setTailles(Set<Taille> tailles) {
        this.tailles = tailles;
    }

    public Produit tailles(Set<Taille> tailles) {
        this.setTailles(tailles);
        return this;
    }

    public Produit addTaille(Taille taille) {
        this.tailles.add(taille);
        taille.getProduits().add(this);
        return this;
    }

    public Produit removeTaille(Taille taille) {
        this.tailles.remove(taille);
        taille.getProduits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", composition='" + getComposition() + "'" +
            ", unite='" + getUnite() + "'" +
            ", prixUnitaireHT=" + getPrixUnitaireHT() +
            "}";
    }
}
