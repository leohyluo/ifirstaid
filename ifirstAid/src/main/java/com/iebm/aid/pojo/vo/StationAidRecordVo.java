package com.iebm.aid.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;

import com.iebm.aid.pojo.StationMessage;

@ApiModel("急救记录实体")
public class StationAidRecordVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("分站急救记录id")
	private Long id;
	@ApiModelProperty("分站id")
	private Long stationId;
	
	@ApiModelProperty("状态")
	private Long status;
	
	@ApiModelProperty("创建人")
	private String creator;
	
	@ApiModelProperty("读取人id")
	private String readerId;
	
	@ApiModelProperty("读取时间")
	private LocalDateTime readTime;
	
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;
	
	@ApiModelProperty("急救记录id")
	private Long aidId;
	
	@ApiModelProperty("主诉")
	private String mainSymptomText;
	
	@ApiModelProperty("患者姓名")
	private String name;
	
	@ApiModelProperty("患者性别")
	private String gender;
	
	@ApiModelProperty("危急分级")
	private String mpdsTitle;
	
	public StationAidRecordVo (StationMessage record) {
		BeanUtils.copyProperties(record, this);
		this.mainSymptomText=record.getEventAidRecord().getMainSymptomText();
		this.name=record.getEventAidRecord().getName();
		this.aidId=record.getEventAidRecord().getId();
		this.gender=record.getEventAidRecord().getGender();
		this.mpdsTitle=record.getEventAidRecord().getMpdsTitle();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMainSymptomText() {
		return mainSymptomText;
	}

	public void setMainSymptomText(String mainSymptomText) {
		this.mainSymptomText = mainSymptomText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCreateTime() {
		if(this.createTime != null) {
			return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		return null;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}

	public Long getAidId() {
		return aidId;
	}

	public void setAidId(Long aidId) {
		this.aidId = aidId;
	}

	public String getMpdsTitle() {
		return mpdsTitle;
	}

	public void setMpdsTitle(String mpdsTitle) {
		this.mpdsTitle = mpdsTitle;
	}
	
	
	
}
