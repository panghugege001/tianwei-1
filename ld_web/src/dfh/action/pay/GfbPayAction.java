package dfh.action.pay;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.model.enums.CommonGfbEnum;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class GfbPayAction  extends SubActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(GfbPayAction.class);
	//网关版本号
	private final String version = "2.1";
	//字符集:1 GBK 2 UTF-8
	private final String charset = "2";
	//网关语言版本:1 中文  2 英文
	private final String language = "1";
	//报文加密方式:1 MD5 2 SHA
	private final String signType = "1";
	//交易代码
	private final String tranCode = "8888";
	//商户代码
	private String merchantID;
	//订单号
	private String merOrderNum;
	//交易金额
	private String tranAmt;
	//商户提取佣金金额
	private String feeAmt;
	//币种
	private final String currencyType = "156";
	//商户前台通知地址
	private String frontMerUrl;
	//商户后台通知地址
	private String backgroundMerUrl;
	//交易时间格式：YYYYMMDDHHMMSS
	private String tranDateTime;
	//国付宝转入账户
	private String virCardNoIn;
	//用户浏览器 IP
	private String tranIP;
	//订单是否允许重复提交:0不允许重复 1 允许重复 默认
	private final String isRepeatSubmit = "0";
	//商品名称
	private final String goodsName = "168";
	//商品详情
	private String goodsDetail;
	//买方姓名
	private String buyerName;
	//买方联系方式
	private String buyerContact;
	//商户备用信息字段
	private String merRemark1;
	//商户备用信息字段
	private String merRemark2;
	//密文串
	private String signValue;
	//开启时间戳防钓鱼机制时必填 格式：YYYYMMDDHHMMSS 
	private String gopayServerTime;
	//银行代码
	private String bankCode;
	//用户类型取值范围:1（为个人支付）2（为企业支付）
	private final String userType = "1";
	//商户识别码
	private String key;

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
			String payType = getRequest().getParameter("payType");
			if(!CommonGfbEnum.existKey(payType)){
				setError_info("[提示]非法参数");
				return ERROR;
			}
			CommonGfbEnum cmgfb = CommonGfbEnum.getCommonGfb(payType);
			//判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { cmgfb.getText() }, Const.class);
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
			if (StringUtils.isBlank(tranAmt)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(tranAmt) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(tranAmt) > 10000) {
					setError_info("[提示]存款金额不能超过10000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			// 支付银行
			if (StringUtils.isBlank(bankCode)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 获取商家订单号
			merOrderNum = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderGfbNo", new Object[] {user.getLoginname(), Double.parseDouble(tranAmt), payType}, String.class);
			if (StringUtils.isBlank(merOrderNum)) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			
			merchantID = cmgfb.getMerchantCode();
			feeAmt = "";
			tranDateTime = DateUtil.fmtyyyyMMddHHmmss(new Date());
			frontMerUrl = "";
			backgroundMerUrl = "http://pay.jiekoue68.com:2112/gfb/callBackPay.aspx";
			tranIP = getIpAddr();
			gopayServerTime = "";
			key = cmgfb.getMerchantKey();
			virCardNoIn = cmgfb.getVirCardNoIn();
			jumpUrl = cmgfb.getUrl();
			merRemark1 = payType;
			//组织加密明文
			String plain = "version=[" + version + "]tranCode=[" + tranCode + "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl + "]orderId=[]gopayOutOrderId=[]tranIP=[" + tranIP + "]respCode=[]gopayServerTime=[" + gopayServerTime + "]VerficationCode=[" + key + "]";
			signValue = DigestUtils.md5Hex(plain.getBytes("UTF-8"));
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	//回调
	public String callBackPay(){
		try {
			getRequest().setCharacterEncoding("UTF-8");
			String version = getRequest().getParameter("version");
			String charset = getRequest().getParameter("charset");
			String language = getRequest().getParameter("language");
			String signType = getRequest().getParameter("signType");
			String tranCode = getRequest().getParameter("tranCode");
			String merchantID = getRequest().getParameter("merchantID");
			String merOrderNum = getRequest().getParameter("merOrderNum");
			String tranAmt = getRequest().getParameter("tranAmt");
			String feeAmt = getRequest().getParameter("feeAmt");
			String frontMerUrl = getRequest().getParameter("frontMerUrl");
			String backgroundMerUrl =getRequest().getParameter("backgroundMerUrl");
			String tranDateTime = getRequest().getParameter("tranDateTime");
			String tranIP = getRequest().getParameter("tranIP");
			String respCode = getRequest().getParameter("respCode");
			String msgExt = getRequest().getParameter("msgExt");
			String orderId = getRequest().getParameter("orderId");
			String gopayOutOrderId = getRequest().getParameter("gopayOutOrderId");
			String bankCode = getRequest().getParameter("bankCode");
			String tranFinishTime = getRequest().getParameter("tranFinishTime");
			String merRemark1 =  getRequest().getParameter("merRemark1");
			String merRemark2 =  getRequest().getParameter("merRemark2");
			CommonGfbEnum cmgfb = CommonGfbEnum.getCommonGfbByMerchantCode(merchantID);
			String VerficationCode = cmgfb.getMerchantKey();
			String signValueFromGopay = getRequest().getParameter("signValue");
			
			log.info("国付宝回调开始merchantID："+merchantID);
			
			// 组织加密明文
			String plain = "version=[" + version + "]tranCode=[" + tranCode + "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl + "]orderId=[" + orderId + "]gopayOutOrderId=[" + gopayOutOrderId + "]tranIP=[" + tranIP + "]respCode=[" + respCode + "]gopayServerTime=[]VerficationCode=[" + VerficationCode + "]";
			String signValue = DigestUtils.md5Hex(plain.getBytes("UTF-8"));
			
			if(signValue.equals(signValueFromGopay)){
				if("0000".equals(respCode)){
					//处理账单信息
					String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "updateGfbOrderStatus", new Object[] { merOrderNum, Double.parseDouble(tranAmt), cmgfb.getCode() }, String.class);
					if (returnmsg == null) {
						log.info("交易成功,你支付的" + tranAmt + "元已经到账,此次交易的订单号为" + merOrderNum);
						writeText("RespCode=0000|JumpURL=");
					}
					System.out.println(returnmsg);
				}else{
					log.info("error---"+"respCode:"+respCode+",msgExt:"+msgExt);
				}
			}
			else{
				log.info("gfb签名验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		} 
		
	    return null ;
	}

	
	/**
	 * 获取国付宝服务器时间 用于时间戳
	 * @return 格式YYYYMMDDHHMMSS
	 */
	private String getGfbServerTime() {
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000); 
		GetMethod getMethod = new GetMethod("https://gateway.gopay.com.cn/time.do");
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"GBK");  
		// 执行getMethod
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(getMethod);			
			if (statusCode == HttpStatus.SC_OK){
				String respString = StringUtils.trim((new String(getMethod.getResponseBody(),"GBK")));
				return respString;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getMerOrderNum() {
		return merOrderNum;
	}

	public void setMerOrderNum(String merOrderNum) {
		this.merOrderNum = merOrderNum;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getFrontMerUrl() {
		return frontMerUrl;
	}

	public void setFrontMerUrl(String frontMerUrl) {
		this.frontMerUrl = frontMerUrl;
	}

	public String getBackgroundMerUrl() {
		return backgroundMerUrl;
	}

	public void setBackgroundMerUrl(String backgroundMerUrl) {
		this.backgroundMerUrl = backgroundMerUrl;
	}

	public String getTranDateTime() {
		return tranDateTime;
	}

	public void setTranDateTime(String tranDateTime) {
		this.tranDateTime = tranDateTime;
	}

	public String getVirCardNoIn() {
		return virCardNoIn;
	}

	public void setVirCardNoIn(String virCardNoIn) {
		this.virCardNoIn = virCardNoIn;
	}

	public String getTranIP() {
		return tranIP;
	}

	public void setTranIP(String tranIP) {
		this.tranIP = tranIP;
	}

	public String getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerContact() {
		return buyerContact;
	}

	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
	}

	public String getMerRemark1() {
		return merRemark1;
	}

	public void setMerRemark1(String merRemark1) {
		this.merRemark1 = merRemark1;
	}

	public String getMerRemark2() {
		return merRemark2;
	}

	public void setMerRemark2(String merRemark2) {
		this.merRemark2 = merRemark2;
	}

	public String getSignValue() {
		return signValue;
	}

	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}

	public String getGopayServerTime() {
		return gopayServerTime;
	}

	public void setGopayServerTime(String gopayServerTime) {
		this.gopayServerTime = gopayServerTime;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVersion() {
		return version;
	}

	public String getCharset() {
		return charset;
	}

	public String getLanguage() {
		return language;
	}

	public String getSignType() {
		return signType;
	}

	public String getTranCode() {
		return tranCode;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public String getIsRepeatSubmit() {
		return isRepeatSubmit;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getUserType() {
		return userType;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	

}
