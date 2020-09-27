package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import dfh.action.tpp.paramenum.GFBFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;

public class GFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	GFBThirdPartyPayment(String id,String name,String url){
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
			if (money > 50000) {
				message = "[提示]存款金额不能超过50000！";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderGfbNo", new Object[] {user.getLoginname(),money,"gfb1"}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = GFBFormParamEnum.toMap();
			
			formData.put(GFBFormParamEnum.merOrderNum, orderNo);
			formData.put(GFBFormParamEnum.tranDateTime, DateUtil.fmtyyyyMMddHHmmss(new Date()));
			formData.put(GFBFormParamEnum.tranIP, getIp());
			formData.put(GFBFormParamEnum.tranAmt, Double.toString(money));
			formData.put(GFBFormParamEnum.bankCode, bankId);
			formData.put(GFBFormParamEnum.merRemark1, "gfb1");
			
			String key = "sduifyho87sdkfh2";
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(GFBFormParamEnum.url.toString());
			noAppendValue.add(GFBFormParamEnum.charset.toString());
			noAppendValue.add(GFBFormParamEnum.language.toString());
			noAppendValue.add(GFBFormParamEnum.signType.toString());
			noAppendValue.add(GFBFormParamEnum.currencyType.toString());
			noAppendValue.add(GFBFormParamEnum.virCardNoIn.toString());
			noAppendValue.add(GFBFormParamEnum.isRepeatSubmit.toString());
			noAppendValue.add(GFBFormParamEnum.goodsName.toString());
			noAppendValue.add(GFBFormParamEnum.goodsDetail.toString());
			noAppendValue.add(GFBFormParamEnum.buyerName.toString());
			noAppendValue.add(GFBFormParamEnum.buyerContact.toString());
			noAppendValue.add(GFBFormParamEnum.merRemark1.toString());
			noAppendValue.add(GFBFormParamEnum.merRemark2.toString());
			noAppendValue.add(GFBFormParamEnum.signValue.toString());
			noAppendValue.add(GFBFormParamEnum.bankCode.toString());
			noAppendValue.add(GFBFormParamEnum.userType.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : GFBFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(p.toString()+"=["+value+"]");
				}
			}
			signSrc.append("VerficationCode=[" + key + "]");
			key = DigestUtils.md5Hex(signSrc.toString());
			formData.put(GFBFormParamEnum.signValue, key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
