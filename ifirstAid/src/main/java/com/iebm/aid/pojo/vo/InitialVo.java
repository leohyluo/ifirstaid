package com.iebm.aid.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("120初始化响应结果")
public class InitialVo {

	@ApiModelProperty("系统调度唯一编码")
	private String eventId;

	public InitialVo(String eventId) {
		this.eventId = eventId;
	}
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	
}
