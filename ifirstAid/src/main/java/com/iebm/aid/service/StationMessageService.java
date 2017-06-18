package com.iebm.aid.service;

import com.iebm.aid.common.BaseService;
import com.iebm.aid.pojo.StationMessage;
import com.iebm.aid.pojo.vo.TokenVo;

public interface StationMessageService extends BaseService<StationMessage, Long> {

	void sendMessageToStation(TokenVo tokenvo, String stationIds, String recordId); 
}
