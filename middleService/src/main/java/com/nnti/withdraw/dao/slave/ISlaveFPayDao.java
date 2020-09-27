package com.nnti.withdraw.dao.slave;

import com.nnti.withdraw.model.vo.FPayorder;

public interface ISlaveFPayDao {

	FPayorder get(String pno);
	
	FPayorder getByBillno(String billno);
	
	int create(FPayorder order);
	
	int update(FPayorder order);
}