package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.domain.PhotoEntity;
import com.bonlimousin.livestock.domain.NoteEntity;
import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.repository.CattleRepository;
import com.bonlimousin.livestock.service.CattleService;
import com.bonlimousin.livestock.service.dto.CattleCriteria;
import com.bonlimousin.livestock.service.CattleQueryService;

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
 * Integration tests for the {@link CattleResource} REST controller.
 */
@SpringBootTest(classes = BonLivestockServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CattleResourceIT {

    private static final Integer DEFAULT_EAR_TAG_ID = 0;
    private static final Integer UPDATED_EAR_TAG_ID = 1;
    private static final Integer SMALLER_EAR_TAG_ID = 0 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final UserRole DEFAULT_VISIBILITY = UserRole.ROLE_ADMIN;
    private static final UserRole UPDATED_VISIBILITY = UserRole.ROLE_USER;

    private static final Boolean DEFAULT_UP_FOR_SALE = false;
    private static final Boolean UPDATED_UP_FOR_SALE = true;

    private static final Boolean DEFAULT_SHOW_BLUP = false;
    private static final Boolean UPDATED_SHOW_BLUP = true;

    private static final Boolean DEFAULT_ALERT = false;
    private static final Boolean UPDATED_ALERT = true;

    private static final String DEFAULT_STORY_HANDLE = "AAAAAAAAAA";
    private static final String UPDATED_STORY_HANDLE = "BBBBBBBBBB";

    @Autowired
    private CattleRepository cattleRepository;

    @Autowired
    private CattleService cattleService;

    @Autowired
    private CattleQueryService cattleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCattleMockMvc;

    private CattleEntity cattleEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CattleEntity createEntity(EntityManager em) {
        CattleEntity cattleEntity = new CattleEntity()
            .earTagId(DEFAULT_EAR_TAG_ID)
            .name(DEFAULT_NAME)
            .visibility(DEFAULT_VISIBILITY)
            .upForSale(DEFAULT_UP_FOR_SALE)
            .showBlup(DEFAULT_SHOW_BLUP)
            .alert(DEFAULT_ALERT)
            .storyHandle(DEFAULT_STORY_HANDLE);
        return cattleEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CattleEntity createUpdatedEntity(EntityManager em) {
        CattleEntity cattleEntity = new CattleEntity()
            .earTagId(UPDATED_EAR_TAG_ID)
            .name(UPDATED_NAME)
            .visibility(UPDATED_VISIBILITY)
            .upForSale(UPDATED_UP_FOR_SALE)
            .showBlup(UPDATED_SHOW_BLUP)
            .alert(UPDATED_ALERT)
            .storyHandle(UPDATED_STORY_HANDLE);
        return cattleEntity;
    }

    @BeforeEach
    public void initTest() {
        cattleEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createCattle() throws Exception {
        int databaseSizeBeforeCreate = cattleRepository.findAll().size();
        // Create the Cattle
        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isCreated());

        // Validate the Cattle in the database
        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeCreate + 1);
        CattleEntity testCattle = cattleList.get(cattleList.size() - 1);
        assertThat(testCattle.getEarTagId()).isEqualTo(DEFAULT_EAR_TAG_ID);
        assertThat(testCattle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCattle.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
        assertThat(testCattle.isUpForSale()).isEqualTo(DEFAULT_UP_FOR_SALE);
        assertThat(testCattle.isShowBlup()).isEqualTo(DEFAULT_SHOW_BLUP);
        assertThat(testCattle.isAlert()).isEqualTo(DEFAULT_ALERT);
        assertThat(testCattle.getStoryHandle()).isEqualTo(DEFAULT_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void createCattleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cattleRepository.findAll().size();

        // Create the Cattle with an existing ID
        cattleEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Cattle in the database
        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEarTagIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cattleRepository.findAll().size();
        // set the field null
        cattleEntity.setEarTagId(null);

        // Create the Cattle, which fails.


        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cattleRepository.findAll().size();
        // set the field null
        cattleEntity.setName(null);

        // Create the Cattle, which fails.


        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpForSaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cattleRepository.findAll().size();
        // set the field null
        cattleEntity.setUpForSale(null);

        // Create the Cattle, which fails.


        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShowBlupIsRequired() throws Exception {
        int databaseSizeBeforeTest = cattleRepository.findAll().size();
        // set the field null
        cattleEntity.setShowBlup(null);

        // Create the Cattle, which fails.


        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAlertIsRequired() throws Exception {
        int databaseSizeBeforeTest = cattleRepository.findAll().size();
        // set the field null
        cattleEntity.setAlert(null);

        // Create the Cattle, which fails.


        restCattleMockMvc.perform(post("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCattles() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList
        restCattleMockMvc.perform(get("/api/cattles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cattleEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].earTagId").value(hasItem(DEFAULT_EAR_TAG_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())))
            .andExpect(jsonPath("$.[*].upForSale").value(hasItem(DEFAULT_UP_FOR_SALE.booleanValue())))
            .andExpect(jsonPath("$.[*].showBlup").value(hasItem(DEFAULT_SHOW_BLUP.booleanValue())))
            .andExpect(jsonPath("$.[*].alert").value(hasItem(DEFAULT_ALERT.booleanValue())))
            .andExpect(jsonPath("$.[*].storyHandle").value(hasItem(DEFAULT_STORY_HANDLE)));
    }
    
    @Test
    @Transactional
    public void getCattle() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get the cattle
        restCattleMockMvc.perform(get("/api/cattles/{id}", cattleEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cattleEntity.getId().intValue()))
            .andExpect(jsonPath("$.earTagId").value(DEFAULT_EAR_TAG_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()))
            .andExpect(jsonPath("$.upForSale").value(DEFAULT_UP_FOR_SALE.booleanValue()))
            .andExpect(jsonPath("$.showBlup").value(DEFAULT_SHOW_BLUP.booleanValue()))
            .andExpect(jsonPath("$.alert").value(DEFAULT_ALERT.booleanValue()))
            .andExpect(jsonPath("$.storyHandle").value(DEFAULT_STORY_HANDLE));
    }


    @Test
    @Transactional
    public void getCattlesByIdFiltering() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        Long id = cattleEntity.getId();

        defaultCattleShouldBeFound("id.equals=" + id);
        defaultCattleShouldNotBeFound("id.notEquals=" + id);

        defaultCattleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCattleShouldNotBeFound("id.greaterThan=" + id);

        defaultCattleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCattleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId equals to DEFAULT_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.equals=" + DEFAULT_EAR_TAG_ID);

        // Get all the cattleList where earTagId equals to UPDATED_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.equals=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId not equals to DEFAULT_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.notEquals=" + DEFAULT_EAR_TAG_ID);

        // Get all the cattleList where earTagId not equals to UPDATED_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.notEquals=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId in DEFAULT_EAR_TAG_ID or UPDATED_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.in=" + DEFAULT_EAR_TAG_ID + "," + UPDATED_EAR_TAG_ID);

        // Get all the cattleList where earTagId equals to UPDATED_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.in=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId is not null
        defaultCattleShouldBeFound("earTagId.specified=true");

        // Get all the cattleList where earTagId is null
        defaultCattleShouldNotBeFound("earTagId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId is greater than or equal to DEFAULT_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.greaterThanOrEqual=" + DEFAULT_EAR_TAG_ID);

        // Get all the cattleList where earTagId is greater than or equal to UPDATED_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.greaterThanOrEqual=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId is less than or equal to DEFAULT_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.lessThanOrEqual=" + DEFAULT_EAR_TAG_ID);

        // Get all the cattleList where earTagId is less than or equal to SMALLER_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.lessThanOrEqual=" + SMALLER_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId is less than DEFAULT_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.lessThan=" + DEFAULT_EAR_TAG_ID);

        // Get all the cattleList where earTagId is less than UPDATED_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.lessThan=" + UPDATED_EAR_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllCattlesByEarTagIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where earTagId is greater than DEFAULT_EAR_TAG_ID
        defaultCattleShouldNotBeFound("earTagId.greaterThan=" + DEFAULT_EAR_TAG_ID);

        // Get all the cattleList where earTagId is greater than SMALLER_EAR_TAG_ID
        defaultCattleShouldBeFound("earTagId.greaterThan=" + SMALLER_EAR_TAG_ID);
    }


    @Test
    @Transactional
    public void getAllCattlesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where name equals to DEFAULT_NAME
        defaultCattleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cattleList where name equals to UPDATED_NAME
        defaultCattleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCattlesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where name not equals to DEFAULT_NAME
        defaultCattleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the cattleList where name not equals to UPDATED_NAME
        defaultCattleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCattlesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCattleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cattleList where name equals to UPDATED_NAME
        defaultCattleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCattlesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where name is not null
        defaultCattleShouldBeFound("name.specified=true");

        // Get all the cattleList where name is null
        defaultCattleShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCattlesByNameContainsSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where name contains DEFAULT_NAME
        defaultCattleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cattleList where name contains UPDATED_NAME
        defaultCattleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCattlesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where name does not contain DEFAULT_NAME
        defaultCattleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cattleList where name does not contain UPDATED_NAME
        defaultCattleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCattlesByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where visibility equals to DEFAULT_VISIBILITY
        defaultCattleShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the cattleList where visibility equals to UPDATED_VISIBILITY
        defaultCattleShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllCattlesByVisibilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where visibility not equals to DEFAULT_VISIBILITY
        defaultCattleShouldNotBeFound("visibility.notEquals=" + DEFAULT_VISIBILITY);

        // Get all the cattleList where visibility not equals to UPDATED_VISIBILITY
        defaultCattleShouldBeFound("visibility.notEquals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllCattlesByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultCattleShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the cattleList where visibility equals to UPDATED_VISIBILITY
        defaultCattleShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllCattlesByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where visibility is not null
        defaultCattleShouldBeFound("visibility.specified=true");

        // Get all the cattleList where visibility is null
        defaultCattleShouldNotBeFound("visibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllCattlesByUpForSaleIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where upForSale equals to DEFAULT_UP_FOR_SALE
        defaultCattleShouldBeFound("upForSale.equals=" + DEFAULT_UP_FOR_SALE);

        // Get all the cattleList where upForSale equals to UPDATED_UP_FOR_SALE
        defaultCattleShouldNotBeFound("upForSale.equals=" + UPDATED_UP_FOR_SALE);
    }

    @Test
    @Transactional
    public void getAllCattlesByUpForSaleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where upForSale not equals to DEFAULT_UP_FOR_SALE
        defaultCattleShouldNotBeFound("upForSale.notEquals=" + DEFAULT_UP_FOR_SALE);

        // Get all the cattleList where upForSale not equals to UPDATED_UP_FOR_SALE
        defaultCattleShouldBeFound("upForSale.notEquals=" + UPDATED_UP_FOR_SALE);
    }

    @Test
    @Transactional
    public void getAllCattlesByUpForSaleIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where upForSale in DEFAULT_UP_FOR_SALE or UPDATED_UP_FOR_SALE
        defaultCattleShouldBeFound("upForSale.in=" + DEFAULT_UP_FOR_SALE + "," + UPDATED_UP_FOR_SALE);

        // Get all the cattleList where upForSale equals to UPDATED_UP_FOR_SALE
        defaultCattleShouldNotBeFound("upForSale.in=" + UPDATED_UP_FOR_SALE);
    }

    @Test
    @Transactional
    public void getAllCattlesByUpForSaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where upForSale is not null
        defaultCattleShouldBeFound("upForSale.specified=true");

        // Get all the cattleList where upForSale is null
        defaultCattleShouldNotBeFound("upForSale.specified=false");
    }

    @Test
    @Transactional
    public void getAllCattlesByShowBlupIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where showBlup equals to DEFAULT_SHOW_BLUP
        defaultCattleShouldBeFound("showBlup.equals=" + DEFAULT_SHOW_BLUP);

        // Get all the cattleList where showBlup equals to UPDATED_SHOW_BLUP
        defaultCattleShouldNotBeFound("showBlup.equals=" + UPDATED_SHOW_BLUP);
    }

    @Test
    @Transactional
    public void getAllCattlesByShowBlupIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where showBlup not equals to DEFAULT_SHOW_BLUP
        defaultCattleShouldNotBeFound("showBlup.notEquals=" + DEFAULT_SHOW_BLUP);

        // Get all the cattleList where showBlup not equals to UPDATED_SHOW_BLUP
        defaultCattleShouldBeFound("showBlup.notEquals=" + UPDATED_SHOW_BLUP);
    }

    @Test
    @Transactional
    public void getAllCattlesByShowBlupIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where showBlup in DEFAULT_SHOW_BLUP or UPDATED_SHOW_BLUP
        defaultCattleShouldBeFound("showBlup.in=" + DEFAULT_SHOW_BLUP + "," + UPDATED_SHOW_BLUP);

        // Get all the cattleList where showBlup equals to UPDATED_SHOW_BLUP
        defaultCattleShouldNotBeFound("showBlup.in=" + UPDATED_SHOW_BLUP);
    }

    @Test
    @Transactional
    public void getAllCattlesByShowBlupIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where showBlup is not null
        defaultCattleShouldBeFound("showBlup.specified=true");

        // Get all the cattleList where showBlup is null
        defaultCattleShouldNotBeFound("showBlup.specified=false");
    }

    @Test
    @Transactional
    public void getAllCattlesByAlertIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where alert equals to DEFAULT_ALERT
        defaultCattleShouldBeFound("alert.equals=" + DEFAULT_ALERT);

        // Get all the cattleList where alert equals to UPDATED_ALERT
        defaultCattleShouldNotBeFound("alert.equals=" + UPDATED_ALERT);
    }

    @Test
    @Transactional
    public void getAllCattlesByAlertIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where alert not equals to DEFAULT_ALERT
        defaultCattleShouldNotBeFound("alert.notEquals=" + DEFAULT_ALERT);

        // Get all the cattleList where alert not equals to UPDATED_ALERT
        defaultCattleShouldBeFound("alert.notEquals=" + UPDATED_ALERT);
    }

    @Test
    @Transactional
    public void getAllCattlesByAlertIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where alert in DEFAULT_ALERT or UPDATED_ALERT
        defaultCattleShouldBeFound("alert.in=" + DEFAULT_ALERT + "," + UPDATED_ALERT);

        // Get all the cattleList where alert equals to UPDATED_ALERT
        defaultCattleShouldNotBeFound("alert.in=" + UPDATED_ALERT);
    }

    @Test
    @Transactional
    public void getAllCattlesByAlertIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where alert is not null
        defaultCattleShouldBeFound("alert.specified=true");

        // Get all the cattleList where alert is null
        defaultCattleShouldNotBeFound("alert.specified=false");
    }

    @Test
    @Transactional
    public void getAllCattlesByStoryHandleIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where storyHandle equals to DEFAULT_STORY_HANDLE
        defaultCattleShouldBeFound("storyHandle.equals=" + DEFAULT_STORY_HANDLE);

        // Get all the cattleList where storyHandle equals to UPDATED_STORY_HANDLE
        defaultCattleShouldNotBeFound("storyHandle.equals=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllCattlesByStoryHandleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where storyHandle not equals to DEFAULT_STORY_HANDLE
        defaultCattleShouldNotBeFound("storyHandle.notEquals=" + DEFAULT_STORY_HANDLE);

        // Get all the cattleList where storyHandle not equals to UPDATED_STORY_HANDLE
        defaultCattleShouldBeFound("storyHandle.notEquals=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllCattlesByStoryHandleIsInShouldWork() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where storyHandle in DEFAULT_STORY_HANDLE or UPDATED_STORY_HANDLE
        defaultCattleShouldBeFound("storyHandle.in=" + DEFAULT_STORY_HANDLE + "," + UPDATED_STORY_HANDLE);

        // Get all the cattleList where storyHandle equals to UPDATED_STORY_HANDLE
        defaultCattleShouldNotBeFound("storyHandle.in=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllCattlesByStoryHandleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where storyHandle is not null
        defaultCattleShouldBeFound("storyHandle.specified=true");

        // Get all the cattleList where storyHandle is null
        defaultCattleShouldNotBeFound("storyHandle.specified=false");
    }
                @Test
    @Transactional
    public void getAllCattlesByStoryHandleContainsSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where storyHandle contains DEFAULT_STORY_HANDLE
        defaultCattleShouldBeFound("storyHandle.contains=" + DEFAULT_STORY_HANDLE);

        // Get all the cattleList where storyHandle contains UPDATED_STORY_HANDLE
        defaultCattleShouldNotBeFound("storyHandle.contains=" + UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void getAllCattlesByStoryHandleNotContainsSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);

        // Get all the cattleList where storyHandle does not contain DEFAULT_STORY_HANDLE
        defaultCattleShouldNotBeFound("storyHandle.doesNotContain=" + DEFAULT_STORY_HANDLE);

        // Get all the cattleList where storyHandle does not contain UPDATED_STORY_HANDLE
        defaultCattleShouldBeFound("storyHandle.doesNotContain=" + UPDATED_STORY_HANDLE);
    }


    @Test
    @Transactional
    public void getAllCattlesByPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);
        PhotoEntity photo = PhotoResourceIT.createEntity(em);
        em.persist(photo);
        em.flush();
        cattleEntity.addPhoto(photo);
        cattleRepository.saveAndFlush(cattleEntity);
        Long photoId = photo.getId();

        // Get all the cattleList where photo equals to photoId
        defaultCattleShouldBeFound("photoId.equals=" + photoId);

        // Get all the cattleList where photo equals to photoId + 1
        defaultCattleShouldNotBeFound("photoId.equals=" + (photoId + 1));
    }


    @Test
    @Transactional
    public void getAllCattlesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);
        NoteEntity note = NoteResourceIT.createEntity(em);
        em.persist(note);
        em.flush();
        cattleEntity.addNote(note);
        cattleRepository.saveAndFlush(cattleEntity);
        Long noteId = note.getId();

        // Get all the cattleList where note equals to noteId
        defaultCattleShouldBeFound("noteId.equals=" + noteId);

        // Get all the cattleList where note equals to noteId + 1
        defaultCattleShouldNotBeFound("noteId.equals=" + (noteId + 1));
    }


    @Test
    @Transactional
    public void getAllCattlesByMatrilinealityIsEqualToSomething() throws Exception {
        // Initialize the database
        cattleRepository.saveAndFlush(cattleEntity);
        MatrilinealityEntity matrilineality = MatrilinealityResourceIT.createEntity(em);
        em.persist(matrilineality);
        em.flush();
        cattleEntity.setMatrilineality(matrilineality);
        cattleRepository.saveAndFlush(cattleEntity);
        Long matrilinealityId = matrilineality.getId();

        // Get all the cattleList where matrilineality equals to matrilinealityId
        defaultCattleShouldBeFound("matrilinealityId.equals=" + matrilinealityId);

        // Get all the cattleList where matrilineality equals to matrilinealityId + 1
        defaultCattleShouldNotBeFound("matrilinealityId.equals=" + (matrilinealityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCattleShouldBeFound(String filter) throws Exception {
        restCattleMockMvc.perform(get("/api/cattles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cattleEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].earTagId").value(hasItem(DEFAULT_EAR_TAG_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())))
            .andExpect(jsonPath("$.[*].upForSale").value(hasItem(DEFAULT_UP_FOR_SALE.booleanValue())))
            .andExpect(jsonPath("$.[*].showBlup").value(hasItem(DEFAULT_SHOW_BLUP.booleanValue())))
            .andExpect(jsonPath("$.[*].alert").value(hasItem(DEFAULT_ALERT.booleanValue())))
            .andExpect(jsonPath("$.[*].storyHandle").value(hasItem(DEFAULT_STORY_HANDLE)));

        // Check, that the count call also returns 1
        restCattleMockMvc.perform(get("/api/cattles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCattleShouldNotBeFound(String filter) throws Exception {
        restCattleMockMvc.perform(get("/api/cattles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCattleMockMvc.perform(get("/api/cattles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCattle() throws Exception {
        // Get the cattle
        restCattleMockMvc.perform(get("/api/cattles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCattle() throws Exception {
        // Initialize the database
        cattleService.save(cattleEntity);

        int databaseSizeBeforeUpdate = cattleRepository.findAll().size();

        // Update the cattle
        CattleEntity updatedCattleEntity = cattleRepository.findById(cattleEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCattleEntity are not directly saved in db
        em.detach(updatedCattleEntity);
        updatedCattleEntity
            .earTagId(UPDATED_EAR_TAG_ID)
            .name(UPDATED_NAME)
            .visibility(UPDATED_VISIBILITY)
            .upForSale(UPDATED_UP_FOR_SALE)
            .showBlup(UPDATED_SHOW_BLUP)
            .alert(UPDATED_ALERT)
            .storyHandle(UPDATED_STORY_HANDLE);

        restCattleMockMvc.perform(put("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCattleEntity)))
            .andExpect(status().isOk());

        // Validate the Cattle in the database
        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeUpdate);
        CattleEntity testCattle = cattleList.get(cattleList.size() - 1);
        assertThat(testCattle.getEarTagId()).isEqualTo(UPDATED_EAR_TAG_ID);
        assertThat(testCattle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCattle.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
        assertThat(testCattle.isUpForSale()).isEqualTo(UPDATED_UP_FOR_SALE);
        assertThat(testCattle.isShowBlup()).isEqualTo(UPDATED_SHOW_BLUP);
        assertThat(testCattle.isAlert()).isEqualTo(UPDATED_ALERT);
        assertThat(testCattle.getStoryHandle()).isEqualTo(UPDATED_STORY_HANDLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCattle() throws Exception {
        int databaseSizeBeforeUpdate = cattleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCattleMockMvc.perform(put("/api/cattles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cattleEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Cattle in the database
        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCattle() throws Exception {
        // Initialize the database
        cattleService.save(cattleEntity);

        int databaseSizeBeforeDelete = cattleRepository.findAll().size();

        // Delete the cattle
        restCattleMockMvc.perform(delete("/api/cattles/{id}", cattleEntity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CattleEntity> cattleList = cattleRepository.findAll();
        assertThat(cattleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
