package com.nnti.pay.service.implementations;

import com.nnti.pay.dao.master.IMasterPayCreditDao;
import com.nnti.pay.model.vo.PayCredit;
import com.nnti.pay.service.interfaces.IPayCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PayCreditServiceImpl implements IPayCreditService {

	@Autowired
	private IMasterPayCreditDao masterPayCreditDao;

	public Integer create(PayCredit payCredit) {
		
		return masterPayCreditDao.insert(payCredit);
	}
}