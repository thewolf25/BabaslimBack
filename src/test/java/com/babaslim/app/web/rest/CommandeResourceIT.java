package com.babaslim.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babaslim.app.IntegrationTest;
import com.babaslim.app.domain.Commande;
import com.babaslim.app.repository.CommandeRepository;
import com.babaslim.app.service.dto.CommandeDTO;
import com.babaslim.app.service.mapper.CommandeMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommandeResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ENREGISTREMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ENREGISTREMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private CommandeMapper commandeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandeMockMvc;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande().numero(DEFAULT_NUMERO).dateEnregistrement(DEFAULT_DATE_ENREGISTREMENT);
        return commande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande().numero(UPDATED_NUMERO).dateEnregistrement(UPDATED_DATE_ENREGISTREMENT);
        return commande;
    }

    @BeforeEach
    public void initTest() {
        commande = createEntity(em);
    }

    @Test
    @Transactional
    void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();
        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);
        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCommande.getDateEnregistrement()).isEqualTo(DEFAULT_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void createCommandeWithExistingId() throws Exception {
        // Create the Commande with an existing ID
        commande.setId(1L);
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setNumero(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEnregistrementIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setDateEnregistrement(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateEnregistrement").value(hasItem(DEFAULT_DATE_ENREGISTREMENT.toString())));
    }

    @Test
    @Transactional
    void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateEnregistrement").value(DEFAULT_DATE_ENREGISTREMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).get();
        // Disconnect from session so that the updates on updatedCommande are not directly saved in db
        em.detach(updatedCommande);
        updatedCommande.numero(UPDATED_NUMERO).dateEnregistrement(UPDATED_DATE_ENREGISTREMENT);
        CommandeDTO commandeDTO = commandeMapper.toDto(updatedCommande);

        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCommande.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void putNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.numero(UPDATED_NUMERO);

        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCommande.getDateEnregistrement()).isEqualTo(DEFAULT_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void fullUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.numero(UPDATED_NUMERO).dateEnregistrement(UPDATED_DATE_ENREGISTREMENT);

        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCommande.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void patchNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Delete the commande
        restCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, commande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
