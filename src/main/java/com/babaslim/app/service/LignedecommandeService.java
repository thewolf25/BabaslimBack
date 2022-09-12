package com.babaslim.app.service;

import com.babaslim.app.service.dto.LignedecommandeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.babaslim.app.domain.Lignedecommande}.
 */
public interface LignedecommandeService {
    /**
     * Save a lignedecommande.
     *
     * @param lignedecommandeDTO the entity to save.
     * @return the persisted entity.
     */
    LignedecommandeDTO save(LignedecommandeDTO lignedecommandeDTO);

    /**
     * Partially updates a lignedecommande.
     *
     * @param lignedecommandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LignedecommandeDTO> partialUpdate(LignedecommandeDTO lignedecommandeDTO);

    /**
     * Get all the lignedecommandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LignedecommandeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lignedecommande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LignedecommandeDTO> findOne(Long id);

    /**
     * Delete the "id" lignedecommande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
