package com.nnti.common.dao.master;

import java.util.Map;

public interface IMasterBigBangDao {

	int updateList(Map<String, Object> paramsMap);
	
	int updateList4Slot(Map<String, Object> paramsMap);
}