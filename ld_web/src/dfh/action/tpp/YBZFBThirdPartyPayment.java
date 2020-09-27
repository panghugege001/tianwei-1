package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import dfh.action.customer.DinpayAction;
import dfh.action.tpp.paramenum.YBZFBFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;

public class YBZFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("0");
	YBZFBThirdPartyPayment(String id,String name,String url){
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
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getYbZfbOrderNo", new Object[] {user.getLoginname(),money,"ybzfb"}, String.class);

			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}
			formData = YBZFBFormParamEnum.toMap();			

			// 定单金额（必填）
			formData.put(YBZFBFormParamEnum.paymoney, df.format(money));
			formData.put(YBZFBFormParamEnum.ordernumber,orderNo);
			formData.put(YBZFBFormParamEnum.attach,"wap_"+user.getLoginname());
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(YBZFBFormParamEnum.sign.toString());
			noAppendValue.add(YBZFBFormParamEnum.attach.toString());
			noAppendValue.add(YBZFBFormParamEnum.hrefbackurl.toString());
			noAppendValue.add(YBZFBFormParamEnum.url.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : YBZFBFormParamEnum.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			if(signSrc.lastIndexOf("&")!=-1){
				signSrc = signSrc.deleteCharAt(signSrc.lastIndexOf("&"));
			}
			//System.out.println("singInfo=="+signSrc);
			String singInfo = DinpayAction.signByMD5(signSrc.toString(), YBZFBFormParamEnum.sign.getValue().toString());
			formData.put(YBZFBFormParamEnum.sign,singInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}
}
