package com.example.demo.rest;

import com.example.demo.IntegrationTest;
import com.example.demo.domain.MobileStation;
import com.example.demo.repository.MobileStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MobileStationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class MobileStationResourceIT {

    private static final Float DEFAULT_LAST_KNOWN_X = 1F;
    private static final Float UPDATED_LAST_KNOWN_X = 2F;

    private static final Float DEFAULT_LAST_KNOWN_Y = 1F;
    private static final Float UPDATED_LAST_KNOWN_Y = 2F;

    private static final String ENTITY_API_URL = "/api/mobile-stations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MobileStationRepository mobileStationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMobileStationMockMvc;

    private MobileStation mobileStation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileStation createEntity(EntityManager em) {
        MobileStation mobileStation = new MobileStation().lastKnownX(DEFAULT_LAST_KNOWN_X).lastKnownY(DEFAULT_LAST_KNOWN_Y);
        return mobileStation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileStation createUpdatedEntity(EntityManager em) {
        MobileStation mobileStation = new MobileStation().lastKnownX(UPDATED_LAST_KNOWN_X).lastKnownY(UPDATED_LAST_KNOWN_Y);
        return mobileStation;
    }

    @BeforeEach
    public void initTest() {
        mobileStation = createEntity(em);
    }

    @Test
    @Transactional
    void createMobileStation() throws Exception {
        int databaseSizeBeforeCreate = mobileStationRepository.findAll().size();
        // Create the MobileStation
        restMobileStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileStation)))
            .andExpect(status().isCreated());

        // Validate the MobileStation in the database
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeCreate + 1);
        MobileStation testMobileStation = mobileStationList.get(mobileStationList.size() - 1);
        assertThat(testMobileStation.getLastKnownX()).isEqualTo(DEFAULT_LAST_KNOWN_X);
        assertThat(testMobileStation.getLastKnownY()).isEqualTo(DEFAULT_LAST_KNOWN_Y);
    }

    @Test
    @Transactional
    void createMobileStationWithExistingId() throws Exception {
        // Create the MobileStation with an existing ID
        mobileStation.setId("existing_id");

        int databaseSizeBeforeCreate = mobileStationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMobileStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileStation)))
            .andExpect(status().isBadRequest());

        // Validate the MobileStation in the database
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMobileStations() throws Exception {
        // Initialize the database
        mobileStation.setId(UUID.randomUUID().toString());
        mobileStationRepository.saveAndFlush(mobileStation);

        // Get all the mobileStationList
        restMobileStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileStation.getId())))
            .andExpect(jsonPath("$.[*].lastKnownX").value(hasItem(DEFAULT_LAST_KNOWN_X.doubleValue())))
            .andExpect(jsonPath("$.[*].lastKnownY").value(hasItem(DEFAULT_LAST_KNOWN_Y.doubleValue())));
    }

    @Test
    @Transactional
    void putNewMobileStation() throws Exception {
        // Initialize the database
        mobileStation.setId(UUID.randomUUID().toString());
        mobileStationRepository.saveAndFlush(mobileStation);

        int databaseSizeBeforeUpdate = mobileStationRepository.findAll().size();

        // Update the mobileStation
        MobileStation updatedMobileStation = mobileStationRepository.findById(mobileStation.getId()).get();
        // Disconnect from session so that the updates on updatedMobileStation are not directly saved in db
        em.detach(updatedMobileStation);
        updatedMobileStation.lastKnownX(UPDATED_LAST_KNOWN_X).lastKnownY(UPDATED_LAST_KNOWN_Y);

        restMobileStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMobileStation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMobileStation))
            )
            .andExpect(status().isOk());

        // Validate the MobileStation in the database
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeUpdate);
        MobileStation testMobileStation = mobileStationList.get(mobileStationList.size() - 1);
        assertThat(testMobileStation.getLastKnownX()).isEqualTo(UPDATED_LAST_KNOWN_X);
        assertThat(testMobileStation.getLastKnownY()).isEqualTo(UPDATED_LAST_KNOWN_Y);
    }

    @Test
    @Transactional
    void putNonExistingMobileStation() throws Exception {
        int databaseSizeBeforeUpdate = mobileStationRepository.findAll().size();
        mobileStation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileStation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileStation in the database
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMobileStation() throws Exception {
        int databaseSizeBeforeUpdate = mobileStationRepository.findAll().size();
        mobileStation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileStation in the database
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMobileStation() throws Exception {
        int databaseSizeBeforeUpdate = mobileStationRepository.findAll().size();
        mobileStation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileStationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileStation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileStation in the database
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMobileStation() throws Exception {
        // Initialize the database
        mobileStation.setId(UUID.randomUUID().toString());
        mobileStationRepository.saveAndFlush(mobileStation);

        int databaseSizeBeforeDelete = mobileStationRepository.findAll().size();

        // Delete the mobileStation
        restMobileStationMockMvc
            .perform(delete(ENTITY_API_URL_ID, mobileStation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MobileStation> mobileStationList = mobileStationRepository.findAll();
        assertThat(mobileStationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
