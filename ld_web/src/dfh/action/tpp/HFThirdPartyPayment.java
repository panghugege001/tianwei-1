package dfh.action.tpp;

import java.text.DecimalFormat;

import chinapnr2.SecureLink;
import dfh.action.tpp.paramenum.HFFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.StringUtil;

public class HFThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param url
	 */
	HFThirdPartyPayment(String id,String name,String url){
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
//	/**
//	 * 
//	 */
//	@Override
//	public boolean verification(Users user, String bankId, double money){
//		try{
//			if (!work) {
//				message = "[提示]在线支付正在维护！";
//				return false;
//			}
//			if (StringUtil.isBlank(bankId)) {
//				message = "[提示]支付银行不能为空！";
//				return false;
//			}
//			if (money < 10) {
//				message = "[提示]10元以上或者10元才能存款！";
//				return false;
//			}
//			if (money > 5000) {
//				message =  "[提示]存款金额不能超过5000！";
//				return false;
//			}
//			// 获取商家订单号
//			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHfNo", new Object[] {user.getLoginname(),money}, String.class);
//			if (orderNo == null) {
//				message = "[提示]获取商家订单号失败！";
//				return false;
//			}
//			
//			formData = HFFormParamEnum.toMap();
//			formData.put(HFFormParamEnum.GateId,bankId);
//			formData.put(HFFormParamEnum.OrdId,orderNo);
//			formData.put(HFFormParamEnum.MerPriv,user.getLoginname());
//			formData.put(HFFormParamEnum.OrdAmt,df.format(money));
//			
//			StringBuffer merSb = new StringBuffer();
//			for(Enum p : HFFormParamEnum.values()){
//				merSb.append(formData.get(p));
//			}
//			SecureLink sl = new SecureLink();
//			String path = this.getClass().getResource("").toURI().getPath() + "MerPrK872720.key";
//			int ret = sl.SignMsg((String) formData.get(HFFormParamEnum.MerId), path, merSb.toString());
//			if (ret != 0) {
//				message = "签名验证失败!";
//				return false;
//			}
//			formData.put(HFFormParamEnum.ChkValue,sl.getChkValue());
//			
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			message = "网络繁忙,请稍后再试！";
//		}
//		return false;
//	}

	@Override
	public boolean verification(Users user, String bankId, double money) {
		try{
			if (!work) {
				message = "[提示]在线支付正在维护！";
				return false;
			}
			if (StringUtil.isBlank(bankId)) {
				message = "[提示]支付银行不能为空！";
				return false;
			}
			if (money < 1) {
				message = "[提示]1元以上或者1元才能存款！";
				return false;
			}
			if (money > 5000) {
				message =  "[提示]存款金额不能超过5000！";
				return false;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			String orderNo="";
			// 获取商家订单号
			if(!this.id.equals("汇付")){//不是汇付  那就是汇付1-7
				 orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHfNo1", new Object[] {user.getLoginname(),money,this.id}, String.class);
			}else {
				 orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHfNo", new Object[] {user.getLoginname(),money}, String.class);
			}
			
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}
			
			formData = HFFormParamEnum.toMap();
			formData.put(HFFormParamEnum.GateId,bankId);
			formData.put(HFFormParamEnum.OrdId,orderNo);
			formData.put(HFFormParamEnum.MerPriv,user.getLoginname());
			formData.put(HFFormParamEnum.OrdAmt,df.format(money));
			
			
			SecureLink sl = new SecureLink();
			String keySt="MerPrK872720.key";
			String merID="872720";
			if(!StringUtil.isBlank(this.id)){
				if(this.id.equals("汇付1")){
					keySt="MerPrK873098.key";
					merID="873098";
					formData.put(HFFormParamEnum.MerId, merID);

					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}else if(this.id.equals("汇付2")){
					keySt="MerPrK873096.key";
					merID="873096";
					formData.put(HFFormParamEnum.MerId, merID);

					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}else if(this.id.equals("汇付3")){
					keySt="MerPrK873102.key";
					merID="873102";
					formData.put(HFFormParamEnum.MerId, merID);

					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}else if(this.id.equals("汇付4")){
					keySt="MerPrK873103.key";
					merID="873103";
					formData.put(HFFormParamEnum.MerId, merID);

					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}else if(this.id.equals("汇付5")){
					keySt="MerPrK873100.key";
					merID="873100";
					formData.put(HFFormParamEnum.MerId, merID);

					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}else if(this.id.equals("汇付6")){
					keySt="MerPrK873095.key";
					merID="873095";
					formData.put(HFFormParamEnum.MerId, merID);

					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}else if(this.id.equals("汇付7")){
					keySt="MerPrK873099.key";
					merID="873099";
					formData.put(HFFormParamEnum.MerId, merID);
					formData.put(HFFormParamEnum.BgRetUrl,"http://pay.jiekoue68.com:2112/asp/payHfReturn1.aspx");
				}
			}
			StringBuffer merSb = new StringBuffer();
			for(Enum p : HFFormParamEnum.values()){
				merSb.append(formData.get(p));
			}
			//String merData = Version + CmdId + MerId + OrdId + OrdAmt + CurCode + Pid + RetUrl + MerPriv + GateId + UsrMp + DivDetails + OrderType + PayUsrId + PnrNum + BgRetUrl + IsBalance + RequestDomain + OrderTime + ValidTime + ValidIp + ChkValue;
			String path = this.getClass().getResource("").toURI().getPath() + keySt;
			int ret = sl.SignMsg((String) merID, path, merSb.toString());
			if (ret != 0) {
				message = "签名验证失败!";
				return false;
			}
			formData.put(HFFormParamEnum.ChkValue,sl.getChkValue());
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}
}
