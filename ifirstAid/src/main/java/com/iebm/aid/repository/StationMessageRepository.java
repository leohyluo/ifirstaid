package com.iebm.aid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.pojo.StationMessage;

@Repository
public interface StationMessageRepository extends BaseRepository<StationMessage, Long> {
	@Modifying
	@Query("update StationMessage sm set sm.status = 1 where sm.status=0 and sm.id = ?1 and sm.stationId=?2")
	int setStatus(Long stationAidRecordId,Long stationId);
	
	@Modifying
	@Query("update StationMessage sm set sm.eventAidRecord.id = ?1 where sm.eventAidRecord.id=?2 and sm.stationId=?3")
	int changeRecordId(Long newId,Long recordId,Long stationId);
	
	List<StationMessage> findByEventAidRecord(EventAidRecord eventAidRecord);
}
