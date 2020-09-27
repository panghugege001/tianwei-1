package com.nnti.common.dao.master;

import com.nnti.common.model.vo.OperationLog;

public interface IMasterOperationLogDao {
	
	int insert(OperationLog operationLog);
}