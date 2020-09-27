package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.slave.ISlaveUserBankInfoDao;
import com.nnti.common.model.vo.UserBankInfo;
import com.nnti.common.service.interfaces.IUserBankInfoService;

@Service
public class UserBankInfoServiceImpl implements IUserBankInfoService {

	@Autowired
	private ISlaveUserBankInfoDao slaveUserBankInfoDao;
	
	public List<UserBankInfo> findUserBankList(Map<String, Object> params) throws Exception {
		
		return slaveUserBankInfoDao.findUserBankList(params);
	}
	
	public List<String> findUserRepeatBankNo(Map<String, Object> params) throws Exception {
		
		return slaveUserBankInfoDao.findUserRepeatBankNo(params);
	}
}