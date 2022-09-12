package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Commande} entity.
 */
public class CommandeDTO implements Serializable {

    private Long id;

    @NotNull
    private String numero;

    @NotNull
    private Instant dateEnregistrement;

    private AddresseDTO addresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Instant getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Instant dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
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
        if (!(o instanceof CommandeDTO)) {
            return false;
        }

        CommandeDTO commandeDTO = (CommandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            ", addresse=" + getAddresse() +
            "}";
    }
}
