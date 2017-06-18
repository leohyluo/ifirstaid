package com.iebm.aid.repository;

import org.springframework.stereotype.Repository;

import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.Station;

@Repository
public interface StationRepository extends BaseRepository<Station, Long> {

}
