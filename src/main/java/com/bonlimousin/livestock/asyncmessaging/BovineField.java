package com.bonlimousin.livestock.asyncmessaging;

public enum BovineField {
	EAR_TAG_ID("earTagId"),
	NAME("name"),
	GENDER("gender"),
	BOVINE_STATUS("bovineStatus"),
	;
	
	private final String fieldName;
	private BovineField(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String fieldName() {
		return this.fieldName;
	}
	
	public static BovineField fromFieldName(String fieldName) {
		for(BovineField bf : BovineField.values()) {			
			if(bf.fieldName().equals(fieldName)) {
				return bf;
			}
		}
		return null;
	}
}
