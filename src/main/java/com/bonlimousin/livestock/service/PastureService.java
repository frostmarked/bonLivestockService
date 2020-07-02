package com.bonlimousin.livestock.service;

import com.bonlimousin.livestock.domain.PastureEntity;
import com.bonlimousin.livestock.repository.PastureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PastureEntity}.
 */
@Service
@Transactional
public class PastureService {

    private final Logger log = LoggerFactory.getLogger(PastureService.class);

    private final PastureRepository pastureRepository;

    public PastureService(PastureRepository pastureRepository) {
        this.pastureRepository = pastureRepository;
    }

    /**
     * Save a pasture.
     *
     * @param pastureEntity the entity to save.
     * @return the persisted entity.
     */
    public PastureEntity save(PastureEntity pastureEntity) {
        log.debug("Request to save Pasture : {}", pastureEntity);
        return pastureRepository.save(pastureEntity);
    }

    /**
     * Get all the pastures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PastureEntity> findAll(Pageable pageable) {
        log.debug("Request to get all Pastures");
        return pastureRepository.findAll(pageable);
    }


    /**
     * Get one pasture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PastureEntity> findOne(Long id) {
        log.debug("Request to get Pasture : {}", id);
        return pastureRepository.findById(id);
    }

    /**
     * Delete the pasture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pasture : {}", id);
        pastureRepository.deleteById(id);
    }
}
