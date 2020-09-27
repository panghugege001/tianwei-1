package com.nnti.common.dao.slave;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.UserBankInfo;

public interface ISlaveUserBankInfoDao {
	
	List<UserBankInfo> findUserBankList(Map<String, Object> params);
	
	List<String> findUserRepeatBankNo(Map<String, Object> params);
}