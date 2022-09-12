package com.babaslim.app.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "date_enregistrement", nullable = false)
    private Instant dateEnregistrement;

    @OneToOne
    @JoinColumn(unique = true)
    private Addresse addresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Commande numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Instant getDateEnregistrement() {
        return this.dateEnregistrement;
    }

    public Commande dateEnregistrement(Instant dateEnregistrement) {
        this.setDateEnregistrement(dateEnregistrement);
        return this;
    }

    public void setDateEnregistrement(Instant dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public Addresse getAddresse() {
        return this.addresse;
    }

    public void setAddresse(Addresse addresse) {
        this.addresse = addresse;
    }

    public Commande addresse(Addresse addresse) {
        this.setAddresse(addresse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            "}";
    }
}
