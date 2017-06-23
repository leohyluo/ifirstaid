package com.iebm.aid.service;

import java.util.List;

import com.iebm.aid.common.BaseService;
import com.iebm.aid.pojo.Station;
import com.iebm.aid.pojo.vo.StationVo;

public interface StationService extends BaseService<Station, Long> {

	List<StationVo> findAllStation();
}
