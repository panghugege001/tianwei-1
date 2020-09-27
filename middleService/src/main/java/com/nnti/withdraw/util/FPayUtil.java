package com.nnti.withdraw.util;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.utils.HttpUtil;

import net.sf.json.JSONObject;


/**
 * 秒付存款2 自动转账API
 * 
 * @author
 * 
 */
public class FPayUtil {
	private static Logger log = Logger.getLogger(FPayUtil.class);
	
	
	private static Map<String, HashMap<String, String>> classMap = new HashMap<String, HashMap<String, String>>();
	static {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("apikey", "0ebe5dcb15fe407fa0187afa48af3d98");
		map.put("secretkey","0ebe5dcb15fe407fa0187afa48af3d980ebe5dcb15fe407fa0187afa48af3d98");
		map.put("apiUrl", "http://mfb-jiekou01.vip:8091/");
		map.put("version","1.0");
		// 测试->秒付宝
		classMap.put("wstest", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "fcd0f5fb7f6b4699bdbce90f2652a572");
		map.put("secretkey","yn0zce5dia5jx4ha13k1pe34oj6ozgbmudot950q9k04cc3g2x1nxf9jjwas8he2");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 武松->秒付宝
		classMap.put("ws", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "c4fb000f647a43e7a1d1f046c03e54ec");
		map.put("secretkey","cgofbs0xcza6stgdrpitjdue1sw5475jjnw4m2i4l8thcydmajddrobzl9ejwus4");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 齐乐-> 秒付宝
		classMap.put("ql", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "2166db6129394209afb1f088eded6156");
		map.put("secretkey","sc5slmx4y8xvdnrcohmqhy96ada04gcvr4p9opjcny9zwkssmmcs8s3t3zh8cwzi");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 优乐-> 秒付宝
		classMap.put("ul", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "f7d19051733a4188bbe44b37878cf98c");
		map.put("secretkey","dzbccck56u3686ykmlbtycn9u3m4bdxp7rse7fz4m65xmgm651di43a0b2h8pgus");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 千亿-> 秒付宝
		classMap.put("qy", map);
		
		
		map = new HashMap<String, String>();
		map.put("apikey", "61d6675dd75d4eceb70f0a1b9ab23e04");
		map.put("secretkey","51c7tl35x5pddzybee1xmwzrvn1xkb836iayxl9fxefscn816q70u1a92lc6k6ll");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 龙八-> 秒付宝
		classMap.put("yl", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "ec8419b98dcc4472835b86c558f08e72");
		map.put("secretkey","rafaqekcjc9afzvao4dymgqbweaqr0dim6wgmpn74cf84bdb9v314tnbk3db4ulj");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 梦之城-> 秒付宝
		classMap.put("mzc", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "79e1a7f2e4a142bea3c1bc815c3b9b7a");
		map.put("secretkey","pl95frg9lya0hey1usx1x2s7qo7awp3ic9qzxb96ougq9b140zckqbbns2fqj5dw"); 
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 亚虎-> 秒付宝
		classMap.put("dy1", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "4cbb609e4dfc4802953aa21c0eef8864");
		map.put("secretkey","uleirwuqhorqsj2fo9hgnwx92rlr70t1ius9uqcjm4qsac9vcqdwduwxo4g0b2jh");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 乐虎-> 秒付宝
		classMap.put("lh", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "c2ce11739d8742e59b93663bac2d1a99");
		map.put("secretkey","35a6fq5x4zi6v7re82dajiz8j63nzs19i3t2axz7jmlenwywwrep0c2k51n8mhxb");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		// 优发-> 秒付宝
		classMap.put("ufa", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "59a15347bb7341d8906e2c0aeb4ff801");
		map.put("secretkey","y9c6bl8to4ls6k2np9utb5dqywlq0a97l39f9swx00cgnd9n8c6fp1wswl8f0hte");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		//龙虎-> 秒付宝
		classMap.put("loh", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "8acee30d19524ff0b04ca0ef5be865f9");
		map.put("secretkey","l9h6li1k4t3o1i2zn9rqdc1c9fmskhwpacvx0k7qucjhk98aliqtphv885w6w8zn");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		//龙都-> 秒付宝
		classMap.put("ld", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "78be76df73554e8c979e714217b42f82");
		map.put("secretkey","w80cai4c1fzsau290g8uh7r1ca4tmypv9viv12zxz5pj3dqiib6506zogz0zebp3");
		map.put("apiUrl", "http://69.172.86.14:10213/");
		map.put("version","1.0");
		//尊宝-> 秒付宝
		classMap.put("zb", map);
		
	} 
	

