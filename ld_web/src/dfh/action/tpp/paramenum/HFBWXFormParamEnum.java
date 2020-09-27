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
public enum HFBWXFormParamEnum {	
	
	version("1"),
	agent_id("2067257"),  //随专案变动
	agent_bill_id(""),
	agent_bill_time(""),
	pay_type("30"),
	pay_amt(""),
	notify_url("http://pay.jiekoue68.com:2112/asp/payHhbWxZfReturn.aspx"), //随专案变动 
	return_url("http://pay.zhongxingyue.cn/"), //同步通知地址（页面通知）
	user_ip(""),
	key("834499D4528140A88BE8478B"), //随专案变动
	//----以上加密
	goods_name("weixin_payment"),
	goods_num("1"),
	remark(""),
	goods_note(""),
	sign("");

	private Object value;

	HFBWXFormParamEnum(Object value) {

		this.value = value;
	}

	public static Map<Enum, Object> toMap() {

		Map<Enum, Object> m = new LinkedHashMap<Enum, Object>();
		for (HFBWXFormParamEnum e : values()) {
			m.put(e, e.getValue());
		}
		return m;
	}

	public Object getValue() {

		return value;
	}
}
