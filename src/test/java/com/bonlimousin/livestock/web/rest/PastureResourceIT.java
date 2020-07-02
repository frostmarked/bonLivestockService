package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.PastureEntity;
import com.bonlimousin.livestock.repository.PastureRepository;
import com.bonlimousin.livestock.service.PastureService;
import com.bonlimousin.livestock.service.dto.PastureCriteria;
import com.bonlimousin.livestock.service.PastureQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PastureResource} REST controller.
 */
@SpringBootTest(classes = BonLivestockServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PastureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PastureRepository pastureRepository;

    @Autowired
    private PastureService pastureService;

    @Autowired
    private PastureQueryService pastureQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPastureMockMvc;

    private PastureEntity pastureEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PastureEntity createEntity(EntityManager em) {
        PastureEntity pastureEntity = new PastureEntity()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pastureEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PastureEntity createUpdatedEntity(EntityManager em) {
        PastureEntity pastureEntity = new PastureEntity()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return pastureEntity;
    }

    @BeforeEach
    public void initTest() {
        pastureEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createPasture() throws Exception {
        int databaseSizeBeforeCreate = pastureRepository.findAll().size();
        // Create the Pasture
        restPastureMockMvc.perform(post("/api/pastures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pastureEntity)))
            .andExpect(status().isCreated());

        // Validate the Pasture in the database
        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeCreate + 1);
        PastureEntity testPasture = pastureList.get(pastureList.size() - 1);
        assertThat(testPasture.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPasture.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPastureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pastureRepository.findAll().size();

        // Create the Pasture with an existing ID
        pastureEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPastureMockMvc.perform(post("/api/pastures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pastureEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Pasture in the database
        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pastureRepository.findAll().size();
        // set the field null
        pastureEntity.setName(null);

        // Create the Pasture, which fails.


        restPastureMockMvc.perform(post("/api/pastures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pastureEntity)))
            .andExpect(status().isBadRequest());

        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pastureRepository.findAll().size();
        // set the field null
        pastureEntity.setDescription(null);

        // Create the Pasture, which fails.


        restPastureMockMvc.perform(post("/api/pastures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pastureEntity)))
            .andExpect(status().isBadRequest());

        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPastures() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList
        restPastureMockMvc.perform(get("/api/pastures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pastureEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getPasture() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get the pasture
        restPastureMockMvc.perform(get("/api/pastures/{id}", pastureEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pastureEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getPasturesByIdFiltering() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        Long id = pastureEntity.getId();

        defaultPastureShouldBeFound("id.equals=" + id);
        defaultPastureShouldNotBeFound("id.notEquals=" + id);

        defaultPastureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPastureShouldNotBeFound("id.greaterThan=" + id);

        defaultPastureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPastureShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPasturesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where name equals to DEFAULT_NAME
        defaultPastureShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pastureList where name equals to UPDATED_NAME
        defaultPastureShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPasturesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where name not equals to DEFAULT_NAME
        defaultPastureShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the pastureList where name not equals to UPDATED_NAME
        defaultPastureShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPasturesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPastureShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pastureList where name equals to UPDATED_NAME
        defaultPastureShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPasturesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where name is not null
        defaultPastureShouldBeFound("name.specified=true");

        // Get all the pastureList where name is null
        defaultPastureShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPasturesByNameContainsSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where name contains DEFAULT_NAME
        defaultPastureShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pastureList where name contains UPDATED_NAME
        defaultPastureShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPasturesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where name does not contain DEFAULT_NAME
        defaultPastureShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pastureList where name does not contain UPDATED_NAME
        defaultPastureShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPasturesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where description equals to DEFAULT_DESCRIPTION
        defaultPastureShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the pastureList where description equals to UPDATED_DESCRIPTION
        defaultPastureShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPasturesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where description not equals to DEFAULT_DESCRIPTION
        defaultPastureShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the pastureList where description not equals to UPDATED_DESCRIPTION
        defaultPastureShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPasturesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPastureShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the pastureList where description equals to UPDATED_DESCRIPTION
        defaultPastureShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPasturesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where description is not null
        defaultPastureShouldBeFound("description.specified=true");

        // Get all the pastureList where description is null
        defaultPastureShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllPasturesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where description contains DEFAULT_DESCRIPTION
        defaultPastureShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the pastureList where description contains UPDATED_DESCRIPTION
        defaultPastureShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPasturesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        pastureRepository.saveAndFlush(pastureEntity);

        // Get all the pastureList where description does not contain DEFAULT_DESCRIPTION
        defaultPastureShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the pastureList where description does not contain UPDATED_DESCRIPTION
        defaultPastureShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPastureShouldBeFound(String filter) throws Exception {
        restPastureMockMvc.perform(get("/api/pastures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pastureEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPastureMockMvc.perform(get("/api/pastures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPastureShouldNotBeFound(String filter) throws Exception {
        restPastureMockMvc.perform(get("/api/pastures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPastureMockMvc.perform(get("/api/pastures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPasture() throws Exception {
        // Get the pasture
        restPastureMockMvc.perform(get("/api/pastures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePasture() throws Exception {
        // Initialize the database
        pastureService.save(pastureEntity);

        int databaseSizeBeforeUpdate = pastureRepository.findAll().size();

        // Update the pasture
        PastureEntity updatedPastureEntity = pastureRepository.findById(pastureEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPastureEntity are not directly saved in db
        em.detach(updatedPastureEntity);
        updatedPastureEntity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restPastureMockMvc.perform(put("/api/pastures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPastureEntity)))
            .andExpect(status().isOk());

        // Validate the Pasture in the database
        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeUpdate);
        PastureEntity testPasture = pastureList.get(pastureList.size() - 1);
        assertThat(testPasture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPasture.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPasture() throws Exception {
        int databaseSizeBeforeUpdate = pastureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPastureMockMvc.perform(put("/api/pastures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pastureEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Pasture in the database
        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePasture() throws Exception {
        // Initialize the database
        pastureService.save(pastureEntity);

        int databaseSizeBeforeDelete = pastureRepository.findAll().size();

        // Delete the pasture
        restPastureMockMvc.perform(delete("/api/pastures/{id}", pastureEntity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PastureEntity> pastureList = pastureRepository.findAll();
        assertThat(pastureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
