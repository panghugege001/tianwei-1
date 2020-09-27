package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 币付宝 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum BFBFormParamEnum {
	url("http://i.5dd.com/pay.api"),
	//指令类型  网银-1，卡类-2
	p1_md("1"),
	//商户订单号
	p2_xn("xn123456789"),
	//商户ID
	p3_bn("3852911032"),
	//支付方式ID  银行ID
	p4_pd(""),
	//产品名称
	p5_name("168"),
	//支付金额
	p6_amount(""),
	//币种
	p7_cr("1"),
	//扩展信息
	p8_ex("ex"),
	//通知地址
	p9_url("http://pay.jiekoue68.com:2112/bfb/callBackPay.aspx"),
	//是否通知 不通知=0，通知=1
	p10_reply("1"),
	//调用模式    0 返回充值地址，由商户负责跳转     1 显示币付宝充值界面,跳转到充值    2 不显示币付宝充值界面 直接跳转到
	p11_mode("2"),
	//版本号
	p12_ver("1"),
	//签名值
	sign("");
	
	
	private Object value;
	
	BFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(BFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
