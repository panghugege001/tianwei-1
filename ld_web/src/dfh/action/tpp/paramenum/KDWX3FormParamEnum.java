package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 口带微信支付2 提交的資料欄位，順序將影響MD5加密結果
 * @author Kevin
 *
 */
public enum KDWX3FormParamEnum {
	P_UserId("1002401"),
	P_OrderId(""),
	P_CardId(""),
	P_CardPass(""),
	P_FaceValue(""),
	P_ChannelId("33"),  //以上加密
	P_Quantity("1"),
	P_Price(""),
	P_Description(""),
	P_Notic(""),
	P_Result_URL("http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.php"),
	P_Notify_URL("http://pay.zhongxingyue.cn/"),
	P_PostKey("");
	private Object value;
	
	KDWX3FormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(KDWX3FormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
