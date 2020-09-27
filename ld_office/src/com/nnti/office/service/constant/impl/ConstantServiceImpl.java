package com.nnti.office.service.constant.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.office.dao.constant.ConstantDao;
import com.nnti.office.service.constant.ContantService;

@Service
public class ConstantServiceImpl implements ContantService{
	
	@Autowired
	ConstantDao constantDao;

	@Override
	public boolean isOpenEmail() {
		String value = constantDao.getConstantValue("邮箱");
		if(value.equals("0")) {
			return false;
		}
		return true;
	}
	
}
