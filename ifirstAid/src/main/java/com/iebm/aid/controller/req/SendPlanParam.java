package com.iebm.aid.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("发送预案实体")
public class SendPlanParam {

	@ApiModelProperty("急救记录Id")
	private String recordId;
	
	@ApiModelProperty("急救分站id,多个用逗号分隔")
	private String stationIds;


	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getStationIds() {
		return stationIds;
	}

	public void setStationIds(String stationIds) {
		this.stationIds = stationIds;
	}
	
	
}
