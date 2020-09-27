package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.dao.master.IMasterCreditLogExtendDao;
import com.nnti.common.dao.slave.ISlaveCreditLogExtendDao;
import com.nnti.common.model.vo.CreditLogExtend;
import com.nnti.common.service.interfaces.ICreditLogExtendService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CreditLogExtendServiceImpl implements ICreditLogExtendService {

	@Autowired
	private IMasterCreditLogExtendDao masterCreditLogExtendDao;
	@Autowired
	private ISlaveCreditLogExtendDao slaveCreditLogExtendDao;

	public int create(CreditLogExtend creditLogExtend) throws Exception {

		return masterCreditLogExtendDao.create(creditLogExtend);
	}

	public Integer count(CreditLogExtend creditLogExtend) throws Exception {

		return slaveCreditLogExtendDao.count(creditLogExtend);
	}
}