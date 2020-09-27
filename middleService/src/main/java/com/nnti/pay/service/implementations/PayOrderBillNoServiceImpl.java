package com.nnti.pay.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.pay.dao.master.IMasterPayOrderBillNoDao;
import com.nnti.pay.model.vo.PayOrderBillNo;
import com.nnti.pay.service.interfaces.IPayOrderBillNoService;

@Service
@Transactional(rollbackFor = Exception.class)
public class PayOrderBillNoServiceImpl implements IPayOrderBillNoService {

	@Autowired
	private IMasterPayOrderBillNoDao masterPayOrderBillNoDao;

	public Integer insert(PayOrderBillNo payOrderBilloNo) throws Exception {

		return masterPayOrderBillNoDao.insert(payOrderBilloNo);
	}
}