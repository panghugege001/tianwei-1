package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 匯潮 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum HCFormParamEnum {
	MerNo(""),
	BillNo(""),
	Amount(""),
	ReturnURL(""),
	AdviceURL(""),
	SignInfo(""),
	Remark(""),
	defaultBankNumber(""),
	payType(""),
	orderTime(""),
	products("");
	
	private Object value;
	
	HCFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(HCFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
