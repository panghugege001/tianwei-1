package dfh.service.implementations;

import dfh.dao.ValidatedPayOrderDao;
import dfh.model.PayOrderValidation;
import dfh.service.interfaces.IValidatedPayOrderServcie;

public class ValidatedPayOrderServiceImpl implements IValidatedPayOrderServcie {

	private ValidatedPayOrderDao payorderDao;
	
	@Override
	public Integer getUnPayedOrderCount(String userName) {
		return payorderDao.getUnPayedOrderCount(userName);
	}

	@Override
	public Integer verifyAmount(double amount) {
		return payorderDao.verifyAmount(amount);
	}

	@Override
	public void saveValidatedPayOrder(PayOrderValidation payOrder) {
		payorderDao.save(payOrder);
	}

	public ValidatedPayOrderDao getPayorderDao() {
		return payorderDao;
	}

	public void setPayorderDao(ValidatedPayOrderDao payorderDao) {
		this.payorderDao = payorderDao;
	}

	
	 @Override
	 public PayOrderValidation getUnPayedOrderCountVo(PayOrderValidation payOrder) {
	  return payorderDao.getUnPayedOrderCountVo(payOrder);
	 }
	 
	@Override
	public Integer getUnPayedOrderCountGj(PayOrderValidation payOrder) {
		return payorderDao.getUnPayedOrderCountGj(payOrder);
	}
	
	/****
	 * 保存额度订单
	 */
	@Override
	public Integer saveValidatedPayOrderTwo(String sql) {
		return payorderDao.saveValidatedPayOrderTwo(sql);
	}
	 
}
