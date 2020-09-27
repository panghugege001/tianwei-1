package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.KDZFBFormParamEnum;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;

public class KDZFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	KDZFBThirdPartyPayment(String id,String name,String url){
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
			if (money > 2000) {
				message = "[提示]存款金额不能超过2000！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdZfOrderNo", new Object[] {user.getLoginname(),money}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = KDZFBFormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(KDZFBFormParamEnum.P_Price, df.format(money));
			formData.put(KDZFBFormParamEnum.P_FaceValue, df.format(money));
			formData.put(KDZFBFormParamEnum.P_Notic,user.getLoginname());
			formData.put(KDZFBFormParamEnum.P_OrderId,orderNo);
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(KDZFBFormParamEnum.P_Quantity.toString());
			noAppendValue.add(KDZFBFormParamEnum.P_Description.toString());
			noAppendValue.add(KDZFBFormParamEnum.P_Notic.toString());
			noAppendValue.add(KDZFBFormParamEnum.P_Result_URL.toString());
			noAppendValue.add(KDZFBFormParamEnum.P_Notify_URL.toString());
			noAppendValue.add(KDZFBFormParamEnum.P_PostKey.toString());
			noAppendValue.add(KDZFBFormParamEnum.P_FaceValue.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : KDZFBFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(value+"|");
				}
			}
			
			String key = "4f4b9ebd2ba344ccbd70a3977c71b431";
			String singInfo = EncryptionUtil.encryptPassword(signSrc.append(key).toString());
			System.out.println(signSrc.append(key).toString());
			formData.put(KDZFBFormParamEnum.P_PostKey,singInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
