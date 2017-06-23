package com.iebm.aid.controller.req;

import java.util.StringJoiner;

import com.iebm.aid.pojo.AidFiles;
import com.iebm.aid.pojo.AidRecord;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.utils.JsonUtils;
import com.iebm.aid.utils.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("报案基础信息")
public class BasicInfoReq {
	
	@ApiModelProperty("任务id")
	private String serverId;

	//呼救主诉id
	@ApiModelProperty("主诉id")
	private String mainSymptomId;
	
	//是否是自己呼救
	@ApiModelProperty("是否是自己呼救(1：自己呼救 2：熟人帮助呼救 3：陌生人帮助呼救)")
	private String callType;
	
	//是否跟患者在一起
	@ApiModelProperty("是否跟患者在一起 1：是 2:否")
	private String stayWithPatient;
	
	@ApiModelProperty("患者姓名")
	private String name;
	
	//年龄段
	@ApiModelProperty("年龄段")
	private String age;
	
	//性别
	@ApiModelProperty("性别 1：男 2：女 3：不详")
	private String gender;
	
	//是否有意识
	@ApiModelProperty("是否有意识 1：有意识 2：没有意识 3：不详")
	private String hasAware;
	
	//是否有呼吸
	@ApiModelProperty("是否有呼吸 1：有呼吸 2：没有呼吸 3：不详")
	private String hasBreath;
	
	//特殊病史id
	@ApiModelProperty("特殊病史id")
	private String specDisId;
	
	/**
	 * 扩展字段
	 */
	//事发地点
	@ApiModelProperty("事发地点")
	private String aidAddress;
	
	//呼叫人电话
	@ApiModelProperty("呼叫人电话")
	private String aidMobile;
	
	//发生了什么
	@ApiModelProperty("发生了什么")
	private String whatHappen;

	public String getMainSymptomId() {
		return mainSymptomId;
	}

	public void setMainSymptomId(String mainSymptomId) {
		this.mainSymptomId = mainSymptomId;
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

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return JsonUtils.toJsonString(this);
	}
	
	public String toCommonString() {
		// 是否是自己呼救；是否跟患者在一起；年龄段；性别；是否有意识；是否有呼吸；特殊病史id
		String callType = this.getCallType();
		String stayWithPatient = this.getStayWithPatient();
		String age = this.getAge();
		String gender = this.getGender();
		String hasAware = this.getHasAware();
		String hasBreath = this.getHasBreath();
		String specDiseaseId = this.getSpecDisId();

		StringJoiner joiner = new StringJoiner(",");
		// 1：自己呼救 2：熟人帮助呼救 3：陌生人帮助呼救
		callType = StringUtils.isEmpty(callType) ? "0" : callType;
		// 1：跟患者在一起 2:不跟患者在一起
		stayWithPatient = StringUtils.isEmpty(stayWithPatient) ? "0" : stayWithPatient;
		// 1：青少年（<=12岁）2：中年（<=60岁）3：老年（>60岁）4：婴幼儿（<=1岁）
		if (StringUtils.isNotEmpty(age)) {
			int ageInt = getAge(age);
			if (ageInt <= 1) {
				age = "4";
			} else if (ageInt <= 12) {
				age = "1";
			} else if (ageInt <= 60) {
				age = "2";
			} else if (ageInt > 60) {
				age = "3";
			} else {
				age = "0";
			}
		} else {
			age = "0";
		}
		// 1：男 2：女 3：不详
		gender = StringUtils.isEmpty(gender) ? "0" : gender;
		// 1：有意识 2：没有意识 3：不详
		hasAware = StringUtils.isEmpty(hasAware) ? "0" : hasAware;
		// 1：有呼吸 2：没有呼吸 3：不详
		hasBreath = StringUtils.isEmpty(hasBreath) ? "0" : hasBreath;
		// 特殊病史id
		specDiseaseId = StringUtils.isEmpty(specDiseaseId) ? "0" : specDiseaseId;

		joiner.add(callType).add(stayWithPatient).add(age).add(gender).add(hasAware).add(hasBreath).add(specDiseaseId);
		String result = joiner.toString();
		return result;
	}
	
