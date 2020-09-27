package dfh.action.tpp;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import dfh.action.tpp.paramenum.CZFFormParamEnum1;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.RSAWithSoftware;
import dfh.utils.StringUtil;
import dfh.utils.ZfSendRequestUtil;

public class CZFThirdPartyPayment1 extends ThirdPartyPayment{

	DecimalFormat df = new DecimalFormat("#0.00");
	CZFThirdPartyPayment1(String id,String name,String url){
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

			if(!StringUtil.isAvaliableBankCard(bankId)){
				message = "[提示]支付类型不存在，请重新选择。";
				return false;
			}
			
			if(dfh.utils.StringUtil.isDianKa(bankId)){
				message = "[提示]支付类型不存在，请重新选择。";
				return false;
			}
			
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			
			if (orderNo == null) {
				message = "[提示]获取商家订单号失败！";
				return false;
			}
			// 支付密匙
			String key = "skJsa8dksaj8sQjiery_";
			
			formData = CZFFormParamEnum1.toMap();
			
			//银行代号
			formData.put(CZFFormParamEnum1.bank_code, bankId);
			
			// 商家号（必填）
			formData.put(CZFFormParamEnum1.merchant_code, "2000295699");
			

			// 定单金额（必填）
			formData.put(CZFFormParamEnum1.order_amount,df.format(money));
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			formData.put(CZFFormParamEnum1.order_time,format.format(new Date()));
			// 商品名称（必填）
			formData.put(CZFFormParamEnum1.product_name,user.getLoginname());

			// 商家定单号(必填)
			// 公用业务回传参数（选填）
			formData.put(CZFFormParamEnum1.extra_return_param,user.getLoginname());
			formData.put(CZFFormParamEnum1.order_no,user.getLoginname()+"_e68"+orderNo);
			
			
			String ip = getIp();
			// 客户端IP（选填）
			formData.put(CZFFormParamEnum1.client_ip,StringUtil.isBlank(ip)?"127.0.0.1":ip);
			
			Set<String> noAppendValue = new HashSet<String>();
			noAppendValue.add(CZFFormParamEnum1.sign_type.toString());
			noAppendValue.add(CZFFormParamEnum1.sign.toString());
			noAppendValue.add(CZFFormParamEnum1.url.toString());
			
			StringBuffer signSrc = new StringBuffer(); 
			for(Enum p : CZFFormParamEnum1.values()){
				Object value = formData.get(p);
				if(null!=value&&!noAppendValue.contains(p.toString())&&!"".equals(value)){
					if("direct_pay".equals(value)){
						signSrc.append(p.toString()+"="+value);
					}else{
						signSrc.append(p.toString()+"="+value+"&");
					}
				}
			}
			//signSrc.append("key=").append(key);
			String singInfo = signSrc.toString();
			//formData.put(CZFFormParamEnum1.sign,DigestUtils.md5Hex(singInfo.getBytes("UTF-8")));
			formData.put(CZFFormParamEnum1.sign,RSAWithSoftware.signByPrivateKey(singInfo,ZfSendRequestUtil.PRIVATE_KEY));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		}
		return false;
	}

}
