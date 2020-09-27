package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 讯联宝微信支付 提交的資料欄位，順序將影響MD5加密結果
 * @author Kevin
 *
 */
public enum XLBZFBFormParamEnum {
	apiName("ZFB_PAY"),
	apiVersion("1.0.0.0"),
	platformID("210001440011109"),
	merchNo("210001440011109"),
	orderNo(""),
	tradeDate(""),
	amt(""),
	merchUrl("http://pay.jiekoue68.com:2112/asp/payXlbZfbReturn.aspx"),
	merchParam(""),
	tradeSummary("a"),
	overTime("40"),
	customerIP("127.0.0.1"),
	//以上加密	
	url("http://trade.777pay1.cn:8080/cgi-bin/netpayment/pay_gate.cgi"),
	signMsg(""),
	choosePayType("5"),
	bankCode(""),
	//以上form表单
	qrcode(""),
	key("c97f72752acc35fe4f3ab45c06a78b87");
	private Object value;
	
	XLBZFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(XLBZFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
