package com.iebm.aid.pojo.vo;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("答案实体")
public class AnswerVo {
	
	@JsonIgnore
	@ApiModelProperty(name ="问题id", hidden=true)
	private Integer kqID;
	
	@ApiModelProperty("问题解释")
	private String commExplain;
	
	@ApiModelProperty("答案内容")
	private String answer;
	
	@ApiModelProperty("答案id")
	private String answerId;
	
	@ApiModelProperty("问题类型,1表示需添加备注的问题")
	private String kqType;
	
	@ApiModelProperty("引申问题id")
	private String forwardId;
	
	public AnswerVo(KeyQVo obj) {
		BeanUtils.copyProperties(obj, this);
	}
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public String getKqType() {
		return kqType;
	}

	public void setKqType(String kqType) {
		this.kqType = kqType;
	}

	public String getForwardId() {
		return forwardId;
	}

	public void setForwardId(String forwardId) {
		this.forwardId = forwardId;
	}

	public String getCommExplain() {
		return commExplain;
	}

	public void setCommExplain(String commExplain) {
		this.commExplain = commExplain;
	}

	public Integer getKqID() {
		return kqID;
	}

	public void setKqID(Integer kqID) {
		this.kqID = kqID;
	}
	
}
