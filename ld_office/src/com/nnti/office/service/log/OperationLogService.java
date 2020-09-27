package com.nnti.office.service.log;

import dfh.model.enums.OperationLogType;

public interface OperationLogService {
	
	public void insertOperationLog(String loginname, OperationLogType action, String remark);
	
}
