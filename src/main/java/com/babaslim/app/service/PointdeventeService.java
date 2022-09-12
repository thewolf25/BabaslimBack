package com.babaslim.app.service;

import com.babaslim.app.service.dto.PointdeventeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.babaslim.app.domain.Pointdevente}.
 */
public interface PointdeventeService {
    /**
     * Save a pointdevente.
     *
     * @param pointdeventeDTO the entity to save.
     * @return the persisted entity.
     */
    PointdeventeDTO save(PointdeventeDTO pointdeventeDTO);

    /**
     * Partially updates a pointdevente.
     *
     * @param pointdeventeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PointdeventeDTO> partialUpdate(PointdeventeDTO pointdeventeDTO);

    /**
     * Get all the pointdeventes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PointdeventeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pointdevente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PointdeventeDTO> findOne(Long id);

    /**
     * Delete the "id" pointdevente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
