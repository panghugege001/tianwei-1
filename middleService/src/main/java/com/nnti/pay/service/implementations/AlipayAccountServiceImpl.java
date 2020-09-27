package com.nnti.pay.service.implementations;

import com.nnti.pay.dao.slave.ISlaveAlipayAccountDao;
import com.nnti.pay.model.vo.AlipayAccount;
import com.nnti.pay.service.interfaces.IAlipayAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlipayAccountServiceImpl implements IAlipayAccountService {

	@Autowired
	private ISlaveAlipayAccountDao slaveAlipayAccountDao;

	public AlipayAccount findLoginNameAndDisable(AlipayAccount alipayAccount) {
		
		List<AlipayAccount> list = slaveAlipayAccountDao.findCondition(alipayAccount);
		
		if (list.size() > 0) {
			
			return list.get(0);
		}
		
		return null;
	}
}