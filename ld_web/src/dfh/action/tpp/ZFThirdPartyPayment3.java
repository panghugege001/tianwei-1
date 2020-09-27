package dfh.action.tpp;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import dfh.action.tpp.paramenum.ZFFormParamEnum3;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.StringUtil;

public class ZFThirdPartyPayment3 extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	ZFThirdPartyPayment3(String id,String name,String url){
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
			if (money > 5000) {
				message = "[提示]存款金额不能超过5000！";
				return false;
			}

			if(!StringUtil.isAvaliableBankCard(bankId)){
				message = "[提示]支付类型不存在，请重新选择。";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = ZFFormParamEnum3.toMap();
			
			formData.put(ZFFormParamEnum3.bank_code, bankId);
			// 定单金额（必填）
			formData.put(ZFFormParamEnum3.order_amount,df.format(money));
			// 商家定单号(必填)
			formData.put(ZFFormParamEnum3.order_no,user.getLoginname()+"_e68"+orderNo);
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			formData.put(ZFFormParamEnum3.order_time,format.format(new Date()));
			// 商品名称（必填）
			formData.put(ZFFormParamEnum3.product_name,user.getLoginname());
			// 公用业务回传参数（选填）
			formData.put(ZFFormParamEnum3.extra_return_param,user.getLoginname());
			
			// 商家定单号(必填)
			// 公用业务回传参数（选填）
			if(dfh.utils.StringUtil.isDianKa(bankId)){
				formData.put(ZFFormParamEnum3.extra_return_param,user.getLoginname()+"_flag_" + bankId);
				formData.put(ZFFormParamEnum3.order_no,user.getLoginname()+"_flag_" + bankId+"_e68"+orderNo);
			}else{
				formData.put(ZFFormParamEnum3.extra_return_param,user.getLoginname());
				formData.put(ZFFormParamEnum3.order_no,user.getLoginname()+"_e68"+orderNo);
			}
			
			String ip = getIp();
			// 客户端IP（选填）
			formData.put(ZFFormParamEnum3.client_ip,StringUtil.isBlank(ip)?"127.0.0.1":ip);
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(ZFFormParamEnum3.url.toString());
			noAppendValue.add(ZFFormParamEnum3.sign_type.toString());
			noAppendValue.add(ZFFormParamEnum3.sign.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : ZFFormParamEnum3.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			
			signSrc.append("key=").append("e68a45p32i49sdfsfs3_");
			String singInfo = signSrc.toString();
			formData.put(ZFFormParamEnum3.sign,DigestUtils.md5Hex(singInfo.getBytes("UTF-8")));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
