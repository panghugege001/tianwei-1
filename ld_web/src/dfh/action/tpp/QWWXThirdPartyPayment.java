package dfh.action.tpp;

import java.text.DecimalFormat;

import dfh.action.tpp.paramenum.QWWXFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.MD5;

public class QWWXThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	QWWXThirdPartyPayment(String id,String name,String url){
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean verification(Users user, String bankId, double money){
		
		try {
			if (!work) {
				message = "[提示]在线支付正在维护！";
				return false;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			// 判断订单金额
			if (money < 1) {
				message = "[提示]1元以上才能存款！";
				return false;
			}
			if (money > 3000) {
				message = "[提示]存款金额不能超过3000！";
				return false;
			}
			
			String  customerIP=getIp();
			if (customerIP == null || customerIP.equals("") || customerIP.length()>15) {
				customerIP = "127.0.0.1";
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getQwZfOrderNo", new Object[] {user.getLoginname(),money,"qwwx"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = QWWXFormParamEnum.toMap();
			
			//验签参数
			formData.put(QWWXFormParamEnum.orderid, orderNo);
			formData.put(QWWXFormParamEnum.value, df.format(money));
			formData.put(QWWXFormParamEnum.attach, "wap_"+ user.getLoginname());
			formData.put(QWWXFormParamEnum.payerIp, customerIP);
			
			String typeStr = QWWXFormParamEnum.type.getValues().toString();
			String parterStr = QWWXFormParamEnum.parter.getValues().toString();
			String valueStr = df.format(money);
			String orderidStr = orderNo;
			String callbackurlStr = QWWXFormParamEnum.callbackurl.getValues().toString();
			String md5key = QWWXFormParamEnum.key.getValues().toString();
			
			String sign =MD5.EkaPayBankMd5Sign(typeStr, parterStr, valueStr, orderidStr, callbackurlStr, md5key);
			formData.put(QWWXFormParamEnum.sign, sign);
			formData.remove(QWWXFormParamEnum.key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
