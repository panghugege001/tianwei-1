package com.nnti.pay.dao.slave;

import com.nnti.pay.model.vo.AlipayAccount;
import java.util.List;

public interface ISlaveAlipayAccountDao {
    
	List<AlipayAccount> findCondition(AlipayAccount alipayAccount_new);
}