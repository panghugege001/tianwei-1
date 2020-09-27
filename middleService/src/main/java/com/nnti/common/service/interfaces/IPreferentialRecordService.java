package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.PreferentialRecord;

public interface IPreferentialRecordService {

	PreferentialRecord get(String pno) throws Exception;
	
	int create(PreferentialRecord preferentialRecord) throws Exception;
}