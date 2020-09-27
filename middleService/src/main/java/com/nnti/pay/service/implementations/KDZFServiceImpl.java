package com.nnti.pay.service.implementations;

import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.pay.service.interfaces.IKDZFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KDZFServiceImpl implements IKDZFService {

	@Autowired
	private IUserService userService;

	public int update(User user) throws Exception {

		return userService.update(user);
	}
}