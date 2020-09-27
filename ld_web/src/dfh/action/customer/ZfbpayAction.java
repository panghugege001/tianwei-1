package dfh.action.customer;

import java.text.DecimalFormat;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AlipayUtil;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class ZfbpayAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;
	/****************基本参数******************/
	//接口名称（必填）
	private String service;
	//合作者身份ID（必填）
	private String partner;
	//参数编码字符集（必填）
	private String _input_charset;
	//签名方式（必填）
	private String sign_type;
	//签名（必填）
	private String sign;
	//服务器异步通知页面路径
	private String notify_url;
	//页面跳转同步通知页面路径
	private String return_url;
	/****************业务参数******************/
	//商户网站唯一订单号（必填）
	private String out_trade_no;
	//商品名称（必填）
	private String subject;
	//支付类型（必填）
	private String payment_type;
	//卖家支付宝账号（必填）
	private String seller_email;
	//交易金额（必填）
	private String total_fee;
	//默认支付方式
	private String paymethod;
	//公用回传参数
	private String extra_common_param;
	// 错误信息
	private String error_info;

	public String zfbRedirect() {
		try {
			// 检测用户是否登录
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				setError_info("[提示]登录已过期，请从首页重新登录");
				return ERROR;
			}
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "支付宝" }, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			// 判断在线支付是否在维护
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额是否为空
			if (!isNotNullAndEmpty(total_fee)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				/*if (Double.parseDouble(total_fee) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}*/
				if (Double.parseDouble(total_fee) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			// 获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getZfbOrderNo", new Object[] {}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			//接口名称
			service = AlipayUtil.Config.service_name;
			//合作身份者ID
			partner = AlipayUtil.Config.partner;
			//卖家支付宝账号
			seller_email = AlipayUtil.Config.seller_email;
			//参数编码字符集
			_input_charset = AlipayUtil.Config.input_charset;
			//支付类型1（商品购买）
			payment_type = "1";
			//服务器异步通知页面路径
			notify_url = AlipayUtil.Config.notify_url;
			//页面跳转同步通知页面路径
			return_url = AlipayUtil.Config.return_url;
			//商户网站唯一订单号
			out_trade_no = orderNo;
			//商品名称
			subject = user.getLoginname();
			//交易金额
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (total_fee == null) {
				total_fee = "0.00";
			}
			total_fee = currentNumberFormat.format(Double.parseDouble(total_fee));
			//默认支付方式，directPay（余额支付），creditPay（信用支付）
			paymethod = "directPay";
			//公用回传参数
			extra_common_param = user.getLoginname();
			/**
			 * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
			 * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
			 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
			 * [0, 9, A, Z, _, a, c, g, h, w]
			 */
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(_input_charset)) {
				signSrc.append("_input_charset=").append(_input_charset).append("&");
			}
			if (!"".equals(extra_common_param)) {
				signSrc.append("extra_common_param=").append(extra_common_param).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(out_trade_no)) {
				signSrc.append("out_trade_no=").append(out_trade_no).append("&");
			}
			if (!"".equals(partner)) {
				signSrc.append("partner=").append(partner).append("&");
			}
			if (!"".equals(payment_type)) {
				signSrc.append("payment_type=").append(payment_type).append("&");
			}
			if (!"".equals(paymethod)) {
				signSrc.append("paymethod=").append(paymethod).append("&");
			}
			if (!"".equals(return_url)) {
				signSrc.append("return_url=").append(return_url).append("&");
			}
			if (!"".equals(seller_email)) {
				signSrc.append("seller_email=").append(seller_email).append("&");
			}
			if (!"".equals(service)) {
				signSrc.append("service=").append(service).append("&");
			}
			if (!"".equals(subject)) {
				signSrc.append("subject=").append(subject).append("&");
			}
			if (!"".equals(total_fee)) {
				signSrc.append("total_fee=").append(total_fee).append("&");
			}
			//删除最后一个&字符
			String prestr = signSrc.substring(0, signSrc.length()-1);
			sign = AlipayUtil.sign(prestr, AlipayUtil.Config.key, AlipayUtil.Config.input_charset);
			sign_type = AlipayUtil.Config.sign_type;
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}

	/**
	 * 判断数据是否为空
	 * @param str
	 * @return
	 */
	private boolean isNotNullAndEmpty(String str) {
		boolean b = false;
		if (null != str && str.trim().length() > 0) {
			b = true;
		}
		return b;
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getExtra_common_param() {
		return extra_common_param;
	}

	public void setExtra_common_param(String extra_common_param) {
		this.extra_common_param = extra_common_param;
	}

	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
}
