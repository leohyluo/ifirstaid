package com.iebm.aid.pojo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iebm.aid.common.BaseEntity;

@Entity
@Table(name = "table_station_event_record")
public class StationMessage extends BaseEntity {

	private static final long serialVersionUID = -652480969493798133L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "station_id")
	private Long stationId;
	
//	@Column(name = "record_id")
//	private Long recordId;
	
	@Column(name = "status")
	private Long status;
	
	@Column(name = "creator")
	private String creator;
	
	@Column(name = "create_time")
	private LocalDateTime createTime;
	
	@Column(name = "reader_id")
	private String readerId;
	
	@Column(name = "read_time")
	private LocalDateTime readTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="record_id", nullable=false)
	private EventAidRecord eventAidRecord;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

//	public Long getRecordId() {
//		return recordId;
//	}
//
//	public void setRecordId(Long recordId) {
//		this.recordId = recordId;
//	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public EventAidRecord getEventAidRecord() {
		return eventAidRecord;
	}

	public void setEventAidRecord(EventAidRecord eventAidRecord) {
		this.eventAidRecord = eventAidRecord;
	}
	
	
}
