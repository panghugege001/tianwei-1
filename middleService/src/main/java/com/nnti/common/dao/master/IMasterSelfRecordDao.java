package com.nnti.common.dao.master;

import java.util.Map;
import com.nnti.common.model.vo.SelfRecord;

public interface IMasterSelfRecordDao {

	int updateList(Map<String, Object> paramsMap);

	int create(SelfRecord selfRecord);

	int update(SelfRecord selfRecord);
}