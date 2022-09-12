package com.babaslim.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Addresse.
 */
@Entity
@Table(name = "addresse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Addresse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "rue", nullable = false)
    private String rue;

    @Column(name = "code_postale")
    private Integer codePostale;

    @Column(name = "complement")
    private String complement;

    @Column(name = "commune")
    private String commune;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Addresse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRue() {
        return this.rue;
    }

    public Addresse rue(String rue) {
        this.setRue(rue);
        return this;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public Integer getCodePostale() {
        return this.codePostale;
    }

    public Addresse codePostale(Integer codePostale) {
        this.setCodePostale(codePostale);
        return this;
    }

    public void setCodePostale(Integer codePostale) {
        this.codePostale = codePostale;
    }

    public String getComplement() {
        return this.complement;
    }

    public Addresse complement(String complement) {
        this.setComplement(complement);
        return this;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCommune() {
        return this.commune;
    }

    public Addresse commune(String commune) {
        this.setCommune(commune);
        return this;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Addresse)) {
            return false;
        }
        return id != null && id.equals(((Addresse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Addresse{" +
            "id=" + getId() +
            ", rue='" + getRue() + "'" +
            ", codePostale=" + getCodePostale() +
            ", complement='" + getComplement() + "'" +
            ", commune='" + getCommune() + "'" +
            "}";
    }
}
