package com.bonlimousin.livestock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bonlimousin.livestock.domain.CattleEntity;

/**
 * Spring Data  repository for the CattleEntity entity.
 */
@Repository
public interface CattleRepository extends JpaRepository<CattleEntity, Long>, JpaSpecificationExecutor<CattleEntity> {
	
	Optional<CattleEntity> findOneByEarTagId(Integer earTagId);
}
