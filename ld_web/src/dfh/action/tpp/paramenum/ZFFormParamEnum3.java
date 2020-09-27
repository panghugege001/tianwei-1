package dfh.action.tpp.paramenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 智付3 提交的資料欄位，順序將影響MD5加密結果
 * @author clare
 *
 */
public enum ZFFormParamEnum3 {
	// 支付银行
	bank_code(""),
	// 客户端IP（选填）
	client_ip(""),
	// 公用业务扩展参数（选填）
	extend_param(""),
	// 公用业务回传参数（选填）
	extra_return_param(""),
	// 参数编码字符集(必选)
	input_charset("UTF-8"),
	// 接口版本(必选)固定值:V3.0
	interface_version("V3.0"),
	// 商家号（必填）
	merchant_code("2030020119"),
	// 后台通知地址(必填)
	notify_url("http://pay.jiekoue68.com:2112/asp/payReturn.aspx"),
	// 定单金额（必填）
	order_amount(""),
	// 商家定单号(必填)
	order_no(""),
	// 商家定单时间(必填)
	order_time(""),
	// 商品编号(选填)
	product_code(""),
	// 商品描述（选填）
	product_desc(""),
	// 商品名称（必填）
	product_name(""),
	// 商品数量(选填)
	product_num(""),
	// 页面跳转同步通知地址(选填)
	return_url("http://www.joyomobile.com"),
	// 签名方式(必填)
	sign_type("MD5"),
	// 业务类型(必填)
	service_type("direct_pay"),
	// 商品展示地址(选填)
	show_url(""),
	// 签名(必填)
	sign(""),
	
	url("https://pay.dinpay.com/gateway?input_charset=UTF-8");
	
	private Object value;
	
	ZFFormParamEnum3(Object value){
		this.value = value;
	}
	
	public static Map<Enum,Object> toMap(){
		Map<Enum,Object> m = new HashMap<Enum,Object>();
		for(ZFFormParamEnum3 e : values()){
			m.put(e,e.getValue());
		}
		return m;
	}

	public Object getValue() {
		return value;
	}
}
