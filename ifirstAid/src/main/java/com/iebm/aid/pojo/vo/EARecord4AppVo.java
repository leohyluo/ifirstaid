package com.iebm.aid.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("pad端急救记录列表")
public class EARecord4AppVo {

	@ApiModelProperty("急救记录id")
	private Long id;
	
	@ApiModelProperty("患者姓名")
	private String name;
	
	@ApiModelProperty("时间(yyyy-MM-dd HH:mm:ss)")
	private String createTime;
	
	@ApiModelProperty("危急分级")
	private String mpdsTitle;
	
	@ApiModelProperty("呼叫主诉")
	private String mainSymptomText;
	
	@ApiModelProperty("任务状态")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
