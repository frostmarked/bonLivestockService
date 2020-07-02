package com.bonlimousin.livestock.repository;

import com.bonlimousin.livestock.domain.CattleEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CattleEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CattleRepository extends JpaRepository<CattleEntity, Long>, JpaSpecificationExecutor<CattleEntity> {
}
