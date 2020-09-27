package com.nnti.pay.service.interfaces;

//import com.nnti.common.page.PageInfo;
import com.nnti.pay.model.vo.PayOrder;

public interface IPayOrderService {

	Double sumMoney(PayOrder payOrder);

	PayOrder get(String id);

//	PageInfo findByPage(PageInfo page, PayOrder payOrder);

	int update(PayOrder payOrder);

	int insert(PayOrder payOrder);
}