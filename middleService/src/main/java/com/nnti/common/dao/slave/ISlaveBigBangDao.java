package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.BigBang;

public interface ISlaveBigBangDao {

	Integer count(Map<String, Object> paramsMap);

	List<BigBang> findList(Map<String, Object> paramsMap);
	
	List<BigBang> findListBySlot(Map<String, Object> paramsMap);
}