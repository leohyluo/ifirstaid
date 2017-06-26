package com.iebm.aid.controller.req.third;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iebm.aid.controller.req.EventParam;
import com.iebm.aid.controller.req.PatientParam;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.utils.JsonUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("120登录参数")
public class EventRecordParam {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ApiModelProperty("事件信息")
	private EventParam eventInfo;
	@ApiModelProperty("患者列表")
	private List<PatientParam> patientInfo;
	
	public EventParam getEventInfo() {
		return eventInfo;
	}
	public void setEventInfo(EventParam eventInfo) {
		this.eventInfo = eventInfo;
	}
	public List<PatientParam> getPatientInfo() {
		return patientInfo;
	}
	public void setPatientInfo(List<PatientParam> patientInfo) {
		this.patientInfo = patientInfo;
	}
	
	public EventAidRecord parseToEventAidRecord() {
		try {
			EventAidRecord record = new EventAidRecord();
			if(this.eventInfo != null) {
				record.setEventNo(eventInfo.getEventNo());
				record.setSeatNo(eventInfo.getSeatNo());
				record.setDispatcher(eventInfo.getDispatcher());
			}
			return record;
		} catch (Exception e) {
			logger.error("error on parseToEventAidRecord", e);
			return null;
		}
	}
	
	@Override
	public String toString() {
		return JsonUtils.toJsonString(this);
	}
}
