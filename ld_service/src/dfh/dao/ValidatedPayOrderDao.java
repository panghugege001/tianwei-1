package dfh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.model.PayOrderValidation;

public class ValidatedPayOrderDao extends UniversalDao {

	public ValidatedPayOrderDao(){}
	
	/**
	 * 获取玩家未支付的订单的数量
	 * @param userName
	 * @return
	 */
	public Integer getUnPayedOrderCount(String userName) {
		Criteria criteria = getSession().createCriteria(PayOrderValidation.class);
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("status", "0"));  //未支付状态
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * 验证额度是否存在
	 * @param amount
	 * @return
	 */
	public Integer verifyAmount(double amount) {
		Criteria criteria = getSession().createCriteria(PayOrderValidation.class);
		criteria.add(Restrictions.eq("amount", amount));    
		criteria.add(Restrictions.eq("status", "0"));   //未支付状态
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * 保存额度验证订单
	 * @param payOrder
	 */
	public void saveValidatedPayOrder(PayOrderValidation payOrder) {
		save(payOrder);
	}
	
	/**
	 * 获取玩家未支付的订单的数量
	 * @param userName
	 * @return
	 */
	
	public PayOrderValidation getUnPayedOrderCountVo(PayOrderValidation payOrder) {
		PayOrderValidation payOrderValidation = new PayOrderValidation();
		Criteria criteria = getSession().createCriteria(PayOrderValidation.class);
		criteria.add(Restrictions.eq("userName",  payOrder.getUserName()));
		if("wechat".equals(payOrder.getType())){
			criteria.add(Restrictions.eq("type", payOrder.getType()));
		}else {
			criteria.add(Restrictions.eq("type", "wechat"));
		}
		criteria.add(Restrictions.eq("status", "0"));  //未支付状态
		List<PayOrderValidation> list =criteria.list();
		
		if(list!=null&&list.size()>0&&list.get(0)!=null){
			 payOrderValidation = list.get(0);
			 return  payOrderValidation;
		}
		return null;
	}
	/**
	 * 获取玩家未支付的订单的数量 (改进)
	 * @param userName
	 * @return
	 */
	
	public Integer getUnPayedOrderCountGj(PayOrderValidation payOrder) {
		Criteria criteria = getSession().createCriteria(PayOrderValidation.class);	
		criteria.add(Restrictions.or(Restrictions.eq("amount", payOrder.getAmount()), Restrictions.eq("userName", payOrder.getUserName())));
		criteria.add(Restrictions.eq("status", "0"));  //未支付状态
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
     }
	
}
