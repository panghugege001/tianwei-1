package com.nnti.common.dao.slave;

import com.nnti.common.model.vo.BankInfo;
import java.util.List;
import java.util.Map;

public interface ISlaveBankInfoDao {

	List<BankInfo> findBankInfoList(BankInfo bankInfo);

	List<BankInfo> findBankInfoList2(Map<String, Object> map);

	BankInfo findById(Integer id);

	List<BankInfo> findZfbBankInfo(BankInfo bankInfo);
	
	List<BankInfo> findDepositBankInfo(BankInfo bankInfo);
	
	BankInfo getBank(String bankCard);
}