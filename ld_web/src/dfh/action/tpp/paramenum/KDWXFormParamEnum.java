package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 口袋微信支付 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum KDWXFormParamEnum {
	P_UserId("1002397"),
	P_OrderId(""),
	Attr1(""),
	Attr2(""),
	P_Price(""),
	P_ChannelId("33"),
	P_FaceValue(""),
	P_Quantity("1"),
	P_Description(""),
	P_Notic(""),
	P_Result_URL("http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.aspx"),
	P_Notify_URL("http://pay.zhongxingyue.cn/"),
	P_PostKey("");
	
	private Object value;
	
	KDWXFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(KDWXFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
