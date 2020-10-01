package dfh.action.tpp.paramenum;

import java.util.LinkedHashMap;
import java.util.Map;

import dfh.utils.Constants;

/**
 * 
 */
public enum YFZFBFormParamEnum {
	BANK_CODE("ALIPAY"),
	CUSTOMER_IP(""),
	CUSTOMER_PHONE(""),
	INPUT_CHARSET("UTF-8"),
	MER_NO(Constants.YFZFB_MerNo),
	NOTIFY_URL(Constants.YFZFB_Result_URL),
	ORDER_AMOUNT(""),
	ORDER_NO(""),
	PRODUCT_NAME("e68"), 
	PRODUCT_NUM(""), 
	RECEIVE_ADDRESS(""),
	REFERER("xxx.com"),
	RETURN_PARAMS(""),
	RETURN_URL(Constants.YFZFB_Notify_URL),
	VERSION("V2.0"),
	apiUrl(Constants. YFZF_apiUrl),
	KEY(Constants.YFZFB_KEY),   //同mey_key
	//以上加密
	SIGN("");
	//以上form表单
	
	private Object value;

	YFZFBFormParamEnum(Object value) {

		this.value = value;
	}

	public static Map<Enum, Object> toMap() {

		Map<Enum, Object> m = new LinkedHashMap<Enum, Object>();
		for (YFZFBFormParamEnum e : values()) {
			m.put(e, e.getValue());
		}
		return m;
	}

	public Object getValue() {

		return value;
	}
}