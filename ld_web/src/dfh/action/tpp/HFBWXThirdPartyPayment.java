package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.HFBWXFormParamEnum;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;

public class HFBWXThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.0");
	HFBWXThirdPartyPayment(String id,String name,String url){
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
			// 判断订单金额
			if (money <  1) { 
				message = "[提示]1元以上或者1元才能存款！";
				return false;
			}
			if (money > 3000) {
				message = "[提示]存款金额不能超过3000！";
				return false;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			//获取商家订单号
			String agent_bill_id = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,	"getHhbWxZfOrderNo", new Object[] { user.getLoginname(),money }, String.class);
			if (agent_bill_id == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}
			
			formData = HFBWXFormParamEnum.toMap();
			formData.put(HFBWXFormParamEnum.agent_bill_id,agent_bill_id);
			formData.put(HFBWXFormParamEnum.agent_bill_time,DateUtil.fmtyyyyMMddHHmmss(new Date()));
			formData.put(HFBWXFormParamEnum.pay_amt,df.format(money));
			formData.put(HFBWXFormParamEnum.user_ip,getIp() == null ? "" : getIp().replace('.', '_'));
			formData.put(HFBWXFormParamEnum.remark,user.getLoginname());
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(HFBWXFormParamEnum.goods_name.toString());
			noAppendValue.add(HFBWXFormParamEnum.goods_num.toString());
			noAppendValue.add(HFBWXFormParamEnum.remark.toString());
			noAppendValue.add(HFBWXFormParamEnum.goods_note.toString());
			noAppendValue.add(HFBWXFormParamEnum.sign.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : HFBWXFormParamEnum.values()){
				Object value = formData.get(p);
				if(!noAppendValue.contains(p.toString())){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			signSrc = signSrc.deleteCharAt(signSrc.length()-1);   //去除掉最后"&"
			String sign =EncryptionUtil.encryptPassword(signSrc.toString());
			formData.put(HFBWXFormParamEnum.sign,sign);
			formData.remove(HFBWXFormParamEnum.key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
