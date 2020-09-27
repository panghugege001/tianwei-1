package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.dao.master.IMasterCashoutDao;
import com.nnti.common.dao.slave.ISlaveCashoutDao;
import com.nnti.common.model.vo.Cashout;
import com.nnti.common.service.interfaces.ICashoutService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CashoutServiceImpl implements ICashoutService {
	
	@Autowired
	private IMasterCashoutDao masterCashoutDao;
	@Autowired
	private ISlaveCashoutDao slaveCashoutDao;
	
	public int create(Cashout cashout) throws Exception {
		
		return masterCashoutDao.create(cashout);
	}

	public Cashout get(String pno) throws Exception {
		
		return masterCashoutDao.get(pno);
	}
}