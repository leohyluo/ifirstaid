package com.iebm.aid.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iebm.aid.common.AbstractService;
import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.Station;
import com.iebm.aid.pojo.vo.StationVo;
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

	@Override
	public List<StationVo> findAllStation() {
		return Optional.ofNullable(findAll()).orElseGet(ArrayList::new).stream().map(StationVo::new)
				.collect(Collectors.toList());
	}

}
