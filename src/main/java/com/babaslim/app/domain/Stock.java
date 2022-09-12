package com.babaslim.app.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_ean", nullable = false)
    private String codeEAN;

    @Min(value = 0)
    @Column(name = "quantite_restante")
    private Integer quantiteRestante;

    @NotNull
    @Column(name = "date_achat", nullable = false)
    private Instant dateAchat;

    @Column(name = "date_de_peremption")
    private Instant dateDePeremption;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEAN() {
        return this.codeEAN;
    }

    public Stock codeEAN(String codeEAN) {
        this.setCodeEAN(codeEAN);
        return this;
    }

    public void setCodeEAN(String codeEAN) {
        this.codeEAN = codeEAN;
    }

    public Integer getQuantiteRestante() {
        return this.quantiteRestante;
    }

    public Stock quantiteRestante(Integer quantiteRestante) {
        this.setQuantiteRestante(quantiteRestante);
        return this;
    }

    public void setQuantiteRestante(Integer quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public Instant getDateAchat() {
        return this.dateAchat;
    }

    public Stock dateAchat(Instant dateAchat) {
        this.setDateAchat(dateAchat);
        return this;
    }

    public void setDateAchat(Instant dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Instant getDateDePeremption() {
        return this.dateDePeremption;
    }

    public Stock dateDePeremption(Instant dateDePeremption) {
        this.setDateDePeremption(dateDePeremption);
        return this;
    }

    public void setDateDePeremption(Instant dateDePeremption) {
        this.dateDePeremption = dateDePeremption;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return id != null && id.equals(((Stock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", codeEAN='" + getCodeEAN() + "'" +
            ", quantiteRestante=" + getQuantiteRestante() +
            ", dateAchat='" + getDateAchat() + "'" +
            ", dateDePeremption='" + getDateDePeremption() + "'" +
            "}";
    }
}
