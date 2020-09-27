package com.nnti.common.service.interfaces;

import com.nnti.common.model.vo.OperationLog;

public interface IOperatorLogService {
	
	int insert(OperationLog operationLog) throws Exception;
}