package dfh.action.tpp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import dfh.action.tpp.paramenum.ZFBFormParamEnum;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AlipayUtil;
import dfh.utils.AxisUtil;

public class ZFBThirdPartyPayment extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	ZFBThirdPartyPayment(String id,String name,String url){
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
			if (money > 10000) {
				message = "[提示]存款金额不能超过5000！";
				return false;
			}
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "支付宝" }, Const.class);
			if (constPay == null) {
				message = "[提示]支付类型不存在，请重新选择。";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getZfbOrderNo", new Object[] {}, String.class);
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}

			formData = ZFBFormParamEnum.toMap();
			//接口名称
			formData.put(ZFBFormParamEnum.service,AlipayUtil.Config.service_name);
			//合作身份者ID
			formData.put(ZFBFormParamEnum.partner,AlipayUtil.Config.partner);
			//卖家支付宝账号
			formData.put(ZFBFormParamEnum.seller_email,AlipayUtil.Config.seller_email);
			//参数编码字符集
			formData.put(ZFBFormParamEnum._input_charset,AlipayUtil.Config.input_charset);
			//支付类型1（商品购买）
			formData.put(ZFBFormParamEnum.payment_type,"1");
			//服务器异步通知页面路径
			formData.put(ZFBFormParamEnum.notify_url,AlipayUtil.Config.notify_url);
			//页面跳转同步通知页面路径
			formData.put(ZFBFormParamEnum.return_url,AlipayUtil.Config.return_url);
			//商户网站唯一订单号
			formData.put(ZFBFormParamEnum.out_trade_no,orderNo);
			//商品名称
			formData.put(ZFBFormParamEnum.subject,user.getLoginname());
			//默认支付方式，directPay（余额支付），creditPay（信用支付）
			formData.put(ZFBFormParamEnum.paymethod,"directPay");
			//默认支付方式，directPay（余额支付），creditPay（信用支付）
			formData.put(ZFBFormParamEnum._input_charset,user.getLoginname());
			formData.put(ZFBFormParamEnum.total_fee,money);
			
			
			/**
			 * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
			 * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
			 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
			 * [0, 9, A, Z, _, a, c, g, h, w]
			 */
		
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(ZFBFormParamEnum.sign_type.toString());
			noAppendValue.add(ZFBFormParamEnum.sign.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : ZFBFormParamEnum.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					signSrc.append(p.toString()+"="+value+"&");
				}
			}
			String singInfo = signSrc.substring(0, signSrc.length()-1);
			formData.put(ZFBFormParamEnum.sign,AlipayUtil.sign(singInfo, AlipayUtil.Config.key, AlipayUtil.Config.input_charset));
			formData.put(ZFBFormParamEnum.sign_type,AlipayUtil.Config.sign_type);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
