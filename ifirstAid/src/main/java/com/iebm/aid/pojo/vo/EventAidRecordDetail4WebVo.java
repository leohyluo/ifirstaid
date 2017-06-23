package com.iebm.aid.pojo.vo;

import java.util.List;

import com.iebm.aid.pojo.EventAidRecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("急救记录详细")
public class EventAidRecordDetail4WebVo {

	@ApiModelProperty("患者信息")
	private PatientInfoVo patientInfo;
	
	@ApiModelProperty("急救问答")
	private List<ProcessKeyqVo> historyKeyq;
		
	@ApiModelProperty("预案")
	private List<PlanVo> plans;
	
	@ApiModelProperty("已发送分站")
	private String stationList;
	
	@ApiModelProperty("修改的内容")
	private String deltaChange;
	
	public EventAidRecordDetail4WebVo(EventAidRecord record) {
		this.patientInfo = new PatientInfoVo(record);
		this.deltaChange = record.getDeltaChange();
	}

	public PatientInfoVo getPatientInfo() {
		return patientInfo;
	}

	public void setPatientInfo(PatientInfoVo patientInfo) {
		this.patientInfo = patientInfo;
	}

	public List<ProcessKeyqVo> getHistoryKeyq() {
		return historyKeyq;
	}

	public void setHistoryKeyq(List<ProcessKeyqVo> historyKeyq) {
		this.historyKeyq = historyKeyq;
	}

	public List<PlanVo> getPlans() {
		return plans;
	}

	public void setPlans(List<PlanVo> plans) {
		this.plans = plans;
	}

	public String getDeltaChange() {
		return deltaChange;
	}

	public void setDeltaChange(String deltaChange) {
		this.deltaChange = deltaChange;
	}

	public String getStationList() {
		return stationList;
	}

	public void setStationList(String stationList) {
		this.stationList = stationList;
	}
	
}
