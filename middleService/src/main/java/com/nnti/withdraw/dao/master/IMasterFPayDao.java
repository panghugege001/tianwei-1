package com.nnti.withdraw.dao.master;

import com.nnti.withdraw.model.vo.FPayorder;

public interface IMasterFPayDao {
	

	FPayorder get(String pno);
	
	FPayorder getByBillno(String billno);
	
	int create(FPayorder order);
	
	int update(FPayorder order);
}