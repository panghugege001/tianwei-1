package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nnti.common.model.vo.BankCardinfo;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.User;
import com.nnti.pay.model.vo.TotalDeposit;


public interface IBankInfoService {

	List<BankInfo> findBankInfoList(BankInfo bankInfo) throws Exception;

	List<BankInfo> findBankInfoList2(Map<String, Object> map) throws Exception;

	BankInfo findById(Integer id) throws Exception;

	List<BankInfo> findZfbBankInfo(BankInfo bankInfo) throws Exception;
	
	List<BankInfo> findDepositBankInfo(BankInfo bankInfo) throws Exception;

	int update(BankInfo bankInfo) throws Exception;
	
	int update2(Map<String, Object> params) throws Exception;
	
	BankInfo getBank(String bankCard) throws Exception;
	
	int controlBank(String bankCard,Integer paySwitch) throws Exception;
	
	BankInfo findBankinfoByParams(User user,String paytype,TotalDeposit totalDeposit,String [] bankname) throws Exception;
	
	BankCardinfo findBankInfo(Map<String, Object> map) throws Exception;
	
	boolean  findBankStatus(String bankName);   
}