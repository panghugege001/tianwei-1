package com.nnti.pay.dao.master;

import com.nnti.pay.model.vo.MerchantPay;

import java.util.List;
import java.util.Map;

public interface IMasterMerchantPayDao {

	Integer insert(MerchantPay merchantPay);

	Integer update(MerchantPay merchantPay);

	Integer delete(Long id);

	List<MerchantPay> findByCondition(Map<String, Object> map);

	MerchantPay get(Long id);
	
	MerchantPay controlSwitch(String payway);
}