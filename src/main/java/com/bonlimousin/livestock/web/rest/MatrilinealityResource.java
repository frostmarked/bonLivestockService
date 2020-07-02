package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.service.MatrilinealityService;
import com.bonlimousin.livestock.web.rest.errors.BadRequestAlertException;
import com.bonlimousin.livestock.service.dto.MatrilinealityCriteria;
import com.bonlimousin.livestock.service.MatrilinealityQueryService;

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
 * REST controller for managing {@link com.bonlimousin.livestock.domain.MatrilinealityEntity}.
 */
@RestController
@RequestMapping("/api")
public class MatrilinealityResource {

    private final Logger log = LoggerFactory.getLogger(MatrilinealityResource.class);

    private static final String ENTITY_NAME = "bonLivestockServiceMatrilineality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatrilinealityService matrilinealityService;

    private final MatrilinealityQueryService matrilinealityQueryService;

    public MatrilinealityResource(MatrilinealityService matrilinealityService, MatrilinealityQueryService matrilinealityQueryService) {
        this.matrilinealityService = matrilinealityService;
        this.matrilinealityQueryService = matrilinealityQueryService;
    }

    /**
     * {@code POST  /matrilinealities} : Create a new matrilineality.
     *
     * @param matrilinealityEntity the matrilinealityEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matrilinealityEntity, or with status {@code 400 (Bad Request)} if the matrilineality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/matrilinealities")
    public ResponseEntity<MatrilinealityEntity> createMatrilineality(@Valid @RequestBody MatrilinealityEntity matrilinealityEntity) throws URISyntaxException {
        log.debug("REST request to save Matrilineality : {}", matrilinealityEntity);
        if (matrilinealityEntity.getId() != null) {
            throw new BadRequestAlertException("A new matrilineality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatrilinealityEntity result = matrilinealityService.save(matrilinealityEntity);
        return ResponseEntity.created(new URI("/api/matrilinealities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /matrilinealities} : Updates an existing matrilineality.
     *
     * @param matrilinealityEntity the matrilinealityEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matrilinealityEntity,
     * or with status {@code 400 (Bad Request)} if the matrilinealityEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matrilinealityEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/matrilinealities")
    public ResponseEntity<MatrilinealityEntity> updateMatrilineality(@Valid @RequestBody MatrilinealityEntity matrilinealityEntity) throws URISyntaxException {
        log.debug("REST request to update Matrilineality : {}", matrilinealityEntity);
        if (matrilinealityEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MatrilinealityEntity result = matrilinealityService.save(matrilinealityEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matrilinealityEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /matrilinealities} : get all the matrilinealities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matrilinealities in body.
     */
    @GetMapping("/matrilinealities")
    public ResponseEntity<List<MatrilinealityEntity>> getAllMatrilinealities(MatrilinealityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Matrilinealities by criteria: {}", criteria);
        Page<MatrilinealityEntity> page = matrilinealityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matrilinealities/count} : count all the matrilinealities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/matrilinealities/count")
    public ResponseEntity<Long> countMatrilinealities(MatrilinealityCriteria criteria) {
        log.debug("REST request to count Matrilinealities by criteria: {}", criteria);
        return ResponseEntity.ok().body(matrilinealityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /matrilinealities/:id} : get the "id" matrilineality.
     *
     * @param id the id of the matrilinealityEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matrilinealityEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/matrilinealities/{id}")
    public ResponseEntity<MatrilinealityEntity> getMatrilineality(@PathVariable Long id) {
        log.debug("REST request to get Matrilineality : {}", id);
        Optional<MatrilinealityEntity> matrilinealityEntity = matrilinealityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matrilinealityEntity);
    }

    /**
     * {@code DELETE  /matrilinealities/:id} : delete the "id" matrilineality.
     *
     * @param id the id of the matrilinealityEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/matrilinealities/{id}")
    public ResponseEntity<Void> deleteMatrilineality(@PathVariable Long id) {
        log.debug("REST request to delete Matrilineality : {}", id);
        matrilinealityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
