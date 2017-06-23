package com.iebm.aid.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iebm.aid.common.AbstractService;
import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.common.DataPool;
import com.iebm.aid.common.GlobalConstants;
import com.iebm.aid.common.enums.AppType;
import com.iebm.aid.common.enums.MessageStatus;
import com.iebm.aid.controller.req.BasicInfoReq;
import com.iebm.aid.controller.req.EARecord4WebParam;
import com.iebm.aid.controller.req.EditRecordParam;
import com.iebm.aid.controller.req.HistoryRecordParam;
import com.iebm.aid.controller.req.third.EventRecordParam;
import com.iebm.aid.pojo.CacheKeyQ;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.pojo.KeyQ;
import com.iebm.aid.pojo.MainSymptom;
import com.iebm.aid.pojo.Station;
import com.iebm.aid.pojo.StationMessage;
import com.iebm.aid.pojo.User;
import com.iebm.aid.pojo.vo.AnswerVo;
import com.iebm.aid.pojo.vo.EventAidRecord4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordDetail4WebVo;
import com.iebm.aid.pojo.vo.EventAidRecordHistory4WebVo;
import com.iebm.aid.pojo.vo.HistoryKeyQVo;
import com.iebm.aid.pojo.vo.KeyQVo;
import com.iebm.aid.pojo.vo.PlanVo;
import com.iebm.aid.pojo.vo.ProcessKeyqVo;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.repository.EventAidRecordRepository;
import com.iebm.aid.repository.KeyQRepository;
import com.iebm.aid.repository.PlanRepository;
import com.iebm.aid.repository.StationMessageRepository;
import com.iebm.aid.service.CacheKeyQService;
import com.iebm.aid.service.EventAidRecordService;
import com.iebm.aid.service.MainSymptomService;
import com.iebm.aid.service.StationMessageService;
import com.iebm.aid.service.StationService;
import com.iebm.aid.service.UserService;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.EBMEnDecrypt;
import com.iebm.aid.utils.StringUtils;


@Service
@Transactional
public class EventAidRecordServiceImpl extends AbstractService<EventAidRecord, Long> implements EventAidRecordService {

	@Resource
	private EventAidRecordRepository repository;
	@Resource
	private PlanRepository planRepository;
	@Resource
	private CacheKeyQService cacheKeyQService;
	@Resource
	private MainSymptomService mainSymptomService;
	@Resource
	private UserService userService;
	@Resource
	private StationService stationService;
	@Resource
	private StationMessageService stationMessageService;
	@Resource
	private StationMessageRepository stationMessageRepository;
	@Resource
	private KeyQRepository keyQRepository;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
 
	@Override
	public Long saveEventAidRecord(String eventId, String serverId, List<PlanVo> planvoList, TokenVo tokenVo) {
		EventRecordParam seatInfo = DataPool.getEntity(eventId, EventRecordParam.class);
		BasicInfoReq basicInfo = DataPool.getEntity(serverId, BasicInfoReq.class);
		if(basicInfo == null) {
			logger.warn("could not write event aid record, because require parameter missing");
			return null;
		}
		EventAidRecord record = null;
		if(seatInfo != null) {
			record = seatInfo.parseToEventAidRecord();
			record.setAppType(String.valueOf(AppType.WEB.getType()));
		} else {
			record = new EventAidRecord();
			record.setAppType(String.valueOf(AppType.ANDROID.getType()));
		}
		basicInfo.fillEventAidRecord(record);
		
		CacheKeyQ cacheKeyq = cacheKeyQService.findByServerId(serverId);	
		
		MainSymptom mainSymptom = mainSymptomService.findByMainId(cacheKeyq.getSympID());
		StringJoiner joiner = new StringJoiner(",");
		if(CollectionUtils.isNotEmpty(planvoList)) {
			planvoList.stream().map(PlanVo::getPlanId).collect(toList()).stream().forEach(e->joiner.add(e));
		}
		String mainSymptomText = mainSymptom.getTitle();
		mainSymptomText = EBMEnDecrypt.decrypt(mainSymptomText, GlobalConstants.DECRYPT_CHARSET);
		
		String cureProcess = parseToProcessText(cacheKeyq.getSympID(), cacheKeyq.getProcessKeyQIDs(),
				cacheKeyq.getProcessAnswerIDs(), cacheKeyq.getProcessAnswerTexts());
		
		record.setMainSympId(mainSymptom.getMainId());
		record.setMainSymptomText(mainSymptomText);
		record.setCureProcess(cureProcess);
		record.setPlanIds(joiner.toString());
		record.setCreateTime(LocalDateTime.now());
		record.setCreator(tokenVo.getUserId());
		record.setProcessKeyQIDs(cacheKeyq.getProcessKeyQIDs());
		record.setProcessAnswerIDs(cacheKeyq.getProcessAnswerIDs());
		record.setProcessAnswerTexts(cacheKeyq.getProcessAnswerTexts());
		EventAidRecord ear = save(record);
		return ear.getId();
	}
	
