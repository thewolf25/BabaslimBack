package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Addresse} entity.
 */
public class AddresseDTO implements Serializable {

    private Long id;

    @NotNull
    private String rue;

    private Integer codePostale;

    private String complement;

    private String commune;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public Integer getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(Integer codePostale) {
        this.codePostale = codePostale;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddresseDTO)) {
            return false;
        }

        AddresseDTO addresseDTO = (AddresseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addresseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddresseDTO{" +
            "id=" + getId() +
            ", rue='" + getRue() + "'" +
            ", codePostale=" + getCodePostale() +
            ", complement='" + getComplement() + "'" +
            ", commune='" + getCommune() + "'" +
            "}";
    }
}
