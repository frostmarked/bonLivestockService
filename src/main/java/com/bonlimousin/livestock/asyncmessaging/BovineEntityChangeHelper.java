package com.bonlimousin.livestock.asyncmessaging;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.bonlimousin.livestock.domain.CattleEntity;
import com.bonlimousin.livestock.domain.MatrilinealityEntity;

import net.logstash.logback.encoder.org.apache.commons.lang3.math.NumberUtils;

public class BovineEntityChangeHelper {

	private BovineEntityChangeHelper() {
		
	}
	
	public static CattleEntity convert(EntityChangeVO from, CattleEntity to) {
		for(BovineField field : BovineField.values()) {
			populateEntityParam(field, from, to);			
		}
		return to;
	}

	public static void populateEntityParam(BovineField field, EntityChangeVO vo, CattleEntity ce) {
		switch (field) {
		case EAR_TAG_ID:
			String strEarTagId = vo.getEntityValue().get(field.fieldName()).toString();
			Integer earTagId = NumberUtils.createInteger(strEarTagId);
			ce.setEarTagId(earTagId);
			break;
		case NAME:
			String name = vo.getEntityValue().get(field.fieldName()).toString();
			ce.setName(name);
			break;
		default:
			break;
		}
	}
	
	public static List<MatrilinealityEntity> matchNameWithMatrilineality(String name, List<MatrilinealityEntity> mats) {
		if(StringUtils.trimToNull(name) == null) {
			return Collections.emptyList();
		}
		return mats.stream().filter(mat -> {
			String regex = mat.getCattleNameRegexPattern();
			return name.matches(regex);			
		}).collect(Collectors.toList());
	}
}
