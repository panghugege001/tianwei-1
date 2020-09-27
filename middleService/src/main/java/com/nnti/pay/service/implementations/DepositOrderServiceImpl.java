package com.nnti.pay.service.implementations;

import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.MyUtils;
import com.nnti.pay.dao.master.IMasterDepositOrderDao;
import com.nnti.pay.model.vo.DepositOrder;
import com.nnti.pay.service.interfaces.IDepositOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class DepositOrderServiceImpl implements IDepositOrderService {

	@Autowired
	private IUserService userService;
	@Autowired
	private IMasterDepositOrderDao masterDepositOrderDao;

	public String createDepositId(String loginName, BankInfo bank) throws Exception {

		String depositId = MyUtils.randomByLength(5);

		User user = userService.get(loginName);

		Assert.notEmpty(user);

		DepositOrder depositOrder = new DepositOrder();
		depositOrder.setLoginName(loginName);
		depositOrder.setBankName(bank.getBankName());
		depositOrder.setDepositId(depositId);
		depositOrder.setCreateTime(new Date());
		depositOrder.setStatus(0);
		depositOrder.setAccountName(bank.getUserName());
		depositOrder.setBankNo(bank.getBankCard());
		depositOrder.setRemark(bank.getAccountNo());

		masterDepositOrderDao.insert(depositOrder);

		return depositId;
	}
	
	@Override
	public String createDepositIdMc(String loginName,Double amount, BankInfo bank,Integer length) throws Exception {
		String depositId = MyUtils.randomByLength(length);

		User user = userService.get(loginName);

		Assert.notEmpty(user);

		DepositOrder depositOrder = new DepositOrder();
		depositOrder.setLoginName(loginName);
		depositOrder.setBankName(bank.getBankName());
		depositOrder.setDepositId(depositId);
		depositOrder.setCreateTime(new Date());
		depositOrder.setUpdateTime(null);
		depositOrder.setStatus(0);
		depositOrder.setAccountName(bank.getUserName());
		depositOrder.setBankNo(bank.getBankCard());
		depositOrder.setFlag(1);
		depositOrder.setType("4");
		depositOrder.setAmount(amount);

		masterDepositOrderDao.insert(depositOrder);

		return depositId;
	}
	
	
	

	@Override
	public DepositOrder findById(String depositId) {
		return masterDepositOrderDao.get(depositId);
	}

	@Override
	public Boolean discardOrder(DepositOrder depositOrder) {
		return masterDepositOrderDao.discardOrder(depositOrder);
	}

	@Override
	public  List<DepositOrder>  findByLoginName(String loginame) {
		Map<String , Object> params = new HashMap<String , Object>();
		params.put("loginName", loginame);
		params.put("status", 0);
		params.put("type", 4);
		return masterDepositOrderDao.findByLoginName(params);
	}


}