package com.iebm.aid.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iebm.aid.common.DataPool;
import com.iebm.aid.controller.req.BasicInfoReq;
import com.iebm.aid.controller.req.EventParam;
import com.iebm.aid.controller.req.KeyQParam;
import com.iebm.aid.controller.req.SendPlanParam;
import com.iebm.aid.controller.req.third.EventRecordParam;
import com.iebm.aid.pojo.CacheKeyQ;
import com.iebm.aid.pojo.MainSymptom;
import com.iebm.aid.pojo.Mpds;
import com.iebm.aid.pojo.vo.InitialVo;
import com.iebm.aid.pojo.vo.KeyQVo;
import com.iebm.aid.pojo.vo.MainSymptomVo;
import com.iebm.aid.pojo.vo.PlanVo;
import com.iebm.aid.pojo.vo.ResponseMessageVo;
import com.iebm.aid.pojo.vo.ResponseMessageVo2;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.service.AidRecordService;
import com.iebm.aid.service.CacheKeyQService;
import com.iebm.aid.service.EventAidRecordService;
import com.iebm.aid.service.EventRecordService;
import com.iebm.aid.service.KeyQService;
import com.iebm.aid.service.MainSymptomService;
import com.iebm.aid.service.MpdsService;
import com.iebm.aid.service.PlanService;
import com.iebm.aid.service.StationMessageService;
import com.iebm.aid.service.UserService;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.JsonUtils;
import com.iebm.aid.utils.StringUtils;
import com.iebm.aid.utils.VerifyUtils;
import com.iebm.aid.web.ResponseMessage;
import com.iebm.aid.web.ResponseStatus;
import com.iebm.aid.web.WebUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description="急救决策支持")
@RestController
@RequestMapping("/app/main")
public class MainController {
	
	private Logger logger = LoggerFactory.getLogger(MainController.class);

	@Resource
	private MainSymptomService mainSymptomService;
	@Resource
	private KeyQService keyQService;
	@Resource
	private CacheKeyQService cacheKeyQService;
	@Resource
	private PlanService planService;
	@Resource
	private MpdsService mpdsService;
	@Resource
	private AidRecordService aidRecordService;
	@Resource
	private UserService userService;
	@Resource
	private EventRecordService eventRecordService;
	@Resource
	private EventAidRecordService eventAidRecordService;
	@Resource
	private StationMessageService stationMessageService;
	
	@ApiOperation("120急救中心初始化接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/initial")
	public ResponseMessage initial(@RequestBody EventRecordParam param, HttpServletRequest request) {
		EventParam eventParam = param.getEventInfo();
		String eventNo = eventParam.getEventNo();
		String seatNo = eventParam.getSeatNo();
		String dispatcher = eventParam.getDispatcher();
		String[] requireParams = {eventNo, seatNo, dispatcher};
		
		if(VerifyUtils.isEmpty(requireParams)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING); 
		}
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		if(tokenVo == null) {
			return WebUtils.buildResponseMessage(ResponseStatus.EXCEPTION);
		}
		String key = String.valueOf(System.currentTimeMillis());
		InitialVo vo = new InitialVo(key);
		
		DataPool.put(key, param);
		
		return WebUtils.buildSuccessResponseMessage(vo);
	}
	
	@ApiOperation(value = "获取所有主诉", notes="获取所有主诉")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping(value = "/findAllMainSymptom")
	public ResponseMessage findAllMainSymptom() {
		List<MainSymptom> mainSymptomList = mainSymptomService.findAll();
		List<MainSymptomVo> voList = mainSymptomList.stream().map(MainSymptomVo::new).collect(Collectors.toList());
		return WebUtils.buildSuccessResponseMessage(voList);
	}
	
	@ApiOperation(value = "获取第一个问题", notes = "获取第一个问题", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping(value = "/getFirstQues")
	public ResponseMessageVo getFirstQues(@RequestBody BasicInfoReq basicInfo) {
		String serverId = basicInfo.getServerId();
		if(StringUtils.isNotEmpty(serverId)) {
			DataPool.put(serverId, basicInfo);
		} else {
			serverId = String.valueOf(System.currentTimeMillis());
			DataPool.put(serverId, basicInfo);
		}
		
		List<KeyQVo> keyQList = cacheKeyQService.getFirstKeyQ(basicInfo, serverId);		
		ResponseMessageVo vo = new ResponseMessageVo(serverId, keyQList);
		return vo;
	}
	
	@ApiOperation(value = "获取问题与答案", notes = "获取问题与答案", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping(value = "/searchQues")
	public ResponseMessageVo2 searchQues(@RequestBody KeyQParam param, HttpServletRequest request) {
		logger.info("searchQues params is :" + JsonUtils.toJsonString(param));
		String type = "1";
		List<KeyQVo> keyqList = keyQService.searchKeyQ(param);
		List<PlanVo> planList = null;
		Mpds mpds = null;
		Long recordId = null;
		if(CollectionUtils.isEmpty(keyqList)) {
			type = "2";
			String serverId = param.getServerId();
			String eventId = param.getEventId();
			CacheKeyQ cacheKeyq = cacheKeyQService.update(serverId, param.getAllKqIds(), param.getAllAnswerIds(), param.getAllTexts());
			planList = planService.queryByServerId(serverId);
			//CacheKeyQ cacheKeyq = cacheKeyQService.findByServerId(serverId);
			mpds = mpdsService.findMpdsGrade(cacheKeyq);
			//保存诊断记录
			TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
			recordId = eventAidRecordService.saveEventAidRecord(eventId, serverId, planList, tokenVo);
		}
		
		return new ResponseMessageVo2(type, keyqList, planList, mpds, recordId);
	}
	
	@ApiOperation(value = "发送预案", notes = "发送预案", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name="token", value="客户端token", required = true, dataType="String", paramType="header")
	})
	@PostMapping("/sendPlans")
	public ResponseMessage sendPlans(@RequestBody SendPlanParam param, HttpServletRequest request) {
		String recordId = param.getRecordId();
		String stationIds = param.getStationIds();
		String[] requireParams = {recordId, stationIds};
		if(VerifyUtils.isEmpty(requireParams)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		TokenVo tokenVo = (TokenVo) request.getAttribute("tokenVo");
		stationMessageService.sendMessageToStation(tokenVo, stationIds, recordId);
		return WebUtils.buildSuccessResponseMessage();
	}
	
}
