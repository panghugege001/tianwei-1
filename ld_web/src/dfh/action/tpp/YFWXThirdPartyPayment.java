package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.YFWXFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Yom;

public class YFWXThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	YFWXThirdPartyPayment(String id,String name,String url){
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
			if (money < 2) {
				message = "[提示]2元以上或者2元才能存款!";
				return false;
			}
			if (money > 3000) {
				message = "[提示]存款金额不能超过3000！";
				return false;
			}
			
			String  customerIP=getIp();
			if (customerIP == null || customerIP.equals("")) {
				customerIP = "127.0.0.1";
			}
			
			//分层处理，新玩家需满一周才能支付
			Boolean flag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUserReDate", new Object[] {user.getLoginname()}, Boolean.class);
			if(!flag){
				message = "[提示]新注册用户一周后才能使用此支付！";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getYfZfOrderNo", new Object[] {user.getLoginname(),money,"yfwx"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = YFWXFormParamEnum.toMap();
			
			formData.put(YFWXFormParamEnum.ORDER_NO, orderNo);
			formData.put(YFWXFormParamEnum.ORDER_AMOUNT, df.format(money));
			formData.put(YFWXFormParamEnum.RETURN_PARAMS, "wap_"+ user.getLoginname());
			formData.put(YFWXFormParamEnum.CUSTOMER_IP, customerIP);
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(YFWXFormParamEnum.SIGN.toString());
			noAppendValue.add(YFWXFormParamEnum.apiUrl.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : YFWXFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			if(signSrc.lastIndexOf("&")!=-1){
				signSrc = signSrc.deleteCharAt(signSrc.lastIndexOf("&"));
			}
			String sign = Yom.getMD5(signSrc.toString());
			formData.put(YFWXFormParamEnum.SIGN, sign);
			formData.remove(YFWXFormParamEnum.KEY);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