	@Override
	public EventAidRecordDetail4WebVo getDetail(Long id) {
		String subSplit = ",";
		EventAidRecord record = get(id);
		EventAidRecordDetail4WebVo recordDetailVo = new EventAidRecordDetail4WebVo(record);
		String planIds = record.getPlanIds();
		if(StringUtils.isNotEmpty(planIds)) {
			List<String> planIdList = Arrays.asList(planIds.split(","));
			List<PlanVo> planvoList = Optional.ofNullable(planRepository.findByPlanIdIn(planIdList))
					.orElseGet(ArrayList::new).stream().map(PlanVo::new).collect(toList());
			recordDetailVo.setPlans(planvoList);
		}
		
		//构建诊断过程
		String mainId = record.getMainSympId();
		String processKqIds = record.getProcessKeyQIDs();
		String processAnswerIds = record.getProcessAnswerIDs();
		String processAnswerTexts = record.getProcessAnswerTexts();
		int kqIdCount = org.springframework.util.StringUtils.countOccurrencesOf(processKqIds, subSplit);
		int answerIdCount = org.springframework.util.StringUtils.countOccurrencesOf(processAnswerIds, subSplit);
		int answerTextCount = org.springframework.util.StringUtils.countOccurrencesOf(processAnswerTexts, subSplit);
		
		if(kqIdCount == answerIdCount && answerIdCount == answerTextCount) {
			List<Integer> processKqIdList = Arrays.asList(processKqIds.split(subSplit)).stream().map(Integer::parseInt).collect(toList());
			List<String> processAnswerIdList = Arrays.asList(processAnswerIds.split(subSplit));
			List<String> processAnswerTextList = Arrays.asList(processAnswerTexts.split(subSplit,-1));
			List<KeyQ> keyqList = DataPool.get(KeyQ.class).stream().filter(e->e.getMainID().equals(mainId)).collect(toList());
			List<ProcessKeyqVo> processKeyqVoList = new ArrayList<>();
			
			for(int i = 0; i < processKqIdList.size(); i++) {
				Integer itemKqId = processKqIdList.get(i);
				String answerId = processAnswerIdList.get(i);
				String answerText = processAnswerTextList.get(i);
				keyqList.stream().filter(e->e.getKqID() == itemKqId).filter(e->e.getAnswerId().equals(answerId))
					.findFirst().ifPresent(e->{
						ProcessKeyqVo keyqvo = new ProcessKeyqVo(e);
						keyqvo.setCommExplain(answerText);
						processKeyqVoList.add(keyqvo);
					});
			}
			recordDetailVo.setHistoryKeyq(processKeyqVoList);
			
		} else {
			logger.info("could not load cure process by record id", id);
		}
		//构建已发送急救分站
		StringJoiner joiner = new StringJoiner(",");
		List<StationMessage> stationMessageList = stationMessageService.searchByRecord(id);
		Optional.ofNullable(stationMessageList).orElseGet(ArrayList::new).forEach(e->{
			Station station = stationService.get(e.getStationId());
			Optional.ofNullable(station).ifPresent(e2->{
				joiner.add(e2.getStationName());
			});
		});
		recordDetailVo.setStationList(joiner.toString());
		return recordDetailVo;
	}

