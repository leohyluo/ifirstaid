package com.iebm.aid.pojo.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;

import com.iebm.aid.pojo.EventAidRecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("web端急救记录列表")
public class EventAidRecord4WebVo {

	@ApiModelProperty("急救记录id")
	private Long id;
	
	@ApiModelProperty("事件编号")
	private String eventNo;
	
	@ApiModelProperty("时间(yyyy-MM-dd HH:mm:ss)")
	private LocalDateTime createTime;
	
	@ApiModelProperty("座席号")
	private String seatNo;
	
	@ApiModelProperty("调度员")
	private String dispatcher;
	
	@ApiModelProperty("患者姓名")
	private String name;
	
	@ApiModelProperty("危急分级")
	private String mpdsTitle;
	
	@ApiModelProperty("呼叫主诉")
	private String mainSymptomText;
	
	@ApiModelProperty("任务状态")
	private String status;
	
	public EventAidRecord4WebVo(EventAidRecord record) {
		BeanUtils.copyProperties(record, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getCreateTime() {
		if(createTime != null) {
			return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		return null;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMpdsTitle() {
		return mpdsTitle;
	}

	public void setMpdsTitle(String mpdsTitle) {
		this.mpdsTitle = mpdsTitle;
	}

	public String getMainSymptomText() {
		return mainSymptomText;
	}

	public void setMainSymptomText(String mainSymptomText) {
		this.mainSymptomText = mainSymptomText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
