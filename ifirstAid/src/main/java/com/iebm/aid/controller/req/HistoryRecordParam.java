package com.iebm.aid.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("搜索修改历史实体")
public class HistoryRecordParam extends PageParam {

	@ApiModelProperty("记录id")
	private String recordId;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	
}
