package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 讯联宝微信支付 提交的資料欄位，順序將影響MD5加密結果
 * @author Kevin
 *
 */
public enum XLBWYFormParamEnum {
	apiName("WEB_PAY_B2C"),
	apiVersion("1.0.0.0"),
	platformID("210000520010301"),
	merchNo("210000520010301"),
	orderNo(""),
	tradeDate(""),
	amt(""),
	merchUrl("http://pay.jiekoue68.com:2112/asp/payXlbWyReturn.aspx"),
	merchParam(""),
	tradeSummary("a"),
	//以上加密	
	url("http://trade.777pay.cn:8080/cgi-bin/netpayment/pay_gate.cgi"),
	signMsg(""),
	choosePayType("1"),
	bankCode(""),
	//以上form表单
	key("6bd669e55f2bc5ea1859da37e455a19d");
	private Object value;
	
	XLBWYFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(XLBWYFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
