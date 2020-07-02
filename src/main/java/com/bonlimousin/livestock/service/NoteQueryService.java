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

import com.bonlimousin.livestock.domain.NoteEntity;
import com.bonlimousin.livestock.domain.*; // for static metamodels
import com.bonlimousin.livestock.repository.NoteRepository;
import com.bonlimousin.livestock.service.dto.NoteCriteria;

/**
 * Service for executing complex queries for {@link NoteEntity} entities in the database.
 * The main input is a {@link NoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NoteEntity} or a {@link Page} of {@link NoteEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NoteQueryService extends QueryService<NoteEntity> {

    private final Logger log = LoggerFactory.getLogger(NoteQueryService.class);

    private final NoteRepository noteRepository;

    public NoteQueryService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Return a {@link List} of {@link NoteEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NoteEntity> findByCriteria(NoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NoteEntity> specification = createSpecification(criteria);
        return noteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link NoteEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NoteEntity> findByCriteria(NoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NoteEntity> specification = createSpecification(criteria);
        return noteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NoteEntity> specification = createSpecification(criteria);
        return noteRepository.count(specification);
    }

    /**
     * Function to convert {@link NoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NoteEntity> createSpecification(NoteCriteria criteria) {
        Specification<NoteEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NoteEntity_.id));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getCategory(), NoteEntity_.category));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), NoteEntity_.note));
            }
            if (criteria.getActualDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualDate(), NoteEntity_.actualDate));
            }
            if (criteria.getPastureId() != null) {
                specification = specification.and(buildSpecification(criteria.getPastureId(),
                    root -> root.join(NoteEntity_.pasture, JoinType.LEFT).get(PastureEntity_.id)));
            }
            if (criteria.getCattleId() != null) {
                specification = specification.and(buildSpecification(criteria.getCattleId(),
                    root -> root.join(NoteEntity_.cattle, JoinType.LEFT).get(CattleEntity_.id)));
            }
        }
        return specification;
    }
}
