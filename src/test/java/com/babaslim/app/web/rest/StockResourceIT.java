package com.babaslim.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babaslim.app.IntegrationTest;
import com.babaslim.app.domain.Stock;
import com.babaslim.app.repository.StockRepository;
import com.babaslim.app.service.dto.StockDTO;
import com.babaslim.app.service.mapper.StockMapper;
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
 * Integration tests for the {@link StockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockResourceIT {

    private static final String DEFAULT_CODE_EAN = "AAAAAAAAAA";
    private static final String UPDATED_CODE_EAN = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE_RESTANTE = 0;
    private static final Integer UPDATED_QUANTITE_RESTANTE = 1;

    private static final Instant DEFAULT_DATE_ACHAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACHAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_DE_PEREMPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DE_PEREMPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockMockMvc;

    private Stock stock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .codeEAN(DEFAULT_CODE_EAN)
            .quantiteRestante(DEFAULT_QUANTITE_RESTANTE)
            .dateAchat(DEFAULT_DATE_ACHAT)
            .dateDePeremption(DEFAULT_DATE_DE_PEREMPTION);
        return stock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock()
            .codeEAN(UPDATED_CODE_EAN)
            .quantiteRestante(UPDATED_QUANTITE_RESTANTE)
            .dateAchat(UPDATED_DATE_ACHAT)
            .dateDePeremption(UPDATED_DATE_DE_PEREMPTION);
        return stock;
    }

    @BeforeEach
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();
        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);
        restStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getCodeEAN()).isEqualTo(DEFAULT_CODE_EAN);
        assertThat(testStock.getQuantiteRestante()).isEqualTo(DEFAULT_QUANTITE_RESTANTE);
        assertThat(testStock.getDateAchat()).isEqualTo(DEFAULT_DATE_ACHAT);
        assertThat(testStock.getDateDePeremption()).isEqualTo(DEFAULT_DATE_DE_PEREMPTION);
    }

    @Test
    @Transactional
    void createStockWithExistingId() throws Exception {
        // Create the Stock with an existing ID
        stock.setId(1L);
        StockDTO stockDTO = stockMapper.toDto(stock);

        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeEANIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setCodeEAN(null);

        // Create the Stock, which fails.
        StockDTO stockDTO = stockMapper.toDto(stock);

        restStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAchatIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setDateAchat(null);

        // Create the Stock, which fails.
        StockDTO stockDTO = stockMapper.toDto(stock);

        restStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeEAN").value(hasItem(DEFAULT_CODE_EAN)))
            .andExpect(jsonPath("$.[*].quantiteRestante").value(hasItem(DEFAULT_QUANTITE_RESTANTE)))
            .andExpect(jsonPath("$.[*].dateAchat").value(hasItem(DEFAULT_DATE_ACHAT.toString())))
            .andExpect(jsonPath("$.[*].dateDePeremption").value(hasItem(DEFAULT_DATE_DE_PEREMPTION.toString())));
    }

    @Test
    @Transactional
    void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc
            .perform(get(ENTITY_API_URL_ID, stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.codeEAN").value(DEFAULT_CODE_EAN))
            .andExpect(jsonPath("$.quantiteRestante").value(DEFAULT_QUANTITE_RESTANTE))
            .andExpect(jsonPath("$.dateAchat").value(DEFAULT_DATE_ACHAT.toString()))
            .andExpect(jsonPath("$.dateDePeremption").value(DEFAULT_DATE_DE_PEREMPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .codeEAN(UPDATED_CODE_EAN)
            .quantiteRestante(UPDATED_QUANTITE_RESTANTE)
            .dateAchat(UPDATED_DATE_ACHAT)
            .dateDePeremption(UPDATED_DATE_DE_PEREMPTION);
        StockDTO stockDTO = stockMapper.toDto(updatedStock);

        restStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getCodeEAN()).isEqualTo(UPDATED_CODE_EAN);
        assertThat(testStock.getQuantiteRestante()).isEqualTo(UPDATED_QUANTITE_RESTANTE);
        assertThat(testStock.getDateAchat()).isEqualTo(UPDATED_DATE_ACHAT);
        assertThat(testStock.getDateDePeremption()).isEqualTo(UPDATED_DATE_DE_PEREMPTION);
    }

    @Test
    @Transactional
    void putNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockWithPatch() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock using partial update
        Stock partialUpdatedStock = new Stock();
        partialUpdatedStock.setId(stock.getId());

        partialUpdatedStock.codeEAN(UPDATED_CODE_EAN).quantiteRestante(UPDATED_QUANTITE_RESTANTE);

        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStock))
            )
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getCodeEAN()).isEqualTo(UPDATED_CODE_EAN);
        assertThat(testStock.getQuantiteRestante()).isEqualTo(UPDATED_QUANTITE_RESTANTE);
        assertThat(testStock.getDateAchat()).isEqualTo(DEFAULT_DATE_ACHAT);
        assertThat(testStock.getDateDePeremption()).isEqualTo(DEFAULT_DATE_DE_PEREMPTION);
    }

    @Test
    @Transactional
    void fullUpdateStockWithPatch() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock using partial update
        Stock partialUpdatedStock = new Stock();
        partialUpdatedStock.setId(stock.getId());

        partialUpdatedStock
            .codeEAN(UPDATED_CODE_EAN)
            .quantiteRestante(UPDATED_QUANTITE_RESTANTE)
            .dateAchat(UPDATED_DATE_ACHAT)
            .dateDePeremption(UPDATED_DATE_DE_PEREMPTION);

        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStock))
            )
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getCodeEAN()).isEqualTo(UPDATED_CODE_EAN);
        assertThat(testStock.getQuantiteRestante()).isEqualTo(UPDATED_QUANTITE_RESTANTE);
        assertThat(testStock.getDateAchat()).isEqualTo(UPDATED_DATE_ACHAT);
        assertThat(testStock.getDateDePeremption()).isEqualTo(UPDATED_DATE_DE_PEREMPTION);
    }

    @Test
    @Transactional
    void patchNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, stock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