	@Override
	public Page<EventAidRecord4WebVo> findByPage(EARecord4WebParam param, String userId) {
		Sort sortObj;
		if (StringUtils.isNotEmpty(param.getOrder()) && !StringUtils.isNotEmpty(param.getSort())) {
			sortObj = new Sort(new Order(param.getOrder().toUpperCase().equals("ASC") ? Direction.ASC : Direction.DESC, param.getSort())).and(new Sort(
					new Order(Direction.ASC, "id")));
		} else {
			sortObj = new Sort(new Order(Direction.DESC, "createTime"));
		}
		
		
		Pageable pageable = new PageRequest(param.getPage(), param.getRows(), sortObj);
		Specification<EventAidRecord> spec = new Specification<EventAidRecord>() {
			
			String eventNo = param.getEventNo();
			String seatNo = param.getSeatNo();
			String dispatcher = param.getDispatcher();
			String name = param.getName();
			String mainSymptomText = param.getMainSymptomText();
			String createTime = param.getCreateTime();
			String endTime = param.getEndTime();
			String aidMobile = param.getAidMobile();
			
			@Override
			public Predicate toPredicate(Root<EventAidRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				if(StringUtils.isNotEmpty(eventNo)) {
					ps.add(cb.equal(root.get("eventNo"), eventNo));
				}
				if(StringUtils.isNotEmpty(seatNo)) {
					ps.add(cb.equal(root.get("seatNo"), seatNo));
				}
				if(StringUtils.isNotEmpty(dispatcher)) {
					ps.add(cb.equal(root.get("dispatcher"), dispatcher));
				}
				if(StringUtils.isNotEmpty(name)) {
					ps.add(cb.equal(root.get("name"), name));
				}
				if(StringUtils.isNotEmpty(mainSymptomText)) {
					ps.add(cb.equal(root.get("mainSymptomText"), mainSymptomText));
				}
				if(StringUtils.isNotEmpty(createTime)) {
					LocalDate startDate = LocalDate.parse(createTime);					
					LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
					LocalDateTime endDateTime = LocalDateTime.now();
					if(StringUtils.isNotEmpty(endTime)) {
						LocalDate endDate = LocalDate.parse(endTime);
						endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
					}
					ps.add(cb.between(root.get("createTime"), startDateTime, endDateTime));
				}
				if(StringUtils.isNotEmpty(aidMobile)) {
					ps.add(cb.equal(root.get("aidMobile"), aidMobile));
				}
				ps.add(cb.isNull(root.get("rootId")));
				return CollectionUtils.isEmpty(ps) ? null : cb.and(ps.toArray(new Predicate[ps.size()]));
			}
		};
		Converter<EventAidRecord, EventAidRecord4WebVo> converter = (e) -> {
			return new EventAidRecord4WebVo(e);
		};
		Page<EventAidRecord4WebVo> page = repository.findAll(spec, pageable).map(converter);
		List<EventAidRecord4WebVo> list = page.getContent();
		//拼装list中的stationList属性(分站1-已读)
		Optional.ofNullable(list).orElseGet(ArrayList::new).stream().peek(e->{
			Long recordId = e.getId();
			List<StationMessage> messageList = stationMessageService.searchByRecord(recordId);
			StringJoiner joiner = new StringJoiner(",");
			Optional.ofNullable(messageList).orElseGet(ArrayList::new).stream().forEach(e2->{
				Station station = stationService.get(e2.getStationId());
				if(station != null) {
					String stationName = station.getStationName();
					String status = "未读";
					if(e2.getStatus() != null) {
						status = e2.getStatus().longValue() == MessageStatus.READED.getStatus().longValue() ? "已读" : "未读";
					}
					String itemStatus = stationName + "(" + status + ")";
					joiner.add(itemStatus);
				}
			});
			e.setStationList(joiner.toString());
		}).collect(Collectors.toList());
		
		return page;
	}
	
