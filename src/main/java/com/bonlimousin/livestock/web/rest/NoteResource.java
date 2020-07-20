package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.domain.NoteEntity;
import com.bonlimousin.livestock.service.NoteService;
import com.bonlimousin.livestock.web.rest.errors.BadRequestAlertException;
import com.bonlimousin.livestock.service.dto.NoteCriteria;
import com.bonlimousin.livestock.service.NoteQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bonlimousin.livestock.domain.NoteEntity}.
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "bonLivestockServiceNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteService noteService;

    private final NoteQueryService noteQueryService;

    public NoteResource(NoteService noteService, NoteQueryService noteQueryService) {
        this.noteService = noteService;
        this.noteQueryService = noteQueryService;
    }

    /**
     * {@code POST  /notes} : Create a new note.
     *
     * @param noteEntity the noteEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteEntity, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notes")
    public ResponseEntity<NoteEntity> createNote(@Valid @RequestBody NoteEntity noteEntity) throws URISyntaxException {
        log.debug("REST request to save Note : {}", noteEntity);
        if (noteEntity.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NoteEntity result = noteService.save(noteEntity);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notes} : Updates an existing note.
     *
     * @param noteEntity the noteEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteEntity,
     * or with status {@code 400 (Bad Request)} if the noteEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notes")
    public ResponseEntity<NoteEntity> updateNote(@Valid @RequestBody NoteEntity noteEntity) throws URISyntaxException {
        log.debug("REST request to update Note : {}", noteEntity);
        if (noteEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NoteEntity result = noteService.save(noteEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("/notes")
    public ResponseEntity<List<NoteEntity>> getAllNotes(NoteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Notes by criteria: {}", criteria);
        Page<NoteEntity> page = noteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notes/count} : count all the notes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notes/count")
    public ResponseEntity<Long> countNotes(NoteCriteria criteria) {
        log.debug("REST request to count Notes by criteria: {}", criteria);
        return ResponseEntity.ok().body(noteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param id the id of the noteEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteEntity> getNote(@PathVariable Long id) {
        log.debug("REST request to get Note : {}", id);
        Optional<NoteEntity> noteEntity = noteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noteEntity);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param id the id of the noteEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
