package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 寰訊 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum IPSFormParamEnum {
	FailUrl(""),
	Merchanturl(""),
	Amount(""),
	Bankco(""),
	Attach("");
	
	private Object value;
	
	IPSFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(IPSFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
