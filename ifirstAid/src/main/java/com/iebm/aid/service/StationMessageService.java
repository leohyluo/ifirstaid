package com.iebm.aid.service;

import java.util.List;

import com.iebm.aid.common.BaseService;
import com.iebm.aid.controller.req.MarkReadAidRecordParam;
import com.iebm.aid.controller.req.SearchStationAidRecordsParam;
import com.iebm.aid.pojo.StationMessage;
import com.iebm.aid.pojo.vo.StationAidRecordVo;
import com.iebm.aid.pojo.vo.TokenVo;

public interface StationMessageService extends BaseService<StationMessage, Long> {

	void sendMessageToStation(TokenVo tokenvo, String stationIds, String recordId); 
	
	public int markReadAidRecord(MarkReadAidRecordParam param, String userId);
	
	public long remindCount( String userId);
	
	public List<StationAidRecordVo> searchStationAidRecords(SearchStationAidRecordsParam param, String userId);
	
	List<StationMessage> searchByRecord(Long recordId); 
}
