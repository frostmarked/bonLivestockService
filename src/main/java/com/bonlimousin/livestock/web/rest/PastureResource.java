package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.domain.PastureEntity;
import com.bonlimousin.livestock.service.PastureService;
import com.bonlimousin.livestock.web.rest.errors.BadRequestAlertException;
import com.bonlimousin.livestock.service.dto.PastureCriteria;
import com.bonlimousin.livestock.service.PastureQueryService;

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
 * REST controller for managing {@link com.bonlimousin.livestock.domain.PastureEntity}.
 */
@RestController
@RequestMapping("/api")
public class PastureResource {

    private final Logger log = LoggerFactory.getLogger(PastureResource.class);

    private static final String ENTITY_NAME = "bonLivestockServicePasture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PastureService pastureService;

    private final PastureQueryService pastureQueryService;

    public PastureResource(PastureService pastureService, PastureQueryService pastureQueryService) {
        this.pastureService = pastureService;
        this.pastureQueryService = pastureQueryService;
    }

    /**
     * {@code POST  /pastures} : Create a new pasture.
     *
     * @param pastureEntity the pastureEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pastureEntity, or with status {@code 400 (Bad Request)} if the pasture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pastures")
    public ResponseEntity<PastureEntity> createPasture(@Valid @RequestBody PastureEntity pastureEntity) throws URISyntaxException {
        log.debug("REST request to save Pasture : {}", pastureEntity);
        if (pastureEntity.getId() != null) {
            throw new BadRequestAlertException("A new pasture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PastureEntity result = pastureService.save(pastureEntity);
        return ResponseEntity.created(new URI("/api/pastures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pastures} : Updates an existing pasture.
     *
     * @param pastureEntity the pastureEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pastureEntity,
     * or with status {@code 400 (Bad Request)} if the pastureEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pastureEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pastures")
    public ResponseEntity<PastureEntity> updatePasture(@Valid @RequestBody PastureEntity pastureEntity) throws URISyntaxException {
        log.debug("REST request to update Pasture : {}", pastureEntity);
        if (pastureEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PastureEntity result = pastureService.save(pastureEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pastureEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pastures} : get all the pastures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pastures in body.
     */
    @GetMapping("/pastures")
    public ResponseEntity<List<PastureEntity>> getAllPastures(PastureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pastures by criteria: {}", criteria);
        Page<PastureEntity> page = pastureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pastures/count} : count all the pastures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pastures/count")
    public ResponseEntity<Long> countPastures(PastureCriteria criteria) {
        log.debug("REST request to count Pastures by criteria: {}", criteria);
        return ResponseEntity.ok().body(pastureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pastures/:id} : get the "id" pasture.
     *
     * @param id the id of the pastureEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pastureEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pastures/{id}")
    public ResponseEntity<PastureEntity> getPasture(@PathVariable Long id) {
        log.debug("REST request to get Pasture : {}", id);
        Optional<PastureEntity> pastureEntity = pastureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pastureEntity);
    }

    /**
     * {@code DELETE  /pastures/:id} : delete the "id" pasture.
     *
     * @param id the id of the pastureEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pastures/{id}")
    public ResponseEntity<Void> deletePasture(@PathVariable Long id) {
        log.debug("REST request to delete Pasture : {}", id);
        pastureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
