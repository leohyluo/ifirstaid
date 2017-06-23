package com.iebm.aid.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iebm.aid.common.BaseRepository;
import com.iebm.aid.pojo.EventAidRecord;

@Repository
public interface EventAidRecordRepository extends BaseRepository<EventAidRecord, Long> {

	@Query("select max(id) from EventAidRecord where id = :lrecordId or rootId = :recordId "
			+ " or rootId = (select rootId from EventAidRecord where id = :lrecordId)")
	Long findLastRecord(@Param(value = "recordId") String recordId, @Param(value = "lrecordId") Long lrecordId);
}
