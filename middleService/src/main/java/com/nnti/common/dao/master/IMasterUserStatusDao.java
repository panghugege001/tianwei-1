package com.nnti.common.dao.master;

import com.nnti.common.model.vo.UserStatus;

public interface IMasterUserStatusDao {

	int create(UserStatus userStatus);

	int update(UserStatus userStatus);
	
	UserStatus get(String loginName);
}