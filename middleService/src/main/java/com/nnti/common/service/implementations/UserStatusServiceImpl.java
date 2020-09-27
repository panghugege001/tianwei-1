package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterUserStatusDao;
import com.nnti.common.model.vo.UserStatus;
import com.nnti.common.service.interfaces.IUserStatusService;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserStatusServiceImpl implements IUserStatusService {

	@Autowired
	private IMasterUserStatusDao masterUserStatusDao;
	
	public UserStatus get(String loginName) throws Exception {

		return masterUserStatusDao.get(loginName);
	}

	public int create(UserStatus userStatus) throws Exception {

		return masterUserStatusDao.create(userStatus);
	}

	public int update(UserStatus userStatus) throws Exception {
		
		return masterUserStatusDao.update(userStatus);
	}
}