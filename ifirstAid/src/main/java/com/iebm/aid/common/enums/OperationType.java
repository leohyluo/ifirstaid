package com.iebm.aid.common.enums;

public enum OperationType {

	INSERT("0"),		
	UPDATE("1");			
	
	private String type;
	
	private OperationType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public static OperationType findByType(String type) {
		for(OperationType item : values()) {
			if(item.type == type) {
				return item;
			}
		}
		throw new IllegalArgumentException("Cannot create enum from " + type);
	}
}
