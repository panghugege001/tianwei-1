package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterActionLogDao;
import com.nnti.common.model.vo.ActionLog;
import com.nnti.common.service.interfaces.IActionLogService;

@Service
public class ActionLogServiceImpl implements IActionLogService {

	@Autowired
	private IMasterActionLogDao masterActionLogDao;
	
	public int create(ActionLog actionLog) throws Exception {
		
		return masterActionLogDao.create(actionLog);
	}
}