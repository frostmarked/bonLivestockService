package com.bonlimousin.livestock.asyncmessaging;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.domain.MatrilinealityEntity;
import com.bonlimousin.livestock.domain.enumeration.UserRole;
import com.bonlimousin.livestock.repository.CattleRepository;
import com.bonlimousin.livestock.repository.MatrilinealityRepository;
import com.bonlimousin.livestock.service.CattleService;

@Service
@KafkaListener(topics = {
		"ENTITY_CHANGE_BOVINEENTITY" }, containerFactory = "entityChangeKafkaListenerContainerFactory")
public class BovineEntityChangeConsumer {

	private final Logger log = LoggerFactory.getLogger(BovineEntityChangeConsumer.class);

	private final CattleRepository cattleRepository;
	private final CattleService cattleService;
	private final MatrilinealityRepository matrilinealityRepository;

	public BovineEntityChangeConsumer(CattleService cattleService, CattleRepository cattleRepository,
			MatrilinealityRepository matrilinealityRepository) {
		this.cattleService = cattleService;
		this.cattleRepository = cattleRepository;
		this.matrilinealityRepository = matrilinealityRepository;
	}

	@KafkaHandler
	public void listen(@Payload EntityChangeVO vo, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
		log.debug("Consumed topic {} with key {} and message in {}", topic, key, vo);

		CattleEntity ce = BovineEntityChangeHelper.convert(vo, new CattleEntity());
		Optional<CattleEntity> opt = this.cattleRepository.findOneByEarTagId(ce.getEarTagId());
		if (opt.isPresent()) { // update no mater action
			updateCattleEntity(vo, opt.get());
		} else { // create no mater action
			createCattleEntity(vo, ce);
		}
	}

	private void updateCattleEntity(EntityChangeVO vo, CattleEntity currce) {
		for (String fieldName : vo.getChangedEntityFields()) {
			BovineField bf = BovineField.fromFieldName(fieldName);
			if (bf != null && bf != BovineField.NAME) {
				BovineEntityChangeHelper.populateEntityParam(bf, vo, currce);
			}
		}
		if (StringUtils.trimToNull(currce.getName()) == null) {
			BovineEntityChangeHelper.populateEntityParam(BovineField.NAME, vo, currce);
		}
		if (currce.getMatrilineality() == null) {
			Optional.ofNullable(vo.getEntityValue().get(BovineField.GENDER.fieldName()))
				.map(Object::toString)
				.filter("HEIFER"::equalsIgnoreCase)
				.ifPresent(v -> matchNameWithMatrilineality(currce));
		}

		log.info("Update cattle record for bovine with earTagId {} with changed fields {}", currce.getEarTagId(),
				vo.getChangedEntityFields());
		this.cattleService.save(currce);
	}

	private void createCattleEntity(EntityChangeVO vo, CattleEntity ce) {
		ce.setAlert(true);
		ce.setShowBlup(false);
		ce.setUpForSale(false);
		if(!Optional.ofNullable(vo.getEntityValue().get(BovineField.BOVINE_STATUS.fieldName()))
				.map(Object::toString)
				.filter("ON_FARM"::equalsIgnoreCase).isEmpty()) {
			ce.setVisibility(UserRole.ROLE_ANONYMOUS);
		} else {
			ce.setVisibility(UserRole.ROLE_ADMIN);
		}
		matchNameWithMatrilineality(ce);
		log.info("Create new cattle record for bovine with earTagId {}", ce.getEarTagId());
		this.cattleService.save(ce);
	}

	private void matchNameWithMatrilineality(CattleEntity ce) {
		List<MatrilinealityEntity> mats = matrilinealityRepository.findAll();
		List<MatrilinealityEntity> matchedmats = BovineEntityChangeHelper.matchNameWithMatrilineality(ce.getName(),
				mats);
		if (!matchedmats.isEmpty()) {
			ce.setMatrilineality(matchedmats.get(0));
		}
	}
}
