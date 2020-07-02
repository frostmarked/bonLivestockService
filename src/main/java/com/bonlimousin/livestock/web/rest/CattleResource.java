package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.service.CattleService;
import com.bonlimousin.livestock.web.rest.errors.BadRequestAlertException;
import com.bonlimousin.livestock.service.dto.CattleCriteria;
import com.bonlimousin.livestock.service.CattleQueryService;

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
 * REST controller for managing {@link com.bonlimousin.livestock.domain.CattleEntity}.
 */
@RestController
@RequestMapping("/api")
public class CattleResource {

    private final Logger log = LoggerFactory.getLogger(CattleResource.class);

    private static final String ENTITY_NAME = "bonLivestockServiceCattle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CattleService cattleService;

    private final CattleQueryService cattleQueryService;

    public CattleResource(CattleService cattleService, CattleQueryService cattleQueryService) {
        this.cattleService = cattleService;
        this.cattleQueryService = cattleQueryService;
    }

    /**
     * {@code POST  /cattles} : Create a new cattle.
     *
     * @param cattleEntity the cattleEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cattleEntity, or with status {@code 400 (Bad Request)} if the cattle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cattles")
    public ResponseEntity<CattleEntity> createCattle(@Valid @RequestBody CattleEntity cattleEntity) throws URISyntaxException {
        log.debug("REST request to save Cattle : {}", cattleEntity);
        if (cattleEntity.getId() != null) {
            throw new BadRequestAlertException("A new cattle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CattleEntity result = cattleService.save(cattleEntity);
        return ResponseEntity.created(new URI("/api/cattles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cattles} : Updates an existing cattle.
     *
     * @param cattleEntity the cattleEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cattleEntity,
     * or with status {@code 400 (Bad Request)} if the cattleEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cattleEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cattles")
    public ResponseEntity<CattleEntity> updateCattle(@Valid @RequestBody CattleEntity cattleEntity) throws URISyntaxException {
        log.debug("REST request to update Cattle : {}", cattleEntity);
        if (cattleEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CattleEntity result = cattleService.save(cattleEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cattleEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cattles} : get all the cattles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cattles in body.
     */
    @GetMapping("/cattles")
    public ResponseEntity<List<CattleEntity>> getAllCattles(CattleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cattles by criteria: {}", criteria);
        Page<CattleEntity> page = cattleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cattles/count} : count all the cattles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cattles/count")
    public ResponseEntity<Long> countCattles(CattleCriteria criteria) {
        log.debug("REST request to count Cattles by criteria: {}", criteria);
        return ResponseEntity.ok().body(cattleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cattles/:id} : get the "id" cattle.
     *
     * @param id the id of the cattleEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cattleEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cattles/{id}")
    public ResponseEntity<CattleEntity> getCattle(@PathVariable Long id) {
        log.debug("REST request to get Cattle : {}", id);
        Optional<CattleEntity> cattleEntity = cattleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cattleEntity);
    }

    /**
     * {@code DELETE  /cattles/:id} : delete the "id" cattle.
     *
     * @param id the id of the cattleEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cattles/{id}")
    public ResponseEntity<Void> deleteCattle(@PathVariable Long id) {
        log.debug("REST request to delete Cattle : {}", id);
        cattleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
