package com.nnti.office.service.log.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.office.dao.log.OperationlogsDao;
import com.nnti.office.model.log.Operationlogs;
import com.nnti.office.service.log.OperationLogService;

import dfh.model.enums.OperationLogType;
import dfh.utils.DateUtil;

@Service
public class OperationLogServiceImpl implements OperationLogService{
	
	@Autowired
	OperationlogsDao operationlogsDao;
	
	public void insertOperationLog(String loginname, OperationLogType action, String remark) {
		Operationlogs operationlogs = new Operationlogs();
		operationlogs.setLoginname(loginname);
		operationlogs.setAction(action.getCode());
		operationlogs.setRemark(remark);
		operationlogs.setCreatetime(DateUtil.getCurrentTimestamp());
		operationlogsDao.insertLog(operationlogs);
	}
	
}
