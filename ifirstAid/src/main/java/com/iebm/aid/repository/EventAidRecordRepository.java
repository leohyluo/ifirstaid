package com.iebm.aid.repository;

import org.springframework.stereotype.Repository;

import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.EventAidRecord;

@Repository
public interface EventAidRecordRepository extends BaseRepository<EventAidRecord, Long> {

}
