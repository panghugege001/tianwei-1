package dfh.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 支付
 */
public class ZfSendRequestUtil {
	private static Logger log = Logger.getLogger(ZfSendRequestUtil.class);
	
	private final static String ZfPostUrl = "https://pay.dinpay.com/gateway";
	/***************************
	 * 通用支付2
	 */
	public final static String ZF_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA3kkhMMn/YbNSsDfM30OVkiLFQwsNrp1QtsOQ1jJO69zLilDGV23eam+YFex/Rrk92KHQYPCvsR+rfzMqKPmqKF/DxJjS2DT1S73Emy1zwqTtgMkIa76LlvWL8rPdEGqfVrFeckc7HCPBQkB4yvGqtLFb0dSu54ULqIAGqzLa6wIDAQAB";
	
	public final static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ0k3rtTH+Xptum0"
																	+"IQ0U/0mjvfwrUUQgu+on8CFKfriHw5yraMiwpQFcadRfxs54OjG5wSFllJs1/sLW"
																	+"4KLIgaCHXcZDt//0xYSYFoEPlAJ/t1XXlEf1kVUCdWz9DAw7NA5OjJ0phOqsC7Yu"
																	+"xQ78u9BsQr20xpqIZdgtuD5M3kcRAgMBAAECgYAoNp4068G28UcYqTroAR6PNrLQ"
																	+"jDlAqOvNsSi3LDwfdsatGgQ3j1S6c261pUjX31ZtES8GH9uWmIu4SoqzvYHGLxXV"
																	+"/LwkB5LpiYDZtBByHPZwP33eve+8nxzsYV+meGMrD2UiKrL7++daHtFTl/tPqlAV"
																	+"YIj4uQ5plGLY5yyaUQJBAMnTXcMdCAyAuETUwkTtcENBxysrBqAlEOGs4T1Lb3Up"
																	+"jKGWquESLsj0BnLNpjUkotWVLHOiyZeD0cQTSad8sj0CQQDHUypCsNiO+P5Dnr+W"
																	+"LzspWt8xykHXIgLnxxKuo/aigsx+S1uQfnolE1namGmRlh1t5S41lFOS2hPsvoEA"
																	+"lBllAkEAtoOAbSvZ1YSqHwTQUgju1sOrW2xjZ3tdktgGMKoBN1DFrnNUQIly6aNU"
																	+"GYnD3pcs8j0en8v8xhodHlXa662meQJABPd0iR7g9ocLxuGQwoxAvCIQh6wTgenO"
																	+"CrpyI7ngLmJ19/umt23/WyclSQYdM0x7RT69rQYaIwGeF3sTudD25QJAEpHSir7y"
																	+"NpqSXrlabLxHuZ5KCvw6LC+OeS6wkG1Q2RZsQnI9MhGL1lDBa2pkVZ1aR7B1FX5c"
																	+"pA86Qu6rboVPwQ==";
	
	
	
	
	/*******
	 * 通用支付2
	 */
	public final static String ZF_PUBLIC_KEY2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0yQZBG0mFFc2J0x64U3uWeMjHhnOC+91hvDC2Scg6SZlPykdTxOWdjZ6R9p8BnvrMuD9xfGxjPVv8g/+bUysVG4wxYhYe6XfrFImVqwvfQo43hBgDNx7+vfBnQQH6hyiHEIBwT/0z1aYpRoT0DbjBC3WsdPG+uv8TwzZ/4BhDSwIDAQAB";
	
