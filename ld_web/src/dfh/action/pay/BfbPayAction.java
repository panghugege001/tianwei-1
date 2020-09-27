package dfh.action.pay;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class BfbPayAction  extends SubActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(BfbPayAction.class);
	
	private final String key = "81eb96def5fc4cac8f86eeebdad1064b";
	//指令类型  网银-1，卡类-2
	private final String p1_md = "1";
	//商户订单号
	private	String p2_xn = "xn123456789";
	//商户ID
	private final String p3_bn = "3852911032";
	//支付方式ID  银行ID
	private	String p4_pd;
	//产品名称
	private	String p5_name = "168";
	//支付金额
	private	String p6_amount;
	//币种
	private	final String p7_cr = "1";
	//扩展信息
	private	final String p8_ex = "ex";
	//通知地址
	private final String p9_url = "http://pay.jiekoue68.com:2112/bfb/callBackPay.aspx";
	//是否通知 不通知=0，通知=1
	private final String p10_reply = "1";
	//调用模式    0 返回充值地址，由商户负责跳转     1 显示币付宝充值界面,跳转到充值    2 不显示币付宝充值界面 直接跳转到
	private final String p11_mode = "2";
	//版本号
	private	 String p12_ver = "1";
	//签名值
	private	String sign;
	
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
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { "币付宝" }, Const.class);
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
			if (StringUtils.isBlank(p6_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(p6_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(p6_amount) > 50000) {
					setError_info("[提示]存款金额不能超过50000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			// 支付银行
			if (StringUtils.isBlank(p4_pd)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 获取商家订单号
			p2_xn = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderBfbNo", new Object[] {user.getLoginname(),Double.parseDouble(p6_amount)}, String.class);
			if (StringUtils.isBlank(p2_xn)) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			
			StringBuffer str = new StringBuffer();
			str.append("p1_md=").append(p1_md)
			.append("&p2_xn=").append(p2_xn)
			.append("&p3_bn=").append(p3_bn)
			.append("&p4_pd=").append(p4_pd)
			.append("&p5_name=").append(p5_name)
			.append("&p6_amount=").append(p6_amount)
			.append("&p7_cr=").append(p7_cr)
			.append("&p8_ex=").append(p8_ex)
			.append("&p9_url=").append(p9_url)
			.append("&p10_reply=").append(p10_reply)
			.append("&p11_mode=").append(p11_mode)
			.append("&p12_ver=").append(p12_ver)
			.append(key);
			sign = DigestUtils.md5Hex(str.toString());
			
			return SUCCESS ;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	//回调
	public String callBackPay(){
		/*p1_md=1&p2_sn=1111111111&p3_xn=111111&p4_amt=100&p5_ex=xxxxx&p6_pd=1001&p7_st=success&p8_reply=2+商户密匙*/
		try {
			getRequest().setCharacterEncoding("UTF-8");
			//指令类型	网银=1,卡类=2
			String p1_md = getRequest().getParameter("p1_md");
			//币付宝订单
			String p2_sn = getRequest().getParameter("p2_sn");
			//商户订单号
			String p3_xn = getRequest().getParameter("p3_xn");
			//支付金额
			String p4_amt = getRequest().getParameter("p4_amt");
			//扩展信息
			String p5_ex = getRequest().getParameter("p5_ex");
			//支付方式
			String p6_pd = getRequest().getParameter("p6_pd");
			//状态	成功=success,失败=faile
			String p7_st = getRequest().getParameter("p7_st");
			//通知方式	1=通知,2=显示
			String p8_reply = getRequest().getParameter("p8_reply");
			//签名值
			String sign = getRequest().getParameter("sign");
			
			log.info("\n币付宝--》p3_xn："+p3_xn+"p4_amt:"+p4_amt+"p7_st:"+p7_st+"p8_reply:"+p8_reply+"sign:"+sign);
			
			StringBuffer str = new StringBuffer();
			str.append("p1_md=").append(p1_md)
			.append("&p2_sn=").append(p2_sn)
			.append("&p3_xn=").append(p3_xn)
			.append("&p4_amt=").append(p4_amt)
			.append("&p5_ex=").append(p5_ex)
			.append("&p6_pd=").append(p6_pd)
			.append("&p7_st=").append(p7_st)
			.append("&p8_reply=").append(p8_reply)
			.append(key);
			String md5sign = DigestUtils.md5Hex(str.toString());
			
			if(md5sign.equalsIgnoreCase(sign)){
				if(StringUtils.isNotEmpty(p7_st) && "success".equals(p7_st)){//&& "1".equals(p8_reply)
					log.info("success:"+"p7_st:"+p7_st+"p8_reply:"+p8_reply+"订单号:"+p3_xn);
					//					处理账单信息
					String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "updateBfbOrderStatus", new Object[] { p3_xn, Double.parseDouble(p4_amt)}, String.class);
					if (returnmsg == null) {
						log.info("交易成功,你支付的" + p4_amt + "元已经到账,此次交易的订单号为" + p3_xn);
						writeText("success");
					}else{
						log.info(returnmsg);
						if("此笔交易已经支付成功".equals(returnmsg)){
							writeText("success");
						}else{
							writeText(returnmsg);
						}
					}
				}else{
					log.info("error:"+"p7_st:"+p7_st+"p8_reply:"+p8_reply+"订单号:"+p3_xn);
					writeText("error:"+"p7_st:"+p7_st+"p8_reply:"+p8_reply+"订单号:"+p3_xn);
				}
			}else{
				log.info("bfb签名验证失败");
				writeText("bfb签名验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			writeText("抛出异常");
			return null ;
		} 
		
	    return null ;
	}

	public String getP2_xn() {
		return p2_xn;
	}

	public void setP2_xn(String p2_xn) {
		this.p2_xn = p2_xn;
	}

	public String getP4_pd() {
		return p4_pd;
	}

	public void setP4_pd(String p4_pd) {
		this.p4_pd = p4_pd;
	}

	public String getP5_name() {
		return p5_name;
	}

	public void setP5_name(String p5_name) {
		this.p5_name = p5_name;
	}

	public String getP6_amount() {
		return p6_amount;
	}

	public void setP6_amount(String p6_amount) {
		this.p6_amount = p6_amount;
	}

	public String getP12_ver() {
		return p12_ver;
	}

	public void setP12_ver(String p12_ver) {
		this.p12_ver = p12_ver;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getP1_md() {
		return p1_md;
	}

	public String getP3_bn() {
		return p3_bn;
	}

	public String getP7_cr() {
		return p7_cr;
	}

	public String getP8_ex() {
		return p8_ex;
	}

	public String getP9_url() {
		return p9_url;
	}

	public String getP10_reply() {
		return p10_reply;
	}

	public String getP11_mode() {
		return p11_mode;
	}
	
	
}
