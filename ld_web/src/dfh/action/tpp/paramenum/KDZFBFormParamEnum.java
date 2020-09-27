package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 智付3 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum KDZFBFormParamEnum {
	P_UserId("1001242"),
	P_OrderId(""),
	P_Space1(""),
	P_Space2(""),
	P_Price(""),
	P_ChannelId("2"),
	P_Quantity("1"),
	P_Description(""),
	P_Notic(""),
	P_FaceValue(""),
	P_Result_URL("http://pay.jiekoue68.com:2112/asp/payKdZfReturn.aspx"),
	P_Notify_URL("http://pay.zhongxingyue.cn/"),
	P_PostKey("");
	
	private Object value;
	
	KDZFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(KDZFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
