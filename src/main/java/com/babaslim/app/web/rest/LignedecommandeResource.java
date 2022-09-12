package com.babaslim.app.web.rest;

import com.babaslim.app.repository.LignedecommandeRepository;
import com.babaslim.app.service.LignedecommandeService;
import com.babaslim.app.service.dto.LignedecommandeDTO;
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
 * REST controller for managing {@link com.babaslim.app.domain.Lignedecommande}.
 */
@RestController
@RequestMapping("/api")
public class LignedecommandeResource {

    private final Logger log = LoggerFactory.getLogger(LignedecommandeResource.class);

    private static final String ENTITY_NAME = "lignedecommande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LignedecommandeService lignedecommandeService;

    private final LignedecommandeRepository lignedecommandeRepository;

    public LignedecommandeResource(LignedecommandeService lignedecommandeService, LignedecommandeRepository lignedecommandeRepository) {
        this.lignedecommandeService = lignedecommandeService;
        this.lignedecommandeRepository = lignedecommandeRepository;
    }

    /**
     * {@code POST  /lignedecommandes} : Create a new lignedecommande.
     *
     * @param lignedecommandeDTO the lignedecommandeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lignedecommandeDTO, or with status {@code 400 (Bad Request)} if the lignedecommande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lignedecommandes")
    public ResponseEntity<LignedecommandeDTO> createLignedecommande(@Valid @RequestBody LignedecommandeDTO lignedecommandeDTO)
        throws URISyntaxException {
        log.debug("REST request to save Lignedecommande : {}", lignedecommandeDTO);
        if (lignedecommandeDTO.getId() != null) {
            throw new BadRequestAlertException("A new lignedecommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LignedecommandeDTO result = lignedecommandeService.save(lignedecommandeDTO);
        return ResponseEntity
            .created(new URI("/api/lignedecommandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lignedecommandes/:id} : Updates an existing lignedecommande.
     *
     * @param id the id of the lignedecommandeDTO to save.
     * @param lignedecommandeDTO the lignedecommandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lignedecommandeDTO,
     * or with status {@code 400 (Bad Request)} if the lignedecommandeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lignedecommandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lignedecommandes/{id}")
    public ResponseEntity<LignedecommandeDTO> updateLignedecommande(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LignedecommandeDTO lignedecommandeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Lignedecommande : {}, {}", id, lignedecommandeDTO);
        if (lignedecommandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lignedecommandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lignedecommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LignedecommandeDTO result = lignedecommandeService.save(lignedecommandeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lignedecommandeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lignedecommandes/:id} : Partial updates given fields of an existing lignedecommande, field will ignore if it is null
     *
     * @param id the id of the lignedecommandeDTO to save.
     * @param lignedecommandeDTO the lignedecommandeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lignedecommandeDTO,
     * or with status {@code 400 (Bad Request)} if the lignedecommandeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lignedecommandeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lignedecommandeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lignedecommandes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LignedecommandeDTO> partialUpdateLignedecommande(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LignedecommandeDTO lignedecommandeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lignedecommande partially : {}, {}", id, lignedecommandeDTO);
        if (lignedecommandeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lignedecommandeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lignedecommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LignedecommandeDTO> result = lignedecommandeService.partialUpdate(lignedecommandeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lignedecommandeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lignedecommandes} : get all the lignedecommandes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lignedecommandes in body.
     */
    @GetMapping("/lignedecommandes")
    public ResponseEntity<List<LignedecommandeDTO>> getAllLignedecommandes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Lignedecommandes");
        Page<LignedecommandeDTO> page = lignedecommandeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lignedecommandes/:id} : get the "id" lignedecommande.
     *
     * @param id the id of the lignedecommandeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lignedecommandeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lignedecommandes/{id}")
    public ResponseEntity<LignedecommandeDTO> getLignedecommande(@PathVariable Long id) {
        log.debug("REST request to get Lignedecommande : {}", id);
        Optional<LignedecommandeDTO> lignedecommandeDTO = lignedecommandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lignedecommandeDTO);
    }

    /**
     * {@code DELETE  /lignedecommandes/:id} : delete the "id" lignedecommande.
     *
     * @param id the id of the lignedecommandeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lignedecommandes/{id}")
    public ResponseEntity<Void> deleteLignedecommande(@PathVariable Long id) {
        log.debug("REST request to delete Lignedecommande : {}", id);
        lignedecommandeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
