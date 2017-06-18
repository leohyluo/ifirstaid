package com.iebm.aid.repository;

import org.springframework.stereotype.Repository;

import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.StationMessage;

@Repository
public interface StationMessageRepository extends BaseRepository<StationMessage, Long> {

}
