package dfh.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

public class FlashPayUtil {
	private static Logger log = Logger.getLogger(FlashPayUtil.class);

	private static String middleUrl = Configuration.getInstance().getValue("middleservice.url");
    //private static String middleUrl = "http://192.168.80.41:9080/withdraw";
	private static String product = "ld";   
	
	
	public static String updateFPayOrder(String orderNo) {
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String url = middleUrl + "withdraw/updateFPay";
			httpClient = createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			NameValuePair eOrderNo = new NameValuePair( "order_no" , orderNo );
			NameValuePair eProduct = new NameValuePair( "product" , product );
			postMethod.setRequestBody( new NameValuePair[]{eOrderNo,eProduct});
			
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String phpHtml = postMethod.getResponseBodyAsString();
				if (phpHtml == null || phpHtml.equals("")) {
					log.info("秒付宝中间件没有数据响应");
					return null;
				}
				return phpHtml;
			} else {
				log.info("Response Code: " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 消息: " + e.toString());
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}
	
	
	

	//重新提交
	public static String resubmit(String orderNo,String operator,String ip) {
		System.out.println(orderNo+"重新提交");   
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String url = middleUrl + "withdraw/resubmitFPay";
			httpClient = createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			NameValuePair eOrderNo = new NameValuePair( "order_no" , orderNo );
			NameValuePair eProduct = new NameValuePair( "product" , product );
			NameValuePair eOperator = new NameValuePair( "operator" , operator );
			NameValuePair eIp = new NameValuePair( "ip" , ip );
			postMethod.setRequestBody( new NameValuePair[]{eOrderNo,eProduct,eOperator,eIp});
			
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String phpHtml = postMethod.getResponseBodyAsString();
				if (phpHtml == null || phpHtml.equals("")) {
					log.info("秒付宝中间件没有数据响应");
					return null;
				}
				return phpHtml;
			} else {
				log.info("Response Code: " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 消息: " + e.toString());
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}
	
	
	
	
	public static String addFPay(String orderNo,String operator,String remark,String ip) {
		System.out.println(orderNo+"--执行审核且秒提操作");   
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String url = middleUrl + "withdraw/addFPay";
			httpClient = createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			NameValuePair eOrderNo = new NameValuePair( "order_no" , orderNo );
			NameValuePair eProduct = new NameValuePair( "product" , product );
			NameValuePair eOperator = new NameValuePair( "operator" , operator );
			NameValuePair eRemark = new NameValuePair( "remark" , remark );
			NameValuePair eIp = new NameValuePair( "ip" , ip );
			postMethod.setRequestBody( new NameValuePair[]{eOrderNo,eProduct,eOperator,eRemark,eIp});
			
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String phpHtml = postMethod.getResponseBodyAsString();
				if (phpHtml == null || phpHtml.equals("")) {
					log.info("秒付宝中间件没有数据响应");
					return null;
				}
				return phpHtml;
			} else {
				log.info("Response Code: " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 消息: " + e.toString());
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}
	
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 240*1000);
		httpclient.setParams(params);
		return httpclient;
	}

}
