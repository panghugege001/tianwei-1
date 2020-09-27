package com.nnti.withdraw.service.interfaces;

import com.nnti.withdraw.model.dto.WithdrawDTO;

public interface IWithdrawService {
	
	//出款
	String addCashOut(WithdrawDTO dto) throws Exception;
	
	String notify(String data,String product);
	
	String updateFPay(String orderNo,String product);
	
	String addFPay(String orderNo,String operator,String remark,String ip,String product) throws Exception;
	
	String resubmitFPay(String orderNo,String operator,String ip,String product) throws Exception;

}
