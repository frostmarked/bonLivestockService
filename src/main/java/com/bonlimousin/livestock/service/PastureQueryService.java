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

import com.bonlimousin.livestock.domain.PastureEntity;
import com.bonlimousin.livestock.domain.*; // for static metamodels
import com.bonlimousin.livestock.repository.PastureRepository;
import com.bonlimousin.livestock.service.dto.PastureCriteria;

/**
 * Service for executing complex queries for {@link PastureEntity} entities in the database.
 * The main input is a {@link PastureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PastureEntity} or a {@link Page} of {@link PastureEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PastureQueryService extends QueryService<PastureEntity> {

    private final Logger log = LoggerFactory.getLogger(PastureQueryService.class);

    private final PastureRepository pastureRepository;

    public PastureQueryService(PastureRepository pastureRepository) {
        this.pastureRepository = pastureRepository;
    }

    /**
     * Return a {@link List} of {@link PastureEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PastureEntity> findByCriteria(PastureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PastureEntity> specification = createSpecification(criteria);
        return pastureRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PastureEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PastureEntity> findByCriteria(PastureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PastureEntity> specification = createSpecification(criteria);
        return pastureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PastureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PastureEntity> specification = createSpecification(criteria);
        return pastureRepository.count(specification);
    }

    /**
     * Function to convert {@link PastureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PastureEntity> createSpecification(PastureCriteria criteria) {
        Specification<PastureEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PastureEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PastureEntity_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PastureEntity_.description));
            }
        }
        return specification;
    }
}
