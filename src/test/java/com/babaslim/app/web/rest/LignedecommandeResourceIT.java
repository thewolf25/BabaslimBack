package com.babaslim.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babaslim.app.IntegrationTest;
import com.babaslim.app.domain.Lignedecommande;
import com.babaslim.app.repository.LignedecommandeRepository;
import com.babaslim.app.service.dto.LignedecommandeDTO;
import com.babaslim.app.service.mapper.LignedecommandeMapper;
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
 * Integration tests for the {@link LignedecommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LignedecommandeResourceIT {

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final Double DEFAULT_PRIX_UNITAIRE_HT = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE_HT = 2D;

    private static final String ENTITY_API_URL = "/api/lignedecommandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LignedecommandeRepository lignedecommandeRepository;

    @Autowired
    private LignedecommandeMapper lignedecommandeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLignedecommandeMockMvc;

    private Lignedecommande lignedecommande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lignedecommande createEntity(EntityManager em) {
        Lignedecommande lignedecommande = new Lignedecommande().quantity(DEFAULT_QUANTITY).prixUnitaireHT(DEFAULT_PRIX_UNITAIRE_HT);
        return lignedecommande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lignedecommande createUpdatedEntity(EntityManager em) {
        Lignedecommande lignedecommande = new Lignedecommande().quantity(UPDATED_QUANTITY).prixUnitaireHT(UPDATED_PRIX_UNITAIRE_HT);
        return lignedecommande;
    }

    @BeforeEach
    public void initTest() {
        lignedecommande = createEntity(em);
    }

    @Test
    @Transactional
    void createLignedecommande() throws Exception {
        int databaseSizeBeforeCreate = lignedecommandeRepository.findAll().size();
        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);
        restLignedecommandeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeCreate + 1);
        Lignedecommande testLignedecommande = lignedecommandeList.get(lignedecommandeList.size() - 1);
        assertThat(testLignedecommande.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testLignedecommande.getPrixUnitaireHT()).isEqualTo(DEFAULT_PRIX_UNITAIRE_HT);
    }

    @Test
    @Transactional
    void createLignedecommandeWithExistingId() throws Exception {
        // Create the Lignedecommande with an existing ID
        lignedecommande.setId(1L);
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        int databaseSizeBeforeCreate = lignedecommandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLignedecommandeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLignedecommandes() throws Exception {
        // Initialize the database
        lignedecommandeRepository.saveAndFlush(lignedecommande);

        // Get all the lignedecommandeList
        restLignedecommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignedecommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].prixUnitaireHT").value(hasItem(DEFAULT_PRIX_UNITAIRE_HT.doubleValue())));
    }

    @Test
    @Transactional
    void getLignedecommande() throws Exception {
        // Initialize the database
        lignedecommandeRepository.saveAndFlush(lignedecommande);

        // Get the lignedecommande
        restLignedecommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, lignedecommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lignedecommande.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.prixUnitaireHT").value(DEFAULT_PRIX_UNITAIRE_HT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLignedecommande() throws Exception {
        // Get the lignedecommande
        restLignedecommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLignedecommande() throws Exception {
        // Initialize the database
        lignedecommandeRepository.saveAndFlush(lignedecommande);

        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();

        // Update the lignedecommande
        Lignedecommande updatedLignedecommande = lignedecommandeRepository.findById(lignedecommande.getId()).get();
        // Disconnect from session so that the updates on updatedLignedecommande are not directly saved in db
        em.detach(updatedLignedecommande);
        updatedLignedecommande.quantity(UPDATED_QUANTITY).prixUnitaireHT(UPDATED_PRIX_UNITAIRE_HT);
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(updatedLignedecommande);

        restLignedecommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lignedecommandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
        Lignedecommande testLignedecommande = lignedecommandeList.get(lignedecommandeList.size() - 1);
        assertThat(testLignedecommande.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testLignedecommande.getPrixUnitaireHT()).isEqualTo(UPDATED_PRIX_UNITAIRE_HT);
    }

    @Test
    @Transactional
    void putNonExistingLignedecommande() throws Exception {
        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();
        lignedecommande.setId(count.incrementAndGet());

        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLignedecommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lignedecommandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLignedecommande() throws Exception {
        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();
        lignedecommande.setId(count.incrementAndGet());

        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLignedecommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLignedecommande() throws Exception {
        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();
        lignedecommande.setId(count.incrementAndGet());

        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLignedecommandeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLignedecommandeWithPatch() throws Exception {
        // Initialize the database
        lignedecommandeRepository.saveAndFlush(lignedecommande);

        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();

        // Update the lignedecommande using partial update
        Lignedecommande partialUpdatedLignedecommande = new Lignedecommande();
        partialUpdatedLignedecommande.setId(lignedecommande.getId());

        partialUpdatedLignedecommande.quantity(UPDATED_QUANTITY);

        restLignedecommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLignedecommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLignedecommande))
            )
            .andExpect(status().isOk());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
        Lignedecommande testLignedecommande = lignedecommandeList.get(lignedecommandeList.size() - 1);
        assertThat(testLignedecommande.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testLignedecommande.getPrixUnitaireHT()).isEqualTo(DEFAULT_PRIX_UNITAIRE_HT);
    }

    @Test
    @Transactional
    void fullUpdateLignedecommandeWithPatch() throws Exception {
        // Initialize the database
        lignedecommandeRepository.saveAndFlush(lignedecommande);

        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();

        // Update the lignedecommande using partial update
        Lignedecommande partialUpdatedLignedecommande = new Lignedecommande();
        partialUpdatedLignedecommande.setId(lignedecommande.getId());

        partialUpdatedLignedecommande.quantity(UPDATED_QUANTITY).prixUnitaireHT(UPDATED_PRIX_UNITAIRE_HT);

        restLignedecommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLignedecommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLignedecommande))
            )
            .andExpect(status().isOk());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
        Lignedecommande testLignedecommande = lignedecommandeList.get(lignedecommandeList.size() - 1);
        assertThat(testLignedecommande.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testLignedecommande.getPrixUnitaireHT()).isEqualTo(UPDATED_PRIX_UNITAIRE_HT);
    }

    @Test
    @Transactional
    void patchNonExistingLignedecommande() throws Exception {
        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();
        lignedecommande.setId(count.incrementAndGet());

        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLignedecommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lignedecommandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLignedecommande() throws Exception {
        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();
        lignedecommande.setId(count.incrementAndGet());

        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLignedecommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLignedecommande() throws Exception {
        int databaseSizeBeforeUpdate = lignedecommandeRepository.findAll().size();
        lignedecommande.setId(count.incrementAndGet());

        // Create the Lignedecommande
        LignedecommandeDTO lignedecommandeDTO = lignedecommandeMapper.toDto(lignedecommande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLignedecommandeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lignedecommandeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lignedecommande in the database
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLignedecommande() throws Exception {
        // Initialize the database
        lignedecommandeRepository.saveAndFlush(lignedecommande);

        int databaseSizeBeforeDelete = lignedecommandeRepository.findAll().size();

        // Delete the lignedecommande
        restLignedecommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, lignedecommande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lignedecommande> lignedecommandeList = lignedecommandeRepository.findAll();
        assertThat(lignedecommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