	public AidRecord parseToAidRecord() {
		AidRecord record = new AidRecord();
		
		record.setAidAddress(StringUtils.string(this.getAidAddress()));
		record.setAidMobile(StringUtils.string(this.getAidMobile()));
		record.setWhatHappen(StringUtils.string(this.getWhatHappen()));
		if(StringUtils.isNotEmpty(this.getStayWithPatient())) {
			String withPatientStr = "";
			if("1".equals(this.getStayWithPatient())) {
				withPatientStr = "是";
			} else if ("2".equals(this.getStayWithPatient())) {
				withPatientStr = "否";
			}
			record.setWithPatient(withPatientStr);
		}
		if(StringUtils.isNotEmpty(this.getCallType())) {
			String callTypeStr = "";
			if("1".equals(this.getCallType())) {
				callTypeStr = "自己呼救";
			} else if ("2".equals(this.getCallType())) {
				callTypeStr = "熟人帮助呼救";
			} else if ("3".equals(this.getCallType())) {
				callTypeStr = "陌生人帮助呼救";
			}
			record.setCallType(callTypeStr);
		}
		record.setName(this.getName());
		record.setAge(this.getAge());
		if(StringUtils.isNotEmpty(this.getHasAware())) {
			String awareStr = "";
			if("1".equals(this.getHasAware())) {
				awareStr = "有意识";
			} else if ("2".equals(this.getHasAware())) {
				awareStr = "无意识";
			} else if ("3".equals(this.getHasAware())) {
				awareStr = "不详";
			}
			record.setHasAware(awareStr);
		}
		if(StringUtils.isNotEmpty(this.getGender())) {
			String genderStr = "";
			if("1".equals(this.getGender())) {
				genderStr = "男";
			} else if ("2".equals(this.getGender())) {
				genderStr = "女";
			} else if ("3".equals(this.getGender())) {
				genderStr = "不详";
			}
			record.setGender(genderStr);
		}
		if(StringUtils.isNotEmpty(this.getHasBreath())) {
			String breathStr = "";
			if("1".equals(this.getHasBreath())) {
				breathStr = "有呼吸";
			} else if ("2".equals(this.getHasBreath())) {
				breathStr = "无呼吸";
			} else if ("3".equals(this.getHasBreath())) {
				breathStr = "不详";
			}
			record.setHasBreath(breathStr);
		}

		return record;
	}
	
	public void fillEventAidRecord(EventAidRecord e) {
		
		e.setAidAddress(StringUtils.string(this.getAidAddress()));
		e.setAidMobile(StringUtils.string(this.getAidMobile()));
		e.setWhatHappen(StringUtils.string(this.getWhatHappen()));
//		if(StringUtils.isNotEmpty(this.getStayWithPatient())) {
//			String withPatientStr = "";
//			if("1".equals(this.getStayWithPatient())) {
//				withPatientStr = "是";
//			} else if ("2".equals(this.getStayWithPatient())) {
//				withPatientStr = "否";
//			}
//			e.setWithPatient(withPatientStr);
//		}
		e.setWithPatient(this.getStayWithPatient());
		
//		if(StringUtils.isNotEmpty(this.getCallType())) {
//			String callTypeStr = "";
//			if("1".equals(this.getCallType())) {
//				callTypeStr = "自己呼救";
//			} else if ("2".equals(this.getCallType())) {
//				callTypeStr = "熟人帮助呼救";
//			} else if ("3".equals(this.getCallType())) {
//				callTypeStr = "陌生人帮助呼救";
//			}
//			e.setCallType(callTypeStr);
//		}
		e.setCallType(this.getCallType());
		
		e.setName(this.getName());
		e.setAge(this.getAge());
//		if(StringUtils.isNotEmpty(this.getHasAware())) {
//			String awareStr = "";
//			if("1".equals(this.getHasAware())) {
//				awareStr = "有意识";
//			} else if ("2".equals(this.getHasAware())) {
//				awareStr = "无意识";
//			} else if ("3".equals(this.getHasAware())) {
//				awareStr = "不详";
//			}
//			e.setHasAware(awareStr);
//		}
		e.setHasAware(this.getHasAware());
		
//		if(StringUtils.isNotEmpty(this.getGender())) {
//			String genderStr = "";
//			if("1".equals(this.getGender())) {
//				genderStr = "男";
//			} else if ("2".equals(this.getGender())) {
//				genderStr = "女";
//			} else if ("3".equals(this.getGender())) {
//				genderStr = "不详";
//			}
//			e.setGender(genderStr);
//		}
		e.setGender(this.getGender());
		
//		if(StringUtils.isNotEmpty(this.getHasBreath())) {
//			String breathStr = "";
//			if("1".equals(this.getHasBreath())) {
//				breathStr = "有呼吸";
//			} else if ("2".equals(this.getHasBreath())) {
//				breathStr = "无呼吸";
//			} else if ("3".equals(this.getHasBreath())) {
//				breathStr = "不详";
//			}
//			e.setHasBreath(breathStr);
//		}
		e.setHasBreath(this.getHasBreath());
	}
	
	public AidFiles parseToAidFiles() {
		AidFiles aidFiles = new AidFiles();
		aidFiles.setUserName(this.getName());
		aidFiles.setGender(this.getGender());
		return aidFiles;
	}
	
	private int getAge(String input) {
		String unit = input.substring(input.length() - 1);
		int inputAge = Integer.parseInt(input.substring(0, input.length() - 1));
		int result = 0;
		if("岁".equals(unit)) {
			result = inputAge;
		} else if ("月".equals(unit)) {
			result = Math.round(inputAge / 12);
		} else if ("天".equals(unit)) {
			result = Math.round(inputAge / 365);
		}
		return result;
	}
}
