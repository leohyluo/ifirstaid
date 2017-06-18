package com.iebm.aid.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iebm.aid.common.AbstractService;
import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.common.enums.MessageStatus;
import com.iebm.aid.pojo.StationMessage;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.repository.StationMessageRepository;
import com.iebm.aid.service.StationMessageService;

@Service
@Transactional
public class StationMessageServiceImpl extends AbstractService<StationMessage, Long> implements StationMessageService {

	@Resource
	private StationMessageRepository repository;
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void sendMessageToStation(TokenVo tokenvo, String stationIds, String recordId) {
		List<String> stationIdList = Arrays.asList(stationIds.split(","));
		List<StationMessage> stationMsgList = new ArrayList<>();
		stationIdList.forEach(e->{
			StationMessage message = new StationMessage();
			message.setStationId(Long.valueOf(e));
			message.setRecordId(Long.valueOf(recordId));
			message.setStatus(MessageStatus.UNREAD.getStatus());
			message.setCreator(tokenvo.getUserId());
			stationMsgList.add(message);
		});
		save(stationMsgList);
	}

	@Override
	protected BaseRepository<StationMessage, Long> getRepository() {
		return repository;
	}	
}
