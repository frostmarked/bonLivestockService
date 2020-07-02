package com.bonlimousin.livestock.service;

import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.repository.CattleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CattleEntity}.
 */
@Service
@Transactional
public class CattleService {

    private final Logger log = LoggerFactory.getLogger(CattleService.class);

    private final CattleRepository cattleRepository;

    public CattleService(CattleRepository cattleRepository) {
        this.cattleRepository = cattleRepository;
    }

    /**
     * Save a cattle.
     *
     * @param cattleEntity the entity to save.
     * @return the persisted entity.
     */
    public CattleEntity save(CattleEntity cattleEntity) {
        log.debug("Request to save Cattle : {}", cattleEntity);
        return cattleRepository.save(cattleEntity);
    }

    /**
     * Get all the cattles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CattleEntity> findAll(Pageable pageable) {
        log.debug("Request to get all Cattles");
        return cattleRepository.findAll(pageable);
    }


    /**
     * Get one cattle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CattleEntity> findOne(Long id) {
        log.debug("Request to get Cattle : {}", id);
        return cattleRepository.findById(id);
    }

    /**
     * Delete the cattle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cattle : {}", id);
        cattleRepository.deleteById(id);
    }
}
