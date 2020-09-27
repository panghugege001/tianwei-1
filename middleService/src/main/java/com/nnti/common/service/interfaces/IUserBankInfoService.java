package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;

import com.nnti.common.model.vo.UserBankInfo;

public interface IUserBankInfoService {

	List<UserBankInfo> findUserBankList(Map<String, Object> params) throws Exception;
	
	List<String> findUserRepeatBankNo(Map<String, Object> params) throws Exception;
}