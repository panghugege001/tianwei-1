package com.nnti.withdraw.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.utils.HttpUtil;

import net.sf.json.JSONObject;


/**
 * 秒付存款2 自动转账API
 * 
 * @author
 * 
 */
public class QlFPayUtil {
	private static Logger log = Logger.getLogger(QlFPayUtil.class);
	
	//测试
	/*public static String apikey = "0ebe5dcb15fe407fa0187afa48af3d98";
	public static String apiUrl = "http://172.16.35.106/transfer";
	public static String version = "1.0";
	public static String secretkey = "0ebe5dcb15fe407fa0187afa48af3d980ebe5dcb15fe407fa0187afa48af3d980ebe5dcb15fe407fa0187afa48af3d98";*/
		
	
	
	//齐乐正式
	public static String apikey = "c4fb000f647a43e7a1d1f046c03e54ec";
	public static String apiUrl = "http://210.83.80.118:808/transfer";
	public static String version = "1.0";
	public static String secretkey = "cgofbs0xcza6stgdrpitjdue1sw5475jjnw4m2i4l8thcydmajddrobzl9ejwus4";
	
	
	
	
	

	
	
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
	public static String api_add_order(String orderNo, String amount, String bankName, String bankAddress,
			String accName, String cardNumber) throws BusinessException {
		log.info(orderNo+"|"+amount+"|"+ bankName+"|"+ bankAddress+"|"+accName+"|"+ cardNumber);
		String url = "";
		String urlEncrypt = "";

		JSONObject data = new JSONObject();
		data.put("method", "add_order");
		data.put("apikey", apikey);
		JSONObject payload = new JSONObject();
		payload.put("order_no", orderNo); // 单号 qy_woodytest_123123
		payload.put("bank_name", bankName); // 银行名字
		payload.put("bank_address", bankAddress); // 银行地址
		payload.put("account_name", accName); // 银行账户
		payload.put("card_number", cardNumber); // 银行卡号
		payload.put("amount", amount); // 银行额度

		url = HttpUtil.getURLJson("", payload).substring(1);
		urlEncrypt = SHA.sign(url + secretkey, "SHA-256");
		data.put("sign", urlEncrypt);
		data.put("data", payload.toString());

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

	public static String api_check_order(String orderNo) {

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
	
		String json=QlFPayUtil.api_add_order("1.0", "WS20172315121440",  "1563333652", "测试账号","中国银行", "asd");
	    String teString = json.substring(1, json.length()-1);
		String test2 =StringEscapeUtils.unescapeJava(teString);
		JSONObject obj  =  JSONObject.fromObject(test2.toString());
		System.out.println(obj.get("StatusCode"));
	 
		System.out.println( QlFPayUtil.api_check_order("12312312312323"));
	 
	    
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