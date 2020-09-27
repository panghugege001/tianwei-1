package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.customer.DinpayAction;
import dfh.action.tpp.paramenum.XLBWYFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;

public class XLBWYThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0");
	XLBWYThirdPartyPayment(String id,String name,String url){
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
			if (money > 50000) {
				message = "[提示]存款金额不能超过50000！";
				return false;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXlbWyOrderNo", new Object[] {user.getLoginname(),money}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = XLBWYFormParamEnum.toMap();

			// 定单金额（必填）
			formData.put(XLBWYFormParamEnum.orderNo,orderNo);
			formData.put(XLBWYFormParamEnum.tradeDate, DateUtil.fmtyyyyMMdd(new Date()));
			formData.put(XLBWYFormParamEnum.amt,df.format(money));			
			formData.put(XLBWYFormParamEnum.merchParam,user.getLoginname());
			System.out.println("bankIdbankIdbankId="+bankId);
			formData.put(XLBWYFormParamEnum.bankCode,bankId);
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(XLBWYFormParamEnum.url.toString());
			noAppendValue.add(XLBWYFormParamEnum.signMsg.toString());
			noAppendValue.add(XLBWYFormParamEnum.choosePayType.toString());
			noAppendValue.add(XLBWYFormParamEnum.bankCode.toString());
			noAppendValue.add(XLBWYFormParamEnum.key.toString());

			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : XLBWYFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(p.toString()+"=").append(value+"&");
				}
			}
			if(signSrc.lastIndexOf("&")>0)
				signSrc.deleteCharAt(signSrc.lastIndexOf("&"));
			String signMsg =  DinpayAction.signByMD5(signSrc.toString(), XLBWYFormParamEnum.key.getValue().toString());
			signMsg.replaceAll("\r", "").replaceAll("\n", "");
			formData.put(XLBWYFormParamEnum.signMsg,signMsg);
			formData.remove(XLBWYFormParamEnum.key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
