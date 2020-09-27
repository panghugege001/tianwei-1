package com.nnti.common.dao.master;

import java.util.List;
import java.util.Map;

import com.nnti.common.model.vo.BankCardinfo;
import com.nnti.common.model.vo.BankInfo;

public interface IMasterBankInfoDao {

	int update(BankInfo bankInfo);
	
	List<BankInfo> findBankInfoList2(Map<String, Object> map);
	
	int update2(Map<String, Object> params);
	
	
	List<BankInfo> findBankinfoByParams(Map<String, Object> map);
	
	BankCardinfo findBankInfo(Map<String, Object> map);
	
	int  findBankStatus(String bankName);   
}