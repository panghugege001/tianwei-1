package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 讯联宝微信支付 提交的資料欄位，順序將影響MD5加密結果
 * @author Kevin
 *
 */
public enum XLBWXFormParamEnum {
	apiName("WECHAT_PAY"),
	apiVersion("1.0.0.0"),
	platformID("210001520011131"),
	merchNo("210001520011131"),
	orderNo(""),
	tradeDate(""),
	amt(""),
	merchUrl("http://pay.jiekoue68.com:2112/asp/payXlbReturn.aspx"),
	merchParam(""),
	tradeSummary("a"),
	overTime("45"),
	customerIP("127.0.0.1"),
	//以上加密	
	url("http://trade.777pay1.cn:8080/cgi-bin/netpayment/pay_gate.cgi"),
	signMsg(""),
	choosePayType("5"),
	bankCode(""),
	//以上form表单
	qrcode(""),
	key("5020b2c37008b436ae330f9fb567a139");
	private Object value;
	
	XLBWXFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(XLBWXFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
