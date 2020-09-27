package dfh.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

/**
 * 前台玩家投诉建议发送到OA系统工具类
 *
 */
public class OAUtil {
	
	private static Logger log = Logger.getLogger(OAUtil.class);
	
	//private static BASE64Encoder encoder = new BASE64Encoder();
	
	private static final String baseUrl = "https://oa-office.net";
	private static final String workFlow_Interface = "/workflow_interface/client/workflow_interface_client.php";
	private static final String workflow_progress = "/workflow_interface/client/workflow_progress_client.php";
	
	public static final String flow_id = "184";             //对接流程的流程id
	private static final String begin_user = "620";     //发起人user_id对应于oa系统中的user_id  visitor=620
	private static final String second_user = "3576";           //第二步审批人user_id对应于oa系统中的user_id  e68:623  qy:624  long8:625  777:626
	private static final String begin_dept_id = "58";         //字符型参数，发起人对应的oa系统中的部门id
	private static final String flow_prcs = "2";            //第二步实际的流程步骤号，暨oa系统中设计的流程步骤号
	
	private static String paramKey = "用户名,问题,描述";
	
	/**
	 * 创建工单（玩家投诉建议问题咨询）
	 * @param loginName
	 * @param type
	 * @param content
	 * @return  流程的流水号
	 */
	public static String createWorkFlow(String loginName, String type, String content){
		HttpClient httpClient = createHttpClient();
		PostMethod method = new PostMethod(baseUrl + workFlow_Interface);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		
		StringBuilder sbStr = new StringBuilder(); 
		//将 “,” 替换为中文逗号
		sbStr.append(loginName.replace(",", "，")).append(",").append(type.replace(",", "，")).append(",").append(content.replace(",", "，"));
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("flow_id", flow_id),
			    new NameValuePair("begin_user", begin_user),
			    new NameValuePair("second_user", second_user),
			    new NameValuePair("begin_dept_id", begin_dept_id),
			    new NameValuePair("array_key", paramKey),
			    new NameValuePair("array_value", sbStr.toString()),
			    new NameValuePair("flow_prcs", flow_prcs)
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info("生成工单" + result);
			JSONObject json = JSONObject.fromObject(result);
			if(json.get("code").toString().equalsIgnoreCase("1")){
				return json.get("msg").toString();
			}	
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (HttpException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally{
			if(method!=null){
				method.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return null;
	}
	
	/**
	 * 查询投诉建议处理进度
	 * @param run_id
	 * @param flow_id
	 * @return
	 */
	public static String workflowProgress(String run_id, String flow_id){
		HttpClient httpClient = createHttpClient();
		PostMethod method = new PostMethod(baseUrl + workflow_progress);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");

		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("run_id", run_id),
			    new NameValuePair("flow_id", flow_id)
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			if(!result.equals("服务异常")){
				return result;
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (HttpException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally{
			if(method!=null){
				method.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return null;
	}
	
	private static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "GBK");
		params.setParameter("http.socket.timeout", 20000);
		httpclient.setParams(params);
		return httpclient;
	}
	
	
	public static void main(String[] args) {
		createWorkFlow("woodytest", "咨询账户问题", "oa投诉建议工作流测试");
		//workflowProgress("5915", "184");
	}
}
