package com.babaslim.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babaslim.app.IntegrationTest;
import com.babaslim.app.domain.Taille;
import com.babaslim.app.repository.TailleRepository;
import com.babaslim.app.service.dto.TailleDTO;
import com.babaslim.app.service.mapper.TailleMapper;
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
 * Integration tests for the {@link TailleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TailleResourceIT {

    private static final String DEFAULT_TAILLE = "AAAAAAAAAA";
    private static final String UPDATED_TAILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tailles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TailleRepository tailleRepository;

    @Autowired
    private TailleMapper tailleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTailleMockMvc;

    private Taille taille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taille createEntity(EntityManager em) {
        Taille taille = new Taille().taille(DEFAULT_TAILLE);
        return taille;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taille createUpdatedEntity(EntityManager em) {
        Taille taille = new Taille().taille(UPDATED_TAILLE);
        return taille;
    }

    @BeforeEach
    public void initTest() {
        taille = createEntity(em);
    }

    @Test
    @Transactional
    void createTaille() throws Exception {
        int databaseSizeBeforeCreate = tailleRepository.findAll().size();
        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);
        restTailleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tailleDTO)))
            .andExpect(status().isCreated());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeCreate + 1);
        Taille testTaille = tailleList.get(tailleList.size() - 1);
        assertThat(testTaille.getTaille()).isEqualTo(DEFAULT_TAILLE);
    }

    @Test
    @Transactional
    void createTailleWithExistingId() throws Exception {
        // Create the Taille with an existing ID
        taille.setId(1L);
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        int databaseSizeBeforeCreate = tailleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTailleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tailleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTailleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tailleRepository.findAll().size();
        // set the field null
        taille.setTaille(null);

        // Create the Taille, which fails.
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        restTailleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tailleDTO)))
            .andExpect(status().isBadRequest());

        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTailles() throws Exception {
        // Initialize the database
        tailleRepository.saveAndFlush(taille);

        // Get all the tailleList
        restTailleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taille.getId().intValue())))
            .andExpect(jsonPath("$.[*].taille").value(hasItem(DEFAULT_TAILLE)));
    }

    @Test
    @Transactional
    void getTaille() throws Exception {
        // Initialize the database
        tailleRepository.saveAndFlush(taille);

        // Get the taille
        restTailleMockMvc
            .perform(get(ENTITY_API_URL_ID, taille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taille.getId().intValue()))
            .andExpect(jsonPath("$.taille").value(DEFAULT_TAILLE));
    }

    @Test
    @Transactional
    void getNonExistingTaille() throws Exception {
        // Get the taille
        restTailleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaille() throws Exception {
        // Initialize the database
        tailleRepository.saveAndFlush(taille);

        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();

        // Update the taille
        Taille updatedTaille = tailleRepository.findById(taille.getId()).get();
        // Disconnect from session so that the updates on updatedTaille are not directly saved in db
        em.detach(updatedTaille);
        updatedTaille.taille(UPDATED_TAILLE);
        TailleDTO tailleDTO = tailleMapper.toDto(updatedTaille);

        restTailleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tailleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tailleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
        Taille testTaille = tailleList.get(tailleList.size() - 1);
        assertThat(testTaille.getTaille()).isEqualTo(UPDATED_TAILLE);
    }

    @Test
    @Transactional
    void putNonExistingTaille() throws Exception {
        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();
        taille.setId(count.incrementAndGet());

        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTailleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tailleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tailleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaille() throws Exception {
        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();
        taille.setId(count.incrementAndGet());

        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTailleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tailleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaille() throws Exception {
        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();
        taille.setId(count.incrementAndGet());

        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTailleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tailleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTailleWithPatch() throws Exception {
        // Initialize the database
        tailleRepository.saveAndFlush(taille);

        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();

        // Update the taille using partial update
        Taille partialUpdatedTaille = new Taille();
        partialUpdatedTaille.setId(taille.getId());

        restTailleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaille))
            )
            .andExpect(status().isOk());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
        Taille testTaille = tailleList.get(tailleList.size() - 1);
        assertThat(testTaille.getTaille()).isEqualTo(DEFAULT_TAILLE);
    }

    @Test
    @Transactional
    void fullUpdateTailleWithPatch() throws Exception {
        // Initialize the database
        tailleRepository.saveAndFlush(taille);

        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();

        // Update the taille using partial update
        Taille partialUpdatedTaille = new Taille();
        partialUpdatedTaille.setId(taille.getId());

        partialUpdatedTaille.taille(UPDATED_TAILLE);

        restTailleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaille))
            )
            .andExpect(status().isOk());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
        Taille testTaille = tailleList.get(tailleList.size() - 1);
        assertThat(testTaille.getTaille()).isEqualTo(UPDATED_TAILLE);
    }

    @Test
    @Transactional
    void patchNonExistingTaille() throws Exception {
        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();
        taille.setId(count.incrementAndGet());

        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTailleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tailleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tailleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaille() throws Exception {
        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();
        taille.setId(count.incrementAndGet());

        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTailleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tailleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaille() throws Exception {
        int databaseSizeBeforeUpdate = tailleRepository.findAll().size();
        taille.setId(count.incrementAndGet());

        // Create the Taille
        TailleDTO tailleDTO = tailleMapper.toDto(taille);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTailleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tailleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Taille in the database
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaille() throws Exception {
        // Initialize the database
        tailleRepository.saveAndFlush(taille);

        int databaseSizeBeforeDelete = tailleRepository.findAll().size();

        // Delete the taille
        restTailleMockMvc
            .perform(delete(ENTITY_API_URL_ID, taille.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Taille> tailleList = tailleRepository.findAll();
        assertThat(tailleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
