package com.babaslim.app.web.rest;

import com.babaslim.app.repository.PointdeventeRepository;
import com.babaslim.app.service.PointdeventeService;
import com.babaslim.app.service.dto.PointdeventeDTO;
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
 * REST controller for managing {@link com.babaslim.app.domain.Pointdevente}.
 */
@RestController
@RequestMapping("/api")
public class PointdeventeResource {

    private final Logger log = LoggerFactory.getLogger(PointdeventeResource.class);

    private static final String ENTITY_NAME = "pointdevente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointdeventeService pointdeventeService;

    private final PointdeventeRepository pointdeventeRepository;

    public PointdeventeResource(PointdeventeService pointdeventeService, PointdeventeRepository pointdeventeRepository) {
        this.pointdeventeService = pointdeventeService;
        this.pointdeventeRepository = pointdeventeRepository;
    }

    /**
     * {@code POST  /pointdeventes} : Create a new pointdevente.
     *
     * @param pointdeventeDTO the pointdeventeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointdeventeDTO, or with status {@code 400 (Bad Request)} if the pointdevente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pointdeventes")
    public ResponseEntity<PointdeventeDTO> createPointdevente(@Valid @RequestBody PointdeventeDTO pointdeventeDTO)
        throws URISyntaxException {
        log.debug("REST request to save Pointdevente : {}", pointdeventeDTO);
        if (pointdeventeDTO.getId() != null) {
            throw new BadRequestAlertException("A new pointdevente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointdeventeDTO result = pointdeventeService.save(pointdeventeDTO);
        return ResponseEntity
            .created(new URI("/api/pointdeventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pointdeventes/:id} : Updates an existing pointdevente.
     *
     * @param id the id of the pointdeventeDTO to save.
     * @param pointdeventeDTO the pointdeventeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointdeventeDTO,
     * or with status {@code 400 (Bad Request)} if the pointdeventeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointdeventeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pointdeventes/{id}")
    public ResponseEntity<PointdeventeDTO> updatePointdevente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PointdeventeDTO pointdeventeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pointdevente : {}, {}", id, pointdeventeDTO);
        if (pointdeventeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointdeventeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointdeventeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PointdeventeDTO result = pointdeventeService.save(pointdeventeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointdeventeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pointdeventes/:id} : Partial updates given fields of an existing pointdevente, field will ignore if it is null
     *
     * @param id the id of the pointdeventeDTO to save.
     * @param pointdeventeDTO the pointdeventeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointdeventeDTO,
     * or with status {@code 400 (Bad Request)} if the pointdeventeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pointdeventeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointdeventeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pointdeventes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointdeventeDTO> partialUpdatePointdevente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PointdeventeDTO pointdeventeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pointdevente partially : {}, {}", id, pointdeventeDTO);
        if (pointdeventeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointdeventeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointdeventeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointdeventeDTO> result = pointdeventeService.partialUpdate(pointdeventeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointdeventeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pointdeventes} : get all the pointdeventes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointdeventes in body.
     */
    @GetMapping("/pointdeventes")
    public ResponseEntity<List<PointdeventeDTO>> getAllPointdeventes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pointdeventes");
        Page<PointdeventeDTO> page = pointdeventeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pointdeventes/:id} : get the "id" pointdevente.
     *
     * @param id the id of the pointdeventeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointdeventeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pointdeventes/{id}")
    public ResponseEntity<PointdeventeDTO> getPointdevente(@PathVariable Long id) {
        log.debug("REST request to get Pointdevente : {}", id);
        Optional<PointdeventeDTO> pointdeventeDTO = pointdeventeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointdeventeDTO);
    }

    /**
     * {@code DELETE  /pointdeventes/:id} : delete the "id" pointdevente.
     *
     * @param id the id of the pointdeventeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pointdeventes/{id}")
    public ResponseEntity<Void> deletePointdevente(@PathVariable Long id) {
        log.debug("REST request to delete Pointdevente : {}", id);
        pointdeventeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
