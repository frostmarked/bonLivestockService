package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.repository.MatrilinealityRepository;
import com.bonlimousin.livestock.service.MatrilinealityService;
import com.bonlimousin.livestock.service.dto.MatrilinealityCriteria;
import com.bonlimousin.livestock.service.MatrilinealityQueryService;

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

import com.bonlimousin.livestock.domain.enumeration.UserRole;
/**
 * Integration tests for the {@link MatrilinealityResource} REST controller.
 */
@SpringBootTest(classes = BonLivestockServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MatrilinealityResourceIT {

    private static final String DEFAULT_FAMILYNAME = "AAAAAAAAAA";
    private static final String UPDATED_FAMILYNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EAR_TAG_ID = 0;
    private static final Integer UPDATED_EAR_TAG_ID = 1;
    private static final Integer SMALLER_EAR_TAG_ID = 0 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATTLE_NAME_REGEX_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_CATTLE_NAME_REGEX_PATTERN = "BBBBBBBBBB";

    private static final Integer DEFAULT_PATRI_ID = 1;
    private static final Integer UPDATED_PATRI_ID = 2;
    private static final Integer SMALLER_PATRI_ID = 1 - 1;

    private static final String DEFAULT_PATRI_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PATRI_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATRI_COUNTRY = "AAAAAA";
    private static final String UPDATED_PATRI_COUNTRY = "BBBBBB";

    private static final Boolean DEFAULT_POLLED = false;
    private static final Boolean UPDATED_POLLED = true;

    private static final String DEFAULT_STORY_HANDLE = "AAAAAAAAAA";
    private static final String UPDATED_STORY_HANDLE = "BBBBBBBBBB";

    private static final UserRole DEFAULT_VISIBILITY = UserRole.ROLE_ADMIN;
    private static final UserRole UPDATED_VISIBILITY = UserRole.ROLE_USER;

    @Autowired
    private MatrilinealityRepository matrilinealityRepository;

    @Autowired
    private MatrilinealityService matrilinealityService;

    @Autowired
    private MatrilinealityQueryService matrilinealityQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatrilinealityMockMvc;

    private MatrilinealityEntity matrilinealityEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatrilinealityEntity createEntity(EntityManager em) {
        MatrilinealityEntity matrilinealityEntity = new MatrilinealityEntity()
            .familyname(DEFAULT_FAMILYNAME)
            .earTagId(DEFAULT_EAR_TAG_ID)
            .name(DEFAULT_NAME)
            .country(DEFAULT_COUNTRY)
            .description(DEFAULT_DESCRIPTION)
            .cattleNameRegexPattern(DEFAULT_CATTLE_NAME_REGEX_PATTERN)
            .patriId(DEFAULT_PATRI_ID)
            .patriName(DEFAULT_PATRI_NAME)
            .patriCountry(DEFAULT_PATRI_COUNTRY)
            .polled(DEFAULT_POLLED)
            .storyHandle(DEFAULT_STORY_HANDLE)
            .visibility(DEFAULT_VISIBILITY);
        return matrilinealityEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatrilinealityEntity createUpdatedEntity(EntityManager em) {
        MatrilinealityEntity matrilinealityEntity = new MatrilinealityEntity()
            .familyname(UPDATED_FAMILYNAME)
            .earTagId(UPDATED_EAR_TAG_ID)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .description(UPDATED_DESCRIPTION)
            .cattleNameRegexPattern(UPDATED_CATTLE_NAME_REGEX_PATTERN)
            .patriId(UPDATED_PATRI_ID)
            .patriName(UPDATED_PATRI_NAME)
            .patriCountry(UPDATED_PATRI_COUNTRY)
            .polled(UPDATED_POLLED)
            .storyHandle(UPDATED_STORY_HANDLE)
            .visibility(UPDATED_VISIBILITY);
        return matrilinealityEntity;
    }

    @BeforeEach
    public void initTest() {
    	matrilinealityRepository.deleteAll();
        matrilinealityEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createMatrilineality() throws Exception {
        int databaseSizeBeforeCreate = matrilinealityRepository.findAll().size();
        // Create the Matrilineality
        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isCreated());

        // Validate the Matrilineality in the database
        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeCreate + 1);
        MatrilinealityEntity testMatrilineality = matrilinealityList.get(matrilinealityList.size() - 1);
        assertThat(testMatrilineality.getFamilyname()).isEqualTo(DEFAULT_FAMILYNAME);
        assertThat(testMatrilineality.getEarTagId()).isEqualTo(DEFAULT_EAR_TAG_ID);
        assertThat(testMatrilineality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMatrilineality.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMatrilineality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMatrilineality.getCattleNameRegexPattern()).isEqualTo(DEFAULT_CATTLE_NAME_REGEX_PATTERN);
        assertThat(testMatrilineality.getPatriId()).isEqualTo(DEFAULT_PATRI_ID);
        assertThat(testMatrilineality.getPatriName()).isEqualTo(DEFAULT_PATRI_NAME);
        assertThat(testMatrilineality.getPatriCountry()).isEqualTo(DEFAULT_PATRI_COUNTRY);
        assertThat(testMatrilineality.isPolled()).isEqualTo(DEFAULT_POLLED);
        assertThat(testMatrilineality.getStoryHandle()).isEqualTo(DEFAULT_STORY_HANDLE);
        assertThat(testMatrilineality.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    public void createMatrilinealityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = matrilinealityRepository.findAll().size();

        // Create the Matrilineality with an existing ID
        matrilinealityEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Matrilineality in the database
        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFamilynameIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setFamilyname(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEarTagIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setEarTagId(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setName(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setCountry(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCattleNameRegexPatternIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setCattleNameRegexPattern(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPatriIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setPatriId(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPatriNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setPatriName(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPatriCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setPatriCountry(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPolledIsRequired() throws Exception {
        int databaseSizeBeforeTest = matrilinealityRepository.findAll().size();
        // set the field null
        matrilinealityEntity.setPolled(null);

        // Create the Matrilineality, which fails.


        restMatrilinealityMockMvc.perform(post("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMatrilinealities() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matrilinealityEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyname").value(hasItem(DEFAULT_FAMILYNAME)))
            .andExpect(jsonPath("$.[*].earTagId").value(hasItem(DEFAULT_EAR_TAG_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cattleNameRegexPattern").value(hasItem(DEFAULT_CATTLE_NAME_REGEX_PATTERN)))
            .andExpect(jsonPath("$.[*].patriId").value(hasItem(DEFAULT_PATRI_ID)))
            .andExpect(jsonPath("$.[*].patriName").value(hasItem(DEFAULT_PATRI_NAME)))
            .andExpect(jsonPath("$.[*].patriCountry").value(hasItem(DEFAULT_PATRI_COUNTRY)))
            .andExpect(jsonPath("$.[*].polled").value(hasItem(DEFAULT_POLLED.booleanValue())))
            .andExpect(jsonPath("$.[*].storyHandle").value(hasItem(DEFAULT_STORY_HANDLE)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }
    
    @Test
    @Transactional
    public void getMatrilineality() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get the matrilineality
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities/{id}", matrilinealityEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matrilinealityEntity.getId().intValue()))
            .andExpect(jsonPath("$.familyname").value(DEFAULT_FAMILYNAME))
            .andExpect(jsonPath("$.earTagId").value(DEFAULT_EAR_TAG_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cattleNameRegexPattern").value(DEFAULT_CATTLE_NAME_REGEX_PATTERN))
            .andExpect(jsonPath("$.patriId").value(DEFAULT_PATRI_ID))
            .andExpect(jsonPath("$.patriName").value(DEFAULT_PATRI_NAME))
            .andExpect(jsonPath("$.patriCountry").value(DEFAULT_PATRI_COUNTRY))
            .andExpect(jsonPath("$.polled").value(DEFAULT_POLLED.booleanValue()))
            .andExpect(jsonPath("$.storyHandle").value(DEFAULT_STORY_HANDLE))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }


    @Test
    @Transactional
    public void getMatrilinealitiesByIdFiltering() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        Long id = matrilinealityEntity.getId();

        defaultMatrilinealityShouldBeFound("id.equals=" + id);
        defaultMatrilinealityShouldNotBeFound("id.notEquals=" + id);

        defaultMatrilinealityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMatrilinealityShouldNotBeFound("id.greaterThan=" + id);

        defaultMatrilinealityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMatrilinealityShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByFamilynameIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where familyname equals to DEFAULT_FAMILYNAME
        defaultMatrilinealityShouldBeFound("familyname.equals=" + DEFAULT_FAMILYNAME);

        // Get all the matrilinealityList where familyname equals to UPDATED_FAMILYNAME
        defaultMatrilinealityShouldNotBeFound("familyname.equals=" + UPDATED_FAMILYNAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByFamilynameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where familyname not equals to DEFAULT_FAMILYNAME
        defaultMatrilinealityShouldNotBeFound("familyname.notEquals=" + DEFAULT_FAMILYNAME);

        // Get all the matrilinealityList where familyname not equals to UPDATED_FAMILYNAME
        defaultMatrilinealityShouldBeFound("familyname.notEquals=" + UPDATED_FAMILYNAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByFamilynameIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where familyname in DEFAULT_FAMILYNAME or UPDATED_FAMILYNAME
        defaultMatrilinealityShouldBeFound("familyname.in=" + DEFAULT_FAMILYNAME + "," + UPDATED_FAMILYNAME);

        // Get all the matrilinealityList where familyname equals to UPDATED_FAMILYNAME
        defaultMatrilinealityShouldNotBeFound("familyname.in=" + UPDATED_FAMILYNAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByFamilynameIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where familyname is not null
        defaultMatrilinealityShouldBeFound("familyname.specified=true");

        // Get all the matrilinealityList where familyname is null
        defaultMatrilinealityShouldNotBeFound("familyname.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByFamilynameContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where familyname contains DEFAULT_FAMILYNAME
        defaultMatrilinealityShouldBeFound("familyname.contains=" + DEFAULT_FAMILYNAME);

        // Get all the matrilinealityList where familyname contains UPDATED_FAMILYNAME
        defaultMatrilinealityShouldNotBeFound("familyname.contains=" + UPDATED_FAMILYNAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByFamilynameNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where familyname does not contain DEFAULT_FAMILYNAME
        defaultMatrilinealityShouldNotBeFound("familyname.doesNotContain=" + DEFAULT_FAMILYNAME);

        // Get all the matrilinealityList where familyname does not contain UPDATED_FAMILYNAME
        defaultMatrilinealityShouldBeFound("familyname.doesNotContain=" + UPDATED_FAMILYNAME);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId equals to DEFAULT_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.equals=" + DEFAULT_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId equals to UPDATED_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.equals=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId not equals to DEFAULT_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.notEquals=" + DEFAULT_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId not equals to UPDATED_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.notEquals=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId in DEFAULT_EAR_TAG_ID or UPDATED_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.in=" + DEFAULT_EAR_TAG_ID + "," + UPDATED_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId equals to UPDATED_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.in=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId is not null
        defaultMatrilinealityShouldBeFound("earTagId.specified=true");

        // Get all the matrilinealityList where earTagId is null
        defaultMatrilinealityShouldNotBeFound("earTagId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId is greater than or equal to DEFAULT_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.greaterThanOrEqual=" + DEFAULT_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId is greater than or equal to UPDATED_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.greaterThanOrEqual=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId is less than or equal to DEFAULT_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.lessThanOrEqual=" + DEFAULT_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId is less than or equal to SMALLER_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.lessThanOrEqual=" + SMALLER_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsLessThanSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId is less than DEFAULT_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.lessThan=" + DEFAULT_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId is less than UPDATED_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.lessThan=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByEarTagIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where earTagId is greater than DEFAULT_EAR_TAG_ID
        defaultMatrilinealityShouldNotBeFound("earTagId.greaterThan=" + DEFAULT_EAR_TAG_ID);

        // Get all the matrilinealityList where earTagId is greater than SMALLER_EAR_TAG_ID
        defaultMatrilinealityShouldBeFound("earTagId.greaterThan=" + SMALLER_EAR_TAG_ID);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where name equals to DEFAULT_NAME
        defaultMatrilinealityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the matrilinealityList where name equals to UPDATED_NAME
        defaultMatrilinealityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where name not equals to DEFAULT_NAME
        defaultMatrilinealityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the matrilinealityList where name not equals to UPDATED_NAME
        defaultMatrilinealityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMatrilinealityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the matrilinealityList where name equals to UPDATED_NAME
        defaultMatrilinealityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where name is not null
        defaultMatrilinealityShouldBeFound("name.specified=true");

        // Get all the matrilinealityList where name is null
        defaultMatrilinealityShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where name contains DEFAULT_NAME
        defaultMatrilinealityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the matrilinealityList where name contains UPDATED_NAME
        defaultMatrilinealityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where name does not contain DEFAULT_NAME
        defaultMatrilinealityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the matrilinealityList where name does not contain UPDATED_NAME
        defaultMatrilinealityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where country equals to DEFAULT_COUNTRY
        defaultMatrilinealityShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the matrilinealityList where country equals to UPDATED_COUNTRY
        defaultMatrilinealityShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where country not equals to DEFAULT_COUNTRY
        defaultMatrilinealityShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the matrilinealityList where country not equals to UPDATED_COUNTRY
        defaultMatrilinealityShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultMatrilinealityShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the matrilinealityList where country equals to UPDATED_COUNTRY
        defaultMatrilinealityShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where country is not null
        defaultMatrilinealityShouldBeFound("country.specified=true");

        // Get all the matrilinealityList where country is null
        defaultMatrilinealityShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByCountryContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where country contains DEFAULT_COUNTRY
        defaultMatrilinealityShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the matrilinealityList where country contains UPDATED_COUNTRY
        defaultMatrilinealityShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where country does not contain DEFAULT_COUNTRY
        defaultMatrilinealityShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the matrilinealityList where country does not contain UPDATED_COUNTRY
        defaultMatrilinealityShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where description equals to DEFAULT_DESCRIPTION
        defaultMatrilinealityShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the matrilinealityList where description equals to UPDATED_DESCRIPTION
        defaultMatrilinealityShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where description not equals to DEFAULT_DESCRIPTION
        defaultMatrilinealityShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the matrilinealityList where description not equals to UPDATED_DESCRIPTION
        defaultMatrilinealityShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMatrilinealityShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the matrilinealityList where description equals to UPDATED_DESCRIPTION
        defaultMatrilinealityShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where description is not null
        defaultMatrilinealityShouldBeFound("description.specified=true");

        // Get all the matrilinealityList where description is null
        defaultMatrilinealityShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where description contains DEFAULT_DESCRIPTION
        defaultMatrilinealityShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the matrilinealityList where description contains UPDATED_DESCRIPTION
        defaultMatrilinealityShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where description does not contain DEFAULT_DESCRIPTION
        defaultMatrilinealityShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the matrilinealityList where description does not contain UPDATED_DESCRIPTION
        defaultMatrilinealityShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByCattleNameRegexPatternIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where cattleNameRegexPattern equals to DEFAULT_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldBeFound("cattleNameRegexPattern.equals=" + DEFAULT_CATTLE_NAME_REGEX_PATTERN);

        // Get all the matrilinealityList where cattleNameRegexPattern equals to UPDATED_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldNotBeFound("cattleNameRegexPattern.equals=" + UPDATED_CATTLE_NAME_REGEX_PATTERN);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCattleNameRegexPatternIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where cattleNameRegexPattern not equals to DEFAULT_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldNotBeFound("cattleNameRegexPattern.notEquals=" + DEFAULT_CATTLE_NAME_REGEX_PATTERN);

        // Get all the matrilinealityList where cattleNameRegexPattern not equals to UPDATED_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldBeFound("cattleNameRegexPattern.notEquals=" + UPDATED_CATTLE_NAME_REGEX_PATTERN);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCattleNameRegexPatternIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where cattleNameRegexPattern in DEFAULT_CATTLE_NAME_REGEX_PATTERN or UPDATED_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldBeFound("cattleNameRegexPattern.in=" + DEFAULT_CATTLE_NAME_REGEX_PATTERN + "," + UPDATED_CATTLE_NAME_REGEX_PATTERN);

        // Get all the matrilinealityList where cattleNameRegexPattern equals to UPDATED_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldNotBeFound("cattleNameRegexPattern.in=" + UPDATED_CATTLE_NAME_REGEX_PATTERN);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCattleNameRegexPatternIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where cattleNameRegexPattern is not null
        defaultMatrilinealityShouldBeFound("cattleNameRegexPattern.specified=true");

        // Get all the matrilinealityList where cattleNameRegexPattern is null
        defaultMatrilinealityShouldNotBeFound("cattleNameRegexPattern.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByCattleNameRegexPatternContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where cattleNameRegexPattern contains DEFAULT_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldBeFound("cattleNameRegexPattern.contains=" + DEFAULT_CATTLE_NAME_REGEX_PATTERN);

        // Get all the matrilinealityList where cattleNameRegexPattern contains UPDATED_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldNotBeFound("cattleNameRegexPattern.contains=" + UPDATED_CATTLE_NAME_REGEX_PATTERN);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByCattleNameRegexPatternNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where cattleNameRegexPattern does not contain DEFAULT_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldNotBeFound("cattleNameRegexPattern.doesNotContain=" + DEFAULT_CATTLE_NAME_REGEX_PATTERN);

        // Get all the matrilinealityList where cattleNameRegexPattern does not contain UPDATED_CATTLE_NAME_REGEX_PATTERN
        defaultMatrilinealityShouldBeFound("cattleNameRegexPattern.doesNotContain=" + UPDATED_CATTLE_NAME_REGEX_PATTERN);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId equals to DEFAULT_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.equals=" + DEFAULT_PATRI_ID);

        // Get all the matrilinealityList where patriId equals to UPDATED_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.equals=" + UPDATED_PATRI_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId not equals to DEFAULT_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.notEquals=" + DEFAULT_PATRI_ID);

        // Get all the matrilinealityList where patriId not equals to UPDATED_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.notEquals=" + UPDATED_PATRI_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId in DEFAULT_PATRI_ID or UPDATED_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.in=" + DEFAULT_PATRI_ID + "," + UPDATED_PATRI_ID);

        // Get all the matrilinealityList where patriId equals to UPDATED_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.in=" + UPDATED_PATRI_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId is not null
        defaultMatrilinealityShouldBeFound("patriId.specified=true");

        // Get all the matrilinealityList where patriId is null
        defaultMatrilinealityShouldNotBeFound("patriId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId is greater than or equal to DEFAULT_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.greaterThanOrEqual=" + DEFAULT_PATRI_ID);

        // Get all the matrilinealityList where patriId is greater than or equal to UPDATED_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.greaterThanOrEqual=" + UPDATED_PATRI_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId is less than or equal to DEFAULT_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.lessThanOrEqual=" + DEFAULT_PATRI_ID);

        // Get all the matrilinealityList where patriId is less than or equal to SMALLER_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.lessThanOrEqual=" + SMALLER_PATRI_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsLessThanSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId is less than DEFAULT_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.lessThan=" + DEFAULT_PATRI_ID);

        // Get all the matrilinealityList where patriId is less than UPDATED_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.lessThan=" + UPDATED_PATRI_ID);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriId is greater than DEFAULT_PATRI_ID
        defaultMatrilinealityShouldNotBeFound("patriId.greaterThan=" + DEFAULT_PATRI_ID);

        // Get all the matrilinealityList where patriId is greater than SMALLER_PATRI_ID
        defaultMatrilinealityShouldBeFound("patriId.greaterThan=" + SMALLER_PATRI_ID);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriNameIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriName equals to DEFAULT_PATRI_NAME
        defaultMatrilinealityShouldBeFound("patriName.equals=" + DEFAULT_PATRI_NAME);

        // Get all the matrilinealityList where patriName equals to UPDATED_PATRI_NAME
        defaultMatrilinealityShouldNotBeFound("patriName.equals=" + UPDATED_PATRI_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriName not equals to DEFAULT_PATRI_NAME
        defaultMatrilinealityShouldNotBeFound("patriName.notEquals=" + DEFAULT_PATRI_NAME);

        // Get all the matrilinealityList where patriName not equals to UPDATED_PATRI_NAME
        defaultMatrilinealityShouldBeFound("patriName.notEquals=" + UPDATED_PATRI_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriNameIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriName in DEFAULT_PATRI_NAME or UPDATED_PATRI_NAME
        defaultMatrilinealityShouldBeFound("patriName.in=" + DEFAULT_PATRI_NAME + "," + UPDATED_PATRI_NAME);

        // Get all the matrilinealityList where patriName equals to UPDATED_PATRI_NAME
        defaultMatrilinealityShouldNotBeFound("patriName.in=" + UPDATED_PATRI_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriName is not null
        defaultMatrilinealityShouldBeFound("patriName.specified=true");

        // Get all the matrilinealityList where patriName is null
        defaultMatrilinealityShouldNotBeFound("patriName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriNameContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriName contains DEFAULT_PATRI_NAME
        defaultMatrilinealityShouldBeFound("patriName.contains=" + DEFAULT_PATRI_NAME);

        // Get all the matrilinealityList where patriName contains UPDATED_PATRI_NAME
        defaultMatrilinealityShouldNotBeFound("patriName.contains=" + UPDATED_PATRI_NAME);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriNameNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriName does not contain DEFAULT_PATRI_NAME
        defaultMatrilinealityShouldNotBeFound("patriName.doesNotContain=" + DEFAULT_PATRI_NAME);

        // Get all the matrilinealityList where patriName does not contain UPDATED_PATRI_NAME
        defaultMatrilinealityShouldBeFound("patriName.doesNotContain=" + UPDATED_PATRI_NAME);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriCountry equals to DEFAULT_PATRI_COUNTRY
        defaultMatrilinealityShouldBeFound("patriCountry.equals=" + DEFAULT_PATRI_COUNTRY);

        // Get all the matrilinealityList where patriCountry equals to UPDATED_PATRI_COUNTRY
        defaultMatrilinealityShouldNotBeFound("patriCountry.equals=" + UPDATED_PATRI_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriCountry not equals to DEFAULT_PATRI_COUNTRY
        defaultMatrilinealityShouldNotBeFound("patriCountry.notEquals=" + DEFAULT_PATRI_COUNTRY);

        // Get all the matrilinealityList where patriCountry not equals to UPDATED_PATRI_COUNTRY
        defaultMatrilinealityShouldBeFound("patriCountry.notEquals=" + UPDATED_PATRI_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriCountryIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriCountry in DEFAULT_PATRI_COUNTRY or UPDATED_PATRI_COUNTRY
        defaultMatrilinealityShouldBeFound("patriCountry.in=" + DEFAULT_PATRI_COUNTRY + "," + UPDATED_PATRI_COUNTRY);

        // Get all the matrilinealityList where patriCountry equals to UPDATED_PATRI_COUNTRY
        defaultMatrilinealityShouldNotBeFound("patriCountry.in=" + UPDATED_PATRI_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriCountry is not null
        defaultMatrilinealityShouldBeFound("patriCountry.specified=true");

        // Get all the matrilinealityList where patriCountry is null
        defaultMatrilinealityShouldNotBeFound("patriCountry.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriCountryContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriCountry contains DEFAULT_PATRI_COUNTRY
        defaultMatrilinealityShouldBeFound("patriCountry.contains=" + DEFAULT_PATRI_COUNTRY);

        // Get all the matrilinealityList where patriCountry contains UPDATED_PATRI_COUNTRY
        defaultMatrilinealityShouldNotBeFound("patriCountry.contains=" + UPDATED_PATRI_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPatriCountryNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where patriCountry does not contain DEFAULT_PATRI_COUNTRY
        defaultMatrilinealityShouldNotBeFound("patriCountry.doesNotContain=" + DEFAULT_PATRI_COUNTRY);

        // Get all the matrilinealityList where patriCountry does not contain UPDATED_PATRI_COUNTRY
        defaultMatrilinealityShouldBeFound("patriCountry.doesNotContain=" + UPDATED_PATRI_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByPolledIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where polled equals to DEFAULT_POLLED
        defaultMatrilinealityShouldBeFound("polled.equals=" + DEFAULT_POLLED);

        // Get all the matrilinealityList where polled equals to UPDATED_POLLED
        defaultMatrilinealityShouldNotBeFound("polled.equals=" + UPDATED_POLLED);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPolledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where polled not equals to DEFAULT_POLLED
        defaultMatrilinealityShouldNotBeFound("polled.notEquals=" + DEFAULT_POLLED);

        // Get all the matrilinealityList where polled not equals to UPDATED_POLLED
        defaultMatrilinealityShouldBeFound("polled.notEquals=" + UPDATED_POLLED);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPolledIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where polled in DEFAULT_POLLED or UPDATED_POLLED
        defaultMatrilinealityShouldBeFound("polled.in=" + DEFAULT_POLLED + "," + UPDATED_POLLED);

        // Get all the matrilinealityList where polled equals to UPDATED_POLLED
        defaultMatrilinealityShouldNotBeFound("polled.in=" + UPDATED_POLLED);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByPolledIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where polled is not null
        defaultMatrilinealityShouldBeFound("polled.specified=true");

        // Get all the matrilinealityList where polled is null
        defaultMatrilinealityShouldNotBeFound("polled.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByStoryHandleIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where storyHandle equals to DEFAULT_STORY_HANDLE
        defaultMatrilinealityShouldBeFound("storyHandle.equals=" + DEFAULT_STORY_HANDLE);

        // Get all the matrilinealityList where storyHandle equals to UPDATED_STORY_HANDLE
        defaultMatrilinealityShouldNotBeFound("storyHandle.equals=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByStoryHandleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where storyHandle not equals to DEFAULT_STORY_HANDLE
        defaultMatrilinealityShouldNotBeFound("storyHandle.notEquals=" + DEFAULT_STORY_HANDLE);

        // Get all the matrilinealityList where storyHandle not equals to UPDATED_STORY_HANDLE
        defaultMatrilinealityShouldBeFound("storyHandle.notEquals=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByStoryHandleIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where storyHandle in DEFAULT_STORY_HANDLE or UPDATED_STORY_HANDLE
        defaultMatrilinealityShouldBeFound("storyHandle.in=" + DEFAULT_STORY_HANDLE + "," + UPDATED_STORY_HANDLE);

        // Get all the matrilinealityList where storyHandle equals to UPDATED_STORY_HANDLE
        defaultMatrilinealityShouldNotBeFound("storyHandle.in=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByStoryHandleIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where storyHandle is not null
        defaultMatrilinealityShouldBeFound("storyHandle.specified=true");

        // Get all the matrilinealityList where storyHandle is null
        defaultMatrilinealityShouldNotBeFound("storyHandle.specified=false");
    }
                @Test
    @Transactional
    public void getAllMatrilinealitiesByStoryHandleContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where storyHandle contains DEFAULT_STORY_HANDLE
        defaultMatrilinealityShouldBeFound("storyHandle.contains=" + DEFAULT_STORY_HANDLE);

        // Get all the matrilinealityList where storyHandle contains UPDATED_STORY_HANDLE
        defaultMatrilinealityShouldNotBeFound("storyHandle.contains=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByStoryHandleNotContainsSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where storyHandle does not contain DEFAULT_STORY_HANDLE
        defaultMatrilinealityShouldNotBeFound("storyHandle.doesNotContain=" + DEFAULT_STORY_HANDLE);

        // Get all the matrilinealityList where storyHandle does not contain UPDATED_STORY_HANDLE
        defaultMatrilinealityShouldBeFound("storyHandle.doesNotContain=" + UPDATED_STORY_HANDLE);
    }


    @Test
    @Transactional
    public void getAllMatrilinealitiesByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where visibility equals to DEFAULT_VISIBILITY
        defaultMatrilinealityShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the matrilinealityList where visibility equals to UPDATED_VISIBILITY
        defaultMatrilinealityShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByVisibilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where visibility not equals to DEFAULT_VISIBILITY
        defaultMatrilinealityShouldNotBeFound("visibility.notEquals=" + DEFAULT_VISIBILITY);

        // Get all the matrilinealityList where visibility not equals to UPDATED_VISIBILITY
        defaultMatrilinealityShouldBeFound("visibility.notEquals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultMatrilinealityShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the matrilinealityList where visibility equals to UPDATED_VISIBILITY
        defaultMatrilinealityShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllMatrilinealitiesByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        matrilinealityRepository.saveAndFlush(matrilinealityEntity);

        // Get all the matrilinealityList where visibility is not null
        defaultMatrilinealityShouldBeFound("visibility.specified=true");

        // Get all the matrilinealityList where visibility is null
        defaultMatrilinealityShouldNotBeFound("visibility.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMatrilinealityShouldBeFound(String filter) throws Exception {
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matrilinealityEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyname").value(hasItem(DEFAULT_FAMILYNAME)))
            .andExpect(jsonPath("$.[*].earTagId").value(hasItem(DEFAULT_EAR_TAG_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cattleNameRegexPattern").value(hasItem(DEFAULT_CATTLE_NAME_REGEX_PATTERN)))
            .andExpect(jsonPath("$.[*].patriId").value(hasItem(DEFAULT_PATRI_ID)))
            .andExpect(jsonPath("$.[*].patriName").value(hasItem(DEFAULT_PATRI_NAME)))
            .andExpect(jsonPath("$.[*].patriCountry").value(hasItem(DEFAULT_PATRI_COUNTRY)))
            .andExpect(jsonPath("$.[*].polled").value(hasItem(DEFAULT_POLLED.booleanValue())))
            .andExpect(jsonPath("$.[*].storyHandle").value(hasItem(DEFAULT_STORY_HANDLE)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));

        // Check, that the count call also returns 1
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMatrilinealityShouldNotBeFound(String filter) throws Exception {
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMatrilineality() throws Exception {
        // Get the matrilineality
        restMatrilinealityMockMvc.perform(get("/api/matrilinealities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatrilineality() throws Exception {
        // Initialize the database
        matrilinealityService.save(matrilinealityEntity);

        int databaseSizeBeforeUpdate = matrilinealityRepository.findAll().size();

        // Update the matrilineality
        MatrilinealityEntity updatedMatrilinealityEntity = matrilinealityRepository.findById(matrilinealityEntity.getId()).get();
        // Disconnect from session so that the updates on updatedMatrilinealityEntity are not directly saved in db
        em.detach(updatedMatrilinealityEntity);
        updatedMatrilinealityEntity
            .familyname(UPDATED_FAMILYNAME)
            .earTagId(UPDATED_EAR_TAG_ID)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .description(UPDATED_DESCRIPTION)
            .cattleNameRegexPattern(UPDATED_CATTLE_NAME_REGEX_PATTERN)
            .patriId(UPDATED_PATRI_ID)
            .patriName(UPDATED_PATRI_NAME)
            .patriCountry(UPDATED_PATRI_COUNTRY)
            .polled(UPDATED_POLLED)
            .storyHandle(UPDATED_STORY_HANDLE)
            .visibility(UPDATED_VISIBILITY);

        restMatrilinealityMockMvc.perform(put("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMatrilinealityEntity)))
            .andExpect(status().isOk());

        // Validate the Matrilineality in the database
        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeUpdate);
        MatrilinealityEntity testMatrilineality = matrilinealityList.get(matrilinealityList.size() - 1);
        assertThat(testMatrilineality.getFamilyname()).isEqualTo(UPDATED_FAMILYNAME);
        assertThat(testMatrilineality.getEarTagId()).isEqualTo(UPDATED_EAR_TAG_ID);
        assertThat(testMatrilineality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMatrilineality.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMatrilineality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMatrilineality.getCattleNameRegexPattern()).isEqualTo(UPDATED_CATTLE_NAME_REGEX_PATTERN);
        assertThat(testMatrilineality.getPatriId()).isEqualTo(UPDATED_PATRI_ID);
        assertThat(testMatrilineality.getPatriName()).isEqualTo(UPDATED_PATRI_NAME);
        assertThat(testMatrilineality.getPatriCountry()).isEqualTo(UPDATED_PATRI_COUNTRY);
        assertThat(testMatrilineality.isPolled()).isEqualTo(UPDATED_POLLED);
        assertThat(testMatrilineality.getStoryHandle()).isEqualTo(UPDATED_STORY_HANDLE);
        assertThat(testMatrilineality.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingMatrilineality() throws Exception {
        int databaseSizeBeforeUpdate = matrilinealityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatrilinealityMockMvc.perform(put("/api/matrilinealities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matrilinealityEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Matrilineality in the database
        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMatrilineality() throws Exception {
        // Initialize the database
        matrilinealityService.save(matrilinealityEntity);

        int databaseSizeBeforeDelete = matrilinealityRepository.findAll().size();

        // Delete the matrilineality
        restMatrilinealityMockMvc.perform(delete("/api/matrilinealities/{id}", matrilinealityEntity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatrilinealityEntity> matrilinealityList = matrilinealityRepository.findAll();
        assertThat(matrilinealityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
