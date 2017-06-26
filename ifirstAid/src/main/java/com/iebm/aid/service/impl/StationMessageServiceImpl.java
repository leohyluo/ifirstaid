package com.iebm.aid.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
import com.iebm.aid.common.enums.MessageStatus;
import com.iebm.aid.controller.req.MarkReadAidRecordParam;
import com.iebm.aid.controller.req.SearchStationAidRecordsParam;
import com.iebm.aid.pojo.EventAidRecord;
import com.iebm.aid.pojo.StationMessage;
import com.iebm.aid.pojo.User;
import com.iebm.aid.pojo.vo.StationAidRecordVo;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.repository.StationMessageRepository;
import com.iebm.aid.service.EventAidRecordService;
import com.iebm.aid.service.StationMessageService;
import com.iebm.aid.service.UserService;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.StringUtils;



@Service
@Transactional
public class StationMessageServiceImpl extends AbstractService<StationMessage, Long> implements StationMessageService {

	@Resource
	private StationMessageRepository repository;
	@Resource
	private EventAidRecordService eventAidRecordService;
	@Resource
	private UserService userService;
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void sendMessageToStation(TokenVo tokenvo, String stationIds, String recordId) {
		List<String> stationIdList = Arrays.asList(stationIds.split(","));
		List<StationMessage> stationMsgList = new ArrayList<>();
		stationIdList.forEach(e->{
			StationMessage message = new StationMessage();
			message.setStationId(Long.valueOf(e));
//			message.setRecordId(Long.valueOf(recordId));
			EventAidRecord eventAidRecord=new EventAidRecord();
			//级联更新问题？是否会把对应策略记录清空
			eventAidRecord.setId(StringUtils.isEmpty(recordId)?0l:Long.parseLong(recordId));
			message.setEventAidRecord(eventAidRecord);
			message.setStatus(MessageStatus.UNREAD.getStatus());
			message.setCreator(tokenvo.getUserId());
			stationMsgList.add(message);
		});
		save(stationMsgList);
	}
	@Override
    public List<StationAidRecordVo> searchStationAidRecords(SearchStationAidRecordsParam param, String userId) {
		User curUser=userService.get(Long.parseLong(userId));
		String stationId=curUser.getStationId();
		Sort sortObj;
		if (StringUtils.isNotEmpty(param.getOrder()) && !StringUtils.isNotEmpty(param.getSort())) {
			sortObj = new Sort(new Order(param.getOrder().toUpperCase().equals("ASC") ? Direction.ASC : Direction.DESC, param.getSort())).and(new Sort(
					new Order(Direction.ASC, "id")));
		} else {
			sortObj = new Sort(new Order(Direction.DESC, "createTime"));
		}
		
		
		Pageable pageable = new PageRequest(param.getPage(), param.getRows(), sortObj);
		Specification<StationMessage> spec = new Specification<StationMessage>() {
			LocalDateTime startTime = null;
			LocalDateTime endTime = LocalDateTime.now();
			String type = param.getType();
			
			@Override
			public Predicate toPredicate(Root<StationMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				if("1".equals(type)) {
					startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);			
				} else if ("2".equals(type)) {
					startTime = LocalDateTime.now().plusDays(-7).withHour(0).withMinute(0).withSecond(0);
				} else if ("3".equals(type)) {
					startTime = LocalDateTime.now().plusMonths(-1).withHour(0).withMinute(0).withSecond(0);
				} else if ("4".equals(type)) {
					LocalDate startdate = LocalDate.parse(param.getStartTime());
					startTime = LocalDateTime.of(startdate, LocalTime.of(0, 0, 0));
					LocalDate endtdate =LocalDate.parse(param.getEndTime());
					endTime =  LocalDateTime.of(endtdate, LocalTime.of(23,59, 59));
				}
				if(startTime != null && endTime != null) {
					ps.add(cb.between(root.get("createTime"), startTime, endTime));
				}
				
				ps.add(cb.equal(root.get("stationId"), stationId));
				
				if(StringUtils.isNotEmpty(param.getPatientName())){
					ps.add(cb.like(root.get("eventAidRecord").get("name"), "%"+param.getPatientName()+"%"));
				}
				if(StringUtils.isNotEmpty(param.getAddress())){
					ps.add(cb.like(root.get("eventAidRecord").get("aidAddress"), "%"+param.getPatientName()+"%"));
				}
				if(StringUtils.isNotEmpty(param.getStatus())){
					ps.add(cb.equal(root.get("status"), param.getStatus()));
				}
				if(StringUtils.isNotEmpty(param.getMainSymptomId())){
					ps.add(cb.equal(root.get("eventAidRecord").get("mainSympId"),param.getMainSymptomId()));
				}
				return CollectionUtils.isEmpty(ps) ? null : cb.and(ps.toArray(new Predicate[ps.size()]));
			}
		};
		Converter<StationMessage, StationAidRecordVo> converter = (e) -> {
			return new StationAidRecordVo(e);
		};
		Page<StationAidRecordVo> page = repository.findAll(spec, pageable).map(converter);
		List<StationAidRecordVo> list = page.getContent();
		
		System.out.println("list size =========="+list.size());
		return page.getContent();
    }
	
	@Override
	public List<StationMessage> searchByRecord(Long recordId) {
		EventAidRecord eventAidRecord = eventAidRecordService.get(recordId);
		return repository.findByEventAidRecord(eventAidRecord);
	}
	
	@Override
    public int markReadAidRecord(MarkReadAidRecordParam param, String userId) {
		User curUser=userService.get(Long.parseLong(userId));
		String stationId=curUser.getStationId();
		return repository.setStatus(Long.parseLong(param.getStationAidRecordId()), Long.parseLong(stationId));
	}
	@Override
    public long remindCount( String userId) {
		User curUser=userService.get(Long.parseLong(userId));
		String stationId=curUser.getStationId();
		Specification<StationMessage> spec = new Specification<StationMessage>() {
			
			@Override
			public Predicate toPredicate(Root<StationMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("status"), "0"));
				
				ps.add(cb.equal(root.get("stationId"), stationId));
				
				return CollectionUtils.isEmpty(ps) ? null : cb.and(ps.toArray(new Predicate[ps.size()]));
			}
		};
		return repository.count(spec);
	}
	
	@Override
	protected BaseRepository<StationMessage, Long> getRepository() {
		return repository;
	}
		
}
