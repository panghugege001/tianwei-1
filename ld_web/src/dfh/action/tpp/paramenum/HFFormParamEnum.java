package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 匯付 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum HFFormParamEnum {
	Version("10"),
	CmdId("Buy"),
	MerId("872720"),
	OrdId(""),
	OrdAmt(""),
	CurCode("RMB"),
	Pid(""),
	RetUrl("http://www.talkgirl.info/"),
	MerPriv(""),
	GateId(""),
	UsrMp(""),
	DivDetails(""),
	OrderType(""),
	PayUsrId(""),
	PnrNum(""),
	BgRetUrl("http://pay.jiekoue68.com:2112/asp/payHfReturn.aspx"),
	IsBalance(""),
	RequestDomain(""),
	OrderTime(""),
	ValidTime(""),
	ValidIp(""),
	ChkValue("");
	
	private Object value;
	
	HFFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(HFFormParamEnum e : HFFormParamEnum.values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}

}
