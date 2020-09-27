package com.nnti.pay.dao.slave;

import com.nnti.pay.model.vo.MerchantPay;
import java.util.List;
import java.util.Map;

public interface ISlaveMerchantPayDao {
	
	List<MerchantPay> findByCondition(Map<String, Object> map);
	
	MerchantPay get(Long id);
}