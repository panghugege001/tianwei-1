package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.CommonHaierFormParamEnum;
import dfh.action.tpp.paramenum.GFBFormParamEnum;
import dfh.model.Users;
import dfh.model.enums.CommonHaierEnum;
import dfh.utils.AxisUtil;
import dfh.utils.Core;
import dfh.utils.DateUtil;
import dfh.utils.MD5Haier;

public class CommonHaierThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	CommonHaierThirdPartyPayment(String id,String name,String url){
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
			
			String payType = "haier";
			if(!CommonHaierEnum.existKey(payType)){
				message = "[提示]非法参数";
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
			if (money > 1000) {
				message = "[提示]存款金额不能超过1000！";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHaierNo", new Object[] {user.getLoginname(), money, "haier"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}
			
			String partner_admin = "gpi001@sina.com";    //*随专案变动
			String notify_url = "http://pay.jiekoue68.com:2112/haier/haierCallBack.aspx";  //*随专案变动  //银行callback url
			String amount = df.format(money);
			formData = CommonHaierFormParamEnum.toMap();
			formData.put(CommonHaierFormParamEnum.memo,user.getLoginname());
			formData.put(CommonHaierFormParamEnum.pay_method,"online_bank^"+amount+"^"+bankId+",C,GC");
			formData.put(CommonHaierFormParamEnum.request_no,orderNo);
			formData.put(CommonHaierFormParamEnum.trade_list,orderNo+"~e68~"+amount+"~1~"+amount+"~~"+partner_admin+"~1~instant~~~~~~"+DateUtil.fmtyyyyMMddHHmmss(new Date())+"~"+notify_url+"~");
			
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(CommonHaierFormParamEnum.sign_type.toString());
			noAppendValue.add(CommonHaierFormParamEnum.sign.toString());
			noAppendValue.add(CommonHaierFormParamEnum.url.toString());

			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : CommonHaierFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			signSrc = signSrc.deleteCharAt(signSrc.length()-1);   //去除掉最后"&"
			String sign = MD5Haier.sign(signSrc.toString(), Core.key, CommonHaierFormParamEnum._input_charset.getValue().toString());
			formData.put(CommonHaierFormParamEnum.sign, sign);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
