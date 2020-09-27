package com.nnti.common.service.interfaces;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.User;

public interface IUserService {

	List<User> findUserList(Map<String, Object> params) throws Exception;

	User get(String loginName) throws Exception;

	int update(User user) throws Exception;

	int updateShippingCode(Map<String, Object> paramsMap) throws Exception;
	
}