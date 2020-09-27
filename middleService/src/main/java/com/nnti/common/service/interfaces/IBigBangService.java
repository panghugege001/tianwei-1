package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.BigBang;

public interface IBigBangService {

	Integer count(Map<String, Object> paramsMap) throws Exception;

	int updateList(Map<String, Object> paramsMap) throws Exception;
	
	int updateList4Slot(Map<String, Object> paramsMap) throws Exception;

	List<BigBang> findList(Map<String, Object> paramsMap) throws Exception;

	List<BigBang> findListBySlot(Map<String, Object> paramsMap) throws Exception;
}