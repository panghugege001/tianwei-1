package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.dao.master.IMasterCreditLogDao;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.service.interfaces.ICreditLogService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CreditLogServiceImpl implements ICreditLogService {

	@Autowired
	private IMasterCreditLogDao masterCreditLogDao;
	
	public int create(CreditLog creditLog) throws Exception {
		
		return masterCreditLogDao.create(creditLog);
	}
}