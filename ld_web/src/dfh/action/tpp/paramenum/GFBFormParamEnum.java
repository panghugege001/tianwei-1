package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 币付宝 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum GFBFormParamEnum {
	//网关版本号
	version("2.1"),
	//交易代码
	tranCode("8888"),
	//商户代码
	merchantID("0000010784"),
	//订单号
	merOrderNum(""),
	//交易金额
	tranAmt(""),
	//商户提取佣金金额
	feeAmt(""),
	//交易时间格式：YYYYMMDDHHMMSS
	tranDateTime(""),
	//商户前台通知地址
	frontMerUrl(""),
	//商户后台通知地址
	backgroundMerUrl("http://pay.jiekoue68.com:2112/gfb/callBackPay.aspx"),
	orderId(""),
	gopayOutOrderId(""),
	//用户浏览器 IP
	tranIP(""),
	
	respCode(""),
	//开启时间戳防钓鱼机制时必填 格式：YYYYMMDDHHMMSS 
	gopayServerTime(""),

	//字符集:1 GBK 2 UTF-8
	charset("2"),
	//网关语言版本:1 中文  2 英文
	language("1"),
	//报文加密方式:1 MD5 2 SHA
	signType("1"),
	//币种
	currencyType("156"),
	//国付宝转入账户
	virCardNoIn("0000000002000004007"),
	//订单是否允许重复提交:0不允许重复 1 允许重复 默认
	isRepeatSubmit("0"),
	//商品名称
	goodsName("168"),
	//商品详情
	goodsDetail(""),
	//买方姓名
	buyerName(""),
	//买方联系方式
	buyerContact(""),
	//商户备用信息字段
	merRemark1(""),
	//商户备用信息字段
	merRemark2(""),
	//密文串
	signValue(""),
	//银行代码
	bankCode(""),
	//用户类型取值范围:1（为个人支付）2（为企业支付）
	userType("1"),
	
	url("https://gateway.gopay.com.cn/Trans/WebClientAction.do");
	
	private Object value;
	
	GFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(GFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
