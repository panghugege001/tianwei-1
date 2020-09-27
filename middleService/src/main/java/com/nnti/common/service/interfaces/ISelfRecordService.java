package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.SelfRecord;

public interface ISelfRecordService {

	int updateList(Map<String, Object> paramsMap) throws Exception;

	int create(SelfRecord selfRecord) throws Exception;

	int update(SelfRecord selfRecord) throws Exception;

	List<SelfRecord> findList(Map<String, Object> paramsMap) throws Exception;
}