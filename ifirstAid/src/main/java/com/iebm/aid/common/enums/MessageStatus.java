package com.iebm.aid.common.enums;

public enum MessageStatus {

	UNREAD(0L),			//未读
	READED(1L);			//已读	
	
	private Long status;
	
	private MessageStatus(Long status){
		this.status = status;
	}

	public Long getStatus() {
		return status;
	}
	
	public static MessageStatus findByStatus(Long status) {
		for(MessageStatus item : values()) {
			if(item.status == status) {
				return item;
			}
		}
		throw new IllegalArgumentException("Cannot create enum from " + status);
	}
}
