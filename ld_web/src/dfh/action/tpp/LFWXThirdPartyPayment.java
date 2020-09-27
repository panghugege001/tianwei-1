package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.LFWXFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;
import dfh.utils.DigestUtils;

public class LFWXThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	LFWXThirdPartyPayment(String id,String name,String url){
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
			String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
			if(Double.parseDouble(sumMoney)<1000){
				message = "[提示]您的活跃度不足.暂不能使用微信支付！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getLfWxOrderNo", new Object[] {user.getLoginname(),money}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = LFWXFormParamEnum.toMap();
			
			formData.put(LFWXFormParamEnum.outOrderId, orderNo);
			// 定单金额（必填）
			formData.put(LFWXFormParamEnum.amount,df.format(money));
			formData.put(LFWXFormParamEnum.returnParam,user.getLoginname());
			formData.put(LFWXFormParamEnum.submitTime,DateUtil.fmtyyyyMMddHHmmss(new Date()));
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(LFWXFormParamEnum.sign.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : LFWXFormParamEnum.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			signSrc.delete(signSrc.length()-1, signSrc.length());
			signSrc.append("53CD1F703BD380CA7C6EB730A018715B");
			String singInfo = signSrc.toString();
			formData.put(LFWXFormParamEnum.sign,DigestUtils.md5DigestAsHex(singInfo.getBytes("UTF-8")));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
