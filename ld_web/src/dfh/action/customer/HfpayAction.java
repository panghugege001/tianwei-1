package dfh.action.customer;

import chinapnr2.SecureLink;
import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class HfpayAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;
	// 版本号 定长2位 目前固定为10。如版本升级，能向前兼容。
	private String Version;
	// 每一种消息类型代表一种交易，具体交易类型代码见HHTUTU接口交易类型定义UUTTHH
	private String CmdId;
	// 由钱管家系统分配的6位数字代码，商户的唯一标识
	private String MerId;
	// 订单号 由商户的系统生成，必须保证唯一。失败的订单2天内可以被重复支付。
	private String OrdId;
	// 订单金额 订单的总金额，应大于或等于各分账金额总和
	private String OrdAmt;
	// 固定填写RMB
	private String CurCode;
	// 商品编号 由商户系统产生，含有商品的信息，不作为订单的唯一标识
	private String Pid;
	// 交易完成后,钱管家系统把交易结果通过页面方式，发送到该地址上
	private String RetUrl;
	// 商户私有域 为商户的自定义字段，该字段在交易完成后由钱管家系统原样返回
	private String MerPriv;
	// 银行ID
	// 支付的银行号，具体参见HHTUTU标准数据附录UUTTHH。强烈建议商户置该域为空，否则新增钱管家系统银行支付渠道，商户系统也需要作相应改动。如果为空，则银行的选择页面在钱管家系统完成；否则，跳过银行选择页面，直接跳转到网银系统
	private String GateId;
	// 购买者手机号 钱管家系统提供按照手机号查询订单的功能
	private String UsrMp;
	// 分账明细 对于支付：如果商户仅仅实现支付功能，无分帐需求，该域为空；否则填写的格式为
	// “角色:用户名:金额;”，如果多笔分帐时使用分号分割，其中角色固定填写Agent，用户名为商户下属的用户，例如订单金额为1000元，分账明细为：
	// Agent:user1:50.00;Agent:user2:800.00表示分给user1，50元，分给user2，800元，剩余的150元分给商户本身。
	private String DivDetails;
	// 订单类型 由商户系统产生，含有商品的信息，不作为订单的唯一标识
	private String OrderType;
	// 付费用户号 对于航空公司,该域为必填。
	private String PayUsrId;
	// Pnr号 Pnr号
	private String PnrNum;
	// 订单支付时，商户后台应答地址
	private String BgRetUrl;
	// 是否结算 ‘Y’ – 结算 ‘N’ – 不结算
	private String IsBalance;
	// 请求域名 用于防钓鱼时，商户上传的请求域名参数。参数示例www.chinapnr.com
	// ,也就是仅需要根域名,不需要http://和之后的详细页面及参数信息
	private String RequestDomain;
	// 订单时间 用于防钓鱼时，商户上传的订单时间，格式yyyyMMddHHmmss
	private String OrderTime;
	// 用于防钓鱼时，商户上传的有效时间，单位分钟
	private String ValidTime;
	// 用于防钓鱼时，商户上传的有效IP
	private String ValidIp;
	// 各接口所列有的请求参数和返回参数如无个别说明，都需要参与签名，参与签名的数据体为：按照每个接口中包含的参数值（不包含参数名）的顺序（按接口表格中从左到右，从上到下的顺序）进行字符串相加。如果参数为可选项并且为空，则该参数值不参与签名。
	private String ChkValue;
	// 错误信息
	private String error_info;

	public String hfRedirect() {
		try {
			// 检测用户是否登录
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				setError_info("[提示]登录已过期，请从首页重新登录");
				return ERROR;
			}
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "汇付" }, Const.class);
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
			if (!isNotNullAndEmpty(OrdAmt)) {
				setError_info("[提示]存款额度不能为空！");
				return ERROR;
			}
			try {
				if (Double.parseDouble(OrdAmt) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					return ERROR;
				}
				if (Double.parseDouble(OrdAmt) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return ERROR;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(GateId)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 支付银行
			if (!isNotNullAndEmpty(GateId)) {
				setError_info("[提示]支付银行不能为空！");
				return ERROR;
			}
			// 获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderHfNo", new Object[] {user.getLoginname(),Double.parseDouble(OrdAmt)}, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return ERROR;
			}
			/** 必须* */
			Version = "10";
			/** 必须* */
			CmdId = "Buy";
			/** 必须* */
			MerId = "872720";
			/** 必须* */
			OrdId = orderNo;
			/** 必须* */
			CurCode = "RMB";
			/** 可选* */
			Pid = "";
			/** 必须* */
			RetUrl = "http://www.talkgirl.info/";
			/** 可选* */
			MerPriv = user.getLoginname();
			/** 可选* */
			UsrMp = "";
			/** 可选* */
			DivDetails = "";
			/** 可选* */
			OrderType = "";
			/** 可选* */
			PayUsrId = "";
			/** 可选* */
			PnrNum = "";
			/** 必须* */
			BgRetUrl = "http://pay.jiekoue68.com:2112/asp/payHfReturn.aspx";
			
			/** 可选* */
			IsBalance = "";
			/** 可选* */
			RequestDomain = "";
			/** 可选* */
			OrderTime = "";
			/** 可选* */
			ValidTime = "";
			/** 可选* */
			ValidIp = "";
			/**必须保留两位小数**/
			OrdAmt=OrdAmt+".00";
			/** 必须* */
			ChkValue="";
			String merData = Version + CmdId + MerId + OrdId + OrdAmt + CurCode + Pid + RetUrl + MerPriv + GateId + UsrMp + DivDetails + OrderType + PayUsrId + PnrNum + BgRetUrl + IsBalance + RequestDomain + OrderTime + ValidTime + ValidIp + ChkValue;
			SecureLink sl = new SecureLink();
			String path = this.getClass().getResource("").toURI().getPath() + "MerPrK872720.key";
			int ret = sl.SignMsg(MerId, path, merData);
			if (ret != 0) {
				setError_info("签名验证失败!");
				return ERROR;
			}
			/** 必须* */
			ChkValue = sl.getChkValue();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return ERROR;
		}
	}

	/**
	 * 获取签名的数据
	 * <li>MerData = Version + MerId + OrdId + TransAmt + TransType + GateId +MerPriv + RetUrl
	 * @param merId
	 * @param merData
	 * @return
	 */
	public static String getSignValue(String merId, String MerKeyFile, String merData) {
		SecureLink sl = new SecureLink();
		//LOG.info("私钥路径:{}", MerKeyFile);
		int ret = sl.SignMsg(merId, MerKeyFile, merData);
		if (ret != 0) {
			return null;
		}
		return sl.getChkValue();
	}

	/**
	 * 验证返回签名的数据
	 * <li>merData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode + Pid +OrdId + MerPriv + RetType + DivDetails + GateId
	 * @param merData
	 * @param chkValue
	 * @return
	 */
	public static boolean veriSignMsg(String merData, String MerKeyFile, String chkValue) {
		SecureLink sl = new SecureLink();
		int ret = sl.VeriSignMsg(MerKeyFile, merData, chkValue);
		if (ret != 0) {
			return false;
		}
		return true;
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

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getCmdId() {
		return CmdId;
	}

	public void setCmdId(String cmdId) {
		CmdId = cmdId;
	}

	public String getMerId() {
		return MerId;
	}

	public void setMerId(String merId) {
		MerId = merId;
	}

	public String getOrdId() {
		return OrdId;
	}

	public void setOrdId(String ordId) {
		OrdId = ordId;
	}

	public String getOrdAmt() {
		return OrdAmt;
	}

	public void setOrdAmt(String ordAmt) {
		OrdAmt = ordAmt;
	}

	public String getCurCode() {
		return CurCode;
	}

	public void setCurCode(String curCode) {
		CurCode = curCode;
	}

	public String getPid() {
		return Pid;
	}

	public void setPid(String pid) {
		Pid = pid;
	}

	public String getRetUrl() {
		return RetUrl;
	}

	public void setRetUrl(String retUrl) {
		RetUrl = retUrl;
	}

	public String getMerPriv() {
		return MerPriv;
	}

	public void setMerPriv(String merPriv) {
		MerPriv = merPriv;
	}

	public String getGateId() {
		return GateId;
	}

	public void setGateId(String gateId) {
		GateId = gateId;
	}

	public String getUsrMp() {
		return UsrMp;
	}

	public void setUsrMp(String usrMp) {
		UsrMp = usrMp;
	}

	public String getDivDetails() {
		return DivDetails;
	}

	public void setDivDetails(String divDetails) {
		DivDetails = divDetails;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public String getPayUsrId() {
		return PayUsrId;
	}

	public void setPayUsrId(String payUsrId) {
		PayUsrId = payUsrId;
	}

	public String getPnrNum() {
		return PnrNum;
	}

	public void setPnrNum(String pnrNum) {
		PnrNum = pnrNum;
	}

	public String getBgRetUrl() {
		return BgRetUrl;
	}

	public void setBgRetUrl(String bgRetUrl) {
		BgRetUrl = bgRetUrl;
	}

	public String getIsBalance() {
		return IsBalance;
	}

	public void setIsBalance(String isBalance) {
		IsBalance = isBalance;
	}

	public String getRequestDomain() {
		return RequestDomain;
	}

	public void setRequestDomain(String requestDomain) {
		RequestDomain = requestDomain;
	}

	public String getOrderTime() {
		return OrderTime;
	}

	public void setOrderTime(String orderTime) {
		OrderTime = orderTime;
	}

	public String getValidTime() {
		return ValidTime;
	}

	public void setValidTime(String validTime) {
		ValidTime = validTime;
	}

	public String getValidIp() {
		return ValidIp;
	}

	public void setValidIp(String validIp) {
		ValidIp = validIp;
	}

	public String getChkValue() {
		return ChkValue;
	}

	public void setChkValue(String chkValue) {
		ChkValue = chkValue;
	}

	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
}
