package com.iebm.aid.controller.req;

import java.util.ArrayList;
import java.util.List;

import com.iebm.aid.common.enums.OperationType;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.pojo.vo.DeltaChange;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.JsonUtils;
import com.iebm.aid.utils.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("修改急救记录实体")
public class EditRecordParam {

	@ApiModelProperty("记录id")
	private String recordId;

	// 呼救主诉id
	@ApiModelProperty("主诉id")
	private String mainSympId;

	// 呼叫主诉
	private String mainSymptomText;

	// 是否是自己呼救
	@ApiModelProperty("是否是自己呼救(1：自己呼救 2：熟人帮助呼救 3：陌生人帮助呼救)")
	private String callType;

	// 是否跟患者在一起
	@ApiModelProperty("是否跟患者在一起 1：是 2:否")
	private String stayWithPatient;

	@ApiModelProperty("患者姓名")
	private String name;

	// 年龄段
	@ApiModelProperty("年龄段")
	private String age;

	// 性别
	@ApiModelProperty("性别 1：男 2：女 3：不详")
	private String gender;

	// 是否有意识
	@ApiModelProperty("是否有意识 1：有意识 2：没有意识 3：不详")
	private String hasAware;

	// 是否有呼吸
	@ApiModelProperty("是否有呼吸 1：有呼吸 2：没有呼吸 3：不详")
	private String hasBreath;

	// 特殊病史id
	@ApiModelProperty("特殊病史id")
	private String specDisId;

	/**
	 * 扩展字段
	 */
	// 事发地点
	@ApiModelProperty("事发地点")
	private String aidAddress;

	// 呼叫人电话
	@ApiModelProperty("呼叫人电话")
	private String aidMobile;

	// 发生了什么
	@ApiModelProperty("发生了什么")
	private String whatHappen;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getMainSympId() {
		return mainSympId;
	}

	public void setMainSympId(String mainSympId) {
		this.mainSympId = mainSympId;
	}

	public String getMainSymptomText() {
		return mainSymptomText;
	}

	public void setMainSymptomText(String mainSymptomText) {
		this.mainSymptomText = mainSymptomText;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getStayWithPatient() {
		return stayWithPatient;
	}

	public void setStayWithPatient(String stayWithPatient) {
		this.stayWithPatient = stayWithPatient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHasAware() {
		return hasAware;
	}

	public void setHasAware(String hasAware) {
		this.hasAware = hasAware;
	}

	public String getHasBreath() {
		return hasBreath;
	}

	public void setHasBreath(String hasBreath) {
		this.hasBreath = hasBreath;
	}

	public String getSpecDisId() {
		return specDisId;
	}

	public void setSpecDisId(String specDisId) {
		this.specDisId = specDisId;
	}

	public String getAidAddress() {
		return aidAddress;
	}

	public void setAidAddress(String aidAddress) {
		this.aidAddress = aidAddress;
	}

	public String getAidMobile() {
		return aidMobile;
	}

	public void setAidMobile(String aidMobile) {
		this.aidMobile = aidMobile;
	}

	public String getWhatHappen() {
		return whatHappen;
	}

	public void setWhatHappen(String whatHappen) {
		this.whatHappen = whatHappen;
	}

	public void mergeCommonInfo(String originalId, EventAidRecord record) {
		List<DeltaChange> deltaList = CollectionUtils.isEmpty(record.getDeltaList()) ? new ArrayList<>() : record.getDeltaList();
		if(StringUtils.isNotEmpty(name)) {
			DeltaChange delta = new DeltaChange("name", record.getName(), this.name);
			deltaList.add(delta);
		}
		if(StringUtils.isNotEmpty(aidAddress)) {
			DeltaChange delta = new DeltaChange("aidAddress", record.getAidAddress(), this.aidAddress);
			deltaList.add(delta);
			record.setAidAddress(aidAddress);
		}
		if(StringUtils.isNotEmpty(whatHappen)) {
			DeltaChange delta = new DeltaChange("whatHappen", record.getWhatHappen(), this.whatHappen);
			deltaList.add(delta);
			record.setWhatHappen(whatHappen);
		}
		if(CollectionUtils.isNotEmpty(deltaList)) {
			record.setOperateType(OperationType.UPDATE.getType());
			record.setOriginalId(originalId);
			record.getDeltaList().addAll(deltaList);
			String deltaChangeStr = JsonUtils.toJsonString(deltaList);
			record.setDeltaChange(deltaChangeStr);
		}
	}
}
