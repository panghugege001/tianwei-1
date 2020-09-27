package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import com.nnti.common.dao.master.IMasterUserDao;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

	@Autowired
	private IMasterUserDao masterUserDao;

	public User get(String loginName) throws Exception {

		return masterUserDao.get(loginName);
	}

	public List<User> findUserList(Map<String, Object> params) throws Exception {

		return masterUserDao.findUserList(params);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public int update(User user) throws Exception {

		return masterUserDao.update(user);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public int updateShippingCode(Map<String, Object> paramsMap) throws Exception {

		return masterUserDao.updateShippingCode(paramsMap);
	}
}