package com.babaslim.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babaslim.app.IntegrationTest;
import com.babaslim.app.domain.Addresse;
import com.babaslim.app.repository.AddresseRepository;
import com.babaslim.app.service.dto.AddresseDTO;
import com.babaslim.app.service.mapper.AddresseMapper;
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
 * Integration tests for the {@link AddresseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddresseResourceIT {

    private static final String DEFAULT_RUE = "AAAAAAAAAA";
    private static final String UPDATED_RUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE_POSTALE = 1;
    private static final Integer UPDATED_CODE_POSTALE = 2;

    private static final String DEFAULT_COMPLEMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNE = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddresseRepository addresseRepository;

    @Autowired
    private AddresseMapper addresseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddresseMockMvc;

    private Addresse addresse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Addresse createEntity(EntityManager em) {
        Addresse addresse = new Addresse()
            .rue(DEFAULT_RUE)
            .codePostale(DEFAULT_CODE_POSTALE)
            .complement(DEFAULT_COMPLEMENT)
            .commune(DEFAULT_COMMUNE);
        return addresse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Addresse createUpdatedEntity(EntityManager em) {
        Addresse addresse = new Addresse()
            .rue(UPDATED_RUE)
            .codePostale(UPDATED_CODE_POSTALE)
            .complement(UPDATED_COMPLEMENT)
            .commune(UPDATED_COMMUNE);
        return addresse;
    }

    @BeforeEach
    public void initTest() {
        addresse = createEntity(em);
    }

    @Test
    @Transactional
    void createAddresse() throws Exception {
        int databaseSizeBeforeCreate = addresseRepository.findAll().size();
        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);
        restAddresseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresseDTO)))
            .andExpect(status().isCreated());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeCreate + 1);
        Addresse testAddresse = addresseList.get(addresseList.size() - 1);
        assertThat(testAddresse.getRue()).isEqualTo(DEFAULT_RUE);
        assertThat(testAddresse.getCodePostale()).isEqualTo(DEFAULT_CODE_POSTALE);
        assertThat(testAddresse.getComplement()).isEqualTo(DEFAULT_COMPLEMENT);
        assertThat(testAddresse.getCommune()).isEqualTo(DEFAULT_COMMUNE);
    }

    @Test
    @Transactional
    void createAddresseWithExistingId() throws Exception {
        // Create the Addresse with an existing ID
        addresse.setId(1L);
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        int databaseSizeBeforeCreate = addresseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddresseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRueIsRequired() throws Exception {
        int databaseSizeBeforeTest = addresseRepository.findAll().size();
        // set the field null
        addresse.setRue(null);

        // Create the Addresse, which fails.
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        restAddresseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresseDTO)))
            .andExpect(status().isBadRequest());

        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addresseRepository.saveAndFlush(addresse);

        // Get all the addresseList
        restAddresseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addresse.getId().intValue())))
            .andExpect(jsonPath("$.[*].rue").value(hasItem(DEFAULT_RUE)))
            .andExpect(jsonPath("$.[*].codePostale").value(hasItem(DEFAULT_CODE_POSTALE)))
            .andExpect(jsonPath("$.[*].complement").value(hasItem(DEFAULT_COMPLEMENT)))
            .andExpect(jsonPath("$.[*].commune").value(hasItem(DEFAULT_COMMUNE)));
    }

    @Test
    @Transactional
    void getAddresse() throws Exception {
        // Initialize the database
        addresseRepository.saveAndFlush(addresse);

        // Get the addresse
        restAddresseMockMvc
            .perform(get(ENTITY_API_URL_ID, addresse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addresse.getId().intValue()))
            .andExpect(jsonPath("$.rue").value(DEFAULT_RUE))
            .andExpect(jsonPath("$.codePostale").value(DEFAULT_CODE_POSTALE))
            .andExpect(jsonPath("$.complement").value(DEFAULT_COMPLEMENT))
            .andExpect(jsonPath("$.commune").value(DEFAULT_COMMUNE));
    }

    @Test
    @Transactional
    void getNonExistingAddresse() throws Exception {
        // Get the addresse
        restAddresseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddresse() throws Exception {
        // Initialize the database
        addresseRepository.saveAndFlush(addresse);

        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();

        // Update the addresse
        Addresse updatedAddresse = addresseRepository.findById(addresse.getId()).get();
        // Disconnect from session so that the updates on updatedAddresse are not directly saved in db
        em.detach(updatedAddresse);
        updatedAddresse.rue(UPDATED_RUE).codePostale(UPDATED_CODE_POSTALE).complement(UPDATED_COMPLEMENT).commune(UPDATED_COMMUNE);
        AddresseDTO addresseDTO = addresseMapper.toDto(updatedAddresse);

        restAddresseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addresseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addresseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
        Addresse testAddresse = addresseList.get(addresseList.size() - 1);
        assertThat(testAddresse.getRue()).isEqualTo(UPDATED_RUE);
        assertThat(testAddresse.getCodePostale()).isEqualTo(UPDATED_CODE_POSTALE);
        assertThat(testAddresse.getComplement()).isEqualTo(UPDATED_COMPLEMENT);
        assertThat(testAddresse.getCommune()).isEqualTo(UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void putNonExistingAddresse() throws Exception {
        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();
        addresse.setId(count.incrementAndGet());

        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddresseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addresseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddresse() throws Exception {
        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();
        addresse.setId(count.incrementAndGet());

        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddresse() throws Exception {
        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();
        addresse.setId(count.incrementAndGet());

        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addresseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddresseWithPatch() throws Exception {
        // Initialize the database
        addresseRepository.saveAndFlush(addresse);

        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();

        // Update the addresse using partial update
        Addresse partialUpdatedAddresse = new Addresse();
        partialUpdatedAddresse.setId(addresse.getId());

        partialUpdatedAddresse.rue(UPDATED_RUE);

        restAddresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddresse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddresse))
            )
            .andExpect(status().isOk());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
        Addresse testAddresse = addresseList.get(addresseList.size() - 1);
        assertThat(testAddresse.getRue()).isEqualTo(UPDATED_RUE);
        assertThat(testAddresse.getCodePostale()).isEqualTo(DEFAULT_CODE_POSTALE);
        assertThat(testAddresse.getComplement()).isEqualTo(DEFAULT_COMPLEMENT);
        assertThat(testAddresse.getCommune()).isEqualTo(DEFAULT_COMMUNE);
    }

    @Test
    @Transactional
    void fullUpdateAddresseWithPatch() throws Exception {
        // Initialize the database
        addresseRepository.saveAndFlush(addresse);

        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();

        // Update the addresse using partial update
        Addresse partialUpdatedAddresse = new Addresse();
        partialUpdatedAddresse.setId(addresse.getId());

        partialUpdatedAddresse.rue(UPDATED_RUE).codePostale(UPDATED_CODE_POSTALE).complement(UPDATED_COMPLEMENT).commune(UPDATED_COMMUNE);

        restAddresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddresse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddresse))
            )
            .andExpect(status().isOk());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
        Addresse testAddresse = addresseList.get(addresseList.size() - 1);
        assertThat(testAddresse.getRue()).isEqualTo(UPDATED_RUE);
        assertThat(testAddresse.getCodePostale()).isEqualTo(UPDATED_CODE_POSTALE);
        assertThat(testAddresse.getComplement()).isEqualTo(UPDATED_COMPLEMENT);
        assertThat(testAddresse.getCommune()).isEqualTo(UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void patchNonExistingAddresse() throws Exception {
        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();
        addresse.setId(count.incrementAndGet());

        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addresseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddresse() throws Exception {
        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();
        addresse.setId(count.incrementAndGet());

        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddresse() throws Exception {
        int databaseSizeBeforeUpdate = addresseRepository.findAll().size();
        addresse.setId(count.incrementAndGet());

        // Create the Addresse
        AddresseDTO addresseDTO = addresseMapper.toDto(addresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddresseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addresseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Addresse in the database
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddresse() throws Exception {
        // Initialize the database
        addresseRepository.saveAndFlush(addresse);

        int databaseSizeBeforeDelete = addresseRepository.findAll().size();

        // Delete the addresse
        restAddresseMockMvc
            .perform(delete(ENTITY_API_URL_ID, addresse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Addresse> addresseList = addresseRepository.findAll();
        assertThat(addresseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
