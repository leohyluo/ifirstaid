package com.iebm.aid.pojo.vo;

import org.springframework.beans.BeanUtils;

import com.iebm.aid.common.GlobalConstants;
import com.iebm.aid.pojo.KeyQ;
import com.iebm.aid.utils.EBMEnDecrypt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("记录详情-急救问答")
public class ProcessKeyqVo {

	@ApiModelProperty("问题id")
	private Integer kqID;
	
	@ApiModelProperty("问题内容")
	private String kqTitle;
	
	@ApiModelProperty("问题解释")
	private String commExplain;
	
	@ApiModelProperty("答案内容")
	private String answer;
	
	@ApiModelProperty("答案id")
	private String answerId;

	public ProcessKeyqVo(KeyQ keyq) {
		BeanUtils.copyProperties(keyq, this);
	}
	
	public Integer getKqID() {
		return kqID;
	}

	public void setKqID(Integer kqID) {
		this.kqID = kqID;
	}

	public String getKqTitle() {
		kqTitle = EBMEnDecrypt.decrypt(kqTitle, GlobalConstants.DECRYPT_CHARSET);
		return kqTitle;
	}

	public void setKqTitle(String kqTitle) {
		this.kqTitle = kqTitle;
	}

	public String getCommExplain() {
		return commExplain;
	}

	public void setCommExplain(String commExplain) {
		this.commExplain = commExplain;
	}

	public String getAnswer() {
		answer = EBMEnDecrypt.decrypt(answer, GlobalConstants.DECRYPT_CHARSET);
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
	
	
}
