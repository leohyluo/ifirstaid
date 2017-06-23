package com.iebm.aid.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.iebm.aid.common.BaseService;
import com.iebm.aid.controller.req.EARecord4WebParam;
import com.iebm.aid.controller.req.EditRecordParam;
import com.iebm.aid.controller.req.HistoryRecordParam;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.pojo.vo.EventAidRecord4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordDetail4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordHistory4WebVo;
import com.iebm.aid.pojo.vo.HistoryKeyQVo;
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
	 * 分页查询急救记录的所有修改记录
	 * @return
	 */
	public Page<EventAidRecordHistory4WebVo> findHistory(HistoryRecordParam param);
	
	/**
	 * 修改急救记录
	 * @param userId
	 * @param recordId
	 * @param param
	 */
	void edit(String userId, EditRecordParam param);
	
	/**
	 * 查询最新的修改记录
	 * @param recordId
	 * @return
	 */
	EventAidRecordDetail4WebVo findLastModifyRecord(String recordId);
	
	/**
	 * 从记录中获取已问过的问题与答案
	 * @param recordId
	 * @return
	 */
	List<HistoryKeyQVo> findProcessKeyQ(String recordId);

}
