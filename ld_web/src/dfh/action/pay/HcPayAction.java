package dfh.action.pay;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.HcResponseCode;
import dfh.utils.StringUtil;

public class HcPayAction  extends SubActionSupport{
	private static Logger log = Logger.getLogger(HcPayAction.class);
	
	
	private String merNo;
	private String key;
	private String returnURL;
	private String adviceURL;
	private String billNo ; 
	private String amount ;
	private String signInfo ;
	private String orderTime ;  //如19880101215415  YYYYMMDDHHMMSS
	private String defaultBankNumber = "ICBC" ;
	private String Remark ;
	private String products ;
	private String payfly; //支付通道区分
	private String payType;
	
	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}


	// 错误信息
	private String error_info;
	
	//提交支付 汇潮一麻袋
	public String commitHcYmdPay(){
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
			String payName = payfly.equals("hckj")?"汇潮":"汇潮网银";
			
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { payName }, Const.class);
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
                //验证
				if (Double.parseDouble(amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(amount) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			// 支付银行
			if (StringUtils.isBlank(defaultBankNumber)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 获取商家订单号
			billNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHCYmdNo", new Object[] {user.getLoginname(),Double.parseDouble(amount),payfly}, String.class);
			if (StringUtils.isBlank(billNo)) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			
			// 汇潮快捷支付
			if(payfly.equals("hckj")){
				merNo=Constants.HC_MerNo ;
				key=Constants.HC_Key;
			    returnURL=Constants.ReturnURL;
				adviceURL=Constants.AdviceURL;
				orderTime = DateUtil.fmtyyyyMMddHHmmss(new Date()); 
				payType="noCard";
				defaultBankNumber="";
			}
			//汇潮网银支付
			else if(payfly.equals("hcwy")){
				 merNo=Constants.MerNo;
				 key=Constants.MD5key;
			     returnURL=Constants.ReturnURLYmd;
				 adviceURL=Constants.AdviceURLYmd;
				 orderTime = DateUtil.fmtyyyyMMddHHmmss(new Date());
				 payType="B2CDebit";
			}
			
			String md5src = "MerNo="+merNo +"&"+ "BillNo="+billNo +"&"+ "Amount="+amount +"&"+"OrderTime="+orderTime+"&"+ "ReturnURL="+returnURL +"&"+"AdviceURL="+adviceURL+"&"+key ;
			System.out.println(md5src);
			signInfo = DigestUtils.md5Hex(md5src).toUpperCase();//MD5检验结果
			Remark = "" ;  //[选填]
			products = "e68" ;//[选填]
			
			return SUCCESS ;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}
	
	// 汇潮 回调
	public String callBackYmdPay(){
		try {
			System.out.println("===================汇潮网银支付回调===================");
			getRequest().setCharacterEncoding("UTF-8");
			String MerNo =getRequest().getParameter("MerNo");
			String BillNo = getRequest().getParameter("BillNo");
			String OrderNo = getRequest().getParameter("OrderNo");
			String Amount = getRequest().getParameter("Amount");
            String tradeOrder = getRequest().getParameter("tradeOrder");
			String Succeed = getRequest().getParameter("Succeed");
			String Result = getRequest().getParameter("Result");
			String SignInfo = getRequest().getParameter("SignInfo");
			String MD5key = null;
			log.info("商户号:"+MerNo);
			//快捷支付
            if(MerNo.equals(Constants.HC_MerNo)){
            	MD5key = Constants.HC_Key;
			}
            //网银支付
            else if(MerNo.equals(Constants.MerNo)){
				MD5key = Constants.MD5key;
			}
			else{
				log.error("商户号不正确");
				return "商户号不正确";
			}
            
			String md5src = "MerNo="+MerNo+"&"+"BillNo="+BillNo+"&"+"OrderNo="+OrderNo+"&"+"Amount="+Amount+"&"+"Succeed="+Succeed+"&"+MD5key;
			String md5sign; //MD5加密后的字符串
			log.info("md5src:"+md5src+"\n") ;
			md5sign = DigestUtils.md5Hex(md5src).toUpperCase();//MD5检验结果
			log.info("md5sign:"+md5sign+"\n") ;
			if(SignInfo.equals(md5sign)){
				if(StringUtils.isNotEmpty(Succeed) && StringUtil.isNumeric(Succeed)){
					Integer responseCode = Integer.parseInt(Succeed) ;
					if(responseCode == HcResponseCode.SUCCESS.value()){
							//处理账单信息
						String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "updateHCYmdOrderStatus", new Object[] { BillNo, Double.parseDouble(Amount),MerNo}, String.class);
						if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
							log.info(HcResponseCode.valueOf(responseCode)+"交易成功,你支付的" + Amount + "元已经到账,此次交易的订单号为" + BillNo);
							writeText("ok");
						}else{
							writeText("fail");
						}
						log.info(returnmsg);
					}else{
						log.info("出现错误："+Succeed+"-->"+HcResponseCode.valueOf(responseCode));
						writeText("fail");
					}
				}else{
					log.info("error:"+Succeed);
					writeText("error:"+Succeed);
				}
			}else{
				log.info("签名验证失败");
				writeText("签名验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		} 
	    return null ;
	}
	


	public String getOrderTime() {
		return orderTime;
	}


	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}


	public String getDefaultBankNumber() {
		return defaultBankNumber;
	}


	public void setDefaultBankNumber(String defaultBankNumber) {
		this.defaultBankNumber = defaultBankNumber;
	}


	public String getRemark() {
		return Remark;
	}


	public void setRemark(String remark) {
		Remark = remark;
	}


	public String getProducts() {
		return products;
	}


	public void setProducts(String products) {
		this.products = products;
	}

	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getAdviceURL() {
		return adviceURL;
	}

	public void setAdviceURL(String adviceURL) {
		this.adviceURL = adviceURL;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	public String getPayfly() {
		return payfly;
	}

	public void setPayfly(String payfly) {
		this.payfly = payfly;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	
}
