package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.KDZFB2FormParamEnum;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class KDZFB2ThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	KDZFB2ThirdPartyPayment(String id,String name,String url){
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
			
			// 判断订单金额
			if (money < 1) {
				message = "[提示]1元以上或者1元才能存款！";
				return false;
			}
			if (money > 3000) {
				message = "[提示]存款金额不能超过3000！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdWxZfsOrderNo", new Object[] {user.getLoginname(),money,"kdzfb2"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = KDZFB2FormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(KDZFB2FormParamEnum.P_Price, df.format(money));
			formData.put(KDZFB2FormParamEnum.P_FaceValue, df.format(money));

			formData.put(KDZFB2FormParamEnum.P_OrderId,orderNo);
			formData.put(KDZFB2FormParamEnum.P_Notic,"wap_"+user.getLoginname());
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(KDZFB2FormParamEnum.P_FaceValue.toString());
			noAppendValue.add(KDZFB2FormParamEnum.P_Quantity.toString());
			noAppendValue.add(KDZFB2FormParamEnum.P_Description.toString());
			noAppendValue.add(KDZFB2FormParamEnum.P_Notic.toString());
			noAppendValue.add(KDZFB2FormParamEnum.P_Result_URL.toString());
			noAppendValue.add(KDZFB2FormParamEnum.P_Notify_URL.toString());
			noAppendValue.add(KDZFB2FormParamEnum.P_PostKey.toString());
			noAppendValue.add(KDZFB2FormParamEnum.apiUrl.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : KDZFB2FormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(value+"|");
				}
			}
			signSrc.append(Constants.KDZFB2_KEY);
			String singInfo = EncryptionUtil.encryptPassword(signSrc.toString());
			formData.put(KDZFB2FormParamEnum.P_PostKey,singInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
