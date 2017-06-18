package com.iebm.aid.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.iebm.aid.common.BaseService;
import com.iebm.aid.controller.req.EARecord4WebParam;
import com.iebm.aid.controller.req.EditRecordParam;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.pojo.vo.EventAidRecord4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordDetail4WebVo;
import com.iebm.aid.pojo.vo.PlanVo;
import com.iebm.aid.pojo.vo.TokenVo;

public interface EventAidRecordService extends BaseService<EventAidRecord, Long> {
	
	/**
	 * 保存急救记录
	 * @param eventId
	 * @param serverId
	 * @param planvoList
	 * @param tokenVo
	 */
	Long saveEventAidRecord(String eventId, String serverId, List<PlanVo> planvoList, TokenVo tokenVo);
	
	/**
	 * 查询急救记录详情
	 * @param id
	 * @return
	 */
	EventAidRecordDetail4WebVo getDetail(Long id);
	
	/**
	 * 分页查询
	 * @return
	 */	
	public Page<EventAidRecord4WebVo> findByPage(EARecord4WebParam param, String userId);
	
	/**
	 * 修改急救记录
	 * @param userId
	 * @param recordId
	 * @param param
	 */
	void edit(String userId, EditRecordParam param);
}
