package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.UserStatus;

public interface IUserStatusService {

	UserStatus get(String loginName) throws Exception;
	
	int create(UserStatus userStatus) throws Exception;
	
	int update(UserStatus userStatus) throws Exception;
}