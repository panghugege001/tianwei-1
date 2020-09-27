package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.Cashout;

public interface ICashoutService {
	
	int create(Cashout cashout) throws Exception;
	
	Cashout get(String pno) throws Exception;
}