	public final static String PRIVATE_KEY2 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMSHNCZHakDJxj2y"
																					+"SZ1MxbjHa1M6U990n01Fo7cx4cSz2/9TkJAHJIoFlV5l5HxhLVyXUfjlIDDUDVht"
																					+"o6pnWkXPp6+7OR1URjyLXYHOE25ab0QBOGVz6jmlGyiea76XbY28PZYrHfnq3acG"
																					+"65sAkyxqOb0UnZxf2g364DdYdrwbAgMBAAECgYEAgp82myamSY+S0pj4crN/S1RI"
																					+"UlYXPxM9eleJ/A4Md/fvhiHdmM0WjSIu6EIi/kR8qOjuk9Z+cdXPVPHycSUmisvQ"
																					+"sBPn4hKCoIfY61FZ1IbjN7LXRFwiVrUUR6a9axSeMwQuEBDaT+biJdlh4nyxowYx"
																					+"PqB4MIo+hs9Ez77arDECQQDhxNGmk9wzyPzgbp3HTVjJZZl9VnHuJpI6ubb1DKmy"
																					+"P8ctnpICVpnGOdbKuiHZMhL05YUzZaAcfKJa9qSV/s2pAkEA3tgJYqoA0spBhCXL"
																					+"IqphYHKQD+MDioVGh4HvIaPDJo9L12EHd/VlA5rApV8D/B4JpOKA6/+TuxGrmpQG"
																					+"vXJuIwJBAJk2UzxjXW17OTkpeEN3lwhdvZMUZg7C+DuIKsQTytmfdJDd6qJRmNuw"
																					+"BrbfJ/dYhwrQ89sUXVrXrepjNQBhwbkCQCNbJ3IOAv9us6Jh1K71VWnWA2Fh5Ufv"
																					+"CVeql+RAdVAC4EeVz3eK6bH+tZ4HSzyPD3ynq0jGTgwaK53k6oTYSP0CQQCzOBf/"
																					+"Z6cVI+gkinZ1W8SuPVdYTw84ZM92Ow/mrlOchF0ZHmt70a7NvrmDKwlqnuqGn3jk"
																					+"v7DUEsYlRLsDO5Vk";
																			
	
	
	

	
	
	@SuppressWarnings("deprecation")
	public static String sendRequest(String sign, String merchant_code,String order_no , String order_amount , String service_type ,String notify_url ,String interface_version ,String sign_type,String order_time ,String product_name ,String extra_return_param) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(ZfPostUrl);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

		NameValuePair signE = new NameValuePair( "sign" , sign);
		NameValuePair merchant_codeE = new NameValuePair( "merchant_code" , merchant_code);
		NameValuePair order_noE = new NameValuePair( "order_no" , order_no);
		NameValuePair order_amountE = new NameValuePair( "order_amount" , order_amount);
		NameValuePair service_typeE = new NameValuePair( "service_type" , service_type);
		NameValuePair notify_urlE = new NameValuePair( "notify_url" , notify_url);
		NameValuePair interface_versionE = new NameValuePair( "interface_version" , interface_version);
		NameValuePair sign_typeE = new NameValuePair( "sign_type" , sign_type);
		NameValuePair order_timeE = new NameValuePair( "order_time" , order_time);
		NameValuePair product_nameE = new NameValuePair( "product_name" , product_name);
		NameValuePair extra_return_paramE = new NameValuePair( "extra_return_param" , extra_return_param);
		method.setRequestBody( new NameValuePair[]{signE , merchant_codeE , order_noE , order_amountE , service_typeE , notify_urlE , interface_versionE , sign_typeE , order_timeE , product_nameE , extra_return_paramE});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	public static String exportQrcode(String info){
		String resp_code = compileVerifyData("<resp_code>(.*?)</resp_code>", info) ;
		String resp_desc = compileVerifyData("<resp_desc>(.*?)</resp_desc>", info) ;
		if(resp_code.equals("SUCCESS")){
			//解密 , 验签
//			String signStr = RSAWithSoftware.validateSignByPublicKey(paramStr, publicKey, signedData) ;
			String qrcode = compileVerifyData("<qrcode>(.*?)</qrcode>", info) ;
			return qrcode ;
		}else{
			log.info("智付微信生成二维码失败resp_desc:"+resp_code+"  resp_desc:" + resp_desc);
			return null ;
		}
	}
	
	public  static String compileVerifyData(String pattern,String verifyData){
    	Pattern p=Pattern.compile(pattern);
    	Matcher m=p.matcher(verifyData);
    	String result = "";
    	while(m.find()){
    		result = m.group(1);
        }
    	return result;
    }
	
	public static void main(String[] args) {
	}

}
