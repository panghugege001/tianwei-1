package dfh.action.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.AxisFault;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import dfh.action.SubActionSupport;
import dfh.exception.PostFailedException;
import dfh.exception.ResponseFailedException;
import dfh.json.JSONObject;
import dfh.jubaobar.pay.JubaoPay;
import dfh.jubaobar.pay.RSA;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.DepositOrder;
import dfh.model.OnlineToken;
import dfh.model.PayMerchant;
import dfh.model.PayOrderValidation;
import dfh.model.Users;
import dfh.model.common.Response;
import dfh.model.enums.CommonZfEnum;
import dfh.model.enums.VipLevel;
import dfh.model.pay.PayRequestVo;
import dfh.model.pay.PayWayVo;
import dfh.remote.DocumentParser;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.KenoResponseBean;
import dfh.security.EncryptionUtil;
import dfh.utils.AESUtil;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.KenoUtil;
import dfh.utils.MD5;
import dfh.utils.MyWebUtils;
import dfh.utils.RSAWithSoftware;
import dfh.utils.StringUtil;
import dfh.utils.Yom;
import dfh.utils.ZfSendRequestUtil;

public class DinpayAction extends SubActionSupport {
	private Logger log = Logger.getLogger(DinpayAction.class);   
	/**  
	 *          
	 */
	private static final long serialVersionUID = 3991838913619985234L;        
	
	protected ObjectMapper JSON = new ObjectMapper();
	private List<PayWayVo> payWayVos;
	private PayRequestVo payVo = new PayRequestVo();
	private Response message = new Response();
   
	// 参数编码字符集(必选)
	private String input_charset;      
	// 接口版本(必选)固定值:V3.0
	private String interface_version;
	// 商家号（必填）
	private String merchant_code;
	// 后台通知地址(必填)
	private String notify_url;
	// 定单金额（必填）
	private String order_amount;
	// 商家定单号(必填)
	private String order_no;
	// 商家定单时间(必填)
	private String order_time;
	// 签名方式(必填)
	private String sign_type;
	// 商品编号(选填)
	private String product_code;
	// 商品描述（选填）
	private String product_desc;
	// 商品名称（必填）
	private String product_name;
	// 端口数量(选填)
	private String product_num;
	// 页面跳转同步通知地址(选填)
	private String return_url;
	// 业务类型(必填)
	private String service_type;
	// 商品展示地址(选填)
	private String show_url;
	// 公用业务扩展参数（选填）
	private String extend_param;
	// 公用业务回传参数（选填）
	private String extra_return_param;
	// 直联通道代码（选填）
	private String bank_code;
	// 客户端IP（选填）
	private String client_ip;
	// 签名
	private String sign;
	private String qrcode;
	// 支付密匙
	private String key;
	// 错误信息
	private String error_info;
	// 智付
	private String zf;
	// 智付
	private String zfTwo;
	
	private String zfThree ;
	
	private String zfDk;
	//智付点卡1
	private String zfDk1;
	// 智付微信
	private String zfwx;
	// 智付微信1
	private String zfwx1;
	//通用智付
	private HashMap<String, String> commonZf = new HashMap<String, String>();
	// 环迅
	private String ips;
	// 汇付
	private String hf;
	//汇潮
	private String hc ;
	// 汇潮一麻袋
	private String hcymd;
	//币付宝
	private String bfb;
	//国付宝
	private HashMap<String, String> gfbMap = new HashMap<String, String>();
	//海尔
	private HashMap<String, String> hrMap = new HashMap<String, String>();
	//支付宝
	private String zfb;
	//PT客户端使用
	private String token ;
	private String url ;
	
	private String weixinpay; 
	
	//乐富微信
	private String lfwx;
	//新贝微信
	private String xbwx;
	
	private String kdzf;
	//汇付宝	支付宝支付
	private String hhbzf;
	//口袋微信支付
	private String kdwxzf;
	//口袋微信支付2
	private String kdwxzf2;
	//汇付宝 微信支付
	private String hhbwxzf;	
	//聚宝支付宝
	private String jubzfb;
	//迅联宝
	private String xlb;
	//迅联宝网银
	private String xlbwy;
	//微信额度验证开关
	private String wxedyz;
	
	//微信额度验证
	private String wxValidaTeAmout;
	//微信额度验证编码id
	private String  wxValidaId;
	//优付支付宝
	private String yfzfb;	
	
	//新贝支付宝
	private String xbzfb;
	//口袋支付宝2 and 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3
	private HashMap<String, String> commonKdWx = new HashMap<String, String>();
	//银宝支付宝
	private HashMap<String, String> commonYbZfb = new HashMap<String, String>();
	//优付支付宝 and 微信
	private HashMap<String, String> commonYfZf = new HashMap<String, String>(); 
	
	//千网
	private HashMap<String, String> commonQwZf = new HashMap<String, String>();
	
	//迅联宝
	private String xlbzfb;
	
	public PayRequestVo getPayVo() {
		return payVo;
	}

	public void setPayVo(PayRequestVo payVo) {
		this.payVo = payVo;
	}

	public List<PayWayVo> getPayWayVos() {
		return payWayVos;
	}

	public void setPayWayVos(List<PayWayVo> payWayVos) {
		this.payWayVos = payWayVos;
	}

	public Response getMessage() {
		return message;
	}

	public void setMessage(Response message) {
		this.message = message;
	}

	public String getXlbzfb() {
		return xlbzfb;
	}

	public void setXlbzfb(String xlbzfb) {
		this.xlbzfb = xlbzfb;
	}

	public HashMap<String, String> getCommonQwZf() {
		return commonQwZf;
	}

	public void setCommonQwZf(HashMap<String, String> commonQwZf) {
		this.commonQwZf = commonQwZf;
	}

	public HashMap<String, String> getCommonYbZfb() {
		return commonYbZfb;
	}

	public void setCommonYbZfb(HashMap<String, String> commonYbZfb) {
		this.commonYbZfb = commonYbZfb;
	}

	public HashMap<String, String> getCommonYfZf() {
		return commonYfZf;
	}

	public void setCommonYfZf(HashMap<String, String> commonYfZf) {
		this.commonYfZf = commonYfZf;
	}

	public HashMap<String, String> getCommonKdWx() {
		return commonKdWx;
	}

	public void setCommonKdWx(HashMap<String, String> commonKdWx) {
		this.commonKdWx = commonKdWx;
	}

	public String getXbzfb() {
		return xbzfb;
	}

	public void setXbzfb(String xbzfb) {
		this.xbzfb = xbzfb;
	}

	public String getHcymd() {
		return hcymd;
	}

	public void setHcymd(String hcymd) {
		this.hcymd = hcymd;
	}

	public String getYfzfb() {
		return yfzfb;
	}

	public void setYfzfb(String yfzfb) {
		this.yfzfb = yfzfb;
	}
	
	public String getKdwxzf2() {
		return kdwxzf2;
	}

	public void setKdwxzf2(String kdwxzf2) {
		this.kdwxzf2 = kdwxzf2;
	}

	public String getXlbwy() {
		return xlbwy;
	}

	public void setXlbwy(String xlbwy) {
		this.xlbwy = xlbwy;
	}

	public String getWxedyz() {
		return wxedyz;
	}

	public void setWxedyz(String wxedyz) {
		this.wxedyz = wxedyz;
	}

	public String getWxValidaId() {
		return wxValidaId;
	}

	public void setWxValidaId(String wxValidaId) {
		this.wxValidaId = wxValidaId;
	}

	public String getWxValidaTeAmout() {
		return wxValidaTeAmout;
	}

	public void setWxValidaTeAmout(String wxValidaTeAmout) {
		this.wxValidaTeAmout = wxValidaTeAmout;
	}
	
	public String getHhbwxzf() {
		return hhbwxzf;
	}

	public void setHhbwxzf(String hhbwxzf) {
		this.hhbwxzf = hhbwxzf;
	}

	public String getJubzfb() {
		return jubzfb;
	}

	public void setJubzfb(String jubzfb) {
		this.jubzfb = jubzfb;
	}

	public String getXlb() {
		return xlb;
	}

	public void setXlb(String xlb) {
		this.xlb = xlb;
	}

	public String getKdwxzf() {
		return kdwxzf;
	}

	public void setKdwxzf(String kdwxzf) {
		this.kdwxzf = kdwxzf;
	}
	
	public String getHhbzf() {
		return hhbzf;
	}

	public void setHhbzf(String hhbzf) {
		this.hhbzf = hhbzf;
	}
	
	public String getKdzf() {
		return kdzf;
	}

	public void setKdzf(String kdzf) {
		this.kdzf = kdzf;
	}
	
	public String getWeixinpay() {
		return weixinpay;
	}

	public void setWeixinpay(String weixinpay) {
		this.weixinpay = weixinpay;
	}

	public String getHc() {
		return hc;
	}

	public void setHc(String hc) {
		this.hc = hc;
	}
	
	

	public String getZfwx() {
		return zfwx;
	}

	public void setZfwx(String zfwx) {
		this.zfwx = zfwx;
	}
	public String getZfwx1() {
		return zfwx1;
	}

	public void setZfwx1(String zfwx1) {
		this.zfwx1 = zfwx1;
	}
	
	public String getLfwx() {
		return lfwx;
	}

	public void setLfwx(String lfwx) {
		this.lfwx = lfwx;
	}

	//存款银行
	List<Bankinfo> bankinfo=new ArrayList<Bankinfo>();
	
