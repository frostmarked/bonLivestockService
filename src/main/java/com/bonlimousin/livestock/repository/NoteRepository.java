package com.bonlimousin.livestock.repository;

import com.bonlimousin.livestock.domain.NoteEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the NoteEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long>, JpaSpecificationExecutor<NoteEntity> {
}
