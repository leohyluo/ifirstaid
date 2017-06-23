package com.iebm.aid.pojo.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;

import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.utils.DateUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("修改历史实体")
public class EventAidRecordHistory4WebVo {

	@ApiModelProperty("记录id")
	private Long id;
	
	@ApiModelProperty("修改时间")
	private LocalDateTime createTime;
	
	@ApiModelProperty("平台类型")
	private String appType;
	
	@ApiModelProperty("修改账号")
	private String creator;
	
	/*@ApiModelProperty("修改内容")
	private String deltaChange;*/
	
	public EventAidRecordHistory4WebVo(EventAidRecord record) {
		BeanUtils.copyProperties(record, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateTime() {
		if(createTime != null) {
			return createTime.format(DateTimeFormatter.ofPattern(DateUtils.TIMEF_FORMAT));
		}
		return null;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/*public String getDeltaChange() {
		return deltaChange;
	}

	public void setDeltaChange(String deltaChange) {
		this.deltaChange = deltaChange;
	}*/
	
	
}
