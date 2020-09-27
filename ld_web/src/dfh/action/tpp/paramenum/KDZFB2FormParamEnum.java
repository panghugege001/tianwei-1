package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

import dfh.utils.Constants;

/**
 *  提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum KDZFB2FormParamEnum {
	
	
	P_UserId(Constants.KDZFB2_MerNo),
	P_OrderId(""),
	Attr1(""),
	Attr2(""),
	P_Price(""),
	P_ChannelId("2"),
	P_FaceValue(""),
	P_Quantity("1"),
	P_Description(""),
	P_Notic(""),
	P_Result_URL(Constants.KDZFB2_Result_URL),
	P_Notify_URL(Constants.KDZFB2_Notify_URL),
	apiUrl("https://api.duqee.com/pay/KDBank.aspx"),
	P_PostKey("");
	
	
	
	
	private Object value;
	
	KDZFB2FormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(KDZFB2FormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
