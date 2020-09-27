package dfh.action.tpp.paramenum;

import java.util.LinkedHashMap;
import java.util.Map;

import org.opensaml.common.SignableSAMLObject;

/**
 * 海尔支付 提交的資料欄位，順序將影響MD5加密結果
 * 
 * @author Kevin
 *
 */
public enum CommonHaierFormParamEnum {	
	
	_input_charset("UTF-8"),
	buyer_id("anonymous"),
	buyer_id_type("1"),
	go_cashier("Y"),
	memo(""),
	partner_id("200002058681"),    //*随专案变动
	pay_method(""),
	request_no(""),
	return_url("http://pay.zhongxingyue.cn/"), //同步通知地址（页面通知）
	service("create_instant_pay"),
	trade_list(""),
	version("1.0"),
	
	
	sign_type("MD5"),
	sign(""),
	url("https://mag.kjtpay.com/mag/gateway/receiveOrder.do");

	private Object value;

	CommonHaierFormParamEnum(Object value) {

		this.value = value;
	}

	public static Map<Enum, Object> toMap() {

		Map<Enum, Object> m = new LinkedHashMap<Enum, Object>();
		for (CommonHaierFormParamEnum e : values()) {
			m.put(e, e.getValue());
		}
		return m;
	}

	public Object getValue() {

		return value;
	}
}
