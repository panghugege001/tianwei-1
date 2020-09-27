package com.nnti.pay.dao.master;

import com.nnti.pay.model.vo.PayOrder;

public interface IMasterPayOrderDao {

	int insert(PayOrder payOrder);

	int update(PayOrder payOrder);

	PayOrder get(String orderId);
}