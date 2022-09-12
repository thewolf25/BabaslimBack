package com.babaslim.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.babaslim.app.domain.Stock} entity.
 */
public class StockDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeEAN;

    @Min(value = 0)
    private Integer quantiteRestante;

    @NotNull
    private Instant dateAchat;

    private Instant dateDePeremption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEAN() {
        return codeEAN;
    }

    public void setCodeEAN(String codeEAN) {
        this.codeEAN = codeEAN;
    }

    public Integer getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(Integer quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public Instant getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Instant dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Instant getDateDePeremption() {
        return dateDePeremption;
    }

    public void setDateDePeremption(Instant dateDePeremption) {
        this.dateDePeremption = dateDePeremption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDTO)) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", codeEAN='" + getCodeEAN() + "'" +
            ", quantiteRestante=" + getQuantiteRestante() +
            ", dateAchat='" + getDateAchat() + "'" +
            ", dateDePeremption='" + getDateDePeremption() + "'" +
            "}";
    }
}
