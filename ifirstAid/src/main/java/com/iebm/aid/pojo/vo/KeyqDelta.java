package com.iebm.aid.pojo.vo;

public class KeyqDelta {

	private Integer kqID;
	private String answerId;
	private String commExplain;
	
	public KeyqDelta(Integer kqID, String answerId, String commExplain) {
		this.kqID = kqID;
		this.answerId = answerId;
		this.commExplain = commExplain;
	}
	
	public Integer getKqID() {
		return kqID;
	}
	public void setKqID(Integer kqID) {
		this.kqID = kqID;
	}
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}
	public String getCommExplain() {
		return commExplain;
	}
	public void setCommExplain(String commExplain) {
		this.commExplain = commExplain;
	}
	
	@Override
	public boolean equals(Object obj) {
		KeyqDelta delta = (KeyqDelta) obj;
		if(kqID == delta.getKqID() && answerId.equals(delta.getAnswerId()) && commExplain.equals(delta.getCommExplain())) {
			return true;
		}
		return false;
	}
}
