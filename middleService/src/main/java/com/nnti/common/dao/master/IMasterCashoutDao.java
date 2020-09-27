package com.nnti.common.dao.master;

import com.nnti.common.model.vo.Cashout;

public interface IMasterCashoutDao {

	int create(Cashout cashout);
	
	Cashout get(String pno);
}