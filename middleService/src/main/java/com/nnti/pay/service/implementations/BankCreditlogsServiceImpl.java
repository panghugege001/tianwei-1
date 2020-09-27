package com.nnti.pay.service.implementations;

import com.nnti.common.constants.CreditType;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.pay.dao.master.IMasterBankCreditlogsDao;
import com.nnti.pay.model.vo.BankCreditlogs;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBankCreditlogsService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BankCreditlogsServiceImpl implements IBankCreditlogsService {

	private Logger log = Logger.getLogger(BankCreditlogsServiceImpl.class);

	@Autowired
	private IMasterBankCreditlogsDao masterBankCreditlogsDao;

	@Autowired
	private IMerchantPayService merchantPayService;

	public Integer add(BankCreditlogs bankCreditlogs) {
		
		return masterBankCreditlogsDao.insert(bankCreditlogs);
	}

	@Override
	public void updateAmount(Long id, Double bankAmount, String orderId) throws BusinessException {
		Assert.notEmpty(id, bankAmount, orderId);

		MerchantPay mp = merchantPayService.findById(id);
		if (mp != null) {

			//添加银行额度流水
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
			bankCreditlogs.setCreateTime(DateUtil.getCurrentTimestamp());
			bankCreditlogs.setBankName(mp.getPayName());
			bankCreditlogs.setType(CreditType.NETPAY.getCode());
			bankCreditlogs.setNewCredit(bankAmount);
			bankCreditlogs.setCredit(bankAmount);
			bankCreditlogs.setRemit(bankAmount);
			bankCreditlogs.setRemark(orderId);
			add(bankCreditlogs);

			log.info("调整在线存款结束--------" + bankCreditlogs.getBankName());
		}
	}
}