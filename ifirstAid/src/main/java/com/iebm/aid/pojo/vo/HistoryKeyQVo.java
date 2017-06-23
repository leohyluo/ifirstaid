package com.iebm.aid.pojo.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("已回答过的问题答案实体")
public class HistoryKeyQVo {

	@ApiModelProperty("问题id")
	private Integer kqID;
	
	@ApiModelProperty("问题内容")
	private String kqTitle;
	
	@ApiModelProperty("答案列表")
	List<AnswerVo> answerList;
	
	@ApiModelProperty("已回答的答案id")
	private String originAnswerId;
	
	@ApiModelProperty("已回答的答案内容")
	private String originAnswerText;
	
	@ApiModelProperty("已输入项")
	private String originInput;

	public Integer getKqID() {
		return kqID;
	}

	public void setKqID(Integer kqID) {
		this.kqID = kqID;
	}

	public String getKqTitle() {
		return kqTitle;
	}

	public void setKqTitle(String kqTitle) {
		this.kqTitle = kqTitle;
	}

	public List<AnswerVo> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<AnswerVo> answerList) {
		this.answerList = answerList;
	}

	public String getOriginAnswerId() {
		return originAnswerId;
	}

	public void setOriginAnswerId(String originAnswerId) {
		this.originAnswerId = originAnswerId;
	}

	public String getOriginAnswerText() {
		return originAnswerText;
	}

	public void setOriginAnswerText(String originAnswerText) {
		this.originAnswerText = originAnswerText;
	}

	public String getOriginInput() {
		return originInput;
	}

	public void setOriginInput(String originInput) {
		this.originInput = originInput;
	}
	
	
}
