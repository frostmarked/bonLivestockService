package com.bonlimousin.livestock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.domain.PhotoEntity;
import com.bonlimousin.livestock.domain.enumeration.UserRole;
import com.bonlimousin.livestock.repository.PhotoRepository;
import com.bonlimousin.livestock.service.PhotoQueryService;
import com.bonlimousin.livestock.service.PhotoService;
/**
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@SpringBootTest(classes = BonLivestockServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PhotoResourceIT {

    // 2x2
    private static final byte[] DEFAULT_IMAGE = Base64Utils.decodeFromString(
        "iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAEklEQVR42mP8z/C/ngEIGGEMADdsBP3lTYQdAAAAAElFTkSuQmCC");
    // 4x4
    private static final byte[] UPDATED_IMAGE = Base64Utils.decodeFromString(
        "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAE0lEQVR42mP8z/C/ngEJMJIuAACQHwn5Nb9RUAAAAABJRU5ErkJggg==");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg"; // its ok
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEIGHT = 2;
    private static final Integer UPDATED_HEIGHT = 4;
    private static final Integer SMALLER_HEIGHT = 2 - 1;

    private static final Integer DEFAULT_WIDTH = 2;
    private static final Integer UPDATED_WIDTH = 4;
    private static final Integer SMALLER_WIDTH = 2 - 1;

    private static final Instant DEFAULT_TAKEN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TAKEN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UserRole DEFAULT_VISIBILITY = UserRole.ROLE_ADMIN;
    private static final UserRole UPDATED_VISIBILITY = UserRole.ROLE_USER;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoQueryService photoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private PhotoEntity photoEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhotoEntity createEntity(EntityManager em) {
        PhotoEntity photoEntity = new PhotoEntity()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .caption(DEFAULT_CAPTION)
            .height(DEFAULT_HEIGHT)
            .width(DEFAULT_WIDTH)
            .taken(DEFAULT_TAKEN)
            .visibility(DEFAULT_VISIBILITY);
        // Add required entity
        CattleEntity cattle;
        if (TestUtil.findAll(em, CattleEntity.class).isEmpty()) {
            cattle = CattleResourceIT.createEntity(em);
            em.persist(cattle);
            em.flush();
        } else {
            cattle = TestUtil.findAll(em, CattleEntity.class).get(0);
        }
        photoEntity.setCattle(cattle);
        return photoEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhotoEntity createUpdatedEntity(EntityManager em) {
        PhotoEntity photoEntity = new PhotoEntity()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .caption(UPDATED_CAPTION)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .taken(UPDATED_TAKEN)
            .visibility(UPDATED_VISIBILITY);
        // Add required entity
        CattleEntity cattle;
        if (TestUtil.findAll(em, CattleEntity.class).isEmpty()) {
            cattle = CattleResourceIT.createUpdatedEntity(em);
            em.persist(cattle);
            em.flush();
        } else {
            cattle = TestUtil.findAll(em, CattleEntity.class).get(0);
        }
        photoEntity.setCattle(cattle);
        return photoEntity;
    }

    @BeforeEach
    public void initTest() {
        photoEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();
        // Create the Photo
        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(photoEntity)))
            .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<PhotoEntity> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate + 1);
        PhotoEntity testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPhoto.getCaption()).isEqualTo(DEFAULT_CAPTION);
        assertThat(testPhoto.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPhoto.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPhoto.getTaken()).isEqualTo(DEFAULT_TAKEN);
        assertThat(testPhoto.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    public void createPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // Create the Photo with an existing ID
        photoEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(photoEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<PhotoEntity> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCaptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photoEntity.setCaption(null);

        // Create the Photo, which fails.


        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(photoEntity)))
            .andExpect(status().isBadRequest());

        List<PhotoEntity> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photoEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            // dummy image on list
            .andExpect(jsonPath("$.[*].image").value(hasItem(PhotoQueryService.DEFAULT_LIST_IMAGE_BASE64)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].taken").value(hasItem(DEFAULT_TAKEN.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    @Test
    @Transactional
    public void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", photoEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(photoEntity.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.taken").value(DEFAULT_TAKEN.toString()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }


    @Test
    @Transactional
    public void getPhotosByIdFiltering() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        Long id = photoEntity.getId();

        defaultPhotoShouldBeFound("id.equals=" + id);
        defaultPhotoShouldNotBeFound("id.notEquals=" + id);

        defaultPhotoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.greaterThan=" + id);

        defaultPhotoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPhotosByCaptionIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where caption equals to DEFAULT_CAPTION
        defaultPhotoShouldBeFound("caption.equals=" + DEFAULT_CAPTION);

        // Get all the photoList where caption equals to UPDATED_CAPTION
        defaultPhotoShouldNotBeFound("caption.equals=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    public void getAllPhotosByCaptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where caption not equals to DEFAULT_CAPTION
        defaultPhotoShouldNotBeFound("caption.notEquals=" + DEFAULT_CAPTION);

        // Get all the photoList where caption not equals to UPDATED_CAPTION
        defaultPhotoShouldBeFound("caption.notEquals=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    public void getAllPhotosByCaptionIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where caption in DEFAULT_CAPTION or UPDATED_CAPTION
        defaultPhotoShouldBeFound("caption.in=" + DEFAULT_CAPTION + "," + UPDATED_CAPTION);

        // Get all the photoList where caption equals to UPDATED_CAPTION
        defaultPhotoShouldNotBeFound("caption.in=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    public void getAllPhotosByCaptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where caption is not null
        defaultPhotoShouldBeFound("caption.specified=true");

        // Get all the photoList where caption is null
        defaultPhotoShouldNotBeFound("caption.specified=false");
    }
                @Test
    @Transactional
    public void getAllPhotosByCaptionContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where caption contains DEFAULT_CAPTION
        defaultPhotoShouldBeFound("caption.contains=" + DEFAULT_CAPTION);

        // Get all the photoList where caption contains UPDATED_CAPTION
        defaultPhotoShouldNotBeFound("caption.contains=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    public void getAllPhotosByCaptionNotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where caption does not contain DEFAULT_CAPTION
        defaultPhotoShouldNotBeFound("caption.doesNotContain=" + DEFAULT_CAPTION);

        // Get all the photoList where caption does not contain UPDATED_CAPTION
        defaultPhotoShouldBeFound("caption.doesNotContain=" + UPDATED_CAPTION);
    }


    @Test
    @Transactional
    public void getAllPhotosByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height equals to DEFAULT_HEIGHT
        defaultPhotoShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the photoList where height equals to UPDATED_HEIGHT
        defaultPhotoShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height not equals to DEFAULT_HEIGHT
        defaultPhotoShouldNotBeFound("height.notEquals=" + DEFAULT_HEIGHT);

        // Get all the photoList where height not equals to UPDATED_HEIGHT
        defaultPhotoShouldBeFound("height.notEquals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultPhotoShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the photoList where height equals to UPDATED_HEIGHT
        defaultPhotoShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height is not null
        defaultPhotoShouldBeFound("height.specified=true");

        // Get all the photoList where height is null
        defaultPhotoShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height is greater than or equal to DEFAULT_HEIGHT
        defaultPhotoShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is greater than or equal to UPDATED_HEIGHT
        defaultPhotoShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height is less than or equal to DEFAULT_HEIGHT
        defaultPhotoShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is less than or equal to SMALLER_HEIGHT
        defaultPhotoShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height is less than DEFAULT_HEIGHT
        defaultPhotoShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is less than UPDATED_HEIGHT
        defaultPhotoShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllPhotosByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where height is greater than DEFAULT_HEIGHT
        defaultPhotoShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the photoList where height is greater than SMALLER_HEIGHT
        defaultPhotoShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }


    @Test
    @Transactional
    public void getAllPhotosByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width equals to DEFAULT_WIDTH
        defaultPhotoShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the photoList where width equals to UPDATED_WIDTH
        defaultPhotoShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width not equals to DEFAULT_WIDTH
        defaultPhotoShouldNotBeFound("width.notEquals=" + DEFAULT_WIDTH);

        // Get all the photoList where width not equals to UPDATED_WIDTH
        defaultPhotoShouldBeFound("width.notEquals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultPhotoShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the photoList where width equals to UPDATED_WIDTH
        defaultPhotoShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width is not null
        defaultPhotoShouldBeFound("width.specified=true");

        // Get all the photoList where width is null
        defaultPhotoShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width is greater than or equal to DEFAULT_WIDTH
        defaultPhotoShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the photoList where width is greater than or equal to UPDATED_WIDTH
        defaultPhotoShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width is less than or equal to DEFAULT_WIDTH
        defaultPhotoShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the photoList where width is less than or equal to SMALLER_WIDTH
        defaultPhotoShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width is less than DEFAULT_WIDTH
        defaultPhotoShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the photoList where width is less than UPDATED_WIDTH
        defaultPhotoShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllPhotosByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where width is greater than DEFAULT_WIDTH
        defaultPhotoShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the photoList where width is greater than SMALLER_WIDTH
        defaultPhotoShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }


    @Test
    @Transactional
    public void getAllPhotosByTakenIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where taken equals to DEFAULT_TAKEN
        defaultPhotoShouldBeFound("taken.equals=" + DEFAULT_TAKEN);

        // Get all the photoList where taken equals to UPDATED_TAKEN
        defaultPhotoShouldNotBeFound("taken.equals=" + UPDATED_TAKEN);
    }

    @Test
    @Transactional
    public void getAllPhotosByTakenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where taken not equals to DEFAULT_TAKEN
        defaultPhotoShouldNotBeFound("taken.notEquals=" + DEFAULT_TAKEN);

        // Get all the photoList where taken not equals to UPDATED_TAKEN
        defaultPhotoShouldBeFound("taken.notEquals=" + UPDATED_TAKEN);
    }

    @Test
    @Transactional
    public void getAllPhotosByTakenIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where taken in DEFAULT_TAKEN or UPDATED_TAKEN
        defaultPhotoShouldBeFound("taken.in=" + DEFAULT_TAKEN + "," + UPDATED_TAKEN);

        // Get all the photoList where taken equals to UPDATED_TAKEN
        defaultPhotoShouldNotBeFound("taken.in=" + UPDATED_TAKEN);
    }

    @Test
    @Transactional
    public void getAllPhotosByTakenIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where taken is not null
        defaultPhotoShouldBeFound("taken.specified=true");

        // Get all the photoList where taken is null
        defaultPhotoShouldNotBeFound("taken.specified=false");
    }

    @Test
    @Transactional
    public void getAllPhotosByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where visibility equals to DEFAULT_VISIBILITY
        defaultPhotoShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the photoList where visibility equals to UPDATED_VISIBILITY
        defaultPhotoShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllPhotosByVisibilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where visibility not equals to DEFAULT_VISIBILITY
        defaultPhotoShouldNotBeFound("visibility.notEquals=" + DEFAULT_VISIBILITY);

        // Get all the photoList where visibility not equals to UPDATED_VISIBILITY
        defaultPhotoShouldBeFound("visibility.notEquals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllPhotosByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultPhotoShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the photoList where visibility equals to UPDATED_VISIBILITY
        defaultPhotoShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllPhotosByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photoEntity);

        // Get all the photoList where visibility is not null
        defaultPhotoShouldBeFound("visibility.specified=true");

        // Get all the photoList where visibility is null
        defaultPhotoShouldNotBeFound("visibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllPhotosByCattleIsEqualToSomething() throws Exception {
        // Get already existing entity
        CattleEntity cattle = photoEntity.getCattle();
        photoRepository.saveAndFlush(photoEntity);
        Long cattleId = cattle.getId();

        // Get all the photoList where cattle equals to cattleId
        defaultPhotoShouldBeFound("cattleId.equals=" + cattleId);

        // Get all the photoList where cattle equals to cattleId + 1
        defaultPhotoShouldNotBeFound("cattleId.equals=" + (cattleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhotoShouldBeFound(String filter) throws Exception {
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photoEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            // default list image
            .andExpect(jsonPath("$.[*].image").value(hasItem(PhotoQueryService.DEFAULT_LIST_IMAGE_BASE64)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].taken").value(hasItem(DEFAULT_TAKEN.toString())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));

        // Check, that the count call also returns 1
        restPhotoMockMvc.perform(get("/api/photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhotoShouldNotBeFound(String filter) throws Exception {
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhotoMockMvc.perform(get("/api/photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhoto() throws Exception {
        // Initialize the database
        photoService.save(photoEntity);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        PhotoEntity updatedPhotoEntity = photoRepository.findById(photoEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPhotoEntity are not directly saved in db
        em.detach(updatedPhotoEntity);
        updatedPhotoEntity
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .caption(UPDATED_CAPTION)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .taken(UPDATED_TAKEN)
            .visibility(UPDATED_VISIBILITY);

        restPhotoMockMvc.perform(put("/api/photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPhotoEntity)))
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<PhotoEntity> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        PhotoEntity testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPhoto.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testPhoto.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPhoto.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPhoto.getTaken()).isEqualTo(UPDATED_TAKEN);
        assertThat(testPhoto.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc.perform(put("/api/photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(photoEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<PhotoEntity> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePhoto() throws Exception {
        // Initialize the database
        photoService.save(photoEntity);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Delete the photo
        restPhotoMockMvc.perform(delete("/api/photos/{id}", photoEntity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhotoEntity> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
