package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

import dfh.utils.Constants;

/**
 *  提交的資料欄位，順序將影響MD5加密結果
 * @author KEVIN
 *
 */
public enum QWWXFormParamEnum {
	
	parter(Constants.QWWX_MerNo ),
	type("993"),
	value(""),
	orderid(""),
	callbackurl(Constants.QWWX_Result_URL),
	hrefbackurl(Constants.QWWX_Notify_URL),
	
	key(Constants.QWWX_KEY),
	attach(""),
	payerIp(""),
	sign("");
	
	private Object values;
	
	QWWXFormParamEnum(Object values){
		this.values = values;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(QWWXFormParamEnum e : values()){
			m.put(e,e.getValues());
		}
		return m;
	}

	public Object getValues() {
		return values;
	}
}
