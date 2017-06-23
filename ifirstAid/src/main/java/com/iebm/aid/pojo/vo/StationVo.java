package com.iebm.aid.pojo.vo;

import org.springframework.beans.BeanUtils;

import com.iebm.aid.pojo.Station;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("急救分站实体")
public class StationVo {

	@ApiModelProperty("急救分站id")	
	private Long id;
	
	@ApiModelProperty("急救分站名称")
	private String stationName;
	
	public StationVo(Station station) {
		BeanUtils.copyProperties(station, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	
	
}
