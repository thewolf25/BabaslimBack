package com.babaslim.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babaslim.app.IntegrationTest;
import com.babaslim.app.domain.Pointdevente;
import com.babaslim.app.repository.PointdeventeRepository;
import com.babaslim.app.service.dto.PointdeventeDTO;
import com.babaslim.app.service.mapper.PointdeventeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PointdeventeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PointdeventeResourceIT {

    private static final Integer DEFAULT_SIRET = 1;
    private static final Integer UPDATED_SIRET = 2;

    private static final String ENTITY_API_URL = "/api/pointdeventes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PointdeventeRepository pointdeventeRepository;

    @Autowired
    private PointdeventeMapper pointdeventeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPointdeventeMockMvc;

    private Pointdevente pointdevente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pointdevente createEntity(EntityManager em) {
        Pointdevente pointdevente = new Pointdevente().siret(DEFAULT_SIRET);
        return pointdevente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pointdevente createUpdatedEntity(EntityManager em) {
        Pointdevente pointdevente = new Pointdevente().siret(UPDATED_SIRET);
        return pointdevente;
    }

    @BeforeEach
    public void initTest() {
        pointdevente = createEntity(em);
    }

    @Test
    @Transactional
    void createPointdevente() throws Exception {
        int databaseSizeBeforeCreate = pointdeventeRepository.findAll().size();
        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);
        restPointdeventeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeCreate + 1);
        Pointdevente testPointdevente = pointdeventeList.get(pointdeventeList.size() - 1);
        assertThat(testPointdevente.getSiret()).isEqualTo(DEFAULT_SIRET);
    }

    @Test
    @Transactional
    void createPointdeventeWithExistingId() throws Exception {
        // Create the Pointdevente with an existing ID
        pointdevente.setId(1L);
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        int databaseSizeBeforeCreate = pointdeventeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointdeventeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSiretIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointdeventeRepository.findAll().size();
        // set the field null
        pointdevente.setSiret(null);

        // Create the Pointdevente, which fails.
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        restPointdeventeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPointdeventes() throws Exception {
        // Initialize the database
        pointdeventeRepository.saveAndFlush(pointdevente);

        // Get all the pointdeventeList
        restPointdeventeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointdevente.getId().intValue())))
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET)));
    }

    @Test
    @Transactional
    void getPointdevente() throws Exception {
        // Initialize the database
        pointdeventeRepository.saveAndFlush(pointdevente);

        // Get the pointdevente
        restPointdeventeMockMvc
            .perform(get(ENTITY_API_URL_ID, pointdevente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pointdevente.getId().intValue()))
            .andExpect(jsonPath("$.siret").value(DEFAULT_SIRET));
    }

    @Test
    @Transactional
    void getNonExistingPointdevente() throws Exception {
        // Get the pointdevente
        restPointdeventeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPointdevente() throws Exception {
        // Initialize the database
        pointdeventeRepository.saveAndFlush(pointdevente);

        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();

        // Update the pointdevente
        Pointdevente updatedPointdevente = pointdeventeRepository.findById(pointdevente.getId()).get();
        // Disconnect from session so that the updates on updatedPointdevente are not directly saved in db
        em.detach(updatedPointdevente);
        updatedPointdevente.siret(UPDATED_SIRET);
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(updatedPointdevente);

        restPointdeventeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointdeventeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
        Pointdevente testPointdevente = pointdeventeList.get(pointdeventeList.size() - 1);
        assertThat(testPointdevente.getSiret()).isEqualTo(UPDATED_SIRET);
    }

    @Test
    @Transactional
    void putNonExistingPointdevente() throws Exception {
        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();
        pointdevente.setId(count.incrementAndGet());

        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointdeventeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pointdeventeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPointdevente() throws Exception {
        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();
        pointdevente.setId(count.incrementAndGet());

        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointdeventeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPointdevente() throws Exception {
        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();
        pointdevente.setId(count.incrementAndGet());

        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointdeventeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePointdeventeWithPatch() throws Exception {
        // Initialize the database
        pointdeventeRepository.saveAndFlush(pointdevente);

        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();

        // Update the pointdevente using partial update
        Pointdevente partialUpdatedPointdevente = new Pointdevente();
        partialUpdatedPointdevente.setId(pointdevente.getId());

        restPointdeventeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointdevente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointdevente))
            )
            .andExpect(status().isOk());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
        Pointdevente testPointdevente = pointdeventeList.get(pointdeventeList.size() - 1);
        assertThat(testPointdevente.getSiret()).isEqualTo(DEFAULT_SIRET);
    }

    @Test
    @Transactional
    void fullUpdatePointdeventeWithPatch() throws Exception {
        // Initialize the database
        pointdeventeRepository.saveAndFlush(pointdevente);

        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();

        // Update the pointdevente using partial update
        Pointdevente partialUpdatedPointdevente = new Pointdevente();
        partialUpdatedPointdevente.setId(pointdevente.getId());

        partialUpdatedPointdevente.siret(UPDATED_SIRET);

        restPointdeventeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPointdevente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPointdevente))
            )
            .andExpect(status().isOk());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
        Pointdevente testPointdevente = pointdeventeList.get(pointdeventeList.size() - 1);
        assertThat(testPointdevente.getSiret()).isEqualTo(UPDATED_SIRET);
    }

    @Test
    @Transactional
    void patchNonExistingPointdevente() throws Exception {
        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();
        pointdevente.setId(count.incrementAndGet());

        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointdeventeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pointdeventeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPointdevente() throws Exception {
        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();
        pointdevente.setId(count.incrementAndGet());

        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointdeventeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPointdevente() throws Exception {
        int databaseSizeBeforeUpdate = pointdeventeRepository.findAll().size();
        pointdevente.setId(count.incrementAndGet());

        // Create the Pointdevente
        PointdeventeDTO pointdeventeDTO = pointdeventeMapper.toDto(pointdevente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPointdeventeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pointdeventeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pointdevente in the database
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePointdevente() throws Exception {
        // Initialize the database
        pointdeventeRepository.saveAndFlush(pointdevente);

        int databaseSizeBeforeDelete = pointdeventeRepository.findAll().size();

        // Delete the pointdevente
        restPointdeventeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pointdevente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pointdevente> pointdeventeList = pointdeventeRepository.findAll();
        assertThat(pointdeventeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
