package com.iebm.aid.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("web端急救记录参数实体")
public class EARecord4WebParam extends PageParam {

	@ApiModelProperty("事件编号")
	private String eventNo;
	
	@ApiModelProperty("座席号")
	private String seatNo;
	
	@ApiModelProperty("调度员")
	private String dispatcher;
	
	@ApiModelProperty("事件日期")
	private String createTime;
	
	@ApiModelProperty("事件结束日期")
	private String endTime;
	
	@ApiModelProperty("患者姓名")
	private String name;
	
	@ApiModelProperty("呼叫主诉")
	private String mainSymptomText;
	
	@ApiModelProperty("呼叫人电话")
	private String aidMobile;

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(String dispatcher) {
		this.dispatcher = dispatcher;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMainSymptomText() {
		return mainSymptomText;
	}

	public void setMainSymptomText(String mainSymptomText) {
		this.mainSymptomText = mainSymptomText;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAidMobile() {
		return aidMobile;
	}

	public void setAidMobile(String aidMobile) {
		this.aidMobile = aidMobile;
	}
	
	
}
