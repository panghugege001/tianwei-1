package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 *  提交的資料欄位，順序將影響MD5加密結果
 * @author KEVIN
 *
 */
public enum XBZFBFormParamEnum {
	Version("V1.0"),
	MerchantCode("E03148"),
	OrderId(""),
	Amount(""),
	AsyNotifyUrl("http://pay.jiekoue68.com:2112/asp/payXbZfbReturn.aspx"),
	SynNotifyUrl("http://www.yuxinbaokeji.com/"),
	OrderDate(""),
	TradeIp("127.0.0.1"),
	PayCode("100067"),
	TokenKey("9eHznTmbwqYn4hBxd7PGcaEkLKFc8XBH"),
	Remark1(""),
	Remark2(""),
	url("https://gws.xbeionline.com/Gateway/XbeiPay"),
	SignValue("");
	
	private Object value;
	
	XBZFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(XBZFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
