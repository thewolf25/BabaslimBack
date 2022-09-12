package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Taille} entity.
 */
public class TailleDTO implements Serializable {

    private Long id;

    @NotNull
    private String taille;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TailleDTO)) {
            return false;
        }

        TailleDTO tailleDTO = (TailleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tailleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TailleDTO{" +
            "id=" + getId() +
            ", taille='" + getTaille() + "'" +
            "}";
    }
}
