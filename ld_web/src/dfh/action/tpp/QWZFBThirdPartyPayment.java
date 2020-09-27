package dfh.action.tpp;

import java.text.DecimalFormat;

import dfh.action.tpp.paramenum.QWZFBFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.MD5;

public class QWZFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	QWZFBThirdPartyPayment(String id,String name,String url){
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
				message = "[提示]存款金额不能小于1元！";
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
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getQwZfOrderNo", new Object[] {user.getLoginname(),money,"qwzfb"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = QWZFBFormParamEnum.toMap();
			
			//验签参数
			formData.put(QWZFBFormParamEnum.orderid, orderNo);
			formData.put(QWZFBFormParamEnum.value, df.format(money));
			formData.put(QWZFBFormParamEnum.attach, "wap_"+ user.getLoginname());
			formData.put(QWZFBFormParamEnum.payerIp, customerIP);
			
		    //不验签参数
//			Set<String> noAppendValue = new HashSet<String>();
//			noAppendValue.add(QWZFBFormParamEnum.hrefbackurl.toString());
//			noAppendValue.add(QWZFBFormParamEnum.attach.toString());
//			noAppendValue.add(QWZFBFormParamEnum.payerIp.toString());
//			noAppendValue.add(QWZFBFormParamEnum.sign.toString());
//			noAppendValue.add(QWZFBFormParamEnum.key.toString());
//			
//			
//			StringBuffer signSrc = new StringBuffer(); 
//			for(Enum p : QWZFBFormParamEnum.values()){
//				Object value = formData.get(p);
//				if(!noAppendValue.contains(p.toString())){
//					signSrc.append(p.toString()+"="+value+"&");
//				}
//			}
//			
//			if(signSrc.lastIndexOf("&")!=-1){
//				signSrc = signSrc.deleteCharAt(signSrc.lastIndexOf("&"));
//			}
//			String newSignSrc = signSrc.toString().replace("parter", "partner");
//			System.out.println(newSignSrc);
			String typeStr = QWZFBFormParamEnum.type.getValues().toString();
			String parterStr = QWZFBFormParamEnum.parter.getValues().toString();
			String valueStr = df.format(money);
			String orderidStr = orderNo;
			String callbackurlStr = QWZFBFormParamEnum.callbackurl.getValues().toString();
			String md5key = QWZFBFormParamEnum.key.getValues().toString();
			
			String sign =MD5.EkaPayBankMd5Sign(typeStr, parterStr, valueStr, orderidStr, callbackurlStr, md5key);
			formData.put(QWZFBFormParamEnum.sign, sign);
			formData.remove(QWZFBFormParamEnum.key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
