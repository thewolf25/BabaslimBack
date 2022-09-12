package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Pointdevente} entity.
 */
public class PointdeventeDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer siret;

    private AddresseDTO addresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSiret() {
        return siret;
    }

    public void setSiret(Integer siret) {
        this.siret = siret;
    }

    public AddresseDTO getAddresse() {
        return addresse;
    }

    public void setAddresse(AddresseDTO addresse) {
        this.addresse = addresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointdeventeDTO)) {
            return false;
        }

        PointdeventeDTO pointdeventeDTO = (PointdeventeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pointdeventeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointdeventeDTO{" +
            "id=" + getId() +
            ", siret=" + getSiret() +
            ", addresse=" + getAddresse() +
            "}";
    }
}
