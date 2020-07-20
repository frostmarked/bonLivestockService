package com.bonlimousin.livestock.repository;

import com.bonlimousin.livestock.domain.PastureEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PastureEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PastureRepository extends JpaRepository<PastureEntity, Long>, JpaSpecificationExecutor<PastureEntity> {
}