	public static Map<String, HashMap<String, String>> getClassMap() {
		return classMap;
	}


	public static void setClassMap(Map<String, HashMap<String, String>> classMap) {
		FPayUtil.classMap = classMap;
	}
	
	
	


	/***
	 * 
	 * 秒付存款2 添加订单 Description
	 * 
	 * @param pno
	 * @param operator
	 * @param ip
	 * @param loginName
	 * @return
	 * @throws BusinessException 
	 */
	@SuppressWarnings("finally")
	public static String api_add_order(String orderNo, String amount, String bankName, String bankAddress,String accName, String cardNumber,String product,String depositType) throws BusinessException {
		log.info(orderNo+"|"+amount+"|"+ bankName+"|"+ bankAddress+"|"+accName+"|"+ cardNumber+"|"+product+"|"+depositType);
		HashMap<String , String> map =  classMap.get(product);
		String apikey = map.get("apikey");
		String secretkey = map.get("secretkey");
		String apiUrl = map.get("apiUrl");
		log.info("秒付宝接口："+apikey+"|"+secretkey+"|"+apiUrl);
		
		String url = "";
		String urlEncrypt = "";

		JSONObject data = new JSONObject();
		data.put("method", "add_order");
		data.put("apikey", apikey);
		JSONObject payload = new JSONObject();
		payload.put("order_no", orderNo); // 单号 qy_woodytest_123123
		payload.put("bank_name", bankName); // 银行名字
		payload.put("bank_address", bankAddress); // 银行地址
		payload.put("account_name", accName.trim()); // 银行账户
		payload.put("card_number", cardNumber.trim()); // 银行卡号
		payload.put("amount", amount); // 银行额度
		payload.put("deposit_type", depositType); // 付款类型

		url = HttpUtil.getURLJson("", payload).substring(1);
		urlEncrypt = SHA.sign(url + secretkey, "SHA-256");
		data.put("sign", urlEncrypt);
		data.put("data", payload.toString());
		
		log.info("url："+url);

		PostMethod postMethod = new PostMethod();
		String phpHtml = "";
		try {

			HttpClient httpClient = HttpUtil.createHttpClient();
			postMethod = new PostMethod(apiUrl);
			postMethod.addRequestHeader("content-type", "application/json");
			RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
			postMethod.setRequestEntity(re);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				phpHtml = postMethod.getResponseBodyAsString();
				if (phpHtml == null || phpHtml.equals("")) {
					log.error("api_add_order请求接口出现问题！");
				}
			} else {
				log.error("api_add_order请求接口出现问题！Response Code: " + statusCode);
			}
			log.info(phpHtml);
			return phpHtml;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Response 消息: " + e.toString());
			throw new BusinessException(ErrorCode.SC_40000_110.getCode(), ErrorCode.SC_40000_110.getType());
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}

	
	/***
	 * 查询订单
	 * @param orderNo
	 * @return
	 */
	public static String api_check_order(String orderNo,String product) {
		
		HashMap<String , String> map =  classMap.get(product);
		String apikey = map.get("apikey");
		String secretkey = map.get("secretkey");
		String apiUrl = map.get("apiUrl");

		String url = "";
		String urlEncrypt = "";

		JSONObject data = new JSONObject();
		try {

			data.put("method", "get_order_result");
			data.put("apikey", apikey);
			JSONObject payload = new JSONObject();
			payload.put("order_no", orderNo);// 卡号

			url = HttpUtil.getURLJson("", payload).substring(1);
			urlEncrypt = SHA.sign(url + secretkey, "SHA-256");
			data.put("sign", urlEncrypt);
			data.put("data", payload.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		PostMethod postMethod = new PostMethod();
		String phpHtml = "";
		try {

			HttpClient httpClient = HttpUtil.createHttpClient();
			postMethod = new PostMethod(apiUrl);
			postMethod.addRequestHeader("content-type", "application/json");
			RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
			postMethod.setRequestEntity(re);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				phpHtml = postMethod.getResponseBodyAsString();
				log.info("result--" + phpHtml);
				if (phpHtml == null || phpHtml.equals("")) {
					log.error("请求接口出现问题！");
				}
			} else {
				log.error("Response Code: " + statusCode);
			}

		} catch (Exception e) {
			log.error("Response 消息: " + e);
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return phpHtml;

	}
       
   public static void main(String[] args) throws ParseException, BusinessException {
	
		String json=FPayUtil.api_add_order("WS20172315121440","1.0",  "中国银行", "北京","北京", "465457987897987898978","wstest",null);
	    String teString = json.substring(1, json.length()-1);
		String test2 =StringEscapeUtils.unescapeJava(teString);
		JSONObject obj  =  JSONObject.fromObject(test2.toString());
		System.out.println(obj.get("StatusCode"));
	 
		System.out.println( FPayUtil.api_check_order("12312312312323","ws"));
	}
 
 
    

    /**
	 * 错误码
	 * 代码目前是4位，其中前两位代表业务，后两位代表此业务的结果信息。
	 */
	public static enum ErrCode {
		C0000("0000", "成功"),
		C0301("0301", "提款失败"),
		C0302("0302", "提款失败"),
		C0303("0303", "提款失败"),
		C0304("0304", "商户提款订单时间不正确,以yyyyMMddhhmmss 格式的时间字符串"),
		C0305("0305", "即时交易：订单状态不为已成功，不能进行提款"),
		C0306("0306", "商户提款订单处理完成，请勿重复提款"),
		C0309("0309", "商户提款订单金额不正确"),
		C0311("0311", "提款失败,提款金额大于支付余额"),
		C0312("0312", "提款请求序列号已经存在"),
		C0313("0313", "商户提款订单号已经存在"),
		C0314("0314", "报文验签失败"),
		C0315("0315", "报文验签失败"),
		C0316("0316", "商户止入或者止出"),
		C0317("0317", "提款逻辑验证失败"),
		C0318("0318", "更新余额异常"),
		C0319("0319", "手续费用计算失败"),
		C0320("0320", "提款订单创建失败"),
		C0322("0322", "网关订单异常"),
		C0324("0324", "构建提款订单异常"),
		C0325("0325", "网关订单记帐成功修改提款订单失败"),
		C0326("0326", "报文参数错误"),
		C0328("0328", "提款金额小于等于0"),
		C0329("0329", "商户提款订单处理完成，请勿重复提款"),
		C0332("0332", "请求版本不正确"),
		C0401("0401", "基本数据校验失败"),
		C0402("0402", "校验业务参数失败"),
		C0403("0403", "查询商户请求网关历史记录失败"),
		C0404("0404", "查询API-失败"),
		C0405("0405", "时间验证失败"),
		C0406("0406", "时间转换类型失败"),
		C0407("0407", "开始时间不能大于结束时间"),
		C0408("0408", "查询时间最大范围只能15天以内"),
		C0409("0409", "创建网关返回商户结果失败"),
		C0410("0410", "查询验签失败"),
		C0411("0411", "查询订单不存在"),
		C0009("0009", "订单不存在");

		
		
		private String code;
		private String text;

		private ErrCode(String code, String text) {
			this.code = code;
			this.text = text;
		}

		public String getCode() {
			return code;
		}
		
		public String getText() {
			return text;
		}

	}

}