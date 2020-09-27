package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 智付3 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum XBWXFormParamEnum {
	Version("V1.0"),
	MerchantCode("E02934"),
	OrderId(""),
	Amount(""),
	AsyNotifyUrl("http://pay.jiekoue68.com:2112/asp/payXbwxReturn.aspx"),
	SynNotifyUrl("http://gaoguangya.com/"),
	OrderDate(""),
	TradeIp("127.0.0.1"),
	PayCode("100040"),
	TokenKey("4mBvoq1dtL5eke9qrzy1fNwFGNSb4rXm"),
	Remark1(""),
	Remark2(""),
	url("https://gws.xbeionline.com/Gateway/XbeiPay"),
	SignValue("");
	
	private Object value;
	
	XBWXFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(XBWXFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
