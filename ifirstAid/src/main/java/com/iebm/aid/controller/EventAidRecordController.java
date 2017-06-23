package com.iebm.aid.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iebm.aid.controller.req.EARecord4WebParam;
import com.iebm.aid.controller.req.EditRecordParam;
import com.iebm.aid.controller.req.HistoryRecordParam;
import com.iebm.aid.controller.req.MarkReadAidRecordParam;
import com.iebm.aid.controller.req.SearchRecordDetailParam;
import com.iebm.aid.controller.req.SearchStationAidRecordsParam;
import com.iebm.aid.pojo.vo.EventAidRecord4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordDetail4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordHistory4WebVo;
import com.iebm.aid.pojo.vo.HistoryKeyQVo;
import com.iebm.aid.pojo.vo.StationAidRecordVo;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.service.EventAidRecordService;
import com.iebm.aid.service.StationMessageService;
import com.iebm.aid.utils.StringUtils;
import com.iebm.aid.utils.VerifyUtils;
import com.iebm.aid.web.ResponseMessage;
import com.iebm.aid.web.ResponseStatus;
import com.iebm.aid.web.WebUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description="决策记录")
@RestController
@RequestMapping("/app/eventAidRecord")
public class EventAidRecordController {
	
	@Resource
	private EventAidRecordService eventAidRecordService;
	@Resource
	private StationMessageService stationMessageService;
	
	@ApiOperation(value = "H5端搜索急救记录列表", notes = "H5端搜索急救记录列表", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/web/search")
	public ResponseMessage searchForWeb(@RequestBody EARecord4WebParam param, HttpServletRequest request) {
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		Page<EventAidRecord4WebVo> page = eventAidRecordService.findByPage(param, tokenVo.getUserId());
		return WebUtils.buildSuccessResponseMessage(page);
	}
	
	@ApiOperation(value = "H5端查询修改历史记录", notes = "H5端查询修改历史记录", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/web/history")
	public ResponseMessage historyForWeb(@RequestBody HistoryRecordParam param) {
		Page<EventAidRecordHistory4WebVo> page = eventAidRecordService.findHistory(param);
		return WebUtils.buildSuccessResponseMessage(page);
	}
	
	@ApiOperation(value = "App决策记录查询", notes = "App决策记录查询", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/app/search")
	public ResponseMessage searchForApp(@RequestBody SearchStationAidRecordsParam param, HttpServletRequest request) {
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		List<StationAidRecordVo> page = stationMessageService.searchStationAidRecords(param, tokenVo.getUserId());
		Map rMap=new HashMap();
		rMap.put("content", page);
		return WebUtils.buildSuccessResponseMessage(rMap);
	}
	
	@ApiOperation(value = "app呼叫任务提醒", notes = "app呼叫任务提醒", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/app/remind")
	public ResponseMessage remindCount(HttpServletRequest request) {
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		long count =stationMessageService.remindCount( tokenVo.getUserId());
		Map rMap=new HashMap();
		rMap.put("count", count);
		return WebUtils.buildSuccessResponseMessage(rMap);
	}
	
	@ApiOperation(value = "App标记已读决策记录", notes = "App标记已读决策记录", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/app/markReadAidRecord")
	public ResponseMessage markReadAidRecord(@RequestBody MarkReadAidRecordParam param,HttpServletRequest request) {
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		if(StringUtils.isEmpty(param.getStationAidRecordId())){
			return WebUtils.buildResponseMessage(ResponseStatus.MOBILE_ALREADY_EXISTS);
		}
		stationMessageService.markReadAidRecord(param, tokenVo.getUserId());
		return WebUtils.buildResponseMessage(ResponseStatus.SUCCESS);
	}
	
	@ApiOperation(value = "查看急救记录详情", notes="查看急救记录详情", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "客户端token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/web/showDetail")
	public ResponseMessage getDetail(@RequestBody SearchRecordDetailParam param) {
		String id = param.getId();
		if(StringUtils.isEmpty(id)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		EventAidRecordDetail4WebVo detailVo = eventAidRecordService.getDetail(Long.valueOf(id));
		return WebUtils.buildSuccessResponseMessage(detailVo);
	}
	
	@ApiOperation(value = "修改急救记录", notes="修改急救记录", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "客户端token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/edit")
	public ResponseMessage edit(@RequestBody EditRecordParam param, HttpServletRequest request) {
		String recordId = param.getRecordId();
		if(StringUtils.isEmpty(recordId)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		eventAidRecordService.edit(tokenVo.getUserId(), param);
		return WebUtils.buildSuccessResponseMessage();
	}
	
	@ApiOperation(value = "查看最新急救记录详情", notes="查看最新急救记录详情", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "客户端token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/web/showLastRecordDetail")
	public ResponseMessage showLastRecordDetail(@RequestBody SearchRecordDetailParam param){
		String id = param.getId();
		if(StringUtils.isEmpty(id)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		EventAidRecordDetail4WebVo detailVo = eventAidRecordService.findLastModifyRecord(id);
		return WebUtils.buildSuccessResponseMessage(detailVo);
	}
	
	@ApiOperation(value = "获取已回答的问题与答案", notes="获取已回答的问题与答案", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "客户端token", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping("/getProcessQuestion")
	public ResponseMessage getProcessQuestion(@RequestBody SearchRecordDetailParam param) {
		String recordId = param.getId();
		if(VerifyUtils.isEmpty(recordId)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		List<HistoryKeyQVo> list = eventAidRecordService.findProcessKeyQ(recordId);
		return WebUtils.buildSuccessResponseMessage(list);
	}
}
