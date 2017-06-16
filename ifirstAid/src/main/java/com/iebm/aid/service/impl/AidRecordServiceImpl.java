package com.iebm.aid.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

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
import com.iebm.aid.common.DataPool;
import com.iebm.aid.common.GlobalConstants;
import com.iebm.aid.controller.req.BasicInfoReq;
import com.iebm.aid.controller.req.SearchAidFilesParam;
import com.iebm.aid.pojo.AidFiles;
import com.iebm.aid.pojo.AidRecord;
import com.iebm.aid.pojo.CacheKeyQ;
import com.iebm.aid.pojo.KeyQ;
import com.iebm.aid.pojo.MainSymptom;
import com.iebm.aid.pojo.vo.AidRecordDetailVo;
import com.iebm.aid.pojo.vo.AidRecordVo;
import com.iebm.aid.pojo.vo.PlanVo;
import com.iebm.aid.pojo.vo.TokenVo;
import com.iebm.aid.repository.AidRecordRepository;
import com.iebm.aid.service.AidFilesService;
import com.iebm.aid.service.AidRecordService;
import com.iebm.aid.service.CacheKeyQService;
import com.iebm.aid.service.MainSymptomService;
import com.iebm.aid.utils.CollectionUtils;
import com.iebm.aid.utils.EBMEnDecrypt;
import com.iebm.aid.utils.StringUtils;


@Service
@Transactional
public class AidRecordServiceImpl extends AbstractService<AidRecord, Long> implements AidRecordService {

	@Resource
	private AidRecordRepository repository;
	@Resource
	private AidFilesService aidFilesService;
	@Resource
	private CacheKeyQService cacheKeyQService;
	@Resource
	private MainSymptomService mainSymptomService;
	

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long saveAidFiles(AidFiles aidFiles) {
		if(aidFiles == null) {
			aidFiles = new AidFiles();
		}
		aidFiles.setCreateTime(LocalDateTime.now());
		aidFiles.setLastupdTime(LocalDateTime.now());
		aidFilesService.save(aidFiles);
		return aidFiles.getId();
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveAidRecord(String serverId, List<PlanVo> planvoList, TokenVo tokenVo) {
		BasicInfoReq basicInfo = DataPool.take(serverId, BasicInfoReq.class);
		if(basicInfo == null) {
			return;
		}
		CacheKeyQ cacheKeyq = cacheKeyQService.findByServerId(serverId);
		AidFiles aidFiles = basicInfo.parseToAidFiles();
		Long filesId = this.saveAidFiles(aidFiles);	//急救档案id
		
		MainSymptom mainSymptom = mainSymptomService.findByMainId(cacheKeyq.getSympID());
		StringJoiner joiner = new StringJoiner(",");
		if(CollectionUtils.isNotEmpty(planvoList)) {
			planvoList.stream().map(PlanVo::getPlanId).collect(toList()).stream().forEach(e->joiner.add(e));
		}
		String mainSymptomText = mainSymptom.getTitle();
		mainSymptomText = EBMEnDecrypt.decrypt(mainSymptomText, GlobalConstants.DECRYPT_CHARSET);
		
		String cureProcess = parseToProcessText(cacheKeyq.getSympID(), cacheKeyq.getProcessKeyQIDs(),
				cacheKeyq.getProcessAnswerIDs(), cacheKeyq.getProcessAnswerTexts());
		AidRecord aidRecord = basicInfo.parseToAidRecord();
		aidRecord.setFilesId(filesId.toString());
		aidRecord.setMainSymptomText(mainSymptomText);
		aidRecord.setCureProcess(cureProcess);
		aidRecord.setPlanIds(joiner.toString());
		aidRecord.setCreateTime(LocalDateTime.now());
		aidRecord.setCreator(tokenVo.getUserId());
		save(aidRecord);
	}
	

	@Override
	public void saveAidRecord(BasicInfoReq basicInfo, List<PlanVo> planvoList, TokenVo tokenVo) {
		AidFiles aidFiles = basicInfo.parseToAidFiles();
		Long filesId = this.saveAidFiles(aidFiles);	//急救档案id
		
		StringJoiner joiner = new StringJoiner(",");
		if(CollectionUtils.isNotEmpty(planvoList)) {
			planvoList.stream().map(PlanVo::getPlanId).collect(toList()).stream().forEach(e->joiner.add(e));
		}
		String mainSymptomText = "危急情况";
		
		AidRecord aidRecord = basicInfo.parseToAidRecord();
		aidRecord.setFilesId(filesId.toString());
		aidRecord.setMainSymptomText(mainSymptomText);		
		aidRecord.setPlanIds(joiner.toString());
		aidRecord.setCreateTime(LocalDateTime.now());
		aidRecord.setCreator(tokenVo.getUserId());
		save(aidRecord);
	}

	@Override
	public List<AidRecordVo> search(SearchAidFilesParam param) {
		LocalDateTime startTime = null;
		LocalDateTime endTime = LocalDateTime.now();
		String type = param.getType();
		if("1".equals(type)) {
			startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);			
		} else if ("2".equals(type)) {
			startTime = LocalDateTime.now().plusDays(-7).withHour(0).withMinute(0).withSecond(0);
		} else if ("3".equals(type)) {
			startTime = LocalDateTime.now().plusMonths(-1).withHour(0).withMinute(0).withSecond(0);
		} else if ("4".equals(type)) {
			startTime = LocalDateTime.parse(param.getStartTime());
			endTime = LocalDateTime.parse(param.getEndTime());
		} 
		List<AidRecord> recordList = repository.findByCreateTimeBetween(startTime, endTime);
		return recordList.stream().map(AidRecordVo::new).collect(toList());
	}
	
