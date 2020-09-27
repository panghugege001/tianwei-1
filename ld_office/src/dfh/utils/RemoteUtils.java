package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import dfh.exception.GenericDfhRuntimeException;

public class RemoteUtils {
	public static String getRemoteStatus(String type,String ip) {
		String result = "";
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = "http://"+ip + "/web/?type=" + type;
			System.out.println(url);
			httpClient = HttpUtils.createHttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);     
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);   
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

}
