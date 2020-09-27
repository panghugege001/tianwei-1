package com.nnti.pay.service.interfaces;

import java.util.List;
import java.util.Map;

import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.LosePromo;
import com.nnti.common.model.vo.Proposal;
import com.nnti.pay.model.vo.DepositOrder;

public interface IDepositOrderService {

	String createDepositId(String loginName, BankInfo bank) throws Exception;
	
	String createDepositIdMc(String loginName,Double amount ,BankInfo bank ,Integer length) throws Exception;

    DepositOrder findById(String depositId);
    
    List<DepositOrder> findByLoginName(String loginname) throws Exception;
    
    Boolean discardOrder(DepositOrder depositOrder) throws Exception;
}