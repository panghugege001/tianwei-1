package com.nnti.withdraw.service.interfaces;

import com.nnti.withdraw.model.vo.FPayorder;

public interface IFPayorderService {
	
	FPayorder get(String pno);
	
	FPayorder getByBillno(String billno);
	
	int create(FPayorder order);
	
	int update(FPayorder order);

}
