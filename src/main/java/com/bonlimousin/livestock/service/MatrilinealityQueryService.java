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

import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.domain.*; // for static metamodels
import com.bonlimousin.livestock.repository.MatrilinealityRepository;
import com.bonlimousin.livestock.service.dto.MatrilinealityCriteria;

/**
 * Service for executing complex queries for {@link MatrilinealityEntity} entities in the database.
 * The main input is a {@link MatrilinealityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MatrilinealityEntity} or a {@link Page} of {@link MatrilinealityEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MatrilinealityQueryService extends QueryService<MatrilinealityEntity> {

    private final Logger log = LoggerFactory.getLogger(MatrilinealityQueryService.class);

    private final MatrilinealityRepository matrilinealityRepository;

    public MatrilinealityQueryService(MatrilinealityRepository matrilinealityRepository) {
        this.matrilinealityRepository = matrilinealityRepository;
    }

    /**
     * Return a {@link List} of {@link MatrilinealityEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MatrilinealityEntity> findByCriteria(MatrilinealityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MatrilinealityEntity> specification = createSpecification(criteria);
        return matrilinealityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MatrilinealityEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MatrilinealityEntity> findByCriteria(MatrilinealityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MatrilinealityEntity> specification = createSpecification(criteria);
        return matrilinealityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MatrilinealityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MatrilinealityEntity> specification = createSpecification(criteria);
        return matrilinealityRepository.count(specification);
    }

    /**
     * Function to convert {@link MatrilinealityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MatrilinealityEntity> createSpecification(MatrilinealityCriteria criteria) {
        Specification<MatrilinealityEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MatrilinealityEntity_.id));
            }
            if (criteria.getFamilyname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyname(), MatrilinealityEntity_.familyname));
            }
            if (criteria.getEarTagId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEarTagId(), MatrilinealityEntity_.earTagId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MatrilinealityEntity_.name));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), MatrilinealityEntity_.country));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MatrilinealityEntity_.description));
            }
            if (criteria.getCattleNameRegexPattern() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCattleNameRegexPattern(), MatrilinealityEntity_.cattleNameRegexPattern));
            }
            if (criteria.getPatriId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPatriId(), MatrilinealityEntity_.patriId));
            }
            if (criteria.getPatriName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPatriName(), MatrilinealityEntity_.patriName));
            }
            if (criteria.getPatriCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPatriCountry(), MatrilinealityEntity_.patriCountry));
            }
            if (criteria.getPolled() != null) {
                specification = specification.and(buildSpecification(criteria.getPolled(), MatrilinealityEntity_.polled));
            }
            if (criteria.getStoryHandle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoryHandle(), MatrilinealityEntity_.storyHandle));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), MatrilinealityEntity_.visibility));
            }
        }
        return specification;
    }
}
