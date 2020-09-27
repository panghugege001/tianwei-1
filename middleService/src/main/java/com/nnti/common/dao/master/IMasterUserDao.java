package com.nnti.common.dao.master;

import java.util.List;
import java.util.Map;
import com.nnti.common.model.vo.User;

public interface IMasterUserDao {

	int create(User user);

	int update(User user);

	int updateShippingCode(Map<String, Object> paramsMap);

	int delete(User user);

	User get(String loginName);

	List<User> findUserList(Map<String, Object> params);
}