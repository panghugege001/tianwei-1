package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.XBZFBFormParamEnum;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class XBZFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("0.00");
	XBZFBThirdPartyPayment(String id,String name,String url){
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
			// 判断订单金额
			if (money < 1) {
				message = "[提示]1元以上或者1元才能存款！";
				return false;
			}
			if (money > 3001) {
				message = "[提示]存款金额不能超过3001！";
				return false;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXinBZfbOrderNo", new Object[] {user.getLoginname(),money}, String.class);

			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = XBZFBFormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(XBZFBFormParamEnum.Amount, df.format(money));

			formData.put(XBZFBFormParamEnum.OrderDate,DateUtil.fmtyyyyMMddHHmmss(new Date()));
			formData.put(XBZFBFormParamEnum.OrderId,orderNo);
			formData.put(XBZFBFormParamEnum.Remark2,user.getLoginname());
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(XBZFBFormParamEnum.SignValue.toString());
			noAppendValue.add(XBZFBFormParamEnum.url.toString());
			noAppendValue.add(XBZFBFormParamEnum.Remark1.toString());
			noAppendValue.add(XBZFBFormParamEnum.Remark2.toString());
			
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : XBZFBFormParamEnum.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"=["+value+"]");
				}
			}
			
			String singInfo = EncryptionUtil.encryptPassword(signSrc.toString()).toUpperCase();
			formData.put(XBZFBFormParamEnum.SignValue,singInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}
}
