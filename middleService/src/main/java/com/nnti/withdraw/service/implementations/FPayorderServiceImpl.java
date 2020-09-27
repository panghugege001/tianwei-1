package com.nnti.withdraw.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.withdraw.dao.master.IMasterFPayDao;
import com.nnti.withdraw.dao.slave.ISlaveFPayDao;
import com.nnti.withdraw.model.vo.FPayorder;
import com.nnti.withdraw.service.interfaces.IFPayorderService;

@Service
@Transactional(rollbackFor = Exception.class)
public class FPayorderServiceImpl implements IFPayorderService {
	

	@Autowired
	private IMasterFPayDao masterFPayDao;
	@Autowired
	private ISlaveFPayDao slaveFPayDao;
	
	
	@Override
	public FPayorder get(String pno) {
		return masterFPayDao.get(pno);
	}

	@Override
	public int create(FPayorder order) {
		return masterFPayDao.create(order);
	}

	@Override
	public int update(FPayorder order) {
		return masterFPayDao.update(order);
	}

	@Override
	public FPayorder getByBillno(String billno) {
		return masterFPayDao.getByBillno(billno);
	}

	
}
