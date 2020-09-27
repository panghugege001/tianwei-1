package dfh.action.pay;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.model.enums.CommonHaierEnum;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.Core;
import dfh.utils.DateUtil;
import dfh.utils.MD5Haier;

/**
 * @author addis
 *
 */
public class HaierPayAction  extends SubActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(HaierPayAction.class);
	
	
	   private String service ;
	   private String version;
	   private String  partner_id;
	   private String _input_charset ;
	   private String sign  ;
	   private String sign_type;
	   private String return_url ;
	   private String memo ;
	   //商家订单号
	   private String request_no  ;
	   private String trade_list ;
	   private String operator_id ;
	   private String buyer_id  ;
	   private String buyer_id_type  ;
	   private String buyer_ip  ;
	   private String pay_method   ;
	   private String go_cashier  ;
	   //金额
	   private String amount;
	   //银行代码
	   private String bankco;
	   private String partner_admin;
	   //商城签约url
       private String jumpUrl;


	
	
	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}

	// 错误信息
	private String error_info;
	
	//提交支付
	public String commitPay(){
		
		try {
			// 检测用户是否登录
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				setError_info("[提示]登录已过期，请从首页重新登录");
				return ERROR;
			}
			String payType = "haier";
			if(!CommonHaierEnum.existKey(payType)){
				setError_info("[提示]非法参数");
				return ERROR;
			}
			CommonHaierEnum cmHaier = CommonHaierEnum.getCommonHaier(payType);
			//判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { cmHaier.getText() }, Const.class);
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
			if (StringUtils.isBlank(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(amount) > 1000) {
					setError_info("[提示]存款金额不能超过1000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			// 支付银行
			if (StringUtils.isBlank(bankco)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (amount == null) {
				amount = "0.00";
			}
			amount = currentNumberFormat.format(Double.parseDouble(amount));
			// 获取商家订单号
			request_no = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHaierNo", new Object[] {user.getLoginname(), Double.parseDouble(amount), payType}, String.class);
			if (StringUtils.isBlank(request_no)) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			
			 service = "create_instant_pay";
		     version = "1.0";
		     partner_id =  cmHaier.getMerchantCode();
		     _input_charset  = "UTF-8";
		     sign_type = "MD5";
		     return_url = "http://pay.zhongxingyue.cn/" ;//同步通知地址（页面通知）
		     String notify_url = "http://pay.jiekoue68.com:2112/haier/haierCallBack.aspx";//异步通知地址（服务器通知）
		     partner_admin = cmHaier.getmerchanName();
		     trade_list = request_no+"~qianyi~"+amount+"~1~"+amount+"~~"+partner_admin+"~1~instant~~~~~~"+DateUtil.fmtyyyyMMddHHmmss(new Date())+"~"+notify_url+"~"; 
		     memo  = user.getLoginname();
		     buyer_id  = "anonymous";
		     buyer_id_type  = "1";
		     pay_method = "online_bank^"+amount+"^"+bankco+",C,GC";
		     //pay_method = "";
		     go_cashier = "Y";
		     jumpUrl = cmHaier.getUrl();
		     //直接跳转银行
		     String str = "_input_charset="+_input_charset+"&buyer_id="+buyer_id+"&buyer_id_type="+buyer_id_type+"&go_cashier="+go_cashier+"&memo="+memo+"&partner_id="+partner_id+"&pay_method="+pay_method+"&request_no="+request_no+"&return_url="+return_url+"&service="+service+"&trade_list="+trade_list+"&version=1.0" ;
		     //跳转海尔收银台
		     //String str = "_input_charset="+_input_charset+"&buyer_id="+buyer_id+"&buyer_id_type="+buyer_id_type+"&go_cashier="+go_cashier+"&memo="+memo+"&partner_id="+partner_id+"&request_no="+request_no+"&return_url="+return_url+"&service="+service+"&trade_list="+trade_list+"&version=1.0" ;
		     sign = MD5Haier.sign(str, Core.key, _input_charset);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	


	/***
	 * 
	 * @return
	 * 海尔支付回调函数
	 * 
	 */
	public String haierCallBack(){
		try {
			Map<String, String> sParaTemp = new TreeMap<String, String>();
			sParaTemp.put("notify_id", getRequest().getParameter("notify_id"));
			sParaTemp.put("notify_type", getRequest().getParameter("notify_type"));
			sParaTemp.put("notify_time", getRequest().getParameter("notify_time"));
			sParaTemp.put("_input_charset",getRequest().getParameter("_input_charset"));
			sParaTemp.put("sign", getRequest().getParameter("sign"));
			sParaTemp.put("sign_type", getRequest().getParameter("sign_type"));
			sParaTemp.put("version", getRequest().getParameter("version"));
			sParaTemp.put("outer_trade_no",getRequest().getParameter("outer_trade_no"));
			sParaTemp.put("inner_trade_no",getRequest().getParameter("inner_trade_no"));
			sParaTemp.put("trade_status", getRequest().getParameter("trade_status"));
			sParaTemp.put("trade_amount", getRequest().getParameter("trade_amount"));
			sParaTemp.put("gmt_create", getRequest().getParameter("gmt_create"));
			sParaTemp.put("gmt_payment", getRequest().getParameter("gmt_payment"));
			
			String trade_amount = getRequest().getParameter("trade_amount");
			String signValue = getRequest().getParameter("sign");
			String trade_status = getRequest().getParameter("trade_status");
			String outer_trade_no = getRequest().getParameter("outer_trade_no");
			
			
			PrintWriter out = null;
			log.info("=================海尔支付回调开始=======================");
			// 组织加密明文
			  String signContent = Core.createLinkString(Core.paraFilter(sParaTemp), false);
				if (signValue != null) {
					signValue = signValue.replace(' ', '+');
				}
			  sign = MD5Haier.verify(signContent, signValue, Core.key, _input_charset);
			  boolean  flag = signValue.equals(sign);
			  if(flag){
					log.info("验签成功！交易订单号："+outer_trade_no);
				  if(trade_status.equals("TRADE_SUCCESS")){
						//处理账单信息
						String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderHaier", new Object[] { outer_trade_no, Double.parseDouble(trade_amount), outer_trade_no, "200002058681" }, String.class);
						if (returnmsg == null) {
							try {
								out = this.getResponse().getWriter();
								out.print("SUCCESS");
								out.flush();
								log.info("交易成功,你支付的" + trade_amount + "元已经到账,此次交易的订单号为" + outer_trade_no);
							} catch (Exception e) {
								log.info(e.toString());
							} finally {
								if (out != null) {
									out.close();
								}
							}
						} else {
							out = this.getResponse().getWriter();
							out.print("SUCCESS");
							out.flush();
							log.info("交易失败:" + returnmsg + ",订单号为" + outer_trade_no);
						}
					}
				  else {
						log.info("交易失败");
					}
				  }
			  else{
					log.info("Haier签名验证失败");
			  }
		} catch (Exception e) {
			log.info(e.toString());
		} 
		return null ;
	}
	
	
	
	
	
	

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRequest_no() {
		return request_no;
	}

	public void setRequest_no(String request_no) {
		this.request_no = request_no;
	}

	public String getTrade_list() {
		return trade_list;
	}

	public void setTrade_list(String trade_list) {
		this.trade_list = trade_list;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getBuyer_id_type() {
		return buyer_id_type;
	}

	public void setBuyer_id_type(String buyer_id_type) {
		this.buyer_id_type = buyer_id_type;
	}

	public String getBuyer_ip() {
		return buyer_ip;
	}

	public void setBuyer_ip(String buyer_ip) {
		this.buyer_ip = buyer_ip;
	}

	public String getPay_method() {
		return pay_method;
	}

	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}

	public String getGo_cashier() {
		return go_cashier;
	}

	public void setGo_cashier(String go_cashier) {
		this.go_cashier = go_cashier;
	}


	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getBankco() {
		return bankco;
	}

	public void setBankco(String bankco) {
		this.bankco = bankco;
	}

	
	public String getPartner_admin() {
		return partner_admin;
	}

	public void setPartner_admin(String partner_admin) {
		this.partner_admin = partner_admin;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	
	
}
