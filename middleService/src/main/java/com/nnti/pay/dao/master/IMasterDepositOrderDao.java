package com.nnti.pay.dao.master;

import java.util.List;
import java.util.Map;

import com.nnti.pay.model.vo.DepositOrder;

public interface IMasterDepositOrderDao {
    
	Integer insert(DepositOrder depositOrder);

    DepositOrder get(String depositId);

    List<DepositOrder> findByLoginName(Map<String, Object> params);
    
    Boolean discardOrder(DepositOrder depositOrder) ;

}