package com.nnti.pay.dao.slave;

import com.nnti.pay.model.vo.PayOrder;
import java.util.List;

public interface ISlavePayOrderDao {
	
	Double sumMoney(PayOrder payOrder);
	
	List<PayOrder> findByCondition(PayOrder payOrder);
	
	PayOrder get(String orderId);
	
	List<PayOrder> findByPage(PayOrder payOrder);
}