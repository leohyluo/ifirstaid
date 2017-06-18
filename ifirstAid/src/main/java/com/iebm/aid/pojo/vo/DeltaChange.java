package com.iebm.aid.pojo.vo;

import com.iebm.aid.utils.JsonUtils;

public class DeltaChange {

	private String fieldName;
	private String oldValue;
	private String newValue;
	
	public DeltaChange(String fieldName, String oldValue, String newValue) {
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
	public String toJson(){
		return JsonUtils.toJsonString(this);
	}
}
