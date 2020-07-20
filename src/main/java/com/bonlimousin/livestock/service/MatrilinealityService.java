package com.bonlimousin.livestock.service;

import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.repository.MatrilinealityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MatrilinealityEntity}.
 */
@Service
@Transactional
public class MatrilinealityService {

    private final Logger log = LoggerFactory.getLogger(MatrilinealityService.class);

    private final MatrilinealityRepository matrilinealityRepository;

    public MatrilinealityService(MatrilinealityRepository matrilinealityRepository) {
        this.matrilinealityRepository = matrilinealityRepository;
    }

    /**
     * Save a matrilineality.
     *
     * @param matrilinealityEntity the entity to save.
     * @return the persisted entity.
     */
    public MatrilinealityEntity save(MatrilinealityEntity matrilinealityEntity) {
        log.debug("Request to save Matrilineality : {}", matrilinealityEntity);
        return matrilinealityRepository.save(matrilinealityEntity);
    }

    /**
     * Get all the matrilinealities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatrilinealityEntity> findAll(Pageable pageable) {
        log.debug("Request to get all Matrilinealities");
        return matrilinealityRepository.findAll(pageable);
    }


    /**
     * Get one matrilineality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatrilinealityEntity> findOne(Long id) {
        log.debug("Request to get Matrilineality : {}", id);
        return matrilinealityRepository.findById(id);
    }

    /**
     * Delete the matrilineality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Matrilineality : {}", id);
        matrilinealityRepository.deleteById(id);
    }
}
