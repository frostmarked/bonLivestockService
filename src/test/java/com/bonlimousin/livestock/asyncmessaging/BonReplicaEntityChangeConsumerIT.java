package com.bonlimousin.livestock.asyncmessaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import com.bonlimousin.livestock.BonLivestockServiceApp;
import com.bonlimousin.livestock.config.KafkaTestConfiguration;
import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.service.CattleQueryService;
import com.bonlimousin.livestock.service.CattleService;
import com.bonlimousin.livestock.service.dto.CattleCriteria;

import io.github.jhipster.service.filter.StringFilter;

@SpringBootTest(classes = { BonLivestockServiceApp.class, KafkaTestConfiguration.class })
class BonReplicaEntityChangeConsumerIT {

	@Autowired
	private KafkaTemplate<String, EntityChangeVO> entityChangeKafkaTemplate;

	@Autowired
	private CattleQueryService cattleQueryService;
	
	@Autowired
	private CattleService cattleService;

	@BeforeAll
	public static void setup() {
		KafkaTestConfiguration.startKafka();
	}

	@Test	
	void testEntityChange() throws InterruptedException {
		EntityChangeVO vo = new EntityChangeVO();
		vo.setAction("CREATE");
		vo.setEntityId("1");
		vo.setEntityType("BovineEntity");
		vo.setModifiedBy("jofr");
		vo.setModifiedDate(Instant.now());

		Map<String, Object> entityValue = new HashMap<>();
		entityValue.put(BovineField.EAR_TAG_ID.fieldName(), 1);
		entityValue.put(BovineField.NAME.fieldName(), "bully");
		vo.setEntityValue(entityValue);
		String topic = "ENTITY_CHANGE_BOVINEENTITY";
		String key = vo.getAction();
		ProducerRecord<String, EntityChangeVO> record = new ProducerRecord<>(topic, key, vo);

		entityChangeKafkaTemplate.send(record).addCallback(val -> assertThat(val).isNotNull(),
				ex -> Assert.fail(ex.getMessage()));

		CattleCriteria cattleCriteria = new CattleCriteria();
		StringFilter nameFilter = (StringFilter) new StringFilter()
				.setEquals(entityValue.get(BovineField.NAME.fieldName()).toString());
		cattleCriteria.setName(nameFilter);
		List<CattleEntity> list = null;
		for (int i = 1; i < 10; i++) {
			TimeUnit.SECONDS.sleep(i);
			list = cattleQueryService.findByCriteria(cattleCriteria);
			if (!list.isEmpty()) {
				break;
			}
		}
		if (list.isEmpty()) {
			Assert.fail("Could not find cattle by name");
		}

		assertThat(list).hasSize(1);
		CattleEntity ce = list.get(0);
		assertThat(ce.getEarTagId()).isEqualTo(entityValue.get(BovineField.EAR_TAG_ID.fieldName()));
		
		cattleService.delete(ce.getId());
	}

	@AfterAll
	public static void tearDown() {
		KafkaTestConfiguration.stopKafka();
	}
}