	@Override
	public Page<EventAidRecordHistory4WebVo> findHistory (HistoryRecordParam param) {
		Sort sortObj;
		if (StringUtils.isNotEmpty(param.getOrder()) && !StringUtils.isNotEmpty(param.getSort())) {
			sortObj = new Sort(new Order(param.getOrder().toUpperCase().equals("ASC") ? Direction.ASC : Direction.DESC, param.getSort())).and(new Sort(
					new Order(Direction.ASC, "id")));
		} else {
			sortObj = new Sort(new Order(Direction.DESC, "createTime"));
		}
		EventAidRecord eventAidRecord=repository.getOne(Long.parseLong(param.getRecordId()));
		String	rootId=StringUtils.isNotEmpty(eventAidRecord.getRootId())?eventAidRecord.getRootId():param.getRecordId();
		
		Pageable pageable = new PageRequest(param.getPage(), param.getRows(), sortObj);
		Specification<EventAidRecord> spec = new Specification<EventAidRecord>() {
			
			@Override
			public Predicate toPredicate(Root<EventAidRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				Predicate p1 = cb.equal(root.get("rootId"), param.getRecordId());
				Predicate p2 = cb.equal(root.get("id"), param.getRecordId());
				
				Predicate p = cb.or(p1, p2);
				ps.add(p);
				return CollectionUtils.isEmpty(ps) ? null : cb.and(ps.toArray(new Predicate[ps.size()]));
			}
		};
		Converter<EventAidRecord, EventAidRecordHistory4WebVo> converter = (e) -> {
			return new EventAidRecordHistory4WebVo(e);
		};
		Page<EventAidRecordHistory4WebVo> page = repository.findAll(spec, pageable).map(converter);
		page.getContent().stream().peek(e->{
			User user = userService.get(Long.valueOf(e.getCreator()));
			if(user != null) {
				String appTypeStr = e.getAppType();
				e.setCreator(user.getUserName());
				Station station = stationService.get(Long.valueOf(user.getStationId()));
				if("0".equals(appTypeStr)) {
					Optional.ofNullable(station).ifPresent(e2->{
						String platform = e2.getStationName() + "平板";
						e.setAppType(platform);
					});
				} else if ("1".equals(appTypeStr)) {
					String platform = "指挥中心座席";
					e.setAppType(platform);
				}
				
			}
		}).collect(toList());		
		
		return page;
	}
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void edit(String userId, EditRecordParam param) {
		User user = userService.get(Long.valueOf(userId));
		String stationId=user.getStationId();
		Long recordId = Long.valueOf(param.getRecordId());
		EventAidRecord record = get(recordId);
		EventAidRecord newRecord = record.clone();
		param.mergeCommonInfo(record.getId().toString(), newRecord);
		save(newRecord);
		//更改关联表的recordId
		stationMessageRepository.changeRecordId(newRecord.getId(),recordId,Long.parseLong(stationId));
	}


	@Override
	protected BaseRepository<EventAidRecord, Long> getRepository() {
		return repository;
	}
	
