package com.nnti.pay.service.implementations;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.pay.dao.master.IMasterTotalDepositDao;
import com.nnti.pay.dao.slave.ISlaveTotalDepositDao;
import com.nnti.pay.model.vo.TotalDeposit;
import com.nnti.pay.service.interfaces.ITotalDepositService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TotalDepositServiceImpl implements ITotalDepositService {

	@Autowired
	private IMasterTotalDepositDao masterTotalDepositDao;
	@Autowired
	private ISlaveTotalDepositDao slaveTotalDepositDao;

	public List<TotalDeposit> findByLoginName(String loginname) throws Exception {

		return slaveTotalDepositDao.findByLoginName(loginname);
	}

	public Integer insert(TotalDeposit totalDeposit) throws Exception {

		return masterTotalDepositDao.insert(totalDeposit);
	}

	public Integer update(TotalDeposit totalDeposit) throws Exception {

		return masterTotalDepositDao.update(totalDeposit);
	}
}