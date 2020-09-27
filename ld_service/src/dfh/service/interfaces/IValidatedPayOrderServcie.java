package dfh.service.interfaces;

import dfh.model.PayOrderValidation;

/**
 * 额度验证存款
 */
public interface IValidatedPayOrderServcie {

	/**
	 * 获取用户未支付的额度验证存款订单数量
	 * @param userName  用户名
	 * @return
	 */
	public Integer getUnPayedOrderCount(String userName);
	
	/**
	 * 检查额度是否被占用
	 * @param amount
	 * @return
	 */
	public Integer verifyAmount(double amount);
	
	/**
	 * 保存额度验证存款订单
	 * @param payOrder
	 */
	public void saveValidatedPayOrder(PayOrderValidation payOrder);
	
	
	public PayOrderValidation getUnPayedOrderCountVo(PayOrderValidation payOrder);
	
	public Integer getUnPayedOrderCountGj(PayOrderValidation payOrder);
	
	/**
	 * 保存额度验证存款订单
	 * @param payOrder
	 */
	public Integer saveValidatedPayOrderTwo(String sql);
}
