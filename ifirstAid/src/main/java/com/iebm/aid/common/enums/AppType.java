package com.iebm.aid.common.enums;

public enum AppType {

	ANDROID(0L),		//android
	WEB(1L);			//h5	
	
	private Long type;
	
	private AppType(Long type){
		this.type = type;
	}

	public Long getType() {
		return type;
	}
	
	public static AppType findByType(Long type) {
		for(AppType item : values()) {
			if(item.type == type) {
				return item;
			}
		}
		throw new IllegalArgumentException("Cannot create enum from " + type);
	}
}
