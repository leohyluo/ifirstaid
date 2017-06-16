package com.iebm.aid.controller.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 患者信息
 * @author Administrator
 */
@ApiModel("患者信息")
public class PatientParam {

	@ApiModelProperty("患者姓名")
	private String userName;
	
	@ApiModelProperty("患者年龄")
	private String age;
	
	@ApiModelProperty("年龄单位")
	private String ageUnit;
	
	@ApiModelProperty("患者性别")
	private String gender;
	
	@ApiModelProperty("事发地点")
	private String address;
	
	@ApiModelProperty("患者联系方式")
	private String mobile;
	
	public String getAgeUnit() {
		return ageUnit;
	}
	public void setAgeUnit(String ageUnit) {
		this.ageUnit = ageUnit;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
