package dfh.action.tpp;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import dfh.action.tpp.paramenum.IPSFormParamEnum;
import dfh.model.Users;
import dfh.utils.StringUtil;

public class IPShirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param url
	 */
	IPShirdPartyPayment(String id,String name,String url){
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
			if (money > 2000) {
				message =  "[提示]存款金额不能超过2000！";
				return false;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			
			HttpServletRequest request = ServletActionContext.getRequest();
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

			formData = IPSFormParamEnum.toMap();
			formData.put(IPSFormParamEnum.Attach,user.getLoginname());
			formData.put(IPSFormParamEnum.Bankco,bankId);
			formData.put(IPSFormParamEnum.Merchanturl,basePath+"funds_member_success.jsp");
			formData.put(IPSFormParamEnum.FailUrl,basePath+"funds_member_fail.jsp");
			formData.put(IPSFormParamEnum.Amount,money);
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			message = "网络繁忙,请稍后再试！";
		}
		return false;
	}
}
