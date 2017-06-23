package com.iebm.aid.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("标记已读分站策略")
public class MarkReadAidRecordParam {
	@ApiModelProperty("分站策略关联id")
	private String stationAidRecordId;

	public String getStationAidRecordId() {
		return stationAidRecordId;
	}

	public void setStationAidRecordId(String stationAidRecordId) {
		this.stationAidRecordId = stationAidRecordId;
	}
}
