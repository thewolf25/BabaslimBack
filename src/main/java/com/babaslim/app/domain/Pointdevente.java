package com.babaslim.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pointdevente.
 */
@Entity
@Table(name = "pointdevente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pointdevente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "siret", nullable = false, unique = true)
    private Integer siret;

    @OneToOne
    @JoinColumn(unique = true)
    private Addresse addresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pointdevente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSiret() {
        return this.siret;
    }

    public Pointdevente siret(Integer siret) {
        this.setSiret(siret);
        return this;
    }

    public void setSiret(Integer siret) {
        this.siret = siret;
    }

    public Addresse getAddresse() {
        return this.addresse;
    }

    public void setAddresse(Addresse addresse) {
        this.addresse = addresse;
    }

    public Pointdevente addresse(Addresse addresse) {
        this.setAddresse(addresse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pointdevente)) {
            return false;
        }
        return id != null && id.equals(((Pointdevente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pointdevente{" +
            "id=" + getId() +
            ", siret=" + getSiret() +
            "}";
    }
}
