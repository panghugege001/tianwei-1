package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.XBWXFormParamEnum;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class XBWXThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	XBWXThirdPartyPayment(String id,String name,String url){
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
			Integer moneyType = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "judgeOnlineDepositAmountWay", new Object[] {"新贝微信"}, Integer.class);
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
			if (money > 3000) {
				message = "[提示]存款金额不能超过3000！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXinBWxOrderNo", new Object[] {user.getLoginname(),money}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = XBWXFormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(XBWXFormParamEnum.Amount, df.format(money));

			formData.put(XBWXFormParamEnum.OrderDate,DateUtil.fmtyyyyMMddHHmmss(new Date()));
			formData.put(XBWXFormParamEnum.OrderId,orderNo);
			formData.put(XBWXFormParamEnum.Remark2,user.getLoginname());
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(XBWXFormParamEnum.SignValue.toString());
			noAppendValue.add(XBWXFormParamEnum.url.toString());
			noAppendValue.add(XBWXFormParamEnum.Remark1.toString());
			noAppendValue.add(XBWXFormParamEnum.Remark2.toString());
			
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : XBWXFormParamEnum.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"=["+value+"]");
				}
			}
			
			String singInfo = EncryptionUtil.encryptPassword(signSrc.toString()).toUpperCase();
			formData.put(XBWXFormParamEnum.SignValue,singInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
