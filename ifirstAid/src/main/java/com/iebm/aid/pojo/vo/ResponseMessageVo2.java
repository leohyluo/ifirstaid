package com.iebm.aid.pojo.vo;

import java.util.List;

import com.iebm.aid.pojo.Mpds;
import com.iebm.aid.web.ResponseStatus;

import io.swagger.annotations.ApiModel;

@ApiModel("问题或预案实体")
public class ResponseMessageVo2 {
	
	private int code;
	private QuesOrPlan data;
	private String msg;
	private Long recordId;
		
	public ResponseMessageVo2(String type, List<KeyQVo> keyqList, List<PlanVo> planList, Mpds mpds, Long recordId) {
		this.code = ResponseStatus.SUCCESS.code();
		this.data = new QuesOrPlan(type, keyqList, planList, mpds, recordId);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public QuesOrPlan getData() {
		return data;
	}

	public void setData(QuesOrPlan data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
}

class QuesOrPlan {
	private String type;
	private List<KeyQVo> keyqList;
	private List<PlanVo> planList;
	private Mpds mpds;
	private Long recordId;
	
	public QuesOrPlan(String type, List<KeyQVo> keyqList, List<PlanVo> planList, Mpds mpds, Long recordId) {
		this.type = type;
		this.keyqList = keyqList;
		this.planList = planList;
		this.mpds = mpds;
		this.recordId = recordId;
	}
	
	public List<KeyQVo> getKeyqList() {
		return keyqList;
	}
	public void setKeyqList(List<KeyQVo> keyqList) {
		this.keyqList = keyqList;
	}
	public List<PlanVo> getPlanList() {
		return planList;
	}
	public void setPlanList(List<PlanVo> planList) {
		this.planList = planList;
	}
	public Mpds getMpds() {
		return mpds;
	}
	public void setMpds(Mpds mpds) {
		this.mpds = mpds;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
}
