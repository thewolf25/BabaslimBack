package com.babaslim.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lignedecommande.
 */
@Entity
@Table(name = "lignedecommande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lignedecommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "prix_unitaire_ht")
    private Double prixUnitaireHT;

    @JsonIgnoreProperties(value = { "tailles" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Produit produit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lignedecommande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Lignedecommande quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrixUnitaireHT() {
        return this.prixUnitaireHT;
    }

    public Lignedecommande prixUnitaireHT(Double prixUnitaireHT) {
        this.setPrixUnitaireHT(prixUnitaireHT);
        return this;
    }

    public void setPrixUnitaireHT(Double prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Lignedecommande produit(Produit produit) {
        this.setProduit(produit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lignedecommande)) {
            return false;
        }
        return id != null && id.equals(((Lignedecommande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lignedecommande{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", prixUnitaireHT=" + getPrixUnitaireHT() +
            "}";
    }
}