	private String parseToProcessText(String mainId, String processKqIds, String processAnswerIds, String processAnswerTexts) {
		List<Integer> kqIdList = Arrays.asList(processKqIds.split(",")).stream().map(Integer::parseInt).collect(toList());
		List<String> answerIdList = Arrays.asList(processAnswerIds.split(","));
		List<String> answerTextList = new ArrayList<>();
		if(StringUtils.isNotEmpty(processAnswerTexts)) {
			answerTextList = Arrays.asList(processAnswerTexts.split(",", -1));
		}
		
		StringBuilder sb = new StringBuilder();
		
		List<KeyQ> keyqList = DataPool.get(KeyQ.class).stream().filter(e->e.getMainID().equals(mainId)).collect(toList());
		for (int i = 0; i < kqIdList.size(); i++) {
			int kqId = kqIdList.get(i);
			String answerId = answerIdList.get(i);
			String maualAnswerText = CollectionUtils.isNotEmpty(answerTextList) ? answerTextList.get(i) : "";
			
			String idx = (i + 1) + "";
			Optional<KeyQ> optional = keyqList.stream().filter(e->e.getKqID()==kqId).filter(e->e.getAnswerId().equals(answerId)).findFirst();
			optional.ifPresent(e->{
				String questionText = EBMEnDecrypt.decrypt(e.getKqTitle(), GlobalConstants.DECRYPT_CHARSET);
				String answerText = EBMEnDecrypt.decrypt(e.getAnswer(), GlobalConstants.DECRYPT_CHARSET);
				sb.append(idx).append("、").append(questionText).append("(").append(answerText).append(")");
				if(StringUtils.isNotEmpty(maualAnswerText) && !"0".equals(maualAnswerText)) {
					sb.append("(").append(maualAnswerText).append(")");
				}
				sb.append("<br>");
				//index++;
			});
		}
		return sb.toString();
	}

	@Override
	public EventAidRecordDetail4WebVo findLastModifyRecord(String recordId) {
		Long lastRecordId = repository.findLastRecord(recordId, Long.valueOf(recordId));
		if(lastRecordId != null) {
			EventAidRecordDetail4WebVo detail = getDetail(lastRecordId);
			return detail;
		}
		return null;
	}

	@Override
	public List<HistoryKeyQVo> findProcessKeyQ(String recordId) {
		
		List<HistoryKeyQVo> resultList = new ArrayList<>();
		EventAidRecord record = get(Long.valueOf(recordId));
		String processKqIds = record.getProcessKeyQIDs();
		if(StringUtils.isNotEmpty(processKqIds)) {
			String mainId = record.getMainSympId();
			String processAnswerIds = record.getProcessAnswerIDs();
			String processAnswerTexts = record.getProcessAnswerTexts();
			
			List<Integer> processKqIdList = Stream.of(processKqIds.split(",")).map(Integer::parseInt).collect(toList());
			List<String> processAnswerIdList = Stream.of(processAnswerIds.split(",")).collect(toList());
			List<String> processAnswerTextList = Stream.of(processAnswerTexts.split(",", -1)).collect(toList());
			
			List<KeyQ> keyqList = keyQRepository.findByMainIdAndKqIds(mainId, processKqIdList);
			List<KeyQVo> keyqvoList = keyqList.stream().map(KeyQVo::new).collect(toList());
			
			//key为kqId,value为kqTitle
			Map<Integer, String> kqTitleMap = keyqList.stream().collect(Collectors.toMap(KeyQ::getKqID, KeyQ::getKqTitle,(k1,k2)->k2));
			//key为kqId,value为answerVo集合			
			Map<Integer, List<AnswerVo>> answerMap = keyqvoList.stream().map(AnswerVo::new).collect(Collectors.groupingBy(AnswerVo::getKqID));
			//key为kqId,value为answerId
			Map<Integer, String> answerIdMap = new HashMap<>();
			//key为kqId,value为自输
			Map<Integer, String> answerInputTextMap = new HashMap<>();
			for(int i=0; i<processKqIdList.size(); i++) {
				Integer kqId = processKqIdList.get(i);
				String answerId = processAnswerIdList.get(i);
				String answerText = processAnswerTextList.get(i);
				answerIdMap.put(kqId, answerId);
				answerInputTextMap.put(kqId, answerText);
			}
			
			
			answerMap.forEach((k,v)->{
				
				HistoryKeyQVo item = new HistoryKeyQVo();
				item.setKqID(k);
				String kqTitle = kqTitleMap.get(k);
				if(StringUtils.isNotEmpty(kqTitle)) {
					item.setKqTitle(EBMEnDecrypt.decrypt(kqTitle, GlobalConstants.DECRYPT_CHARSET));
				}
				item.setAnswerList(v);
				item.setOriginAnswerId(answerIdMap.get(k));
				item.setOriginInput(answerInputTextMap.get(k));
				resultList.add(item);
			});
		}
		return resultList;
	}

}
