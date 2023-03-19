package com.example.demo.rest;

import com.example.demo.IntegrationTest;
import com.example.demo.domain.BaseStation;
import com.example.demo.repository.BaseStationRepository;
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
 * Integration tests for the {@link BaseStationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class BaseStationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_X = 1F;
    private static final Float UPDATED_X = 2F;

    private static final Float DEFAULT_Y = 1F;
    private static final Float UPDATED_Y = 2F;

    private static final Float DEFAULT_DETECTION_RADIUS_IN_METERS = 1F;
    private static final Float UPDATED_DETECTION_RADIUS_IN_METERS = 2F;

    private static final String ENTITY_API_URL = "/api/base-stations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BaseStationRepository baseStationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaseStationMockMvc;

    private BaseStation baseStation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaseStation createEntity(EntityManager em) {
        BaseStation baseStation = new BaseStation()
            .name(DEFAULT_NAME)
            .x(DEFAULT_X)
            .y(DEFAULT_Y)
            .detectionRadiusInMeters(DEFAULT_DETECTION_RADIUS_IN_METERS);
        return baseStation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaseStation createUpdatedEntity(EntityManager em) {
        BaseStation baseStation = new BaseStation()
            .name(UPDATED_NAME)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .detectionRadiusInMeters(UPDATED_DETECTION_RADIUS_IN_METERS);
        return baseStation;
    }

    @BeforeEach
    public void initTest() {
        baseStation = createEntity(em);
    }

    @Test
    @Transactional
    void createBaseStation() throws Exception {
        int databaseSizeBeforeCreate = baseStationRepository.findAll().size();
        // Create the BaseStation
        restBaseStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baseStation)))
            .andExpect(status().isCreated());

        // Validate the BaseStation in the database
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeCreate + 1);
        BaseStation testBaseStation = baseStationList.get(baseStationList.size() - 1);
        assertThat(testBaseStation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBaseStation.getX()).isEqualTo(DEFAULT_X);
        assertThat(testBaseStation.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testBaseStation.getDetectionRadiusInMeters()).isEqualTo(DEFAULT_DETECTION_RADIUS_IN_METERS);
    }

    @Test
    @Transactional
    void createBaseStationWithExistingId() throws Exception {
        // Create the BaseStation with an existing ID
        baseStation.setId("existing_id");

        int databaseSizeBeforeCreate = baseStationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaseStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baseStation)))
            .andExpect(status().isBadRequest());

        // Validate the BaseStation in the database
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBaseStations() throws Exception {
        // Initialize the database
        baseStation.setId(UUID.randomUUID().toString());
        baseStationRepository.saveAndFlush(baseStation);

        // Get all the baseStationList
        restBaseStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baseStation.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].detectionRadiusInMeters").value(hasItem(DEFAULT_DETECTION_RADIUS_IN_METERS.doubleValue())));
    }

    @Test
    @Transactional
    void putNewBaseStation() throws Exception {
        // Initialize the database
        baseStation.setId(UUID.randomUUID().toString());
        baseStationRepository.saveAndFlush(baseStation);

        int databaseSizeBeforeUpdate = baseStationRepository.findAll().size();

        // Update the baseStation
        BaseStation updatedBaseStation = baseStationRepository.findById(baseStation.getId()).get();
        // Disconnect from session so that the updates on updatedBaseStation are not directly saved in db
        em.detach(updatedBaseStation);
        updatedBaseStation.name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y).detectionRadiusInMeters(UPDATED_DETECTION_RADIUS_IN_METERS);

        restBaseStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBaseStation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBaseStation))
            )
            .andExpect(status().isOk());

        // Validate the BaseStation in the database
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeUpdate);
        BaseStation testBaseStation = baseStationList.get(baseStationList.size() - 1);
        assertThat(testBaseStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBaseStation.getX()).isEqualTo(UPDATED_X);
        assertThat(testBaseStation.getY()).isEqualTo(UPDATED_Y);
        assertThat(testBaseStation.getDetectionRadiusInMeters()).isEqualTo(UPDATED_DETECTION_RADIUS_IN_METERS);
    }

    @Test
    @Transactional
    void putNonExistingBaseStation() throws Exception {
        int databaseSizeBeforeUpdate = baseStationRepository.findAll().size();
        baseStation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaseStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baseStation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseStation in the database
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaseStation() throws Exception {
        int databaseSizeBeforeUpdate = baseStationRepository.findAll().size();
        baseStation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseStation in the database
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaseStation() throws Exception {
        int databaseSizeBeforeUpdate = baseStationRepository.findAll().size();
        baseStation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseStationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baseStation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaseStation in the database
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaseStation() throws Exception {
        // Initialize the database
        baseStation.setId(UUID.randomUUID().toString());
        baseStationRepository.saveAndFlush(baseStation);

        int databaseSizeBeforeDelete = baseStationRepository.findAll().size();

        // Delete the baseStation
        restBaseStationMockMvc
            .perform(delete(ENTITY_API_URL_ID, baseStation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BaseStation> baseStationList = baseStationRepository.findAll();
        assertThat(baseStationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
