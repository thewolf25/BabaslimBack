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
 * A Taille.
 */
@Entity
@Table(name = "taille")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Taille implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "taille", nullable = false, unique = true)
    private String taille;

    @ManyToMany(mappedBy = "tailles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tailles" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Taille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaille() {
        return this.taille;
    }

    public Taille taille(String taille) {
        this.setTaille(taille);
        return this;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<Produit> produits) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.removeTaille(this));
        }
        if (produits != null) {
            produits.forEach(i -> i.addTaille(this));
        }
        this.produits = produits;
    }

    public Taille produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public Taille addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getTailles().add(this);
        return this;
    }

    public Taille removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getTailles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Taille)) {
            return false;
        }
        return id != null && id.equals(((Taille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Taille{" +
            "id=" + getId() +
            ", taille='" + getTaille() + "'" +
            "}";
    }
}
