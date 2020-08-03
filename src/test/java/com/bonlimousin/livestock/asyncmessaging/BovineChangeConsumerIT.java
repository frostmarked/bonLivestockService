package com.bonlimousin.livestock.asyncmessaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.service.CattleQueryService;
import com.bonlimousin.livestock.service.dto.CattleCriteria;

import io.github.jhipster.service.filter.StringFilter;

@SpringBootTest(classes = { BonLivestockServiceApp.class })
class BovineChangeConsumerIT {

	@Autowired
	private BovineEntityChangeConsumer bovineEntityChangeConsumer;

	@Autowired
	private CattleQueryService cattleQueryService;

	@Test
	@Transactional
	void bovineUpdateName() throws InterruptedException {
		EntityChangeVO vo = createEntityChangeVO("CREATE", "");
		String topic = "ENTITY_CHANGE_BOVINEENTITY";
		String key = vo.getAction();		
		bovineEntityChangeConsumer.listen(vo, key, 1, topic, Instant.now().getEpochSecond());
		vo.getEntityValue().put(BovineField.NAME.fieldName(), "bullyup");		
		bovineEntityChangeConsumer.listen(vo, key, 1, topic, Instant.now().getEpochSecond());
		
		CattleCriteria cattleCriteria = new CattleCriteria();
		StringFilter nameFilter = (StringFilter) new StringFilter()
				.setEquals(vo.getEntityValue().get(BovineField.NAME.fieldName()).toString());
		cattleCriteria.setName(nameFilter);
		List<CattleEntity> list = cattleQueryService.findByCriteria(cattleCriteria);;				
		
		assertThat(list).hasSize(1);
		CattleEntity ce = list.get(0);		
		assertThat(ce.getEarTagId()).isEqualTo(vo.getEntityValue().get(BovineField.EAR_TAG_ID.fieldName()));
	}
	
	@Test
	@Transactional
	void bovineUpdateNameWhereItExists() throws InterruptedException {
		EntityChangeVO vo = createEntityChangeVO("CREATE", "bully");		
		String topic = "ENTITY_CHANGE_BOVINEENTITY";
		String key = vo.getAction();		
		bovineEntityChangeConsumer.listen(vo, key, 1, topic, Instant.now().getEpochSecond());
		vo.getEntityValue().put(BovineField.NAME.fieldName(), "bullyup");		
		bovineEntityChangeConsumer.listen(vo, key, 1, topic, Instant.now().getEpochSecond());
		
		CattleCriteria cattleCriteria = new CattleCriteria();
		StringFilter nameFilter = (StringFilter) new StringFilter()
				.setEquals(vo.getEntityValue().get(BovineField.NAME.fieldName()).toString());
		cattleCriteria.setName(nameFilter);
		List<CattleEntity> list = cattleQueryService.findByCriteria(cattleCriteria);
		
		assertThat(list).isEmpty();
	}
	
	public static EntityChangeVO createEntityChangeVO(String action, String name) {
		EntityChangeVO vo = new EntityChangeVO();
		vo.setAction(action);
		vo.setEntityId("3");
		vo.setEntityType("BovineEntity");
		vo.setModifiedBy("jofr");
		vo.setModifiedDate(Instant.now());

		Map<String, Object> entityValue = new HashMap<>();
		entityValue.put(BovineField.EAR_TAG_ID.fieldName(), 3);
		entityValue.put(BovineField.NAME.fieldName(), name);
		entityValue.put(BovineField.GENDER.fieldName(), "HEIFER");
		entityValue.put(BovineField.BOVINE_STATUS.fieldName(), "ON_FARM");		
		vo.setEntityValue(entityValue);
		List<String> changedFields = Arrays.asList(
			BovineField.EAR_TAG_ID.fieldName(), 
			BovineField.NAME.fieldName(),
			BovineField.GENDER.fieldName(),
			BovineField.BOVINE_STATUS.fieldName()
		);
		vo.setChangedEntityFields(changedFields);
		
		return vo;
	}

}