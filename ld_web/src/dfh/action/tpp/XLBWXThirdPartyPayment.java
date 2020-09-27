package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import dfh.action.customer.DinpayAction;
import dfh.action.tpp.paramenum.XLBWXFormParamEnum;
import dfh.json.JSONObject;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;
import dfh.utils.MD5;
import dfh.utils.StringUtil;
import dfh.utils.XlbWxSendRequestUtil;

public class XLBWXThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#.00");
	XLBWXThirdPartyPayment(String id,String name,String url){
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
			if(money%1!=0){ 
				message = "[提示]存款金额必须为整数！";
				return false ;
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
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXlbOrderNo", new Object[] {user.getLoginname(),money}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = XLBWXFormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(XLBWXFormParamEnum.orderNo,orderNo);
			formData.put(XLBWXFormParamEnum.tradeDate, DateUtil.fmtyyyyMMdd(new Date()));
			formData.put(XLBWXFormParamEnum.amt,df.format(money));			
			formData.put(XLBWXFormParamEnum.merchParam,"wap_"+user.getLoginname());
			
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(XLBWXFormParamEnum.url.toString());
			noAppendValue.add(XLBWXFormParamEnum.signMsg.toString());
			noAppendValue.add(XLBWXFormParamEnum.choosePayType.toString());
			noAppendValue.add(XLBWXFormParamEnum.bankCode.toString());
			noAppendValue.add(XLBWXFormParamEnum.qrcode.toString());
			noAppendValue.add(XLBWXFormParamEnum.key.toString());

			
			StringBuffer signSrc = new StringBuffer(); 
			StringBuffer url = new StringBuffer(XLBWXFormParamEnum.url.getValue().toString()+"?"); 
			for(Enum p : XLBWXFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(p.toString()+"=").append(value+"&");
					url.append(p.toString()+"=").append(value+"&");
				}
			}
			if(signSrc.lastIndexOf("&")>0)
				signSrc.deleteCharAt(signSrc.lastIndexOf("&"));
			String signMsg =  DinpayAction.signByMD5(signSrc.toString(), XLBWXFormParamEnum.key.getValue().toString());
			signMsg.replaceAll("\r", "").replaceAll("\n", "");
			url.append("signMsg="+signMsg);
			System.out.println(url.toString()+"---Post参数");
			String result =MD5. receiveBySend(url.toString(), "UTF-8");
			System.out.println(result+">>>>>>>>>>>>>>>>>wap返回result");
			JSONObject a = new JSONObject(result);
			String resultCode = a.get("resultCode").toString();
			String code = a.get("code").toString();
			byte[] b = null;
			if(resultCode.equals("00")){
				BASE64Decoder decoder = new BASE64Decoder();
			    b = decoder.decodeBuffer(code); 
			}
			qrcode =  new String(b); 
			
			if(StringUtils.isBlank(qrcode)){
				message = "二维码生成失败。";
				return false ;
			}else{
				formData.put(XLBWXFormParamEnum.qrcode, qrcode);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
