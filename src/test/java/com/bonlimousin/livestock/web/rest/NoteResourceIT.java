package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.NoteEntity;
import com.bonlimousin.livestock.domain.PastureEntity;
import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.repository.NoteRepository;
import com.bonlimousin.livestock.service.NoteService;
import com.bonlimousin.livestock.service.dto.NoteCriteria;
import com.bonlimousin.livestock.service.NoteQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bonlimousin.livestock.domain.enumeration.NoteCategory;
/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@SpringBootTest(classes = BonLivestockServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NoteResourceIT {

    private static final NoteCategory DEFAULT_CATEGORY = NoteCategory.GENERAL;
    private static final NoteCategory UPDATED_CATEGORY = NoteCategory.TREATMENT;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACTUAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteQueryService noteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteMockMvc;

    private NoteEntity noteEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteEntity createEntity(EntityManager em) {
        NoteEntity noteEntity = new NoteEntity()
            .category(DEFAULT_CATEGORY)
            .note(DEFAULT_NOTE)
            .actualDate(DEFAULT_ACTUAL_DATE);
        // Add required entity
        CattleEntity cattle;
        if (TestUtil.findAll(em, Cattle.class).isEmpty()) {
            cattle = CattleResourceIT.createEntity(em);
            em.persist(cattle);
            em.flush();
        } else {
            cattle = TestUtil.findAll(em, Cattle.class).get(0);
        }
        noteEntity.setCattle(cattle);
        return noteEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteEntity createUpdatedEntity(EntityManager em) {
        NoteEntity noteEntity = new NoteEntity()
            .category(UPDATED_CATEGORY)
            .note(UPDATED_NOTE)
            .actualDate(UPDATED_ACTUAL_DATE);
        // Add required entity
        CattleEntity cattle;
        if (TestUtil.findAll(em, Cattle.class).isEmpty()) {
            cattle = CattleResourceIT.createUpdatedEntity(em);
            em.persist(cattle);
            em.flush();
        } else {
            cattle = TestUtil.findAll(em, Cattle.class).get(0);
        }
        noteEntity.setCattle(cattle);
        return noteEntity;
    }

    @BeforeEach
    public void initTest() {
        noteEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();
        // Create the Note
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteEntity)))
            .andExpect(status().isCreated());

        // Validate the Note in the database
        List<NoteEntity> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        NoteEntity testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testNote.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testNote.getActualDate()).isEqualTo(DEFAULT_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void createNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note with an existing ID
        noteEntity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<NoteEntity> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteRepository.findAll().size();
        // set the field null
        noteEntity.setCategory(null);

        // Create the Note, which fails.


        restNoteMockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteEntity)))
            .andExpect(status().isBadRequest());

        List<NoteEntity> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(noteEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].actualDate").value(hasItem(DEFAULT_ACTUAL_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", noteEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(noteEntity.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.actualDate").value(DEFAULT_ACTUAL_DATE.toString()));
    }


    @Test
    @Transactional
    public void getNotesByIdFiltering() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        Long id = noteEntity.getId();

        defaultNoteShouldBeFound("id.equals=" + id);
        defaultNoteShouldNotBeFound("id.notEquals=" + id);

        defaultNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where category equals to DEFAULT_CATEGORY
        defaultNoteShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the noteList where category equals to UPDATED_CATEGORY
        defaultNoteShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllNotesByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where category not equals to DEFAULT_CATEGORY
        defaultNoteShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the noteList where category not equals to UPDATED_CATEGORY
        defaultNoteShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllNotesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultNoteShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the noteList where category equals to UPDATED_CATEGORY
        defaultNoteShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllNotesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where category is not null
        defaultNoteShouldBeFound("category.specified=true");

        // Get all the noteList where category is null
        defaultNoteShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where note equals to DEFAULT_NOTE
        defaultNoteShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the noteList where note equals to UPDATED_NOTE
        defaultNoteShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllNotesByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where note not equals to DEFAULT_NOTE
        defaultNoteShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the noteList where note not equals to UPDATED_NOTE
        defaultNoteShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllNotesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultNoteShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the noteList where note equals to UPDATED_NOTE
        defaultNoteShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllNotesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where note is not null
        defaultNoteShouldBeFound("note.specified=true");

        // Get all the noteList where note is null
        defaultNoteShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotesByNoteContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where note contains DEFAULT_NOTE
        defaultNoteShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the noteList where note contains UPDATED_NOTE
        defaultNoteShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllNotesByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where note does not contain DEFAULT_NOTE
        defaultNoteShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the noteList where note does not contain UPDATED_NOTE
        defaultNoteShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllNotesByActualDateIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate equals to DEFAULT_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.equals=" + DEFAULT_ACTUAL_DATE);

        // Get all the noteList where actualDate equals to UPDATED_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.equals=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate not equals to DEFAULT_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.notEquals=" + DEFAULT_ACTUAL_DATE);

        // Get all the noteList where actualDate not equals to UPDATED_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.notEquals=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate in DEFAULT_ACTUAL_DATE or UPDATED_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.in=" + DEFAULT_ACTUAL_DATE + "," + UPDATED_ACTUAL_DATE);

        // Get all the noteList where actualDate equals to UPDATED_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.in=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate is not null
        defaultNoteShouldBeFound("actualDate.specified=true");

        // Get all the noteList where actualDate is null
        defaultNoteShouldNotBeFound("actualDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate is greater than or equal to DEFAULT_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_DATE);

        // Get all the noteList where actualDate is greater than or equal to UPDATED_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.greaterThanOrEqual=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate is less than or equal to DEFAULT_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.lessThanOrEqual=" + DEFAULT_ACTUAL_DATE);

        // Get all the noteList where actualDate is less than or equal to SMALLER_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.lessThanOrEqual=" + SMALLER_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsLessThanSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate is less than DEFAULT_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.lessThan=" + DEFAULT_ACTUAL_DATE);

        // Get all the noteList where actualDate is less than UPDATED_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.lessThan=" + UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void getAllNotesByActualDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);

        // Get all the noteList where actualDate is greater than DEFAULT_ACTUAL_DATE
        defaultNoteShouldNotBeFound("actualDate.greaterThan=" + DEFAULT_ACTUAL_DATE);

        // Get all the noteList where actualDate is greater than SMALLER_ACTUAL_DATE
        defaultNoteShouldBeFound("actualDate.greaterThan=" + SMALLER_ACTUAL_DATE);
    }


    @Test
    @Transactional
    public void getAllNotesByPastureIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(noteEntity);
        PastureEntity pasture = PastureResourceIT.createEntity(em);
        em.persist(pasture);
        em.flush();
        noteEntity.setPasture(pasture);
        noteRepository.saveAndFlush(noteEntity);
        Long pastureId = pasture.getId();

        // Get all the noteList where pasture equals to pastureId
        defaultNoteShouldBeFound("pastureId.equals=" + pastureId);

        // Get all the noteList where pasture equals to pastureId + 1
        defaultNoteShouldNotBeFound("pastureId.equals=" + (pastureId + 1));
    }


    @Test
    @Transactional
    public void getAllNotesByCattleIsEqualToSomething() throws Exception {
        // Get already existing entity
        CattleEntity cattle = noteEntity.getCattle();
        noteRepository.saveAndFlush(noteEntity);
        Long cattleId = cattle.getId();

        // Get all the noteList where cattle equals to cattleId
        defaultNoteShouldBeFound("cattleId.equals=" + cattleId);

        // Get all the noteList where cattle equals to cattleId + 1
        defaultNoteShouldNotBeFound("cattleId.equals=" + (cattleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNoteShouldBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(noteEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].actualDate").value(hasItem(DEFAULT_ACTUAL_DATE.toString())));

        // Check, that the count call also returns 1
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNoteShouldNotBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNoteMockMvc.perform(get("/api/notes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNote() throws Exception {
        // Initialize the database
        noteService.save(noteEntity);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        NoteEntity updatedNoteEntity = noteRepository.findById(noteEntity.getId()).get();
        // Disconnect from session so that the updates on updatedNoteEntity are not directly saved in db
        em.detach(updatedNoteEntity);
        updatedNoteEntity
            .category(UPDATED_CATEGORY)
            .note(UPDATED_NOTE)
            .actualDate(UPDATED_ACTUAL_DATE);

        restNoteMockMvc.perform(put("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNoteEntity)))
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<NoteEntity> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        NoteEntity testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testNote.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testNote.getActualDate()).isEqualTo(UPDATED_ACTUAL_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc.perform(put("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<NoteEntity> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNote() throws Exception {
        // Initialize the database
        noteService.save(noteEntity);

        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Delete the note
        restNoteMockMvc.perform(delete("/api/notes/{id}", noteEntity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NoteEntity> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