	@Override
	public Page<AidRecordVo> findByPage(SearchAidFilesParam param, String userId) {
		Sort sortObj;
		if (StringUtils.isNotEmpty(param.getOrder()) && !StringUtils.isNotEmpty(param.getSort())) {
			sortObj = new Sort(new Order(param.getOrder().toUpperCase().equals("ASC") ? Direction.ASC : Direction.DESC, param.getSort())).and(new Sort(
					new Order(Direction.ASC, "id")));
		} else {
			sortObj = new Sort(new Order(Direction.DESC, "createTime"));
		}
		
		
		Pageable pageable = new PageRequest(param.getPage(), param.getRows(), sortObj);
		Specification<AidRecord> spec = new Specification<AidRecord>() {
			LocalDateTime startTime = null;
			LocalDateTime endTime = LocalDateTime.now();
			String type = param.getType();
			
			@Override
			public Predicate toPredicate(Root<AidRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				if("1".equals(type)) {
					startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);			
				} else if ("2".equals(type)) {
					startTime = LocalDateTime.now().plusDays(-7).withHour(0).withMinute(0).withSecond(0);
				} else if ("3".equals(type)) {
					startTime = LocalDateTime.now().plusMonths(-1).withHour(0).withMinute(0).withSecond(0);
				} else if ("4".equals(type)) {
					startTime = LocalDateTime.parse(param.getStartTime());
					endTime = LocalDateTime.parse(param.getEndTime());
				}
				if(startTime != null && endTime != null) {
					ps.add(cb.between(root.get("createTime"), startTime, endTime));
				}
				if(StringUtils.isNotEmpty(userId)) {
					ps.add(cb.equal(root.get("creator"), userId));
				}
				return CollectionUtils.isEmpty(ps) ? null : cb.and(ps.toArray(new Predicate[ps.size()]));
			}
		};
		Converter<AidRecord, AidRecordVo> converter = (e) -> {
			return new AidRecordVo(e);
		};
		Page<AidRecordVo> page = repository.findAll(spec, pageable).map(converter);
		List<AidRecordVo> list = page.getContent();
		System.out.println("list size =========="+list.size());
		return page;
	}
	
	@Override
	public AidRecordDetailVo getDetail(String id) {
		return repository.findDetailById(id);
	}

	@Override
	protected BaseRepository<AidRecord, Long> getRepository() {
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


}
