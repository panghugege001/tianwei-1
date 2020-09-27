package com.nnti.common.dao.slave;

import com.nnti.common.model.vo.Cashout;

public interface ISlaveCashoutDao {
	
	Cashout get(String pno);
}