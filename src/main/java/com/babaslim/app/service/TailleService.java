package com.babaslim.app.service;

import com.babaslim.app.service.dto.TailleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.babaslim.app.domain.Taille}.
 */
public interface TailleService {
    /**
     * Save a taille.
     *
     * @param tailleDTO the entity to save.
     * @return the persisted entity.
     */
    TailleDTO save(TailleDTO tailleDTO);

    /**
     * Partially updates a taille.
     *
     * @param tailleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TailleDTO> partialUpdate(TailleDTO tailleDTO);

    /**
     * Get all the tailles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TailleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" taille.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TailleDTO> findOne(Long id);

    /**
     * Delete the "id" taille.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
