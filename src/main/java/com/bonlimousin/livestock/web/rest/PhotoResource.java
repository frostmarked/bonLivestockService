package com.bonlimousin.livestock.web.rest;

import com.bonlimousin.livestock.domain.PhotoEntity;
import com.bonlimousin.livestock.service.PhotoService;
import com.bonlimousin.livestock.web.rest.errors.BadRequestAlertException;
import com.bonlimousin.livestock.service.dto.PhotoCriteria;
import com.bonlimousin.livestock.service.PhotoQueryService;

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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bonlimousin.livestock.domain.PhotoEntity}.
 */
@RestController
@RequestMapping("/api")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    private static final String ENTITY_NAME = "bonLivestockServicePhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhotoService photoService;

    private final PhotoQueryService photoQueryService;

    public PhotoResource(PhotoService photoService, PhotoQueryService photoQueryService) {
        this.photoService = photoService;
        this.photoQueryService = photoQueryService;
    }

    /**
     * {@code POST  /photos} : Create a new photo.
     *
     * @param photoEntity the photoEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new photoEntity, or with status {@code 400 (Bad Request)} if the photo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/photos")
    public ResponseEntity<PhotoEntity> createPhoto(@Valid @RequestBody PhotoEntity photoEntity) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photoEntity);
        if (photoEntity.getId() != null) {
            throw new BadRequestAlertException("A new photo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            PhotoEntity result = photoService.save(photoEntity);
            return ResponseEntity.created(new URI("/api/photos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IOException e) {
            throw new BadRequestAlertException("Can not extract dimensions from photo", ENTITY_NAME, "imagecorrupt");
        }
    }

    /**
     * {@code PUT  /photos} : Updates an existing photo.
     *
     * @param photoEntity the photoEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoEntity,
     * or with status {@code 400 (Bad Request)} if the photoEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the photoEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/photos")
    public ResponseEntity<PhotoEntity> updatePhoto(@Valid @RequestBody PhotoEntity photoEntity) throws URISyntaxException {
        log.debug("REST request to update Photo : {}", photoEntity);
        if (photoEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        try {
            PhotoEntity result = photoService.save(photoEntity);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photoEntity.getId().toString()))
                .body(result);
        } catch (IOException e) {
            throw new BadRequestAlertException("Can not extract dimensions from photo", ENTITY_NAME, "imagecorrupt");
        }
    }

    /**
     * {@code GET  /photos} : get all the photos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photos in body.
     */
    @GetMapping("/photos")
    public ResponseEntity<List<PhotoEntity>> getAllPhotos(PhotoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Photos by criteria: {}", criteria);
        Page<PhotoEntity> page = photoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /photos/count} : count all the photos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/photos/count")
    public ResponseEntity<Long> countPhotos(PhotoCriteria criteria) {
        log.debug("REST request to count Photos by criteria: {}", criteria);
        return ResponseEntity.ok().body(photoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /photos/:id} : get the "id" photo.
     *
     * @param id the id of the photoEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photoEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/photos/{id}")
    public ResponseEntity<PhotoEntity> getPhoto(@PathVariable Long id) {
        log.debug("REST request to get Photo : {}", id);
        Optional<PhotoEntity> photoEntity = photoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(photoEntity);
    }

    /**
     * {@code DELETE  /photos/:id} : delete the "id" photo.
     *
     * @param id the id of the photoEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/photos/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        log.debug("REST request to delete Photo : {}", id);
        photoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
