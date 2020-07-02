package com.bonlimousin.livestock.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.bonlimousin.livestock.domain.PhotoEntity;
import com.bonlimousin.livestock.domain.*; // for static metamodels
import com.bonlimousin.livestock.repository.PhotoRepository;
import com.bonlimousin.livestock.service.dto.PhotoCriteria;

/**
 * Service for executing complex queries for {@link PhotoEntity} entities in the database.
 * The main input is a {@link PhotoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PhotoEntity} or a {@link Page} of {@link PhotoEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhotoQueryService extends QueryService<PhotoEntity> {

    private final Logger log = LoggerFactory.getLogger(PhotoQueryService.class);

    private final PhotoRepository photoRepository;

    public PhotoQueryService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    /**
     * Return a {@link List} of {@link PhotoEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PhotoEntity> findByCriteria(PhotoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PhotoEntity> specification = createSpecification(criteria);
        return photoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PhotoEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PhotoEntity> findByCriteria(PhotoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PhotoEntity> specification = createSpecification(criteria);
        return photoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PhotoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PhotoEntity> specification = createSpecification(criteria);
        return photoRepository.count(specification);
    }

    /**
     * Function to convert {@link PhotoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PhotoEntity> createSpecification(PhotoCriteria criteria) {
        Specification<PhotoEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PhotoEntity_.id));
            }
            if (criteria.getCaption() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCaption(), PhotoEntity_.caption));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), PhotoEntity_.height));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), PhotoEntity_.width));
            }
            if (criteria.getTaken() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaken(), PhotoEntity_.taken));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), PhotoEntity_.visibility));
            }
            if (criteria.getCattleId() != null) {
                specification = specification.and(buildSpecification(criteria.getCattleId(),
                    root -> root.join(PhotoEntity_.cattle, JoinType.LEFT).get(CattleEntity_.id)));
            }
        }
        return specification;
    }
}
