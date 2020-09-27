package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import dfh.action.tpp.paramenum.BFBFormParamEnum;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AxisUtil;

public class BFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	BFBThirdPartyPayment(String id,String name,String url){
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
			
			// 判断订单金额
			if (money < 1) {
				message = "[提示]1元以上或者1元才能存款！";
				return false;
			}
			if (money > 50000) {
				message = "[提示]存款金额不能超过50000！";
				return false;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				message = "[提示]代理不能使用在线支付！";
				return false;
			}
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "币付宝" }, Const.class);
			if (constPay == null) {
				message = "[提示]支付类型不存在，请重新选择。";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderBfbNo", new Object[] {user.getLoginname(),money}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = BFBFormParamEnum.toMap();
			//接口名称
			formData.put(BFBFormParamEnum.p2_xn,orderNo);
			formData.put(BFBFormParamEnum.p4_pd,bankId);
			formData.put(BFBFormParamEnum.p6_amount,String.valueOf(money));
			
			/**
			 * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
			 * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
			 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
			 * [0, 9, A, Z, _, a, c, g, h, w]
			 */
			String key = "81eb96def5fc4cac8f86eeebdad1064b";
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(BFBFormParamEnum.sign.toString());
			noAppendValue.add(BFBFormParamEnum.url.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : BFBFormParamEnum.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			
			String singInfo = signSrc.delete(signSrc.length()-1,signSrc.length()).append(key).toString();
			formData.put(BFBFormParamEnum.sign,DigestUtils.md5Hex(singInfo.toString()));
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
