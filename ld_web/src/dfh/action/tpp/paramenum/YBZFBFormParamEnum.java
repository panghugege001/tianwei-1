package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

import dfh.utils.Constants;

/**
 *  提交的資料欄位，順序將影響MD5加密結果
 * @author KEVIN
 *
 */
public enum YBZFBFormParamEnum {
	
	partner(Constants.YBZFB_MerNo),
	banktype("ALIPAY"),
	paymoney(""),
	ordernumber(""),
	callbackurl(Constants.YBZFB_Result_URL),
	
	hrefbackurl(Constants.YBZFB_Notify_URL),
	attach(""),
	sign(Constants.YBZFB_KEY),
	url("http://wytj.9vpay.com/PayBank.aspx");
	
	
	private Object value;
	
	YBZFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(YBZFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
