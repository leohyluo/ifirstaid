package com.iebm.aid.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iebm.aid.common.BaseEntity;
import com.iebm.aid.pojo.vo.KeyqDelta;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.JsonUtils;

@Entity
@Table(name = "table_cachekeyq")
public class CacheKeyQ extends BaseEntity {


	private static final long serialVersionUID = 5599715086100554999L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	//客户端-呼救任务id
	@Column(name = "userID")
	private String userID;
		
	//呼救主诉id
	@Column(name = "sympID")
	private String sympID;
	
	//患者基本信息，共包含7项数据，用“/”分隔，各数据依次分别为：是否是自己呼救；是否跟患者在一起；年龄段；性别；是否有意识；是否有呼吸；特殊病史id。
	@Column(name = "baseInfos")
	private String baseInfos;
	
	//该请求-对应的所有的问题id，用“/”分隔
	@Column(name = "allKeyQIDs")
	private String allKeyQIDs;
	
	//该请求-对应的所有问题的类型，用“/”分隔
	@Column(name = "allKeyQTypes")
	private String allKeyQTypes;
	
	//问题 引申问题id
	@Column(name = "processKeyQIDs")
	private String processKeyQIDs;
	
	//已问过问题的id，用“,”分隔
	@Column(name = "allKQForwardIDs")
	private String allKQForwardIDs;
	
	//已问过问题的答案id，用“,”分隔
	@Column(name = "processAnswerIDs")
	private String processAnswerIDs;
	
	//自己填写的信息，用“,”分隔
	@Column(name = "processAnswerTexts")
	private String processAnswerTexts;
	
	//请求的时间
	@Column(name = "dateTime")
	private String dateTime;
	
	//服务端-呼救任务ID
	@Column(name = "serverID")
	private String serverId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getSympID() {
		return sympID;
	}

	public void setSympID(String sympID) {
		this.sympID = sympID;
	}

	public String getBaseInfos() {
		return baseInfos;
	}

	public void setBaseInfos(String baseInfos) {
		this.baseInfos = baseInfos;
	}
	
	public String getAllKeyQIDs() {
		return allKeyQIDs;
	}

	public void setAllKeyQIDs(String allKeyQIDs) {
		this.allKeyQIDs = allKeyQIDs;
	}

	public String getAllKeyQTypes() {
		return allKeyQTypes;
	}

	public void setAllKeyQTypes(String allKeyQTypes) {
		this.allKeyQTypes = allKeyQTypes;
	}

	public String getProcessKeyQIDs() {
		return processKeyQIDs;
	}

	public void setProcessKeyQIDs(String processKeyQIDs) {
		this.processKeyQIDs = processKeyQIDs;
	}

	public String getAllKQForwardIDs() {
		return allKQForwardIDs;
	}

	public void setAllKQForwardIDs(String allKQForwardIDs) {
		this.allKQForwardIDs = allKQForwardIDs;
	}

	public String getProcessAnswerIDs() {
		return processAnswerIDs;
	}

	public void setProcessAnswerIDs(String processAnswerIDs) {
		this.processAnswerIDs = processAnswerIDs;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getProcessAnswerTexts() {
		return processAnswerTexts;
	}

	public void setProcessAnswerTexts(String processAnswerTexts) {
		this.processAnswerTexts = processAnswerTexts;
	}
	
	public void merge(EventAidRecord record) {
		String oldKqIds = record.getProcessKeyQIDs();
		String oldAnswerIds = record.getProcessAnswerIDs();
		String oldInputTexts = record.getProcessAnswerTexts();
		
		List<KeyqDelta> keyqDeltaList = new ArrayList<>(); 
		Map<Integer, KeyqDelta> oldProcessMap = toMap(oldKqIds, oldAnswerIds, oldInputTexts);
		Map<Integer, KeyqDelta> newProcessMap = toMap(processKeyQIDs, processAnswerIDs, processAnswerTexts);
		for(Integer kqId : newProcessMap.keySet()) {
			KeyqDelta newKeyq = newProcessMap.get(kqId);
			if(oldProcessMap.containsKey(kqId)) {
				KeyqDelta oldKeyq = oldProcessMap.get(kqId);
				if(newKeyq.equals(oldKeyq) == false) {
					keyqDeltaList.add(newKeyq);
				}
			}
		}
		String keyqDelta = "";
		if(CollectionUtils.isNotEmpty(keyqDeltaList)) {
			keyqDelta = JsonUtils.toJsonString(keyqDeltaList);
		}
		record.setKeyqDelta(keyqDelta);
	}
	
	private Map<Integer, KeyqDelta> toMap(String kqIds, String answerIds, String inputTexts) {
		List<Integer> kqIdList = Stream.of(kqIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		List<String> answerIdList = Stream.of(answerIds.split(",")).collect(Collectors.toList());
		List<String> inputTextList = Stream.of(inputTexts.split(",", -1)).collect(Collectors.toList());
		if(kqIdList.size() != answerIdList.size() || kqIdList.size() != inputTextList.size()) {
			System.out.println("kqIds,answerIds,inputTexts 参数个数不一致");
			return null;
		}
		List<KeyqDelta> deltaList = new ArrayList<>();
		for(int i = 0; i < kqIdList.size(); i++) {
			Integer KqId = kqIdList.get(i);
			String answerId = answerIdList.get(i);
			String inputText = inputTextList.get(i);
			KeyqDelta delta = new KeyqDelta(KqId, answerId, inputText);
			deltaList.add(delta);
		}
		Map<Integer, KeyqDelta> map = deltaList.stream().collect(Collectors.toMap(KeyqDelta::getKqID, Function.identity()));
		return map;
	}
	
}
