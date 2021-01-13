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

import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.domain.*; // for static metamodels
import com.bonlimousin.livestock.repository.CattleRepository;
import com.bonlimousin.livestock.service.dto.CattleCriteria;

/**
 * Service for executing complex queries for {@link CattleEntity} entities in the database.
 * The main input is a {@link CattleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CattleEntity} or a {@link Page} of {@link CattleEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CattleQueryService extends QueryService<CattleEntity> {

    private final Logger log = LoggerFactory.getLogger(CattleQueryService.class);

    private final CattleRepository cattleRepository;

    public CattleQueryService(CattleRepository cattleRepository) {
        this.cattleRepository = cattleRepository;
    }

    /**
     * Return a {@link List} of {@link CattleEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CattleEntity> findByCriteria(CattleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CattleEntity> specification = createSpecification(criteria);
        return cattleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CattleEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CattleEntity> findByCriteria(CattleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CattleEntity> specification = createSpecification(criteria);
        return cattleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CattleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CattleEntity> specification = createSpecification(criteria);
        return cattleRepository.count(specification);
    }

    /**
     * Function to convert {@link CattleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CattleEntity> createSpecification(CattleCriteria criteria) {
        Specification<CattleEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CattleEntity_.id));
            }
            if (criteria.getEarTagId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEarTagId(), CattleEntity_.earTagId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CattleEntity_.name));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), CattleEntity_.visibility));
            }
            if (criteria.getUpForSale() != null) {
                specification = specification.and(buildSpecification(criteria.getUpForSale(), CattleEntity_.upForSale));
            }
            if (criteria.getShowBlup() != null) {
                specification = specification.and(buildSpecification(criteria.getShowBlup(), CattleEntity_.showBlup));
            }
            if (criteria.getAlert() != null) {
                specification = specification.and(buildSpecification(criteria.getAlert(), CattleEntity_.alert));
            }
            if (criteria.getStoryHandle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoryHandle(), CattleEntity_.storyHandle));
            }
            if (criteria.getMatrilinealityId() != null) {
                specification = specification.and(buildSpecification(criteria.getMatrilinealityId(),
                    root -> root.join(CattleEntity_.matrilineality, JoinType.LEFT).get(MatrilinealityEntity_.id)));
            }
        }
        return specification;
    }
}
