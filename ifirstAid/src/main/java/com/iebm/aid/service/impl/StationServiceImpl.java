package com.iebm.aid.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iebm.aid.common.AbstractService;
import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.Station;
import com.iebm.aid.repository.StationRepository;
import com.iebm.aid.service.StationService;

@Service
public class StationServiceImpl extends AbstractService<Station, Long> implements StationService {

	@Resource
	private StationRepository repository;
	
	@Override
	protected BaseRepository<Station, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	

}
