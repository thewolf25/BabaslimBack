package com.babaslim.app.web.rest;

import com.babaslim.app.repository.AddresseRepository;
import com.babaslim.app.service.AddresseService;
import com.babaslim.app.service.dto.AddresseDTO;
import com.babaslim.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.babaslim.app.domain.Addresse}.
 */
@RestController
@RequestMapping("/api")
public class AddresseResource {

    private final Logger log = LoggerFactory.getLogger(AddresseResource.class);

    private static final String ENTITY_NAME = "addresse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddresseService addresseService;

    private final AddresseRepository addresseRepository;

    public AddresseResource(AddresseService addresseService, AddresseRepository addresseRepository) {
        this.addresseService = addresseService;
        this.addresseRepository = addresseRepository;
    }

    /**
     * {@code POST  /addresses} : Create a new addresse.
     *
     * @param addresseDTO the addresseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addresseDTO, or with status {@code 400 (Bad Request)} if the addresse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/addresses")
    public ResponseEntity<AddresseDTO> createAddresse(@Valid @RequestBody AddresseDTO addresseDTO) throws URISyntaxException {
        log.debug("REST request to save Addresse : {}", addresseDTO);
        if (addresseDTO.getId() != null) {
            throw new BadRequestAlertException("A new addresse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddresseDTO result = addresseService.save(addresseDTO);
        return ResponseEntity
            .created(new URI("/api/addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /addresses/:id} : Updates an existing addresse.
     *
     * @param id the id of the addresseDTO to save.
     * @param addresseDTO the addresseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addresseDTO,
     * or with status {@code 400 (Bad Request)} if the addresseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addresseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddresseDTO> updateAddresse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AddresseDTO addresseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Addresse : {}, {}", id, addresseDTO);
        if (addresseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addresseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addresseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AddresseDTO result = addresseService.save(addresseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addresseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /addresses/:id} : Partial updates given fields of an existing addresse, field will ignore if it is null
     *
     * @param id the id of the addresseDTO to save.
     * @param addresseDTO the addresseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addresseDTO,
     * or with status {@code 400 (Bad Request)} if the addresseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the addresseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the addresseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/addresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AddresseDTO> partialUpdateAddresse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AddresseDTO addresseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Addresse partially : {}, {}", id, addresseDTO);
        if (addresseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addresseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addresseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AddresseDTO> result = addresseService.partialUpdate(addresseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addresseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /addresses} : get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addresses in body.
     */
    @GetMapping("/addresses")
    public ResponseEntity<List<AddresseDTO>> getAllAddresses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Addresses");
        Page<AddresseDTO> page = addresseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /addresses/:id} : get the "id" addresse.
     *
     * @param id the id of the addresseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addresseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/addresses/{id}")
    public ResponseEntity<AddresseDTO> getAddresse(@PathVariable Long id) {
        log.debug("REST request to get Addresse : {}", id);
        Optional<AddresseDTO> addresseDTO = addresseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addresseDTO);
    }

    /**
     * {@code DELETE  /addresses/:id} : delete the "id" addresse.
     *
     * @param id the id of the addresseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddresse(@PathVariable Long id) {
        log.debug("REST request to delete Addresse : {}", id);
        addresseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
