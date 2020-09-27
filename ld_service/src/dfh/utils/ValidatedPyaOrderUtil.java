package dfh.utils;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import dfh.model.PayOrderValidation;
import dfh.service.interfaces.IValidatedPayOrderServcie;

/**
 * 额度验证处理工具类
 * @date 2015-02-12
 */
public class ValidatedPyaOrderUtil {

	private static final ValidatedPyaOrderUtil instance = new ValidatedPyaOrderUtil();
	
	private Integer i;
	
	/**
	 * 防止外部实例化
	 */
	private ValidatedPyaOrderUtil(){};
	
	/**
	 * 单例
	 * @return
	 */
	public static ValidatedPyaOrderUtil getInstance(){
		return instance;
	}
	
	/**
	 * 生产一个额度验证存款订单
	 * @param payOrder
	 * @return
	 */
	public PayOrderValidation createPayOrder(PayOrderValidation payOrder, IValidatedPayOrderServcie service){
		synchronized (this) {
			i = 0;
			Double newAmount = getAvailableAmount(payOrder.getOriginalAmount(), service);
			if(newAmount == null) {
				payOrder.setCode("-1");  //无法处理该额度的存款
			}else{
				payOrder.setCode("1"); 
				payOrder.setAmount(newAmount);
				payOrder.setCreateTime(new Date());
				//保存
				service.saveValidatedPayOrder(payOrder);
			}
			return payOrder;
		}
	}
	
	/**
	 * 生产一个额度验证存款订单(改进)
	 * @param payOrder
	 * @return
	 */
	public Integer createPayOrderGj(PayOrderValidation payOrder, IValidatedPayOrderServcie service){
		        Integer numSize=0; 
			try {
				String sql = "INSERT into payorder_validation(username, amount,originalAmount, createtime, status,code, type) SELECT '"+payOrder.getUserName()+"', "+payOrder.getAmount()+","+payOrder.getAmount()+", SYSDATE(), '0', '1','wechat' from dual where not exists (select 1 from payorder_validation where amount = "+payOrder.getAmount()+" and status = '0')";
				 numSize = service.saveValidatedPayOrderTwo(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return numSize;
	}
	
	/**
	 * 获取一个可用的额度
	 * @param base 存款基数
	 * @return
	 */
	private Double getAvailableAmount(double base, IValidatedPayOrderServcie service){
		if(i >= 30){
			return null;
		}
		i++;
		String attach = RandomStringUtils.random(2, new char[]{'0', '1', '2', '3', '5', '6', '7', '8', '9'});
		if(attach.equals("00")){
			attach = RandomStringUtils.random(2, new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'});
		}
		Double newAmount = base + Double.valueOf(attach)/100;
		//检查额度是否可用
		Integer counts = service.verifyAmount(newAmount);
		if( counts != null && counts > 0){
			//当前额度已被占用，递归
			return getAvailableAmount(base, service);
		}
		return newAmount;
	}
	
}
