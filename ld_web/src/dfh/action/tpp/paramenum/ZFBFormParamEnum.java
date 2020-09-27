package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝  提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum ZFBFormParamEnum {
	// 支付银行
	_input_charset(""),
	// 客户端IP（选填）
	extra_common_param(""),
	// 公用业务扩展参数（选填）
	notify_url(""),
	// 公用业务回传参数（选填）
	out_trade_no(""),
	// 参数编码字符集(必选)
	partner(""),
	// 接口版本(必选)固定值:V3.0
	payment_type(""),
	// 商家号（必填）
	paymethod(""),
	// 后台通知地址(必填)
	return_url(""),
	// 定单金额（必填）
	seller_email(""),
	// 商家定单号(必填)
	service(""),
	// 商家定单时间(必填)
	subject(""),
	// 商品编号(选填)
	total_fee("0.00"),
	// 商品描述（选填）
	sign(""),
	// 商品名称（必填）
	sign_type("");
	
	private Object value;
	
	ZFBFormParamEnum(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(ZFBFormParamEnum e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