	/**
	 * 登录游戏验证
	 * 
	 * @return
	 */
	public String gameLogin() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) {
				return ERROR;
			}
			// 代理不能登录邮箱
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	public String dinpayRedirect() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			if(user.getFlag()==1){
				setError_info("[提示]该玩家已经冻结！");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			//查看在线支付是否开启
			Const constPay=null;
//			if (user.getLevel() >= VipLevel.COMMON.getCode().intValue()) {
//				constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付" }, Const.class);
//				// 商家号（必填）
//				merchant_code = "2030028802";
//				// 支付密匙
//				key = "uqajaj9298hsau2a832_";
//				// 页面跳转同步通知地址(选填)
//				return_url = "http://www.haoleyigo.com";
//			}else{
				constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付1" }, Const.class);
				// 商家号（必填）
				merchant_code = "2030000006";
				key = "sdkbg6ckajlfv8f03g8_";
				
				// 页面跳转同步通知地址(选填)
				//return_url = "http://pay.jiekoue68.com:2112/member_dinpay_return.asp";
				return_url = "http://www.krhnt.com";
//			}
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(order_amount) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			if(!StringUtil.isAvaliableBankCard(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 参数编码字符集(必选)
			input_charset = "UTF-8";
			// 接口版本(必选)固定值:V3.0
			interface_version = "V3.0";
			// 后台通知地址(必填)
			notify_url = "http://pay.jiekoue68.com:2112/asp/payReturn.aspx";
			// 定单金额（必填）
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (order_amount == null) {
				order_amount = "0.00";
			}
			order_amount = currentNumberFormat.format(Double.parseDouble(order_amount));
			// 商家定单号(必填)
			order_no = user.getLoginname()+"_e68"+orderNo;
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			order_time = format.format(new Date());
			// 签名方式(必填)
			sign_type = "MD5";
			// 商品编号(选填)
			product_code = "";
			// 商品描述（选填）
			product_desc = "";
			// 商品名称（必填）
			product_name = user.getLoginname();
			// 商品数量(选填)
			product_num = "";
			// 业务类型(必填)
			service_type = "direct_pay";
			// 商品展示地址(选填)
			show_url = "";
			// 公用业务扩展参数（选填）
			extend_param = "";
			// 公用业务回传参数（选填）
			extra_return_param = user.getLoginname();
			
			if(dfh.utils.StringUtil.isDianKa(bank_code)){
				extra_return_param +=  "_flag_" + bank_code;
				order_no = extra_return_param+"_e68"+orderNo;
			}
			System.out.println(bank_code+"extra_return_param:"+extra_return_param);
			// 客户端IP（选填）
			client_ip = getIp();
			if (client_ip == null || client_ip.equals("")) {
				client_ip = "127.0.0.1";
			}
			// 组织订单信息
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(bank_code)) {
				signSrc.append("bank_code=").append(bank_code).append("&");
			}
			if (!"".equals(client_ip)) {
				signSrc.append("client_ip=").append(client_ip).append("&");
			}
			if (!"".equals(extend_param)) {
				signSrc.append("extend_param=").append(extend_param).append("&");
			}
			if (!"".equals(extra_return_param)) {
				signSrc.append("extra_return_param=").append(extra_return_param).append("&");
			}
			if (!"".equals(input_charset)) {
				signSrc.append("input_charset=").append(input_charset).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(order_amount)) {
				signSrc.append("order_amount=").append(order_amount).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(order_time)) {
				signSrc.append("order_time=").append(order_time).append("&");
			}
			if (!"".equals(product_code)) {
				signSrc.append("product_code=").append(product_code).append("&");
			}
			if (!"".equals(product_desc)) {
				signSrc.append("product_desc=").append(product_desc).append("&");
			}
			if (!"".equals(product_name)) {
				signSrc.append("product_name=").append(product_name).append("&");
			}
			if (!"".equals(product_num)) {
				signSrc.append("product_num=").append(product_num).append("&");
			}
			if (!"".equals(return_url)) {
				signSrc.append("return_url=").append(return_url).append("&");
			}
			if (!"".equals(service_type)) {
				signSrc.append("service_type=").append(service_type).append("&");
			}
			if (!"".equals(show_url)) {
				signSrc.append("show_url=").append(show_url).append("&");
			}
			signSrc.append("key=").append(key);
			String singInfo = signSrc.toString();
			sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	public String dinpayRedirectTwo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			if(user.getFlag()==1){
				setError_info("[提示]该玩家已经冻结！");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			//查看在线支付是否开启
			Const constPay=null;
				constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付2" }, Const.class);
				// 商家号（必填）
				merchant_code = "2030020118";
				key = "e68a3o92asdfs343464_";
				// 页面跳转同步通知地址(选填)
				//return_url = "http://pay.jiekoue68.com:2112/member_dinpay_return.asp";
				return_url = "http://www.7daymobile.com";
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(order_amount) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			if(!StringUtil.isAvaliableBankCard(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 参数编码字符集(必选)
			input_charset = "UTF-8";
			// 接口版本(必选)固定值:V3.0
			interface_version = "V3.0";
			// 后台通知地址(必填)
			notify_url = "http://pay.jiekoue68.com:2112/asp/payReturn.aspx";
			// 定单金额（必填）
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (order_amount == null) {
				order_amount = "0.00";
			}
			order_amount = currentNumberFormat.format(Double.parseDouble(order_amount));
			// 商家定单号(必填)
			order_no = user.getLoginname()+"_e68"+orderNo;
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			order_time = format.format(new Date());
			// 签名方式(必填)
			sign_type = "MD5";
			// 商品编号(选填)
			product_code = "";
			// 商品描述（选填）
			product_desc = "";
			// 商品名称（必填）
			product_name = user.getLoginname();
			// 商品数量(选填)
			product_num = "";
			// 业务类型(必填)
			service_type = "direct_pay";
			// 商品展示地址(选填)
			show_url = "";
			// 公用业务扩展参数（选填）
			extend_param = "";
			// 公用业务回传参数（选填）
			extra_return_param = user.getLoginname();
			
			
			if(dfh.utils.StringUtil.isDianKa(bank_code)){
				extra_return_param +=  "_flag_" + bank_code;
				order_no = extra_return_param+"_e68"+orderNo;
			}
			System.out.println(bank_code+"extra_return_param:"+extra_return_param);
			
			// 客户端IP（选填）
			client_ip = getIp();
			if (client_ip == null || client_ip.equals("")) {
				client_ip = "127.0.0.1";
			}
			// 组织订单信息
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(bank_code)) {
				signSrc.append("bank_code=").append(bank_code).append("&");
			}
			if (!"".equals(client_ip)) {
				signSrc.append("client_ip=").append(client_ip).append("&");
			}
			if (!"".equals(extend_param)) {
				signSrc.append("extend_param=").append(extend_param).append("&");
			}
			if (!"".equals(extra_return_param)) {
				signSrc.append("extra_return_param=").append(extra_return_param).append("&");
			}
			if (!"".equals(input_charset)) {
				signSrc.append("input_charset=").append(input_charset).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(order_amount)) {
				signSrc.append("order_amount=").append(order_amount).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(order_time)) {
				signSrc.append("order_time=").append(order_time).append("&");
			}
			if (!"".equals(product_code)) {
				signSrc.append("product_code=").append(product_code).append("&");
			}
			if (!"".equals(product_desc)) {
				signSrc.append("product_desc=").append(product_desc).append("&");
			}
			if (!"".equals(product_name)) {
				signSrc.append("product_name=").append(product_name).append("&");
			}
			if (!"".equals(product_num)) {
				signSrc.append("product_num=").append(product_num).append("&");
			}
			if (!"".equals(return_url)) {
				signSrc.append("return_url=").append(return_url).append("&");
			}
			if (!"".equals(service_type)) {
				signSrc.append("service_type=").append(service_type).append("&");
			}
			if (!"".equals(show_url)) {
				signSrc.append("show_url=").append(show_url).append("&");
			}
			signSrc.append("key=").append(key);
			String singInfo = signSrc.toString();
			sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	public String dinpayRedirectThree() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			if(user.getFlag()==1){
				setError_info("[提示]该玩家已经冻结！");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			//查看在线支付是否开启
			Const constPay=null;
				constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付3" }, Const.class);
				// 商家号（必填）
				merchant_code = "2030020119";
				// 支付密匙
				key = "e68a45p32i49sdfsfs3_";
				// 页面跳转同步通知地址(选填)
				return_url = "http://www.joyomobile.com";
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(order_amount) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			if(!StringUtil.isAvaliableBankCard(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 参数编码字符集(必选)
			input_charset = "UTF-8";
			// 接口版本(必选)固定值:V3.0
			interface_version = "V3.0";
			// 后台通知地址(必填)
			notify_url = "http://pay.jiekoue68.com:2112/asp/payReturn.aspx";//http://pay.jiekoue68.com:2112/asp/payReturn.aspx
			// 定单金额（必填）
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (order_amount == null) {
				order_amount = "0.00";
			}
			order_amount = currentNumberFormat.format(Double.parseDouble(order_amount));
			// 商家定单号(必填)
			order_no = user.getLoginname()+"_e68"+orderNo;
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			order_time = format.format(new Date());
			// 签名方式(必填)
			sign_type = "MD5";
			// 商品编号(选填)
			product_code = "";
			// 商品描述（选填）
			product_desc = "";
			// 商品名称（必填）
			product_name = user.getLoginname();
			// 商品数量(选填)
			product_num = "";
			// 业务类型(必填)
			service_type = "direct_pay";
			// 商品展示地址(选填)
			show_url = "";
			// 公用业务扩展参数（选填）
			extend_param = "";
			// 公用业务回传参数（选填）
			extra_return_param = user.getLoginname();
			
			
			if(dfh.utils.StringUtil.isDianKa(bank_code)){
				extra_return_param +=  "_flag_" + bank_code;
				order_no = extra_return_param+"_e68"+orderNo;
			}
			System.out.println(bank_code+"extra_return_param:"+extra_return_param);
			
			// 客户端IP（选填）
			client_ip = getIp();
			if (client_ip == null || client_ip.equals("")) {
				client_ip = "127.0.0.1";
			}
			// 组织订单信息
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(bank_code)) {
				signSrc.append("bank_code=").append(bank_code).append("&");
			}
			if (!"".equals(client_ip)) {
				signSrc.append("client_ip=").append(client_ip).append("&");
			}
			if (!"".equals(extend_param)) {
				signSrc.append("extend_param=").append(extend_param).append("&");
			}
			if (!"".equals(extra_return_param)) {
				signSrc.append("extra_return_param=").append(extra_return_param).append("&");
			}
			if (!"".equals(input_charset)) {
				signSrc.append("input_charset=").append(input_charset).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(order_amount)) {
				signSrc.append("order_amount=").append(order_amount).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(order_time)) {
				signSrc.append("order_time=").append(order_time).append("&");
			}
			if (!"".equals(product_code)) {
				signSrc.append("product_code=").append(product_code).append("&");
			}
			if (!"".equals(product_desc)) {
				signSrc.append("product_desc=").append(product_desc).append("&");
			}
			if (!"".equals(product_name)) {
				signSrc.append("product_name=").append(product_name).append("&");
			}
			if (!"".equals(product_num)) {
				signSrc.append("product_num=").append(product_num).append("&");
			}
			if (!"".equals(return_url)) {
				signSrc.append("return_url=").append(return_url).append("&");
			}
			if (!"".equals(service_type)) {
				signSrc.append("service_type=").append(service_type).append("&");
			}
			if (!"".equals(show_url)) {
				signSrc.append("show_url=").append(show_url).append("&");
			}
			signSrc.append("key=").append(key);
			String singInfo = signSrc.toString();
			sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	public String dinpayRedirectDk() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			if(user.getFlag()==1){
				setError_info("[提示]该玩家已经冻结！");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			//查看在线支付是否开启
			Const constPay=null;
				constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付点卡" }, Const.class);
				// 商家号（必填）
			merchant_code = "2000295555";
				// 支付密匙
			key = "98adKLs2DSghaas8f_5dvaawp";
				// 页面跳转同步通知地址(选填)
				return_url = "http://www.joyomobile.com";
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(order_amount) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			if(!StringUtil.isAvaliableBankCard(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 参数编码字符集(必选)
			input_charset = "UTF-8";
			// 接口版本(必选)固定值:V3.0
			interface_version = "V3.0";
			// 后台通知地址(必填)
			notify_url = "http://pay.jiekoue68.com:2112/asp/payReturn.aspx";//http://pay.jiekoue68.com:2112/asp/payReturn.aspx
			// 定单金额（必填）
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (order_amount == null) {
				order_amount = "0.00";
			}
			order_amount = currentNumberFormat.format(Double.parseDouble(order_amount));
			// 商家定单号(必填)
			order_no = user.getLoginname()+"_e68"+orderNo;
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			order_time = format.format(new Date());
			// 签名方式(必填)
			sign_type = "MD5";
			// 商品编号(选填)
			product_code = "";
			// 商品描述（选填）
			product_desc = "";
			// 商品名称（必填）
			product_name = user.getLoginname();
			// 商品数量(选填)
			product_num = "";
			// 业务类型(必填)
			service_type = "direct_pay";
			// 商品展示地址(选填)
			show_url = "";
			// 公用业务扩展参数（选填）
			extend_param = "";
			// 公用业务回传参数（选填）
			extra_return_param = user.getLoginname();
			
			
			if(dfh.utils.StringUtil.isDianKa(bank_code)){
				extra_return_param +=  "_flag_" + bank_code;
				order_no = extra_return_param+"_e68"+orderNo;
			}else{
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			System.out.println(bank_code+"extra_return_param:"+extra_return_param);
			
			// 客户端IP（选填）
			client_ip = getIp();
			if (client_ip == null || client_ip.equals("")) {
				client_ip = "127.0.0.1";
			}
			// 组织订单信息
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(bank_code)) {
				signSrc.append("bank_code=").append(bank_code).append("&");
			}
			if (!"".equals(client_ip)) {
				signSrc.append("client_ip=").append(client_ip).append("&");
			}
			if (!"".equals(extend_param)) {
				signSrc.append("extend_param=").append(extend_param).append("&");
			}
			if (!"".equals(extra_return_param)) {
				signSrc.append("extra_return_param=").append(extra_return_param).append("&");
			}
			if (!"".equals(input_charset)) {
				signSrc.append("input_charset=").append(input_charset).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(order_amount)) {
				signSrc.append("order_amount=").append(order_amount).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(order_time)) {
				signSrc.append("order_time=").append(order_time).append("&");
			}
			if (!"".equals(product_code)) {
				signSrc.append("product_code=").append(product_code).append("&");
			}
			if (!"".equals(product_desc)) {
				signSrc.append("product_desc=").append(product_desc).append("&");
			}
			if (!"".equals(product_name)) {
				signSrc.append("product_name=").append(product_name).append("&");
			}
			if (!"".equals(product_num)) {
				signSrc.append("product_num=").append(product_num).append("&");
			}
			if (!"".equals(return_url)) {
				signSrc.append("return_url=").append(return_url).append("&");
			}
			if (!"".equals(service_type)) {
				signSrc.append("service_type=").append(service_type).append("&");
			}
			if (!"".equals(show_url)) {
				signSrc.append("show_url=").append(show_url).append("&");
			}
			signSrc.append("key=").append(key);
			String singInfo = signSrc.toString();
			sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	/*****************************
	 * 通用智付1 and  通用智付2
	 * @return
	 */
	public String commonZfRedirect() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			if(user.getFlag()==1){
				setError_info("[提示]该玩家已经冻结！");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			String payType = getRequest().getParameter("payType");
			if(!CommonZfEnum.existKey(payType)){
				setError_info("[提示]非法参数");
				return ERROR;
			}
			CommonZfEnum cmzf = CommonZfEnum.getCommonZf(payType);
			//查看在线支付是否开启
			Const constPay=null;
				constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { cmzf.getText() }, Const.class);
				// 商家号（必填）
				merchant_code = cmzf.getMerchantCode();
				// 支付密匙
				key = cmzf.getMerchantKey();
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(order_amount) > 50000) {
					setError_info("[提示]存款金额不能超过50000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			if(!StringUtil.isAvaliableBankCard(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 参数编码字符集(必选)
			input_charset = "UTF-8";
			// 接口版本(必选)固定值:V3.0
			interface_version = "V3.0";
			// 后台通知地址(必填)
			notify_url = "http://pay.jiekoue68.com:2112/asp/payZfReturn.aspx";
			// 定单金额（必填）
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (order_amount == null) {
				order_amount = "0.00";
			}
			order_amount = currentNumberFormat.format(Double.parseDouble(order_amount));
			// 商家定单号(必填)
			order_no = user.getLoginname()+"_e68"+orderNo;
			// 商家定单时间(必填)
			DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			order_time = format.format(new Date());
			
			// 签名方式(必填)
			sign_type = "RSA-S";
			// 商品编号(选填)
			product_code = "";
			// 商品描述（选填）
			product_desc = "";
			// 商品名称（必填）
			product_name = user.getLoginname();
			// 商品数量(选填)
			product_num = "";
			// 页面跳转同步通知地址(选填)
			return_url = cmzf.getUrl();
			// 业务类型(必填)
			service_type = "direct_pay";
			// 商品展示地址(选填)
			show_url = "";
			// 公用业务扩展参数（选填）
			extend_param = "";
			// 公用业务回传参数（选填）
			extra_return_param = user.getLoginname();
			
			if(dfh.utils.StringUtil.isDianKa(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			System.out.println(bank_code+"extra_return_param:"+extra_return_param);
			

			// 客户端IP（选填）
			client_ip = getIp();
			if (client_ip == null || client_ip.equals("")) {
				client_ip = "127.0.0.1";
			}
			// 组织订单信息
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(bank_code)) {
				signSrc.append("bank_code=").append(bank_code).append("&");
			}
			if (!"".equals(client_ip)) {
				signSrc.append("client_ip=").append(client_ip).append("&");
			}
			if (!"".equals(extend_param)) {
				signSrc.append("extend_param=").append(extend_param).append("&");
			}
			if (!"".equals(extra_return_param)) {
				signSrc.append("extra_return_param=").append(extra_return_param).append("&");
			}
			if (!"".equals(input_charset)) {
				signSrc.append("input_charset=").append(input_charset).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(order_amount)) {
				signSrc.append("order_amount=").append(order_amount).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(order_time)) {
				signSrc.append("order_time=").append(order_time).append("&");
			}
			
			if (!"".equals(product_code)) {
				signSrc.append("product_code=").append(product_code).append("&");
			}
			if (!"".equals(product_desc)) {
				signSrc.append("product_desc=").append(product_desc).append("&");
			}
			if (!"".equals(product_name)) {
				signSrc.append("product_name=").append(product_name).append("&");
			}
			if (!"".equals(product_num)) {
				signSrc.append("product_num=").append(product_num).append("&");
			}
			if (!"".equals(return_url)) {
				signSrc.append("return_url=").append(return_url).append("&");
			}
			if (!"".equals(service_type)) {
				//signSrc.append("service_type=").append(service_type).append("&");
				signSrc.append("service_type=").append(service_type);
			}
			if (!"".equals(show_url)) {
				signSrc.append("show_url=").append(show_url);
			}
			//MD5验签方式 已弃用
			/*signSrc.append("key=").append(key);
			String singInfo = signSrc.toString();
			sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));*/
			
			//通用智付1 RSA-S 验签方式 采用
			String merchant_code =cmzf.getMerchantCode();
			if(merchant_code.equals("2000295699")){
				String singInfo = signSrc.toString();
				log.info("通用智付支付singInfo-->"+singInfo);
				sign = RSAWithSoftware.signByPrivateKey(singInfo,ZfSendRequestUtil.PRIVATE_KEY);
				System.out.println("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
			}
			//通用支付2
			else if(merchant_code.equals("2000295640")){
				String singInfo = signSrc.toString();
				log.info("通用智付支付singInfo-->"+singInfo);
				sign = RSAWithSoftware.signByPrivateKey(singInfo,ZfSendRequestUtil.PRIVATE_KEY2);
				System.out.println("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
			}
			return SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	/*** AES加密 支付发起请求 */
	protected <T> void sendHttpByAES(String url, T vo, HttpServletResponse resq) {
		String responseString = "";
		try {
			PrintWriter out = resq.getWriter();

			Gson gson = new Gson();
			String requestJson = gson.toJson(vo);
			requestJson = AESUtil.encrypt(requestJson);

			responseString = MyWebUtils.getHttpContentByParam(url,MyWebUtils.getListNamevaluepair("requestData", requestJson));
			out.write(responseString);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	// 支付页面选择
//	public String payPage() {
//		try {
//			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//			// 检测用户是否登录
//			if (user == null) {
//				setError_info("[提示]你的登录已过期，请从首页重新登录");
//				return ERROR;
//			}
//			//代理不能使用在线支付
//			if(!user.getRole().equals("MONEY_CUSTOMER")){
//				setError_info("[提示]代理不能使用在线支付！");
//				return ERROR;
//			}
//			if(user.getLevel()!=null){
//				//获取银行账号
//				bankinfo = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getBankinfoEmail", new Object[] {String.valueOf(user.getLevel())}, Bankinfo.class);
//			}
//			
//			String result = HttpUtils.get(AxisUtil.PAY_CENTER_URL + "/pay/pay_way");
//            ResponseData responseData = JSON.readValue(result, ResponseData.class);
//            result = AESUtil.decrypt(responseData.getResponseData());
//            log.info("接收内容：" + result);
//            message = JSON.readValue(result, new TypeReference<Response<List<PayWayVo>>>() {
//            });
//            if (ResponseEnum.SUCCESS.getCode().equals(message.getCode())) {
//                payWayVos = (List<PayWayVo>) message.getData();
//            }
//			return "payPage";
//		} catch (Exception e) {
//			setError_info("网络繁忙，请稍后再试！");
//			return "payPage";
//		}
//	}
	
	
	public String payPageBankBox1() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			if(user.getLevel()!=null){
				//获取银行账号
				bankinfo = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getBankinfoEmail", new Object[] {String.valueOf(user.getLevel())}, Bankinfo.class);
			}
			Boolean payString = false;
			List<Const> consts = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDepositSwitch", new Object[] { 1, 20 }, Const.class);
			List<String> list = new ArrayList();
			for (Const constPay : consts) {
					if (constPay.getId().equals("智付")) {
						if (constPay.getValue().equals("1")) {
							setZf("在线支付页面");
							list.add("智付");
							payString = true;
						}
					} else if (constPay.getId().equals("智付2")) {
						if (constPay.getValue().equals("1")) {
							setZfTwo("在线支付页面");
							list.add("智付2");
							payString = true;
						}
					}else if (constPay.getId().equals("环迅")) {
						if (constPay.getValue().equals("1")) {
							setIps("在线支付页面");
							list.add("环迅");
							payString = true;
						}
					}else if (constPay.getId().equals("汇付")) {
						if (constPay.getValue().equals("1")) {
								setHf("在线支付页面");
								payString = true;
						}
					}
			}
			
			if (!payString) {
				setError_info("在线支付正在维护！");
			}
			GsonUtil.GsonList(bankinfo);
			return null;
		} catch (Exception e) {
			setError_info("网络繁忙，请稍后再试！");
			GsonUtil.GsonObject(getError_info());
			return null;
		}
	}
	
	// 支付页面选择
		public String payPageBox1() {
			try {
				Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
				// 检测用户是否登录
				if (user == null) {
					setError_info("[提示]你的登录已过期，请从首页重新登录");
					return ERROR;
				}
				//代理不能使用在线支付
				if(!user.getRole().equals("MONEY_CUSTOMER")){
					setError_info("[提示]代理不能使用在线支付！");
					return ERROR;
				}
				if(user.getLevel()!=null){
					//获取银行账号
					bankinfo = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getBankinfoEmail", new Object[] {String.valueOf(user.getLevel())}, Bankinfo.class);
				}
				Boolean payString = false;
				List<Const> consts = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDepositSwitch", new Object[] { 1, 20 }, Const.class);
				List<String> list = new ArrayList();
				for (Const constPay : consts) {
					if (user.getLevel() >= VipLevel.COMMON.getCode().intValue()) {
						if (constPay.getId().equals("智付")) {
							if (constPay.getValue().equals("1")) {
								setZf("在线支付页面");
								list.add("智付");
								payString = true;
							}
						} else if (constPay.getId().equals("智付2")) {
							if (constPay.getValue().equals("1")) {
								setZfTwo("在线支付页面");
								list.add("智付2");
								payString = true;
							}
						}else if (constPay.getId().equals("智付3")) {
							if (constPay.getValue().equals("1")) {
								setZfThree("在线支付页面");
								list.add("智付3");
								payString = true;
							}
						} else if (constPay.getId().equals("环迅")) {
							if (constPay.getValue().equals("1")) {
								setIps("在线支付页面");
								list.add("环迅");
								payString = true;
							}
						}else if (constPay.getId().equals("汇付")) {
							if (constPay.getValue().equals("1")) {
									setHf("在线支付页面");
									list.add("汇付");
									payString = true;
							}
						}else if (constPay.getId().equals("汇潮")) {
							if (constPay.getValue().equals("1")) {
								setHc("在线支付页面");
								list.add("汇潮");
								payString = true;
							}
						}
					}else{
						if (constPay.getId().equals("智付1")) {
							if (constPay.getValue().equals("1")) {
								setZf("在线支付页面");
								list.add("智付1");
								payString = true;
							}
						} else if (constPay.getId().equals("环迅")) {
							if (constPay.getValue().equals("1")) {
								setIps("在线支付页面");
								list.add("环迅");
								payString = true;
							}
						}else if (constPay.getId().equals("汇付")) {
							if (constPay.getValue().equals("1")) {
									setHf("在线支付页面");
									list.add("汇付");
									payString = true;
							}
						}else if (constPay.getId().equals("汇潮")) {
							if (constPay.getValue().equals("1")) {
								setHc("在线支付页面");
								list.add("汇潮");
								payString = true;
							}
						}
					}
					
					if (constPay.getId().equals("支付宝")) {
						if (constPay.getValue().equals("1")) {
							setZf("在线支付页面");
							list.add("支付宝");
							payString = true;
						}
					}else if (constPay.getId().equals("智付点卡")) {
						if (constPay.getValue().equals("1")) {
							setZfDk("在线支付页面");
							list.add("智付点卡");
							payString = true;
						}
					}else if (constPay.getId().equals("币付宝")) {
						if (constPay.getValue().equals("1")) {
							setBfb("在线支付页面");
							list.add("币付宝");
							payString = true;
						}
					}
				}
				
				if (!payString) {
					setError_info("在线支付正在维护！");
				}
				GsonUtil.GsonList(list);
				return null;
			} catch (Exception e) {
				setError_info("网络繁忙，请稍后再试！");
				GsonUtil.GsonObject(getError_info());
				return null;
			}
		}
		
		public String checkZfb(){
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject("请登录重试");
				return null;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			try {
				Bankinfo zfbBank = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getZfbBankInfo", new Object[] {user.getLoginname()}, Bankinfo.class);
				GsonUtil.GsonObject(zfbBank);
				return null ;
			} catch (AxisFault e) {
				e.printStackTrace();
			}
			GsonUtil.GsonObject("系统繁忙");
			return null ;
		}
		
		

		
		/****
		 * 通联聚合支付
		 * @return
		 */
		public String gatherDeposit(){
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			log.info(platformID+"支付平台");
			String bankname = platformID.equals("tlzf")?"通联转账":platformID;
			
			// 检测用户是否登录 
			if (user == null) {    
				setError_info("[提示]你的登录已过期，请从首页重新登录");      
				return ERROR;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			try {
				Bankinfo bank = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getGatherPayorder", new Object[] {user.getLoginname(),bankname}, Bankinfo.class);
				if(StringUtils.isNotBlank(bank.getMassage())){
					setError_info(bank.getMassage());
					return ERROR;
				}
				getHttpSession().setAttribute("bank", bank);
			} catch (AxisFault e) {
				e.printStackTrace();
				return ERROR;
			}
			return SUCCESS;
		}
		
		
		

	
		
		/**
		 * 查询第三方支付的符合条件的数据 
		 * @return
		 */
		public String payPageBox2() {
			try {
				Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
				// 检测用户是否登录
				if (user == null) {
					setError_info("[提示]你的登录已过期，请从首页重新登录");
					return ERROR;
				}
				//代理不能使用在线支付
				if(!user.getRole().equals("MONEY_CUSTOMER")){
					setError_info("[提示]代理不能使用在线支付！");
					return ERROR;
				}
				if(user.getLevel()!=null){
					//获取银行账号
					bankinfo = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getBankinfoEmail", new Object[] {String.valueOf(user.getLevel())}, Bankinfo.class);
				}
				List<PayMerchant> payMers = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectPayMerchant", new Object[] {"1",""}, PayMerchant.class);
				StringBuffer result=new StringBuffer("");
				for (PayMerchant payMerchant : payMers) {
				//	$("#box1tab2").append('');
					result.append("<a href=\"/userOnlineHf1.aspx?payType='");
					String str1=URLEncoder.encode(payMerchant.getConstid(), "utf-8");
					str1=URLEncoder.encode(str1, "utf-8");
					result.append(str1+"'\" target='_blank' ><img src='/images/enter.jpg' style=\"margin-left: 35px;margin-top: 20px;\" /></a><p class='pt'> <em  class=\"c_red\"></em>");
				}
				try {
		            HttpServletResponse response = ServletActionContext.getResponse();
		            response.setContentType("application/json; charset=utf-8");
		            response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
		            PrintWriter out;
		            out = response.getWriter();
		            out.write(result.toString());
		            out.flush();
		            out.close();
		            System.out.println("此时调用payPageBox2222----结束");
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            System.out.println("写入页面失败！");
		        }
				return null;
			} catch (Exception e) {
				setError_info("网络繁忙，请稍后再试！");
				GsonUtil.GsonObject(getError_info());
				return null;
			}
		}
	
		
	/**
	 * 判断支付方式是否关闭
	 * @return
	 */
	public String getPayAccounts() {
		try {
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "环迅" }, Const.class);
			if (constPay == null) {
				GsonUtil.GsonObject(2);
			}
			if (constPay.getValue().equals("0")) {
				GsonUtil.GsonObject(0);
			} else {
				GsonUtil.GsonObject(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject(3);
		}
		return null;
	}

	/**
	 * 查询游戏余额
	 * @return
	 * @throws IOException 
	 */
	public String gameAmount() {
		String game = ServletActionContext.getRequest().getParameter("game"); 
		String result=""; 
		Users customer =(Users)ServletActionContext.getRequest().getSession().getAttribute(Constants.SESSION_CUSTOMERID);
		try {  
			if ("ea".equals(game)) {
				result = RemoteCaller.queryCredit(customer.getLoginname())+"";
				ServletActionContext.getResponse().getWriter().print(result==null?"系统繁忙":result);
			}
			
			if ("ag".equals(game)) {
				result = RemoteCaller.queryDspCredit(customer.getLoginname());
				ServletActionContext.getResponse().getWriter().print(result==null?"系统繁忙":result);
			}
			
			if ("agin".equals(game)) {
				result = RemoteCaller.queryDspAginCredit(customer.getLoginname());
				ServletActionContext.getResponse().getWriter().print(result==null?"系统繁忙":result);
			}
			
			if ("bbin".equals(game)) {
				result = RemoteCaller.queryBbinCredit(customer.getLoginname());
				ServletActionContext.getResponse().getWriter().print(result==null?"系统繁忙":result);
			}
			
			if ("keno".equals(game)) {
				KenoResponseBean bean=DocumentParser.parseKenocheckcreditResponseRequest(KenoUtil.checkcredit(customer.getLoginname()));
					if(bean!=null){
						if(bean.getName()!=null && bean.getName().equals("Credit")){
							ServletActionContext.getResponse().getWriter().print(bean.getAmount());
						}else if(bean.getName()!=null && bean.getName().equals("Error")){
							ServletActionContext.getResponse().getWriter().print(bean.getValue());
						}else{
							ServletActionContext.getResponse().getWriter().print("系统繁忙");
						}
					}
			}
			
			if ("ty".equals(game)) {
				result = RemoteCaller.querySBCredit(customer.getLoginname());
				ServletActionContext.getResponse().getWriter().print(result==null?"系统繁忙":result);
			}
			
			if ("pt".equals(game)) {
//				result = PtUtil.getPlayerMoney(customer.getLoginname())+"";
				ServletActionContext.getResponse().getWriter().print(result==null?"系统繁忙":result);
			} 
		} catch (PostFailedException e) {
			try {
				ServletActionContext.getResponse().getWriter().print("系统繁忙");
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (ResponseFailedException e) { 
			try {
				ServletActionContext.getResponse().getWriter().print("系统繁忙");
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (IOException e) { 
			try {
				ServletActionContext.getResponse().getWriter().print("系统繁忙");
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		} catch (Exception e) { 
			try {
				ServletActionContext.getResponse().getWriter().print("系统繁忙");
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		return null;
	}
	
	
	public String ptClientPayOnline(){
		
		try {
			OnlineToken onlineToken = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getToken", new Object[] { token }, OnlineToken.class);
			if(null != onlineToken && ((new Date().getTime() - DateUtil.parseDateForStandard(onlineToken.getTempCreatetime()).getTime()  ) <= 1000*60*3)){
				
				Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { onlineToken.getLoginname() }, Users.class);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				if(null!=url){
					if(url.equals("userOnlineHf1")){
					getHttpSession().setAttribute("hfConstId", "汇付1");
					}else if(url.equals("userOnlineHf2")){
						getHttpSession().setAttribute("hfConstId", "汇付2");
					}else if(url.equals("userOnlineHf3")){
						getHttpSession().setAttribute("hfConstId", "汇付3");
					}else if(url.equals("userOnlineHf4")){
						getHttpSession().setAttribute("hfConstId", "汇付4");
					}else if(url.equals("userOnlineHf5")){
						getHttpSession().setAttribute("hfConstId", "汇付5");
					}else if(url.equals("userOnlineHf6")){
						getHttpSession().setAttribute("hfConstId", "汇付6");
					}else if(url.equals("userOnlineHf7")){
						getHttpSession().setAttribute("hfConstId", "汇付7");
					}
					}
				return  url;
			}else{
				setError_info("身份验证失败.");
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
				return "index" ;
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		setError_info("身份验证失败..");
		return "index" ;
	}

	public String ptClientCardPay(){
		try {
			OnlineToken onlineToken = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getToken", new Object[] { token }, OnlineToken.class);
			if(null != onlineToken && ((new Date().getTime() - DateUtil.parseDateForStandard(onlineToken.getTempCreatetime()).getTime()  ) <= 1000*60*3)){
				
				Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { onlineToken.getLoginname() }, Users.class);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				return SUCCESS;
			}else{
				setError_info("身份验证失败.");
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
				return "index";
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		setError_info("身份验证失败..");
		return "index";
	}
	
	/**
	 * pt客户端支付宝附言存款跳转
	 * @return
	 */ 
	public String ptClientZfbPay(){
		try {
			OnlineToken onlineToken = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getToken", new Object[] { token }, OnlineToken.class);
			if(null != onlineToken && ((new Date().getTime() - DateUtil.parseDateForStandard(onlineToken.getTempCreatetime()).getTime()  ) <= 1000*60*3)){
				
				Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { onlineToken.getLoginname() }, Users.class);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				return SUCCESS;
			}else{
				setError_info("身份验证失败.");
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
				return "index";
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		setError_info("身份验证失败..");
		return "index";
	}
	
	public String zfbDepositRedirect(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			setError_info("[提示]你的登录已过期，请从首页重新登录");
			return ERROR;
		}
		//代理不能使用在线支付
		if(!user.getRole().equals("MONEY_CUSTOMER")){
			setError_info("[提示]代理不能使用在线支付！");
			return ERROR;
		}
		try {
			Bankinfo zfbBank = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getZfbBankRedirectInfo", new Object[] {user.getLoginname(), false}, Bankinfo.class);
			if(StringUtils.isNotBlank(zfbBank.getMassage())){
				setError_info(zfbBank.getMassage());
				return ERROR;
			}
			getHttpSession().setAttribute("zfbBank", zfbBank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String zfbImgCodeDepositRedirect(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			setError_info("[提示]你的登录已过期，请从首页重新登录");
			return ERROR;
		}
		//代理不能使用在线支付
		if(!user.getRole().equals("MONEY_CUSTOMER")){
			setError_info("[提示]代理不能使用在线支付！");
			return ERROR;
		}
		try {
			Bankinfo zfbBank = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getZfbBankRedirectInfo", new Object[] {user.getLoginname() , true}, Bankinfo.class);
			if(StringUtils.isNotBlank(zfbBank.getMassage())){
				setError_info(zfbBank.getMassage());
				return ERROR;
			}
			getHttpSession().setAttribute("zfbBank", zfbBank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate() {
		Date dt = new Date();
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	// 判断数据是否为null
	private boolean isNotNullAndEmpty(String str) {
		boolean b = false;
		if (null != str && str.trim().length() > 0) {
			b = true;
		}
		return b;
	}

	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getInterface_version() {
		return interface_version;
	}

	public void setInterface_version(String interface_version) {
		this.interface_version = interface_version;
	}

	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getProduct_desc() {
		return product_desc;
	}

	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_num() {
		return product_num;
	}

	public void setProduct_num(String product_num) {
		this.product_num = product_num;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public String getShow_url() {
		return show_url;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}

	public String getExtend_param() {
		return extend_param;
	}

	public void setExtend_param(String extend_param) {
		this.extend_param = extend_param;
	}

	public String getExtra_return_param() {
		return extra_return_param;
	}

	public void setExtra_return_param(String extra_return_param) {
		this.extra_return_param = extra_return_param;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getZf() {
		return zf;
	}

	public void setZf(String zf) {
		this.zf = zf;
	}

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public List<Bankinfo> getBankinfo() {
		return bankinfo;
	}

	public void setBankinfo(List<Bankinfo> bankinfo) {
		this.bankinfo = bankinfo;
	}

	public String getHf() {
		return hf;
	}

	public void setHf(String hf) {
		this.hf = hf;
	}

	public String getZfTwo() {
		return zfTwo;
	}

	public void setZfTwo(String zfTwo) {
		this.zfTwo = zfTwo;
	}

	public String getZfThree() {
		return zfThree;
	}

	public void setZfThree(String zfThree) {
		this.zfThree = zfThree;
	}

	public String getZfDk() {
		return zfDk;
	}

	public void setZfDk(String zfDk) {
		this.zfDk = zfDk;
	}

	public String getZfDk1() {
		return zfDk1;
	}

	public void setZfDk1(String zfDk1) {
		this.zfDk1 = zfDk1;
	}

	public HashMap<String, String> getCommonZf() {
		return commonZf;
	}

	public void setCommonZf(HashMap<String, String> commonZf) {
		this.commonZf = commonZf;
	}

	public String getZfb() {
		return zfb;
	}

	public void setZfb(String zfb) {
		this.zfb = zfb;
	}

	public String getBfb() {
		return bfb;
	}

	public void setBfb(String bfb) {
		this.bfb = bfb;
	}

	public HashMap<String, String> getGfbMap() {
		return gfbMap;
	}

	public void setGfbMap(HashMap<String, String> gfbMap) {
		this.gfbMap = gfbMap;
	}
	
	

	public HashMap<String, String> getHrMap() {
		return hrMap;
	}

	public void setHrMap(HashMap<String, String> hrMap) {
		this.hrMap = hrMap;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	public String getXbwx() {
	
		return xbwx;
	}

	
	public void setXbwx(String xbwx) {
	
		this.xbwx = xbwx;
	}




	private String apiCode;
	private String versionCode;
	private String inputCharset;
	private String signType;
	private String redirectURL;
	private String notifyURL;
	private String partner;
	private String buyer;
	private String buyerContactType;
	private String buyerContact;
	// 商家定单号(必填)
	private String outOrderId;
	private String amount;
	private String paymentType;
	private String interfaceCode;
	private String retryFalg;
	private String submitTime;
	private String timeout;
	private String clientIP;
	private String returnParam;
	
	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getBuyerContactType() {
		return buyerContactType;
	}

	public void setBuyerContactType(String buyerContactType) {
		this.buyerContactType = buyerContactType;
	}

	public String getBuyerContact() {
		return buyerContact;
	}

	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public String getRetryFalg() {
		return retryFalg;
	}

	public void setRetryFalg(String retryFalg) {
		this.retryFalg = retryFalg;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getReturnParam() {
		return returnParam;
	}

	public void setReturnParam(String returnParam) {
		this.returnParam = returnParam;
	}

	/**
	 * 乐富微信
	 * @return
	 */
	public String LfWxRedirect() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
			if(Double.parseDouble(sumMoney)<100){
				setError_info("[提示]您的活跃度不足.暂不能使用微信支付！");
				return ERROR;
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"乐富微信"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(order_amount) > 3000) {
					setError_info("[提示]存款金额不能超过3000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getLfWxOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(order_amount)}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			 apiCode="directPay";
			 versionCode="1.0";
			 inputCharset="UTF-8";
			 signType="MD5";
			 redirectURL="http://pay.168.tl/";
			 notifyURL="http://pay.168.tl/asp/payLfwxReturn.aspx"; 
			 sign="";
			 partner="8615339796";
			 buyer="aaa";
			 buyerContactType="email";
			 buyerContact="asd@asd.com";
			// 商家定单号(必填)
			 outOrderId=orderNo;
			 returnParam=user.getLoginname();
			// 定单金额（必填）
			DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
			if (order_amount == null) {
				order_amount = "0.00";
			}
			 amount = currentNumberFormat.format(Double.parseDouble(order_amount));
			 paymentType="ALL";
			 interfaceCode="WALLET_TENCENT_QRCODE";
			 retryFalg="FALSE";
			 submitTime=DateUtil.fmtyyyyMMddHHmmss(new Date());
			 timeout="6H";
			 clientIP="127.0.0.1";
			 key="53CD1F703BD380CA7C6EB730A018715B";
			StringBuffer sbf =new StringBuffer("amount="+amount);
			sbf.append("&apiCode="+apiCode)
			.append("&buyer="+buyer)
			.append("&buyerContact="+buyerContact)
			.append("&buyerContactType="+buyerContactType)
			.append("&clientIP="+clientIP)
			.append("&inputCharset="+inputCharset)
			.append("&interfaceCode="+interfaceCode)
			.append("&notifyURL="+notifyURL)
			.append("&outOrderId="+outOrderId)
			.append("&partner="+partner)
			.append("&paymentType="+paymentType)
			.append("&redirectURL="+redirectURL)
			.append("&retryFalg="+retryFalg)
			.append("&returnParam="+returnParam)
			//.append("&sign="+sign)
			.append("&signType="+signType)
			.append("&submitTime="+submitTime)
			.append("&timeout="+timeout)
			.append("&versionCode="+versionCode)
			.append(key);
			String singInfo = sbf.toString();
			System.out.println("singInfo==="+singInfo);
			sign=dfh.utils.DigestUtils.md5DigestAsHex(singInfo.getBytes("UTF-8"));
			//sign = DigestUtils.md5Hex(singInfo.getBytes("UTF-8"));
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	//新贝支付
	private String version ;
	private String merchantCode ;
	private String orderId ;
	private String asyNotifyUrl ;
	private String synNotifyUrl ;
	private String orderDate ;
	private String tradeIp ;
	private String payCode ;
	private String signValue ;
	private String remark1  ;
	private String remark2  ;
	
	
	
	public String getRemark2() {
	
		return remark2;
	}

	
	public void setRemark2(String remark2) {
	
		this.remark2 = remark2;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAsyNotifyUrl() {
		return asyNotifyUrl;
	}

	public void setAsyNotifyUrl(String asyNotifyUrl) {
		this.asyNotifyUrl = asyNotifyUrl;
	}

	public String getSynNotifyUrl() {
		return synNotifyUrl;
	}

	public void setSynNotifyUrl(String synNotifyUrl) {
		this.synNotifyUrl = synNotifyUrl;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getTradeIp() {
		return tradeIp;
	}

	public void setTradeIp(String tradeIp) {
		this.tradeIp = tradeIp;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getSignValue() {
		return signValue;
	}

	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	
	public String getQrcode() {
	
		return qrcode;
	}

	
	public void setQrcode(String qrcode) {
	
		this.qrcode = qrcode;
	}

	public String XbWxRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			Integer moneyType = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "judgeOnlineDepositAmountWay", new Object[] {"新贝微信"}, Integer.class);
			if(moneyType>0){
				String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
				String divide = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[] {"type999","001","否"}, String.class);
				Double divM = 3000.0;
				if(StringUtil.isNotEmpty(divide)){
					divM = Double.parseDouble(divide.split("#")[1]);
				}
				
				if(moneyType == 1 && Double.parseDouble(sumMoney)>divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道+！");
					return ERROR;
				}else if(moneyType == 2 && Double.parseDouble(sumMoney)<=divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道-！");
					return ERROR;
				}
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"新贝微信"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1 ) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			//获取商家订单号
			orderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXinBWxOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (orderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR; 
			}
			remark2 = user.getLoginname() ;
			version = "V1.0";
			merchantCode = "E02934";
			asyNotifyUrl = "http://pay.jiekoue68.com:2112/asp/payXbwxReturn.aspx";//异步通知地址
			synNotifyUrl = "http://gaoguangya.com/";
			orderDate = DateUtil.fmtyyyyMMddHHmmss(new Date()) ;
			tradeIp = "127.0.0.1";
			payCode = "100040";
			String tokenKey = "4mBvoq1dtL5eke9qrzy1fNwFGNSb4rXm";
			signValue = EncryptionUtil.encryptPassword("Version=["+version+"]MerchantCode=["+merchantCode+"]OrderId=["+orderId+"]Amount=["+amount+"]AsyNotifyUrl=["+asyNotifyUrl+"]SynNotifyUrl=["+synNotifyUrl+"]OrderDate=["+orderDate+"]TradeIp=["+tradeIp+"]PayCode=["+payCode+"]TokenKey=["+tokenKey+"]").toUpperCase();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String KdWxZfRedirect(){
		String wxP_OrderId;
		String wxP_UserId;
		String wxP_ChannelId;
		String wxP_Price;
		Integer wxP_Quantity;
		String wxP_Description;
		String wxP_Notic;
		String wxP_Result_URL;
		String wxP_Notify_URL;
		String wxP_PostKey;
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			Integer moneyType = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "judgeOnlineDepositAmountWay", new Object[] {"口袋微信支付"}, Integer.class);
			if(moneyType>0){
				String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
				String divide = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[] {"type999","001","否"}, String.class);
				Double divM = 3000.0;
				if(StringUtil.isNotEmpty(divide)){
					divM = Double.parseDouble(divide.split("#")[1]);
				}
				
				if(moneyType == 1 && Double.parseDouble(sumMoney)>divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道+！");
					return ERROR;
				}else if(moneyType == 2 && Double.parseDouble(sumMoney)<=divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道-！");
					return ERROR;
				}
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"口袋微信支付"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			//获取商家订单号
			wxP_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdWxZfOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (wxP_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			wxP_UserId = "1002397";
			wxP_ChannelId = "21";			//微信支付
			wxP_Price = amount ;    
			wxP_Quantity = 1 ;
			wxP_Description = "";
			wxP_Notic = user.getLoginname() ;
			wxP_Result_URL = "http://pay.jiekoue68.com:2112/asp/payKdWxZfReturn.aspx";//异步通知地址
			wxP_Notify_URL = "http://pay.zhongxingyue.cn/";
			String wxSalfStr = "2fed5e622429470fbbe7879ec8f6f315";
			
			wxP_PostKey = EncryptionUtil.encryptPassword(wxP_UserId+"|"+wxP_OrderId+"|"+""+"|"+""+"|"+amount+"|"+wxP_ChannelId+"|"+wxSalfStr);
			
			getRequest().setAttribute("wxP_OrderId", wxP_OrderId);
			getRequest().setAttribute("wxP_UserId", wxP_UserId);
			getRequest().setAttribute("wxP_ChannelId", wxP_ChannelId);
			getRequest().setAttribute("wxP_Price", wxP_Price);
			getRequest().setAttribute("wxP_Quantity", wxP_Quantity);
			getRequest().setAttribute("wxP_Description", wxP_Description);
			getRequest().setAttribute("wxP_Notic", wxP_Notic);
			getRequest().setAttribute("wxP_Result_URL", wxP_Result_URL);
			getRequest().setAttribute("wxP_Notify_URL", wxP_Notify_URL);
			getRequest().setAttribute("wxP_PostKey", wxP_PostKey);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	

	

	/****
	 * 口袋微信支付2
	 * @return
	 */
	public String KdWxZfRedirect2(){
		String wxP_OrderId;
		String wxP_UserId;
		String wxP_ChannelId;
		String wxP_Price;
		Integer wxP_Quantity;
		String wxP_Description;
		String wxP_Notic;
		String wxP_Result_URL;
		String wxP_Notify_URL;
		String wxP_PostKey;
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			//检查存款限制
			Integer moneyType = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "judgeOnlineDepositAmountWay", new Object[] {"口袋微信支付2"}, Integer.class);
			if(moneyType>0){
				//检查该玩家某个时间段的存款量
				String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecordForTime", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
				//检查玩家可充值金额（微信通道判断金额）
				String divide = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[] {"type999","001","否"}, String.class);
				Double divM = 3000.0;
				if(StringUtil.isNotEmpty(divide)){
					divM = Double.parseDouble(divide.split("#")[1]);
				}
			   if(moneyType == 1 && Double.parseDouble(sumMoney)>divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道+！");
					return ERROR;
				}else if(moneyType == 2 && Double.parseDouble(sumMoney)<=divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道-！");
					return ERROR;
				}
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"口袋微信支付2"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			//获取商家订单号
			wxP_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdWxZfOrderNo2", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (wxP_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			wxP_UserId = "1002400";
			wxP_ChannelId = "21";			//口袋微信支付 21  33 wap
			wxP_Price = amount ;
			wxP_Quantity = 1 ;
			wxP_Description = "";
			wxP_Notic = user.getLoginname() ;
			wxP_Result_URL = "http://pay.jiekoue68.com:2112/asp/payKdWxZfReturn2.aspx";//异步通知地址
			wxP_Notify_URL = "http://pay.zhongxingyue.cn/";
			String wxSalfStr = "017f1310e0ac4e43b79e2d70f4ce69a1";
			
			wxP_PostKey = EncryptionUtil.encryptPassword(wxP_UserId+"|"+wxP_OrderId+"|"+""+"|"+""+"|"+amount+"|"+wxP_ChannelId+"|"+wxSalfStr);
			
			getRequest().setAttribute("wxP_OrderId", wxP_OrderId);
			getRequest().setAttribute("wxP_UserId", wxP_UserId);
			getRequest().setAttribute("wxP_ChannelId", wxP_ChannelId);
			getRequest().setAttribute("wxP_Price", wxP_Price);
			getRequest().setAttribute("wxP_Quantity", wxP_Quantity);
			getRequest().setAttribute("wxP_Description", wxP_Description);
			getRequest().setAttribute("wxP_Notic", wxP_Notic);
			getRequest().setAttribute("wxP_Result_URL", wxP_Result_URL);
			getRequest().setAttribute("wxP_Notify_URL", wxP_Notify_URL);
			getRequest().setAttribute("wxP_PostKey", wxP_PostKey);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/****
	 * 口袋支付宝2 and 口袋微信支付 and 口袋微信2 and 口袋微信3
	 * 	 * @return
	 */
	public String KdWxZfRedirects(){
		String wxP_OrderId;
		String wxP_UserId = "";
		String wxP_ChannelId="";
		String wxP_Price;
		Integer wxP_Quantity;
		String wxP_Description;
		String wxP_Notic;
		String wxP_Result_URL="";
		String wxP_Notify_URL="";
		String wxP_PostKey;
		String wxSalfStr="";
		String payType = getRequest().getParameter("payType");
		String payName="";
		String apiUrl = "https://api.duqee.com/pay/KDBank.aspx";
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
            //口袋微信1
			if(payType.equals("kdwxzf")){
				setError_info("[提示]支付方式错误");
				payName="口袋微信支付";
				return ERROR;
			}
			//口袋微信2
			else if(payType.equals("kdwxzf2")){
				setError_info("[提示]支付方式错误");
				payName="口袋微信支付2";
				return ERROR;
			}
			//口袋微信3
			else if(payType.equals("kdwxzf3")){
				payName="口袋微信支付3";
			}
			//口袋支付宝2
			else if(payType.equals("kdzfb2")){
				payName="口袋支付宝2";
			}
			//口袋支付宝
			else if(payType.equals("kdzfb")){
				payName="口袋支付宝";
			}
			Integer moneyType = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "judgeOnlineDepositAmountWay", new Object[] {payName}, Integer.class);
			if(moneyType>0){
				String sumMoney = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {user.getLoginname(),"2008-01-01 00:00:01",DateUtil.fmtDateForBetRecods(new Date())}, String.class);
				String divide = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[] {"type999","001","否"}, String.class);
				Double divM = 3000.0;
				if(StringUtil.isNotEmpty(divide)){
					divM = Double.parseDouble(divide.split("#")[1]);
				}
				
			if(moneyType == 1 && Double.parseDouble(sumMoney)>divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道+！");
					return ERROR;
				}else if(moneyType == 2 && Double.parseDouble(sumMoney)<=divM){
					setError_info("[提示]此通道维护升级，请您使用其它支付通道-！");
					return ERROR;
				}
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {payName}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");   
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			//获取商家订单号
			wxP_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdWxZfsOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount),payType}, String.class);
			if (wxP_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			
			// 口袋微信支付1
			if(payType.equals("kdwxzf")){
				wxP_UserId=Constants.KDWX1_MerNo ;
				wxSalfStr=Constants.KDWX1_KEY;
				wxP_Result_URL=Constants.KDWX1_Result_URL;
				wxP_Notify_URL=Constants.KDWX1_Notify_URL;
				wxP_ChannelId = "21";			//微信支付
			}
			// 口袋微信支付2
			else if(payType.equals("kdwxzf2")){
				wxP_UserId=Constants.KDWX2_MerNo ;
				wxSalfStr=Constants.KDWX2_KEY;
				wxP_Result_URL=Constants.KDWX2_Result_URL;
				wxP_Notify_URL=Constants.KDWX2_Notify_URL;
				wxP_ChannelId = "21";			//微信支付
			}
			// 口袋微信支付3
			else if(payType.equals("kdwxzf3")){
				wxP_UserId=Constants.KDWX3_MerNo ;
				wxSalfStr=Constants.KDWX3_KEY;
				wxP_Result_URL=Constants.KDWX3_Result_URL;
				wxP_Notify_URL=Constants.KDWX3_Notify_URL;
				wxP_ChannelId = "21";			//微信支付
			}
			// 口袋支付宝2
			else if(payType.equals("kdzfb2")){
				wxP_UserId=Constants.KDZFB2_MerNo ;
				wxSalfStr=Constants.KDZFB2_KEY;
				wxP_Result_URL=Constants.KDZFB2_Result_URL;
				wxP_Notify_URL=Constants.KDZFB2_Notify_URL;
				wxP_ChannelId = "2";			//支付宝支付
			}
			// 口袋支付宝
			else if(payType.equals("kdzfb")){
				wxP_UserId=Constants.KDZFB_MerNo ;
				wxSalfStr=Constants.KDZFB_KEY;
				wxP_Result_URL=Constants.KDZFB_Result_URL;
				wxP_Notify_URL=Constants.KDZFB_Notify_URL;
				wxP_ChannelId = "2";			//支付宝支付
			}
			wxP_Price = amount ;
			wxP_Quantity = 1 ;
			wxP_Description = "";
			wxP_Notic = user.getLoginname() ;
			//验签
			wxP_PostKey = EncryptionUtil.encryptPassword(wxP_UserId+"|"+wxP_OrderId+"|"+""+"|"+""+"|"+amount+"|"+wxP_ChannelId+"|"+wxSalfStr);
			
			getRequest().setAttribute("wxP_OrderId", wxP_OrderId);
			getRequest().setAttribute("wxP_UserId", wxP_UserId);
			getRequest().setAttribute("wxP_ChannelId", wxP_ChannelId);
			getRequest().setAttribute("wxP_Price", wxP_Price);
			getRequest().setAttribute("wxP_Quantity", wxP_Quantity);
			getRequest().setAttribute("wxP_Description", wxP_Description);
			getRequest().setAttribute("wxP_Notic", wxP_Notic);
			getRequest().setAttribute("wxP_Result_URL", wxP_Result_URL);
			getRequest().setAttribute("wxP_Notify_URL", wxP_Notify_URL);
			getRequest().setAttribute("wxP_PostKey", wxP_PostKey);
			getRequest().setAttribute("apiUrl", apiUrl);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	private String P_UserId ;
	private String P_OrderId;
	private String P_ChannelId ; //充值类型
	private String P_Price ;
	private Integer P_Quantity ;
	private String P_Notic ; //用户附加信息
	private String P_Description ; //银行ID
	private String P_Result_URL ; //充值状态异步通知地址
	private String P_Notify_URL ; //充值后网页同步跳转地址
	private String P_PostKey ; //签名认证串
	
	
	public String getP_UserId() {
		return P_UserId;
	}

	public void setP_UserId(String p_UserId) {
		P_UserId = p_UserId;
	}

	public String getP_OrderId() {
		return P_OrderId;
	}

	public void setP_OrderId(String p_OrderId) {
		P_OrderId = p_OrderId;
	}

	public String getP_ChannelId() {
		return P_ChannelId;
	}

	public void setP_ChannelId(String p_ChannelId) {
		P_ChannelId = p_ChannelId;
	}

	public String getP_Price() {
		return P_Price;
	}

	public void setP_Price(String p_Price) {
		P_Price = p_Price;
	}

	public Integer getP_Quantity() {
		return P_Quantity;
	}

	public void setP_Quantity(Integer p_Quantity) {
		P_Quantity = p_Quantity;
	}

	public String getP_Notic() {
		return P_Notic;
	}

	public void setP_Notic(String p_Notic) {
		P_Notic = p_Notic;
	}

	public String getP_Description() {
		return P_Description;
	}

	public void setP_Description(String p_Description) {
		P_Description = p_Description;
	}

	public String getP_Result_URL() {
		return P_Result_URL;
	}

	public void setP_Result_URL(String p_Result_URL) {
		P_Result_URL = p_Result_URL;
	}

	public String getP_Notify_URL() {
		return P_Notify_URL;
	}

	public void setP_Notify_URL(String p_Notify_URL) {
		P_Notify_URL = p_Notify_URL;
	}

	public String getP_PostKey() {
		return P_PostKey;
	}

	public void setP_PostKey(String p_PostKey) {
		P_PostKey = p_PostKey;
	}

	public String KdZfRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"口袋支付"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 2000) {
				setError_info("[提示]存款金额不能超过1000！");
				return ERROR;
			}
			//获取商家订单号
			P_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getKdZfOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (P_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			P_UserId = "1001242";
			P_ChannelId = "2";			//支付宝
			P_Price = amount ;
			P_Quantity = 1 ;
			P_Description = "";
			P_Notic = user.getLoginname() ;
			P_Result_URL = "http://pay.jiekoue68.com:2112/asp/payKdZfReturn.aspx";//异步通知地址
			P_Notify_URL = "http://pay.zhongxingyue.cn/";
			String SalfStr = "4f4b9ebd2ba344ccbd70a3977c71b431";
			
			P_PostKey = EncryptionUtil.encryptPassword(P_UserId+"|"+P_OrderId+"|"+""+"|"+""+"|"+amount+"|"+P_ChannelId+"|"+SalfStr);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 汇付宝 支付宝支付
	 * @return
	 */
	public String HhbZfRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			int version = 1;
			int pay_type = 0;
			String pay_code = "";
			String return_url = "";
			String agent_bill_time = DateUtil.fmtyyyyMMddHHmmss(new Date());
			String goods_name = "alipay_payment";
			int goods_num = 1;
			String remark = user.getLoginname();
			String goods_note = "";
			String user_ip = getIp()==null?"":getIp().replace('.', '_');
			
			int agent_id = 0;//商户ID
			String key = "";//账户密钥
			String notify_url = "http://pay.jiekoue68.com:2112/asp/payHhbZfReturn.php"; //回调地址
			String agent_bill_id = ""; //订单号
			Double pay_amt = 0d;
			String sign = ""; //签名
			
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"汇付宝支付"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			pay_amt = Double.valueOf(amount);
			pay_type = Integer.valueOf(payCode);
			//生成商家订单号
			agent_bill_id = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getHhbZfOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (agent_bill_id == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			sign = EncryptionUtil.encryptPassword("version="+version+"&agent_id="+agent_id+"&agent_bill_id="+agent_bill_id+"&agent_bill_time="+agent_bill_time+"&pay_type="+pay_type+
					"&pay_amt="+pay_amt+"&notify_url="+notify_url+"&return_url="+return_url+"&user_ip="+user_ip+"&key="+key);
			
			getRequest().setAttribute("version", version);
			getRequest().setAttribute("pay_type", pay_type);
			getRequest().setAttribute("pay_code", pay_code);
			getRequest().setAttribute("agent_id", agent_id);
			getRequest().setAttribute("agent_bill_id", agent_bill_id);
			getRequest().setAttribute("pay_amt", pay_amt);
			getRequest().setAttribute("notify_url", notify_url);
			getRequest().setAttribute("return_url", return_url);
			getRequest().setAttribute("user_ip", user_ip);
			getRequest().setAttribute("agent_bill_time", agent_bill_time);
			getRequest().setAttribute("goods_name", goods_name);
			getRequest().setAttribute("goods_num", goods_num);
			getRequest().setAttribute("remark", remark);
			getRequest().setAttribute("goods_note", goods_note);
			getRequest().setAttribute("sign", sign);
			return SUCCESS;
		} catch (Exception e) {
			log.error("跳转汇付宝支付页面出错", e);
			return ERROR;
		}
	}
	
	
	

	/**
	 * 汇付宝 微信支付
	 * 
	 * @return
	 */
	public String HhbWxZfRedirect() {
		try {
			Users user = (Users) getHttpSession().getAttribute(
					Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}

			int version = 1;  //版本号
			String agent_id="2067257";  //商户内码
			String agent_bill_id=""; //订单号
			String agent_bill_time = DateUtil.fmtyyyyMMddHHmmss(new Date()); //提交单据的时间
			int pay_type = 30; // 微信：30 支付宝：22
			Double pay_amt = 0d;  //订单金额
			String notify_url = "http://pay.jiekoue68.com:2112/asp/payHhbWxZfReturn.aspx"; // 回调地址
			String return_url = "http://pay.zhongxingyue.cn/";
			String user_ip = getIp() == null ? "" : getIp().replace('.', '_');   //ip
			String goods_name = "weixin_payment";
			int goods_num = 1;
			String goods_note = "";
			String remark = user.getLoginname();
			String key = "834499D4528140A88BE8478B";// 账户密钥
			String sign = ""; // 签名

			// 查看在线支付是否开启
			Const constPay = null;
			constPay = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "汇付宝微信" }, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if (!amount.matches("^\\d*[1-9]\\d*$")) {
				setError_info("[提示]存款金额必须为整数！");
				return ERROR;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			pay_amt = Double.valueOf(amount);
			// 生成商家订单号
			agent_bill_id = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,	"getHhbWxZfOrderNo", new Object[] { user.getLoginname(),Double.parseDouble(amount) }, String.class);
			if (agent_bill_id == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			sign = EncryptionUtil.encryptPassword("version=" + version
					+ "&agent_id=" + agent_id + "&agent_bill_id="
					+ agent_bill_id + "&agent_bill_time=" + agent_bill_time
					+ "&pay_type=" + pay_type + "&pay_amt=" + pay_amt
					+ "&notify_url=" + notify_url + "&return_url=" + return_url
					+ "&user_ip=" + user_ip + "&key=" + key);

			getRequest().setAttribute("version", version);
			getRequest().setAttribute("pay_type", pay_type);
			getRequest().setAttribute("agent_id", agent_id);
			getRequest().setAttribute("agent_bill_id", agent_bill_id);
			getRequest().setAttribute("pay_amt", pay_amt);
			getRequest().setAttribute("notify_url", notify_url);
			getRequest().setAttribute("return_url", return_url);
			getRequest().setAttribute("user_ip", user_ip);
			getRequest().setAttribute("agent_bill_time", agent_bill_time);
			getRequest().setAttribute("goods_name", goods_name);
			getRequest().setAttribute("goods_num", goods_num);
			getRequest().setAttribute("remark", remark);
			getRequest().setAttribute("goods_note", goods_note);
			getRequest().setAttribute("sign", sign);
			return SUCCESS;
		} catch (Exception e) {
			log.error("跳转汇付宝支付页面出错", e);
			return ERROR;
		}
	}
	
	
	/**
	 * 聚宝支付宝跳转
	 * @return
	 */
	public String JubZfbRedirect(){
		try {
			Users user = getCustomerFromSession();
			if (user == null){
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"聚宝支付宝"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 2000) {
				setError_info("[提示]存款金额不能超过1000！");
				return ERROR;
			}
			log.info("*********************聚宝支付宝，玩家"+user.getLoginname()+"准备支付"+amount+"元");
			//获取商家订单号
			P_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getJubZfbOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (P_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			String payid = P_OrderId;
			String partnerid = "16060302541234889885";
			String amount = this.amount;
			String payerName = user.getLoginname()+"_"+P_OrderId;
			String goodsName = "商品支付";
			String returnURL = "http://www.juedaifengh.com/";
			String callBackURL = "http://pay.jiekoue68.com:2112/asp/payJubZfbReturn.aspx";
			String remark = user.getLoginname();
			String payMethod = "ALL";
			
			RSA.intialize();
			
			JubaoPay jubaoPay = new JubaoPay(); 
			jubaoPay.setEncrypt("payid",payid);
			jubaoPay.setEncrypt("partnerid",partnerid);//商户合作号，由聚宝云计费生成的唯一标识
			jubaoPay.setEncrypt("amount",amount);
			jubaoPay.setEncrypt("payerName",payerName);
			jubaoPay.setEncrypt("goodsName",goodsName);
			jubaoPay.setEncrypt("returnURL",returnURL);
			jubaoPay.setEncrypt("callBackURL",callBackURL);
			jubaoPay.setEncrypt("payMethod",payMethod);
			jubaoPay.setEncrypt("remark", remark);
			jubaoPay.interpret();
			
			//String message = jubaoPay.getMessage();
			//String signature = jubaoPay.getSignature();
			getRequest().setAttribute("jubaoPay", jubaoPay);
			
			return SUCCESS;
			
		} catch (Exception e) {
			log.error("JubZfbRedirect", e);
		}
		return ERROR;
	}
	
	/******
	 * 迅联宝微信
	 * @return
	 */
	public String XlbRedirect(){
		try {    
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"迅联宝"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			// 客户端IP
			customerIP=getIp();
			if (customerIP == null || customerIP.equals("")) {
				customerIP = "127.0.0.1";
			}
			//获取商家订单号
			P_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXlbOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (P_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 初始化签名
			key = "5020b2c37008b436ae330f9fb567a139";
			// 组织请求数据   
			String apiName="WECHAT_PAY";
			String apiVersion="1.0.0.0";    
			String platformID="210001520011131";       
			String merchNo="210001520011131";
			String orderNo=P_OrderId;
			String tradeDate=DateUtil.fmtyyyyMMdd(new Date());
			String amt=amount;
			System.out.println("************************************apiVersion============="+apiVersion+";amt====================="+amt);
			String merchUrl="http://pay.jiekoue68.com:2112/asp/payXlbReturn.aspx";
			String merchParam=user.getLoginname();
			String tradeSummary="a";
			String choosePayType="5";
			overTime="40";
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("apiName", apiName);
			paramsMap.put("apiVersion", apiVersion);
			paramsMap.put("platformID",platformID);
			paramsMap.put("merchNo", merchNo);
			paramsMap.put("orderNo", orderNo);
			paramsMap.put("tradeDate", tradeDate); 
			paramsMap.put("amt", amount);
			paramsMap.put("merchUrl",merchUrl);
			paramsMap.put("merchParam",merchParam);
			paramsMap.put("tradeSummary", tradeSummary);
			paramsMap.put("overTime", "40");
			paramsMap.put("customerIP", customerIP);
			String paramsStr = String.format("apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s&merchUrl=%s&merchParam=%s&tradeSummary=%s&overTime=%s&customerIP=%s",
			paramsMap.get("apiName"), paramsMap.get("apiVersion"),
			paramsMap.get("platformID"), paramsMap.get("merchNo"),
			paramsMap.get("orderNo"), paramsMap.get("tradeDate"),
			paramsMap.get("amt"), paramsMap.get("merchUrl"),
			paramsMap.get("merchParam"), paramsMap.get("tradeSummary"),
			paramsMap.get("overTime"), paramsMap.get("customerIP"));
			System.out.println(paramsStr+"==================");
			signMsg= signByMD5(paramsStr, key);
			signMsg.replaceAll("\r", "").replaceAll("\n", "");
			
			//构建请求参数
			String url = "http://trade.777pay1.cn:8080/cgi-bin/netpayment/pay_gate.cgi?apiName="+apiName+"&apiVersion="+apiVersion+"&platformID="+platformID+"&merchNo="+merchNo+"&orderNo="+orderNo+"&tradeDate="+tradeDate+"&amt="+amt+"&merchUrl="+merchUrl+"&merchParam="+merchParam+"&tradeSummary="+tradeSummary+"&overTime="+overTime+"&customerIP="+customerIP+"&signMsg="+signMsg;
			String result =MD5. receiveBySend(url, "UTF-8");
			System.out.println(result+">>>>>>>>>>>返回result");
			JSONObject a = new JSONObject(result);
			String resultCode = a.get("resultCode").toString();
			String code = a.get("code").toString();
			byte[] b = null;
			if(resultCode.equals("00")){
				BASE64Decoder decoder = new BASE64Decoder();
			    b = decoder.decodeBuffer(code); 
			}
			qrcode =  new String(b); 
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return SUCCESS;
	}
	
	
	/******
	 * 迅联宝支付宝
	 * @return
	 */
	public String XlbZfbRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"迅联宝支付宝"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			// 客户端IP
			 customerIP=getIp();
			if (customerIP == null || customerIP.equals("")) {
				customerIP = "127.0.0.1";
			}
			
			//获取商家订单号
			P_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXlbZfbOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (P_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 初始化签名
			 key = "c97f72752acc35fe4f3ab45c06a78b87";
			// 组织请求数据
			apiName="ZFB_PAY";
			apiVersion="1.0.0.0";
			platformID="210001440011109";
			merchNo="210001440011109";
			orderNo=P_OrderId;
			tradeDate=DateUtil.fmtyyyyMMdd(new Date());
			amt=amount;
			System.out.println("************************************apiVersion============="+apiVersion+";amt====================="+amt);
			merchUrl="http://pay.jiekoue68.com:2112/asp/payXlbZfbReturn.aspx";
			merchParam=user.getLoginname();
			tradeSummary="a";
			choosePayType="5";
			overTime="40";
			//bankCode=getBankCode();
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("apiName", apiName);
			paramsMap.put("apiVersion", apiVersion);
			paramsMap.put("platformID",platformID);
			paramsMap.put("merchNo", merchNo);
			paramsMap.put("orderNo", orderNo);
			paramsMap.put("tradeDate", tradeDate); 
			paramsMap.put("amt", amount);
			paramsMap.put("merchUrl",merchUrl);
			paramsMap.put("merchParam",merchParam);
			paramsMap.put("tradeSummary", tradeSummary);
			paramsMap.put("overTime", "40");
			paramsMap.put("customerIP", customerIP);
			String paramsStr = String.format("apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s&merchUrl=%s&merchParam=%s&tradeSummary=%s&overTime=%s&customerIP=%s",
							paramsMap.get("apiName"), paramsMap.get("apiVersion"),
							paramsMap.get("platformID"), paramsMap.get("merchNo"),
							paramsMap.get("orderNo"), paramsMap.get("tradeDate"),
							paramsMap.get("amt"), paramsMap.get("merchUrl"),
							paramsMap.get("merchParam"), paramsMap.get("tradeSummary"),
			                paramsMap.get("overTime"), paramsMap.get("customerIP"));
			System.out.println(paramsStr+"==================");
			signMsg= signByMD5(paramsStr, key);
			signMsg.replaceAll("\r", "").replaceAll("\n", "");
			
			//构建请求参数
			String url = "http://trade.777pay1.cn:8080/cgi-bin/netpayment/pay_gate.cgi?apiName="+apiName+"&apiVersion="+apiVersion+"&platformID="+platformID+"&merchNo="+merchNo+"&orderNo="+orderNo+"&tradeDate="+tradeDate+"&amt="+amt+"&merchUrl="+merchUrl+"&merchParam="+merchParam+"&tradeSummary="+tradeSummary+"&overTime="+overTime+"&customerIP="+customerIP+"&signMsg="+signMsg;
			String result =MD5. receiveBySend(url, "UTF-8");
			System.out.println(result+"------------");
			JSONObject a = new JSONObject(result);
			String resultCode = a.get("resultCode").toString();
			String code = a.get("code").toString();
			byte[] b = null;
			if(resultCode.equals("00")){
				BASE64Decoder decoder = new BASE64Decoder();
			    b = decoder.decodeBuffer(code); 
			}
			qrcode =  new String(b); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return SUCCESS;
	}
	
	
	
	/******
	 * 迅联宝网银
	 * @return
	 */
	public String commonXlbWyRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"迅联宝网银"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 50000) {
				setError_info("[提示]存款金额不能超过50000！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(bank_code)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			if(!StringUtil.isAvaliableBankCard(bank_code)){
				setError_info("[提示]支付类型不存在，请重新选择。");
				return ERROR;
			}
			
			//获取商家订单号
			P_OrderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXlbWyOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (P_OrderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 初始化签名
			 key = "6bd669e55f2bc5ea1859da37e455a19d";
			// 组织请求数据
			String  apiName="WEB_PAY_B2C";
			String  apiVersion="1.0.0.0";
			String  platformID="210000520010301";
			String  merchNo="210000520010301";
			String  orderNo=P_OrderId;
			String  tradeDate=DateUtil.fmtyyyyMMdd(new Date());
			String  amt=amount;
			System.out.println("************************************apiVersion============="+apiVersion+";amt====================="+amt);
			String  merchUrl="http://pay.jiekoue68.com:2112/asp/payXlbWyReturn.aspx";
			String  merchParam=user.getLoginname();
			String  tradeSummary="a";
			String  choosePayType="1";
			String  bankCode=bank_code;
			String  signMsg;
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("apiName", apiName);
			paramsMap.put("apiVersion", apiVersion);
			paramsMap.put("platformID",platformID);
			paramsMap.put("merchNo", merchNo);
			paramsMap.put("orderNo", orderNo);
			paramsMap.put("tradeDate", tradeDate); 
			paramsMap.put("amt", amount);
			paramsMap.put("merchUrl",merchUrl);
			paramsMap.put("merchParam",merchParam);
			paramsMap.put("tradeSummary", tradeSummary);
			String paramsStr = String.format("apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s&merchUrl=%s&merchParam=%s&tradeSummary=%s",
							paramsMap.get("apiName"), paramsMap.get("apiVersion"),
							paramsMap.get("platformID"), paramsMap.get("merchNo"),
							paramsMap.get("orderNo"), paramsMap.get("tradeDate"),
							paramsMap.get("amt"), paramsMap.get("merchUrl"),
							paramsMap.get("merchParam"), paramsMap
									.get("tradeSummary"));
			//String signMsg = signData(paramsStr);	// 签名数据
			signMsg= signByMD5(paramsStr, key);
			signMsg.replaceAll("\r", "").replaceAll("\n", "");
			
			getRequest().setAttribute("apiName", apiName);
			getRequest().setAttribute("apiVersion", apiVersion);
			getRequest().setAttribute("platformID", platformID);
			getRequest().setAttribute("merchNo", merchNo);
			getRequest().setAttribute("orderNo", orderNo);
			getRequest().setAttribute("tradeDate", tradeDate);
			getRequest().setAttribute("amt", amt);
			getRequest().setAttribute("merchUrl", merchUrl);
			getRequest().setAttribute("merchParam", merchParam);
			getRequest().setAttribute("tradeSummary", tradeSummary);
			getRequest().setAttribute("signMsg", signMsg);
			getRequest().setAttribute("choosePayType", choosePayType);
			getRequest().setAttribute("bankCode", bankCode);
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return SUCCESS;
	}
	
	
	


	/*****
	 * 优付支付宝 and 微信
	 * @return
	 */
	public String YfRedirect(){
		// 组织请求数据
		String  version="V2.0";
	    String mer_no;
		String input_charset = "UTF-8";
	    String return_url ;
		String notify_url ;
		String referer = "xxx.com"; //上级域名
		String mey_key; //key值
		String bank_code; //银行编码
		String order_no; //商户订单号
		String order_amount; //订单金额
		String product_name="ld"; //商品名称
		String product_num=""; //商品数量
		String customer_phone=""; //消费者电话
		String receive_address=""; //收货地址
		String return_Params;//回传参数
		String customer_ip;//客户端ip
		
		String payType = getRequest().getParameter("payType");
		String payName="";
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR; 
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			//分层处理，新玩家需满一周才能支付
			Boolean flag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUserReDate", new Object[] {user.getLoginname()}, Boolean.class);
			if(!flag){
				setError_info("[提示]新注册用户一周后才能使用此支付！");
				return ERROR;
			}
			
			//优付支付宝
			if(payType.equals("yfzfb")){
				payName="优付支付宝";
			}
			//优付微信
			else if(payType.equals("yfwx")){
				payName="优付微信";
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {payName}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 2) {
				setError_info("[提示]2元以上或者2元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			
			// 客户端IP
			String  customerIP=getIp();
			if (customerIP == null || customerIP.equals("")) {
				customerIP = "127.0.0.1";
			}
			//获取商家订单号
			String orderID = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getYfZfOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount),payType}, String.class);
			if (orderID == null) {
				setError_info("[提示]获取商家订单号失败！");    
				return ERROR;
			}
			// 优付支付宝
			if(payType.equals("yfzfb")){
				mer_no=Constants.YFZFB_MerNo ;
				mey_key=Constants.YFZFB_KEY;
				notify_url=Constants.YFZFB_Result_URL;
				return_url=Constants.YFZFB_Notify_URL;
				bank_code="ALIPAY"; //支付宝
			}
			// 优付微信
			else if(payType.equals("yfwx")){
				mer_no=Constants.YFWX_MerNo ;
				mey_key=Constants.YFWX_KEY;
				notify_url=Constants.YFWX_Result_URL;
				return_url=Constants.YFWX_Notify_URL;
				bank_code="WEIXIN"; //微信
			}
			else {
				setError_info("支付失败，请重新支付");
				return ERROR;
			}
			order_no =  orderID;
			order_amount = amount;
			return_Params=user.getLoginname();
			customer_ip=customerIP; //客户端ip
			
			//拼接参数加密串,按字母升序, 结尾加KEY
			String str = "BANK_CODE="+bank_code+
			"&CUSTOMER_IP="+customer_ip+"&CUSTOMER_PHONE="+customer_phone+
			"&INPUT_CHARSET="+input_charset+"&MER_NO="+mer_no+
			"&NOTIFY_URL="+notify_url+"&ORDER_AMOUNT="+order_amount+
			"&ORDER_NO="+order_no+"&PRODUCT_NAME="+product_name+
			"&PRODUCT_NUM="+product_num+"&RECEIVE_ADDRESS="+receive_address+
			"&REFERER="+referer+"&RETURN_PARAMS="+return_Params+
			"&RETURN_URL="+return_url+"&VERSION="+version+
			"&KEY="+mey_key;
			//Sign 签名
			String sign = Yom.getMD5(str);
			
			getRequest().setAttribute("version", version);
			getRequest().setAttribute("input_charset", input_charset);
			getRequest().setAttribute("return_url", return_url);
			getRequest().setAttribute("notify_url", notify_url);
			getRequest().setAttribute("bank_code", bank_code);
			getRequest().setAttribute("mer_no", mer_no);
			getRequest().setAttribute("order_no", order_no);
			getRequest().setAttribute("order_amount", order_amount);
			getRequest().setAttribute("product_name",product_name);
			getRequest().setAttribute("product_num", product_num);
			getRequest().setAttribute("referer", referer);
			getRequest().setAttribute("customer_ip", customer_ip);
			getRequest().setAttribute("customer_phone", customer_phone);
			getRequest().setAttribute("receive_address", receive_address);
			getRequest().setAttribute("return_Params", return_Params);
			getRequest().setAttribute("sign",sign);
			getRequest().setAttribute("apiUrl", Constants.YFZF_apiUrl);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/*****
	 * 银宝支付宝
	 * @return
	 */
	public String YbZfbRedirect(){
		
		String partner="";
		String callbackurl="";
		String hrefbackurl="http://www.glubanyunshang.com/";
		String key="";
		String url ="http://wytj.9vpay.com/PayBank.aspx";
		
		String payType = getRequest().getParameter("payType");
		String payName="";
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR; 
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			 //银宝支付宝
			if(payType.equals("ybzfb")){
				payName="银宝支付宝";
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {payName}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			
			// 客户端IP
			String  customerIP=getIp();
			if (customerIP == null || customerIP.equals("")) {
				customerIP = "127.0.0.1";
			}
			//获取商家订单号
			String orderID = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getYbZfbOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount),payType}, String.class);
			if (orderID == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 银宝支付宝
			if(payType.equals("ybzfb")){
				partner=Constants.YBZFB_MerNo ;
				key=Constants.YBZFB_KEY;
				callbackurl=Constants.YBZFB_Result_URL;
				hrefbackurl=Constants.YBZFB_Notify_URL;
			}
			String banktype="ALIPAY";
			String paymoney=amount;
			String ordernumber=orderID;
			String attach=user.getLoginname() ;
			//拼接字符串
			String tempStr="partner="+partner+"&banktype="+banktype+"&paymoney="+paymoney+"&ordernumber="+ordernumber+"&callbackurl="+callbackurl;
			System.out.println(tempStr);
			sign= signByMD5(tempStr, key);
			System.out.println("sign:"+sign);
			
			getRequest().setAttribute("partner", partner);
			getRequest().setAttribute("banktype", banktype);
			getRequest().setAttribute("paymoney", paymoney);
			getRequest().setAttribute("ordernumber", ordernumber);
			getRequest().setAttribute("callbackurl", callbackurl);
			getRequest().setAttribute("attach", attach);
			getRequest().setAttribute("hrefbackurl", hrefbackurl);
			getRequest().setAttribute("sign", sign);
			getRequest().setAttribute("url", url);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	
	
	
	
	public static String signByMD5(String sourceData, String key)
			throws Exception {
		String data = sourceData + key;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] sign = md5.digest(data.getBytes());

		return Bytes2HexString(sign).toUpperCase();
	}
	
	/**
	 * 将byte数组转成十六进制的字符串
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer(b.length);
		String hex = "";
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);

			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}
	
	
	public String wechatDepositRedirect(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			setError_info("[提示]你的登录已过期，请从首页重新登录");
			return ERROR;
		}
		try {
			//判断是否存在支付订单
			PayOrderValidation payOrderValidation = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPayOrderValidation", new Object[] {user.getLoginname()}, PayOrderValidation.class);
			if(payOrderValidation !=null){
				setWxValidaTeAmout(Double.toString(payOrderValidation.getAmount()));
				setWxValidaId(Integer.toString(payOrderValidation.getId()));
				
			}
			
			Bankinfo wxBank = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getWxBankRedirectInfo", new Object[] {user.getLoginname()}, Bankinfo.class);
			if(StringUtils.isNotBlank(wxBank.getMassage())){
				setError_info(wxBank.getMassage());
				return ERROR;
			}
			getHttpSession().setAttribute("wxBank", wxBank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}		
	
	/******
	 * 新贝支付宝
	 * @return
	 */
	public String XbZfbRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"新贝支付宝"}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			//判断订单金额必须为保留两位小数
			/*if(!amount.matches("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$")){ 
				setError_info("[提示]存款金额格式错误！");
				return ERROR ;
			}*/
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3001) {
				setError_info("[提示]存款金额不能超过3001！");
				return ERROR;
			}
			//获取商家订单号
			orderId = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getXinBZfbOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount)}, String.class);
			if (orderId == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			remark2 = user.getLoginname() ;
			version = "V1.0";
			merchantCode = "E03148";
			asyNotifyUrl = "http://pay.jiekoue68.com:2112/asp/payXbZfbReturn.aspx";//异步通知地址
			synNotifyUrl = "http://www.yuxinbaokeji.com";
			orderDate = DateUtil.fmtyyyyMMddHHmmss(new Date()) ;   
			tradeIp = "127.0.0.1";
			payCode = "100067";
			String tokenKey = "9eHznTmbwqYn4hBxd7PGcaEkLKFc8XBH";
			signValue = EncryptionUtil.encryptPassword("Version=["+version+"]MerchantCode=["+merchantCode+"]OrderId=["+orderId+"]Amount=["+amount+"]AsyNotifyUrl=["+asyNotifyUrl+"]SynNotifyUrl=["+synNotifyUrl+"]OrderDate=["+orderDate+"]TradeIp=["+tradeIp+"]PayCode=["+payCode+"]TokenKey=["+tokenKey+"]").toUpperCase();
			return SUCCESS;
		} catch (Exception e) {   
			e.printStackTrace();
			return ERROR;     
		}
	}
	
	/*****
	 * 千网支付宝 and 千网微信 
	 * @return
	 */
	public String QwZfRedirect(){
		// 组织请求数据
	    String parter;         //商户
	    String  md5key;    //key
	    String api_url="http://apika.10001000.com/chargebank.aspx";       //网关地址
	    String callbackurl; //回调地址
	    String  hrefbackurl; 
	    String orderid;
	    String type;
	    String value;
	    String attach;
		String customer_ip;//客户端ip
		
		String payType = getRequest().getParameter("payType");
		String payName="";
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR; 
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
            //千网支付宝
			if(payType.equals("qwzfb")){
				payName="千网支付宝";
			}
			//千网微信
			else if(payType.equals("qwwx")){
				payName="千网微信";
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {payName}, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return ERROR;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return ERROR;
			}
			// 判断订单金额
			if (!isNotNullAndEmpty(amount)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			if(!amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return ERROR ;
			}
			if (Double.parseDouble(amount) < 1) {
				setError_info("[提示]1元以上或者1元才能存款！");
				return ERROR;
			}
			if (Double.parseDouble(amount) > 3000) {
				setError_info("[提示]存款金额不能超过3000！");
				return ERROR;
			}
			
			// 客户端IP
			String  customerIP=getIp();
			if (customerIP == null || customerIP.equals("")) {
				customerIP = "127.0.0.1";
			}
			//获取商家订单号
			String orderID = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getQwZfOrderNo", new Object[] {user.getLoginname(),Double.parseDouble(amount),payType}, String.class);
			if (orderID == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			// 千网支付宝
			if(payType.equals("qwzfb")){
				parter=Constants.QWZFB_MerNo ;
				md5key=Constants.QWZFB_KEY;
				callbackurl=Constants.QWZFB_Result_URL;
				hrefbackurl=Constants.QWZFB_Notify_URL;
				type="992"; //支付宝
			}
			// 千网微信
			else if(payType.equals("qwwx")){
				parter=Constants.QWWX_MerNo ;
				md5key=Constants.QWWX_KEY;
				callbackurl=Constants.QWWX_Result_URL;
				hrefbackurl=Constants.QWWX_Notify_URL;
				type="993"; //微信
			}
			else {
				setError_info("支付失败，请重新支付");
				return ERROR;
			}
			orderid =  orderID;
			value = amount;
			attach=user.getLoginname();
			customer_ip=customerIP; //客户端ip
			
			//拼接参数加密串,按字母升序
			String tempStr="parter="+parter+"&type="+type+"&value="+value+"&orderid="+orderid+"&callbackurl="+callbackurl;
			System.out.println("签名字符串："+tempStr);
			sign =MD5. EkaPayBankMd5Sign(type,parter,value,orderid,callbackurl,md5key);//签名
			System.out.println(sign);
			
			getRequest().setAttribute("parter", parter);
			getRequest().setAttribute("type", type);
			getRequest().setAttribute("orderid", orderid);
			getRequest().setAttribute("callbackurl", callbackurl);
			getRequest().setAttribute("hrefbackurl", hrefbackurl);
			getRequest().setAttribute("value", value);
			getRequest().setAttribute("attach", attach);
			getRequest().setAttribute("payerIp", customer_ip);
			getRequest().setAttribute("sign",sign);
			getRequest().setAttribute("api_url",api_url);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	private String apiName;
	private String apiVersion;
	private String platformID;
	private String merchNo;
	private String orderNo;
	private String tradeDate;
	private String amt;
	private String merchUrl;
	private String merchParam;
	private String tradeSummary;
	private String signMsg;
	private String choosePayType;
	private String bankCode;
	private String overTime;
	private String customerIP;

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getPlatformID() {
		return platformID;
	}

	public void setPlatformID(String platformID) {
		this.platformID = platformID;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getMerchUrl() {
		return merchUrl;
	}

	public void setMerchUrl(String merchUrl) {
		this.merchUrl = merchUrl;
	}

	public String getMerchParam() {
		return merchParam;
	}

	public void setMerchParam(String merchParam) {
		this.merchParam = merchParam;
	}

	public String getTradeSummary() {
		return tradeSummary;
	}

	public void setTradeSummary(String tradeSummary) {
		this.tradeSummary = tradeSummary;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String getChoosePayType() {
		return choosePayType;
	}

	public void setChoosePayType(String choosePayType) {
		this.choosePayType = choosePayType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getCustomerIP() {
		return customerIP;
	}

	public void setCustomerIP(String customerIP) {
		this.customerIP = customerIP;
	}
	
	/****
	 * 微信秒存
	 * @return
	 */
	public String wxDeposit(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {    
			setError_info("[提示]你的登录已过期，请从首页重新登录");      
			return ERROR;
		}
		
		//代理不能使用在线支付
		if(!user.getRole().equals("MONEY_CUSTOMER")){
			setError_info("[提示]代理不能使用在线支付！");
			return ERROR;
		}
		
		try {
			//判断是否存在支付订单
			DepositOrder depositOrder = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getDepositPayorder", new Object[] {user.getLoginname()}, DepositOrder.class);
			if(depositOrder !=null){
				setWxValidaTeAmout(Double.toString(depositOrder.getAmount()));
				setWxValidaId(depositOrder.getDepositId());
			}
			Bankinfo wxBank = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getWxDepositPayorder", new Object[] {user.getLoginname()}, Bankinfo.class);
			if(StringUtils.isNotBlank(wxBank.getMassage())){
				setError_info(wxBank.getMassage());
				return ERROR;
			}
			getHttpSession().setAttribute("wxBank", wxBank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
}
