package com.nnti.common.service.implementations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.dao.master.IMasterBankStatusDao;
import com.nnti.common.model.vo.BankStatus;
import com.nnti.common.service.interfaces.IBankStatusService;

@Service
@Transactional(rollbackFor = Exception.class)
public class BankStatusServiceImpl implements IBankStatusService {
	
	@Autowired
	private IMasterBankStatusDao masterBankStatusDao;
	

	@Override
	public String getWithDrawBankStatus(BankStatus bankstatus) throws Exception {
		return masterBankStatusDao.getWithDrawBankStatus(bankstatus);
	}


	

}