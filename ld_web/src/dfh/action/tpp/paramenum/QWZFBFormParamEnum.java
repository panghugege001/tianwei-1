package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

import dfh.utils.Constants;

/**
 *  提交的資料欄位，順序將影響MD5加密結果
 * @author KEVIN
 *
 */
public enum QWZFBFormParamEnum {
	
	parter(Constants.QWZFB_MerNo ),
	type("992"),
	value(""),
	orderid(""),
	callbackurl(Constants.QWZFB_Result_URL),
	hrefbackurl(Constants.QWZFB_Notify_URL),
	
	key(Constants.QWZFB_KEY),
	attach(""),
	payerIp(""),
	sign("");
	
	private Object values;
	
	QWZFBFormParamEnum(Object values){
		this.values = values;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(QWZFBFormParamEnum e : values()){
			m.put(e,e.getValues());
		}
		return m;
	}

	public Object getValues() {
		return values;
	}
}
