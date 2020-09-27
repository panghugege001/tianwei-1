package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.SelfRecord;

public interface ISlaveSelfRecordDao {

	List<SelfRecord> findList(Map<String, Object> paramsMap);
}