package com.nnti.pay.service.implementations;

//import com.nnti.common.page.PageInfo;
import com.nnti.pay.dao.master.IMasterPayOrderDao;
import com.nnti.pay.dao.slave.ISlavePayOrderDao;
import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.service.interfaces.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PayOrderServiceImpl implements IPayOrderService {

	@Autowired
	private IMasterPayOrderDao masterPayOrderDao;
	@Autowired
	private ISlavePayOrderDao slavePayOrderDao;

	public Double sumMoney(PayOrder payOrder) {

		return slavePayOrderDao.sumMoney(payOrder);
	}

	public PayOrder get(String id) {

		return masterPayOrderDao.get(id);
	}

	/*public PageInfo findByPage(PageInfo page, PayOrder payOrder) {

		List<PayOrder> payOrders = slavePayOrderDao.findByPage(payOrder);

		page.setData(payOrders);

		return page;
	}*/

	public int update(PayOrder payOrder) {

		return masterPayOrderDao.update(payOrder);
	}

	public int insert(PayOrder payOrder) {

		return masterPayOrderDao.insert(payOrder);
	}
}