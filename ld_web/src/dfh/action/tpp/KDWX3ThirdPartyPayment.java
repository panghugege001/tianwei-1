package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.KDWX3FormParamEnum;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class KDWX3ThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.0");
	KDWX3ThirdPartyPayment(String id,String name,String url){
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	public static void main(String[] arg0) throws Exception{
		
	}
	  
	/**
	 * 
	 */
	@Override
	public boolean verification(Users user, String bankId, double money) {
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

			Integer moneyType = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "judgeOnlineDepositAmountWay", new Object[] {"口袋微信支付3"}, Integer.class);
			
			if(moneyType>0){
				String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
				String divide = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[] {"type999","001","否"}, String.class);
				Double divM = 3000.0;
				if(StringUtil.isNotEmpty(divide)){
					divM = Double.parseDouble(divide.split("#")[1]);
				}
				
				if(moneyType == 1 && Double.parseDouble(sumMoney)>divM){
					message = "[提示]此通道维护升级，请您使用其它支付通道+！";
					return false;
				}else if(moneyType == 2 && Double.parseDouble(sumMoney)<=divM){
					message = "[提示]此通道维护升级，请您使用其它支付通道-！";
					return false;
				}
			}
			
			// 判断订单金额
			if (money < 1) {
				message = "[提示]1元以上或者1元才能存款！";
				return false;
			}
			if (money > 1000) {
				message = "[提示]存款金额不能超过1000！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdWxZfsOrderNo", new Object[] {user.getLoginname(),money,"kdwxzf3"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = KDWX3FormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(KDWX3FormParamEnum.P_Price, df.format(money));
			formData.put(KDWX3FormParamEnum.P_FaceValue, df.format(money));
			formData.put(KDWX3FormParamEnum.P_OrderId,orderNo);
			formData.put(KDWX3FormParamEnum.P_Notic,"wap_"+user.getLoginname());
			Set<String> noAppendValue = new HashSet<String>();    
			noAppendValue.add(KDWX3FormParamEnum.P_Price.toString());
			noAppendValue.add(KDWX3FormParamEnum.P_Quantity.toString());
			noAppendValue.add(KDWX3FormParamEnum.P_Description.toString());
			noAppendValue.add(KDWX3FormParamEnum.P_Notic.toString());
			noAppendValue.add(KDWX3FormParamEnum.P_Result_URL.toString());
			noAppendValue.add(KDWX3FormParamEnum.P_Notify_URL.toString());
			noAppendValue.add(KDWX3FormParamEnum.P_PostKey.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : KDWX3FormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(value+"|");
				}
			}
			signSrc.append("013bb4749fe24016ac28f1bc8eda4c98");
			String singInfo = EncryptionUtil.encryptPassword(signSrc.toString());
			formData.put(KDWX3FormParamEnum.P_PostKey,singInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
