package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 智付3 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum LFWXFormParamEnum {
	amount(""),
	apiCode("directPay"),
	buyer("aaa"),
	buyerContact("asd@asd.com"),
	buyerContactType("email"),
	clientIP("127.0.0.1"),
	inputCharset("UTF-8"),
	interfaceCode("WALLET_TENCENT_QRCODE"),
	notifyURL("http://pay.168.tl/asp/payLfwxReturn.aspx"),
	outOrderId(""),
	partner("8615339796"),
	paymentType("ALL"),
	redirectURL("http://pay.168.tl/"),
	retryFalg("FALSE"),
	returnParam(""),
	signType("MD5"),
	submitTime(""),
	timeout("6H"),
	versionCode("1.0"),
	sign("");
	
	private Object value;
	
	LFWXFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(LFWXFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
