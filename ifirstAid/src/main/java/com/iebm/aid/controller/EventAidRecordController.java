package com.iebm.aid.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iebm.aid.controller.req.EARecord4WebParam;
import com.iebm.aid.pojo.vo.EventAidRecord4WebVo;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.service.EventAidRecordService;
import com.iebm.aid.web.ResponseMessage;
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
	
	@ApiOperation(value = "H5端搜索急救记录", notes = "H5端搜索急救记录")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/web/search")
	public ResponseMessage searchForWeb(@RequestBody EARecord4WebParam param, HttpServletRequest request) {
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		Page<EventAidRecord4WebVo> page =eventAidRecordService.findByPage(param, tokenVo.getUserId());
		return WebUtils.buildSuccessResponseMessage(page);
	}
}
