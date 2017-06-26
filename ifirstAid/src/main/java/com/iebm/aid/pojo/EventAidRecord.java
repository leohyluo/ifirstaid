package com.iebm.aid.pojo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import com.iebm.aid.common.BaseEntity;
import com.iebm.aid.pojo.vo.DeltaChange;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.StringUtils;

@Entity
@Table(name = "table_event_aid_record")
public class EventAidRecord extends BaseEntity {

	private static final long serialVersionUID = -6327009243660883114L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	//事件编号
	@Column(name = "eventNo")
	private String eventNo;
	
	//座席号
	@Column(name = "seatNo")
	private String seatNo;
	
	//调度员
	@Column(name = "dispatcher")
	private String dispatcher;
	
	@Column(name = "main_sympId")
	private String mainSympId;
	
	@Column(name = "main_sympName")
	private String mainSymptomText;
	
	@Column(name = "cure_process")
	private String cureProcess;
	
	@Column(name = "create_time")
	private LocalDateTime createTime;
	
	//CacheKeyq对象serverId字段
	@Column(name = "server_id")
	private String serverId;
	
	@Column(name = "allKeyQIDs")
	private String allKeyQIDs;
	
	//已问过问题的id，用“,”分隔
	@Column(name = "processKeyQIDs")
	private String processKeyQIDs;
	
	//已问过问题的答案id，用“,”分隔
	@Column(name = "processAnswerIDs")
	private String processAnswerIDs;
	
	//自己填写的信息，用“,”分隔
	@Column(name = "processAnswerTexts")
	private String processAnswerTexts;
	
	//生成预案id集合
	@Column(name = "plan_ids")
	private String planIds;
	
	//事发地点
	@Column(name = "aid_address")
	private String aidAddress;
	
	//呼叫人电话
	@Column(name = "aid_mobile")
	private String aidMobile;
	
	@Column(name = "what_happen")
	private String whatHappen;
	
	//是否与患者一起
	@Column(name = "with_patient")
	private String withPatient;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "gender")
	private String gender;
	
	//患者年龄
	@Column(name = "age")
	private String age;
	
	//是否有意识
	@Column(name = "has_aware")
	private String hasAware;
	
	//是否有呼吸
	@Column(name = "has_breath")
	private String hasBreath;
	
	@Column(name = "call_type")
	private String callType;

	@Column(name = "creator")
	private String creator;
	
	//pad或web
	@Column(name = "app_type")
	private String appType;
	
	//原始数据id
	@Column(name = "original_id")
	private String originalId;
	
	//0新增 1修改
	@Column(name = "operate_type")
	private String operateType;
	
	//发生改变的问题或答案
	@Column(name = "keyq_delta")
	private String keyqDelta;
	
	//发生变化的数据
	@Column(name = "delta_change")
	private String deltaChange;
	
	//危急分级
	@Column(name = "mpds_title")
	private String mpdsTitle;
	
	@Column(name = "root_id")
	private String rootId;
	
	@Transient
	private List<DeltaChange> deltaList;
	
	public EventAidRecord clone(){
		EventAidRecord newEntity = new EventAidRecord();
		BeanUtils.copyProperties(this, newEntity);
		if(StringUtils.isEmpty(rootId)) {
			newEntity.setRootId(String.valueOf(id));
		} else {
			newEntity.setRootId(rootId);
		}
		newEntity.setOriginalId(String.valueOf(id));
		newEntity.setId(null);
		newEntity.setDeltaChange(null);
		newEntity.setKeyqDelta(null);
		return newEntity;
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

	public String getCureProcess() {
		return cureProcess;
	}

	public void setCureProcess(String cureProcess) {
		this.cureProcess = cureProcess;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getPlanIds() {
		return planIds;
	}

	public void setPlanIds(String planIds) {
		this.planIds = planIds;
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

	public String getWithPatient() {
		return withPatient;
	}

	public void setWithPatient(String withPatient) {
		this.withPatient = withPatient;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public String getWhatHappen() {
		return whatHappen;
	}

	public void setWhatHappen(String whatHappen) {
		this.whatHappen = whatHappen;
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

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
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

	public String getMainSympId() {
		return mainSympId;
	}

	public void setMainSympId(String mainSympId) {
		this.mainSympId = mainSympId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getProcessKeyQIDs() {
		return processKeyQIDs;
	}

	public void setProcessKeyQIDs(String processKeyQIDs) {
		this.processKeyQIDs = processKeyQIDs;
	}

	public String getProcessAnswerIDs() {
		return processAnswerIDs;
	}

	public void setProcessAnswerIDs(String processAnswerIDs) {
		this.processAnswerIDs = processAnswerIDs;
	}

	public String getProcessAnswerTexts() {
		return processAnswerTexts;
	}

	public void setProcessAnswerTexts(String processAnswerTexts) {
		this.processAnswerTexts = processAnswerTexts;
	}

	public List<DeltaChange> getDeltaList() {
		if(CollectionUtils.isEmpty(deltaList)) {
			deltaList = new ArrayList<>();
		}
		return deltaList;
	}

	public void setDeltaList(List<DeltaChange> deltaList) {
		this.deltaList = deltaList;
	}

	public String getDeltaChange() {
		return deltaChange;
	}

	public void setDeltaChange(String deltaChange) {
		this.deltaChange = deltaChange;
	}

	public String getMpdsTitle() {
		return mpdsTitle;
	}

	public void setMpdsTitle(String mpdsTitle) {
		this.mpdsTitle = mpdsTitle;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String getAllKeyQIDs() {
		return allKeyQIDs;
	}

	public void setAllKeyQIDs(String allKeyQIDs) {
		this.allKeyQIDs = allKeyQIDs;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getKeyqDelta() {
		return keyqDelta;
	}

	public void setKeyqDelta(String keyqDelta) {
		this.keyqDelta = keyqDelta;
	}
	
	
}
