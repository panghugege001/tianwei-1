package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.CreditLogExtend;

public interface ICreditLogExtendService {

	int create(CreditLogExtend creditLogExtend) throws Exception;
	
	Integer count(CreditLogExtend creditLogExtend) throws Exception;
}