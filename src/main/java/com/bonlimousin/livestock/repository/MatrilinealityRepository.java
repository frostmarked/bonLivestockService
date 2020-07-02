package com.bonlimousin.livestock.repository;

import com.bonlimousin.livestock.domain.MatrilinealityEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MatrilinealityEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatrilinealityRepository extends JpaRepository<MatrilinealityEntity, Long>, JpaSpecificationExecutor<MatrilinealityEntity> {
}
