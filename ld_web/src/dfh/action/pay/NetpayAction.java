package dfh.action.pay;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.eitop.platform.tools.Charset;
import com.eitop.platform.tools.encrypt.xStrEncrypt;

import dfh.action.SubActionSupport;
import dfh.model.Netpay;
import dfh.model.Payorder;
import dfh.model.Users;
import dfh.model.enums.NetpayTypeEnum;
import dfh.netpay.NetpayInterfaces;
import dfh.netpay.NetpayProvider;
import dfh.netpay.Opay;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.SeqService;
import dfh.utils.Constants;
import dfh.utils.StringUtil;

public class NetpayAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(NetpayAction.class);
	private SeqService seqService;
	private NetpayService netpayService;
	private NetpayProvider netpayProvider;

	private CustomerService cs;

	private String loginname;
	private String billno;
	private Double amount;
	private String aliasName;
	private String email;
	private String phone;
	private String newAccount;
	private String password;
	private String confirm_password;
	private String netpayName;
	private String partner;
	private Integer isSucc;
	private String errormsg;
	private String pd_FrpId;

	public String getPd_FrpId() {
		return pd_FrpId;
	}

	public void setPd_FrpId(String pdFrpId) {
		pd_FrpId = pdFrpId;
	}

	private String url;

	public String getUrl() {
		return url;
	}

	public String newAccountConfirmOrder() {
		Users user = (Users) cs.get(Users.class, getLoginname());
		if (user != null) {
			setErrormsg("开户帐号已经存在");
			return INPUT;
		}
		if (cs.checkIsUserAliasName(aliasName) != null) {
			setErrormsg("开户姓名已经存在");
			return INPUT;
		}
		if (amount.doubleValue() < Constants.OPEN_MIN_MONEY.doubleValue()) {
			setErrormsg("开户金额必须大于或者等于" + Constants.OPEN_MIN_MONEY);
			return INPUT;
		}
		if (amount.doubleValue() > 1000) {
			setErrormsg("开户金额必须小于1000元人民币");
			return INPUT;
		}
		setLoginname(Constants.PREFIX_MONEY_CUSTOMER + loginname);
		if (newAccount.toString().equals(Constants.FLAG_TRUE.toString()) && seqService.get(Users.class, getLoginname()) != null) {
			setErrormsg("开户帐号已经存在");
			return INPUT;
		}
		if (StringUtil.isNotEmpty(partner)) {
			Users userPartner = (Users) seqService.get(Users.class, partner);
			// if (userPartner == null ||
			// !userPartner.getRole().equals(UserRole.PARTNER.getCode())) {
			// setErrormsg("不存在此推荐人.如果没有可不填写");
			// return INPUT;
			// }
		}
		if (StringUtil.isEmpty(billno))
			billno = seqService.generateNetpayBillno();
		return SUCCESS;
	}

	public String depositConfirmOrder() {
		Users cus = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (cus == null) {
			setErrormsg("请您先登录");
			return INPUT;
		}
		if (amount.doubleValue() < Constants.NETPAYADD_MIN_MONEY.doubleValue()) {
			setErrormsg("在线充值金额必须大于或者等于" + Constants.NETPAYADD_MIN_MONEY);
			return INPUT;
		}
		if (amount.doubleValue() > Constants.NETPAYADD_MAX_MONEY.doubleValue()) {
			setErrormsg("在线充值金额必须小于" + Constants.NETPAYADD_MAX_MONEY + "元人民币");
			return INPUT;
		}
		setLoginname(cus.getLoginname());
		if (StringUtil.isEmpty(billno))
			billno = seqService.generateNetpayBillno();
		return SUCCESS;
	}

	public String submitOrder() {
		if (billno == null || loginname == null || amount == null) {
			setErrormsg("参数不全");
			return INPUT;
		}
		Netpay netpay = (Netpay) netpayService.get(Netpay.class, StringUtil.trim(netpayName));
		if (netpay == null) {
			setErrormsg("找不到该支付方式");
		} else {
			NetpayInterfaces netpayInterfaces = netpayProvider.getNetpay(netpay.getName());
			// String callBackUrl = getRequest().getScheme() + "://" +
			// getRequest().getServerName() + ":" + getRequest().getServerPort()
			// + getRequest().getContextPath() + "/jsp/"
			// + netpayProvider.getCallBackActionName(netpay.getName()) + ".do";

			String callBackUrl = netpayProvider.getCallBackAddress(netpay.getName());
			log.info("callBackUrl:" + callBackUrl);
			String msg = netpayService.addNetpayOrder(billno, StringUtil.trim(loginname), StringUtil.trim(password), netpayName,
					Constants.FLAG_TRUE.toString().equals(newAccount), StringUtil.trim(partner), amount, StringUtil.trim(aliasName),
					StringUtil.trim(phone), StringUtil.trim(email), netpayInterfaces.getHamc(netpay.getPuturl(), billno, netpay.getMerno(),
							amount, callBackUrl, netpay.getKey()), getIp());
			if (msg != null) {
				setErrormsg(msg);
			} else {
				log.info("callBackUrl:" + callBackUrl);
				String redirectURL = netpayInterfaces.requestURL(netpay.getPuturl(), billno, netpay.getMerno(), amount, callBackUrl, netpay
						.getKey(), getIp());
				log.info("redirectURL:" + redirectURL);
				try {
					// netpay.getName()+"?url="+StringUtil.urlEncode(redirectURL)
					String newUrl = netpayProvider.getSubmitAddress(netpay.getName()) + "?url=" + StringUtil.urlEncode(redirectURL) + "";
					log.info("newURl:" + newUrl);
					getResponse().sendRedirect(newUrl);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					msg = e.getMessage();
					setErrormsg(msg);
				}
			}
		}
		return INPUT;
	}

	/**
	 * 没有中间站的处理
	 * 
	 * @return
	 */
	public String submitOrderForOpay() {
		if (amount == null) {
			setErrormsg("参数不全");
			return INPUT;
		}
		billno = seqService.generateNetpayBillno();
		Users customer = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		Netpay netpay = (Netpay) netpayService.get(Netpay.class, NetpayTypeEnum.OPAY.getCode());
		if (netpay == null) {
			setErrormsg("找不到该支付方式");
		} else {
			String callBackUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort()
					+ getRequest().getContextPath() + "/asp/callBackOpay.aspx";
			String returnUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort()
					+ getRequest().getContextPath() + "/deposit.asp";
			log.info("callBackUrl:" + callBackUrl);
			String msg = netpayService.addNetpayOrder(billno, customer.getLoginname(), null, netpay.getName(), false, null, amount,
					customer.getAccountName(), customer.getPhone(), customer.getEmail(), Opay.getSign(pd_FrpId, netpay.getPuturl(), billno,
							netpay.getMerno(), amount, callBackUrl, returnUrl, netpay.getCode(), netpay.getKey()), getIp());
			if (msg != null) {
				setErrormsg(msg);
			} else {
				log.info("callBackUrl:" + callBackUrl);
				String requestForm = Opay.requestForm(pd_FrpId, netpay.getPuturl(), billno, netpay.getMerno(), amount, callBackUrl,
						returnUrl, netpay.getCode(), netpay.getKey());
				log.info("requestForm:" + requestForm);
				try {
					getResponse().getWriter().println(requestForm);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					msg = e.getMessage();
					setErrormsg(msg);
				}
			}
		}
		return INPUT;
	}

	public String callBackOpay() {
		HttpServletResponse repsonse = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String p02_out_ordercode = request.getParameter("p02_out_ordercode");
		String p03_payamount = request.getParameter("p03_payamount");
		String p10_note = request.getParameter("p10_note");
		String p11_status = request.getParameter("p11_status");
		String p12_ordercode = request.getParameter("p12_ordercode");
		String sign = request.getParameter("sign");

		String output = "";
		try {
			if (StringUtil.isEmpty(p02_out_ordercode) || StringUtil.isEmpty(p03_payamount) || StringUtil.isEmpty(p10_note)
					|| StringUtil.isEmpty(p11_status)) {
				output = "回调参数不全";
			} else {
				Netpay netpay = (Netpay) netpayService.get(Netpay.class, NetpayTypeEnum.OPAY.getCode());
				if (!Opay.callBackValidate(request, netpay.getCode(), netpay.getKey())) {
					output = "交易签名验证失败";
				} else {
					if (p11_status.equals("success")) {
						String msg = netpayService.excuteNetpayOrder(p02_out_ordercode, Double.parseDouble(p03_payamount), true,
								"壹支付平台的订单流水号:" + p12_ordercode, sign, getIp());
						if (msg == null) {
							if (request.getMethod().equalsIgnoreCase("post")) {
								output = "1000";
							} else {
								output = "存款成功";
								setErrormsg(output);
								return SUCCESS;
							}
						} else {
							output = "处理失败:" + msg;
						}
					} else {
						output = "交易失败:" + p11_status;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			output = e.getMessage();
		}
		try {
			repsonse.getWriter().println(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "show";
	}

	// 易宝支付处理
	public String callBack() {
		HttpServletResponse repsonse = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String p1_MerId = request.getParameter("p1_MerId");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r6_Order = request.getParameter("r6_Order");
		String r9_BType = request.getParameter("r9_BType");
		String hmac = request.getParameter("hmac");

		try {
			if (StringUtil.isEmpty(r1_Code) || StringUtil.isEmpty(r3_Amt) || StringUtil.isEmpty(r6_Order) || StringUtil.isEmpty(r9_BType)) {
				repsonse.getWriter().print("回调参数不全");
				return "show";
			}
			Netpay netpay = netpayService.getNetpayByMerno(p1_MerId);
			if (!netpayProvider.getNetpay(NetpayTypeEnum.YEEPAY.getCode()).callBackValidate(request, StringUtil.trim(netpay.getKey()))) {
				repsonse.getWriter().print("交易签名验证失败");
				return "show";
			}
			if (r1_Code.equals("1")) {
				String msg;
				if (r9_BType.equals("1")) {
					msg = null;
					msg = netpayService.excuteNetpayOrder(r6_Order, Double.parseDouble(r3_Amt), r1_Code.equals("1"), "易宝单号:" + r2_TrxId,
							hmac, getIp());
					Payorder order = (Payorder) netpayService.get(Payorder.class, r6_Order);
					if (msg == null) {
						setAliasName(order.getAliasName());
						setNewAccount(order.getNewaccount().toString());
						setIsSucc(Constants.FLAG_TRUE);
						setLoginname(order.getLoginname());
						setPassword(order.getPassword());
						setAmount(order.getMoney());
						setBillno(order.getBillno());
					} else {
						repsonse.getWriter().print("交易成功，处理失败 请联系客服为您补单");
					}
					return "show";
				}
				msg = netpayService.excuteNetpayOrder(r6_Order, Double.parseDouble(r3_Amt), r1_Code.equals("1"), "易宝单号:" + r2_TrxId, hmac,
						getIp());
				if (msg == null)
					repsonse.getWriter().println("success");
				return "asynCall";
			} else {
				repsonse.getWriter().print("交易失败");
				return "show";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}

	// cncard的处理
	public String callBackcncard() {
		HttpServletResponse repsonse = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String c_mid = request.getParameter("c_mid");// 商户编号
		String c_order = request.getParameter("c_order");// 本地订单号
		String c_orderamount = request.getParameter("c_orderamount");// 订单总金额
		String c_ymd = request.getParameter("c_ymd");// 订单产生日期 格式(yyyyMMdd)
		String c_moneytype = request.getParameter("c_moneytype");// 支付币种
		String c_transnum = request.getParameter("c_transnum");// cncard的交易流水号
		String c_succmark = request.getParameter("c_succmark");// 交易成功标识 Y-成功
		// N-失败
		String c_signstr = request.getParameter("c_signstr");// 验证信息
		try {
			if (StringUtil.isEmpty(c_mid) || StringUtil.isEmpty(c_order) || StringUtil.isEmpty(c_orderamount) || StringUtil.isEmpty(c_ymd)
					|| StringUtil.isEmpty(c_moneytype) || StringUtil.isEmpty(c_transnum) || StringUtil.isEmpty(c_succmark)
					|| StringUtil.isEmpty(c_signstr)) {
				repsonse.getWriter().println("回调参数不全");
				return "failUrl";
			}
			Netpay netpay = netpayService.getNetpayByMerno(c_mid);
			if (!netpayProvider.getNetpay(NetpayTypeEnum.CNCARD.getCode()).callBackValidate(request, netpay.getKey())) {
				repsonse.getWriter().println("交易签名验证失败");
				return "failUrl";
			}
			if (c_succmark.equalsIgnoreCase("Y")) {
				String msg;
				msg = netpayService.excuteNetpayOrder(c_order, Double.parseDouble(c_orderamount), c_succmark.equalsIgnoreCase("Y"), "云网单号:"
						+ c_transnum, c_signstr, getIp());
				if (msg == null) {
					repsonse.getWriter().println("success");
					return "successUrl";
				} else {
					repsonse.getWriter().println("交易成功处理失败请联系客服为您补单");
				}
			} else {
				repsonse.getWriter().println("支付未成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failUrl";
	}

	// nps的处理
	public String callBacknps() {
		HttpServletResponse repsonse = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String OrderInfo = Charset.ISO8859_2_Gb(request.getParameter("OrderMessage"));// 订单加密信息
		String signMsg = request.getParameter("Digest");// 签名认证
		try {
			if (StringUtil.isEmpty(OrderInfo) || StringUtil.isEmpty(signMsg)) {
				repsonse.getWriter().println("回调参数不全");
				return "failUrl";
			}
			Netpay netpay = (Netpay) netpayService.get(Netpay.class, NetpayTypeEnum.NPS.getCode());
			if (!netpayProvider.getNetpay(NetpayTypeEnum.NPS.getCode()).callBackValidate(request, netpay.getKey())) {
				repsonse.getWriter().println("交易签名验证失败");
				return "failUrl";
			}
			OrderInfo = xStrEncrypt.StrDecrypt(OrderInfo, netpay.getKey());

			String array[];
			StringTokenizer OrderInfos = new StringTokenizer(OrderInfo, "|");
			array = new String[OrderInfos.countTokens()];
			int i = 0;
			while (OrderInfos.hasMoreTokens()) {
				array[i] = OrderInfos.nextToken();
				i++;
			}
			String m_id = array[0];
			String m_orderid = array[1];
			String m_oamount = array[2];
			String m_ocurrency = array[3];
			String m_url = array[4];
			String m_language = array[5];
			String s_name = array[6];
			String s_addr = array[7];
			String s_postcode = array[8];
			String s_tel = array[9];
			String s_eml = array[10];
			String r_name = array[11];
			String r_addr = array[12];
			String r_postcode = array[13];
			String r_tel = array[14];
			String r_eml = array[15];
			String m_ocomment = array[16];
			String modate = array[17];
			String Status = array[18];

			if (Status.equalsIgnoreCase("2")) {
				String msg;
				msg = netpayService.excuteNetpayOrder(m_orderid, Double.parseDouble(m_oamount), Status.equalsIgnoreCase("2"), "NPS",
						signMsg, getIp());
				if (msg == null) {
					repsonse.getWriter().println("success");
					return "successUrl";
				} else {
					repsonse.getWriter().println("交易成功处理失败请联系客服为您补单");
					return "failUrl";
				}
			} else {
				repsonse.getWriter().println("交易失败");
				return "failUrl";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failUrl";
	}

	// IPS处理
	public String callBackips() {
		HttpServletResponse repsonse = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String billno = request.getParameter("billno");// 本地订单号
		String mercode = request.getParameter("mercode");// 商户编号
		String Currency_type = request.getParameter("Currency_type");// 币种
		String amount = request.getParameter("amount");// 订单金额
		String date = request.getParameter("date");// 订单日期
		String succ = request.getParameter("succ");// 成功标识 Y:支付成功；N：支付失败
		String ipsbillno = request.getParameter("ipsbillno");// ips的订单号
		String retencodetype = request.getParameter("retencodetype");// 签名方式
		String signature = request.getParameter("signature");// 验证信息

		try {
			if (StringUtil.isEmpty(billno) || StringUtil.isEmpty(Currency_type) || StringUtil.isEmpty(amount) || StringUtil.isEmpty(date)
					|| StringUtil.isEmpty(succ) || StringUtil.isEmpty(signature)) {
				repsonse.getWriter().println("回调参数不全");
				return "failUrl";
			}
			Netpay netpay = netpayService.getNetpayByMerno(mercode);
			if (!netpayProvider.getNetpay(NetpayTypeEnum.IPS.getCode()).callBackValidate(request, netpay.getKey())) {
				repsonse.getWriter().println("交易签名验证失败");
				return "failUrl";
			}
			if (succ.equalsIgnoreCase("Y")) {
				String msg;
				msg = netpayService.excuteNetpayOrder(billno, Double.parseDouble(amount), succ.equalsIgnoreCase("Y"), "IPS单号:" + ipsbillno,
						signature, getIp());
				if (msg == null) {
					repsonse.getWriter().println("success");
					return "successUrl";
				} else {
					repsonse.getWriter().println("交易成功处理失败请联系客服为您补单");
				}
			} else {
				repsonse.getWriter().println("支付未成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failUrl";
	}

	// tenpay处理
	public String callBacktenpay() {
		HttpServletResponse repsonse = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String cmdno = request.getParameter("cmdno");
		String pay_result = request.getParameter("pay_result");
		String date = request.getParameter("date");
		String transaction_id = request.getParameter("transaction_id");
		String sp_billno = request.getParameter("sp_billno");
		String total_fee = request.getParameter("total_fee");
		String fee_type = request.getParameter("fee_type");
		String attach = request.getParameter("attach");
		String bargainor_id = request.getParameter("bargainor_id");

		String sign = request.getParameter("sign");

		try {
			if (StringUtil.isEmpty(cmdno) || StringUtil.isEmpty(pay_result) || StringUtil.isEmpty(date)
					|| StringUtil.isEmpty(transaction_id) || StringUtil.isEmpty(sp_billno) || StringUtil.isEmpty(total_fee)
					|| StringUtil.isEmpty(fee_type) || StringUtil.isEmpty(attach) || StringUtil.isEmpty(bargainor_id)
					|| StringUtil.isEmpty(sign)) {
				repsonse.getWriter().println("回调参数不全");
				return "failUrl";
			}
			Netpay netpay = netpayService.getNetpayByMerno(bargainor_id);
			if (!netpayProvider.getNetpay(NetpayTypeEnum.TENPAY.getCode()).callBackValidate(request, netpay.getKey())) {
				repsonse.getWriter().println("交易签名验证失败");
				return "failUrl";
			}
			if (pay_result.equalsIgnoreCase("0")) {
				String msg;
				msg = netpayService.excuteNetpayOrder(billno, Double.parseDouble(total_fee) / 100, pay_result.equalsIgnoreCase("0"),
						"TENPAY单号:" + transaction_id, sign, getIp());
				if (msg == null) {
					repsonse.getWriter().println("success");
					return "successUrl";
				} else {
					repsonse.getWriter().println("交易成功处理失败请联系客服为您补单");
				}
			} else {
				repsonse.getWriter().println("支付未成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failUrl";
	}

	public String getAliasName() {
		return aliasName;
	}

	public Double getAmount() {
		return amount;
	}

	public String getBillno() {
		return billno;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public String getEmail() {
		return email;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public Integer getIsSucc() {
		return isSucc;
	}

	public String getLoginname() {
		return loginname;
	}

	public String getNetpayName() {
		return netpayName;
	}

	public NetpayService getNetpayService() {
		return netpayService;
	}

	public String getNewAccount() {
		return newAccount;
	}

	public String getPartner() {
		return partner;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public SeqService getSeqService() {
		return seqService;
	}

	public String modifyOrder() {
		return SUCCESS;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public void setIsSucc(Integer isSucc) {
		this.isSucc = isSucc;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setNetpayName(String netpayName) {
		this.netpayName = netpayName;
	}

	public void setNetpayService(NetpayService netpayService) {
		this.netpayService = netpayService;
	}

	public void setNewAccount(String newAccount) {
		this.newAccount = newAccount;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}

	public CustomerService getCs() {
		return cs;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public NetpayAction() {
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public NetpayProvider getNetpayProvider() {
		return netpayProvider;
	}

	public void setNetpayProvider(NetpayProvider netpayProvider) {
		this.netpayProvider = netpayProvider;
	}

}
