package com.babaslim.app.service;

import com.babaslim.app.service.dto.AddresseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.babaslim.app.domain.Addresse}.
 */
public interface AddresseService {
    /**
     * Save a addresse.
     *
     * @param addresseDTO the entity to save.
     * @return the persisted entity.
     */
    AddresseDTO save(AddresseDTO addresseDTO);

    /**
     * Partially updates a addresse.
     *
     * @param addresseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AddresseDTO> partialUpdate(AddresseDTO addresseDTO);

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AddresseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" addresse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddresseDTO> findOne(Long id);

    /**
     * Delete the "id" addresse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
