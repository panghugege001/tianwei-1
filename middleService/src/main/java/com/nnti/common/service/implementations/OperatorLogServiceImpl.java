package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterOperationLogDao;
import com.nnti.common.model.vo.OperationLog;
import com.nnti.common.service.interfaces.IOperatorLogService;

@Service
public class OperatorLogServiceImpl implements IOperatorLogService {
	
	@Autowired
	private IMasterOperationLogDao masterOperationLogDao;

	public int insert(OperationLog operationLog) throws Exception {
		
		return masterOperationLogDao.insert(operationLog);
	}
}