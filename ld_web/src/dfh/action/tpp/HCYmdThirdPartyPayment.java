package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import dfh.action.tpp.paramenum.HCYmdFormParamEnum;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class HCYmdThirdPartyPayment extends ThirdPartyPayment{
	DecimalFormat df = new DecimalFormat("0.0");

	HCYmdThirdPartyPayment(String id,String name,String url){
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean verification(Users user, String bankId, double money){
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
				message = "[提示]存款金额不能超过5000！";
				return false;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			
			// 获取商家订单号
			String BillNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHCYmdNo", new Object[] {user.getLoginname(),money,"hcwy"}, String.class);
			if (StringUtils.isBlank(BillNo)) {
				message =  "[提示]获取商家订单号失败！";
				return false;
			}
			
			formData = HCYmdFormParamEnum.toMap();
			formData.put(HCYmdFormParamEnum.MerNo,Constants.MerNo);
			formData.put(HCYmdFormParamEnum.BillNo,BillNo);
			formData.put(HCYmdFormParamEnum.Amount,df.format(money));
			formData.put(HCYmdFormParamEnum.ReturnURL,Constants.ReturnURLYmd);
			formData.put(HCYmdFormParamEnum.AdviceURL,Constants.AdviceURLYmd);
			formData.put(HCYmdFormParamEnum.defaultBankNumber,bankId);
			formData.put(HCYmdFormParamEnum.orderTime,DateUtil.fmtyyyyMMddHHmmss(new Date()));
			String md5src = "MerNo="+Constants.MerNo +"&"+ "BillNo="+BillNo +"&"+ "Amount="+money +"&"+"OrderTime="+formData.get(HCYmdFormParamEnum.orderTime)+"&"+ "ReturnURL="+formData.get(HCYmdFormParamEnum.ReturnURL)  +"&"+"AdviceURL="+formData.get(HCYmdFormParamEnum.AdviceURL)+"&"+Constants.MD5key ;
			System.out.println("ad5src:"+md5src);
			//MD5检验结果
			formData.put(HCYmdFormParamEnum.SignInfo,DigestUtils.md5Hex(md5src).toUpperCase());
			formData.put(HCYmdFormParamEnum.payType,Constants.payType);
			formData.put(HCYmdFormParamEnum.products,"wap_e");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}

}
