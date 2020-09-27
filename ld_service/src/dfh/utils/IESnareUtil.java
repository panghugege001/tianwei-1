package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import dfh.model.IESnare;
import dfh.service.interfaces.CustomerService;

/**
 * IESnare
 * 
 */
public class IESnareUtil extends Thread{
	
	private static final Logger log = Logger.getLogger(IESnareUtil.class);
	
	//private final String iesnareUrl = "http://localhost:888/jax-ws/check_trans.jsp";
	
	private CustomerService cs;
    private String usercode;
    private String enduserip;
    private String blackbox;
	
    public IESnareUtil(CustomerService cs, String usercode, String enduserip, String blackbox){
    	this.cs = cs;
    	this.usercode = usercode;
    	this.enduserip = enduserip;
    	this.blackbox = blackbox;
    }
    
    public static String getDevice(String username , String ip ,String ioBB){
    	String result = "";
    	HttpClient httpClient = HttpUtils.createHttpClient();
    	String iesnareUrl = Configuration.getInstance().getValue("IESnare_URL");
		PostMethod method = new PostMethod(iesnareUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		InputStream is = null ;
		BufferedReader reader = null ;
		
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("username", username),
			    new NameValuePair("ipaddress", ip),
			    new NameValuePair("ioBB", ioBB)
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			//System.out.println(method.getResponseBodyAsString());
			is = method.getResponseBodyAsStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	line = line.trim();
		        if(line.startsWith("device.alias")){
		        	result = line.split("=")[1];
		        	return result ;
		        }
		    }
		} catch (Exception e) {
			log.error("iesnare:" + e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				is.close();
				reader.close();
				method.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null ;
    }
    

    public void getDeviceInfo(){
    	String result = "";
    	HttpClient httpClient = HttpUtils.createHttpClient();
    	String iesnareUrl = Configuration.getInstance().getValue("IESnare_URL");
		PostMethod method = new PostMethod(iesnareUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		InputStream is = null ;
		BufferedReader reader = null ;
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("username", usercode),
			    new NameValuePair("ipaddress", enduserip),
			    new NameValuePair("ioBB", blackbox)
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			//System.out.println(method.getResponseBodyAsString());
			is = method.getResponseBodyAsStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	line = line.trim();
		        if(line.startsWith("device.alias")){
		        	result = line.split("=")[1];
		        	break;
		        }
		    }
			reader.close();
			is.close();
			if(!result.equals("")){
				log.info("iesnare:" + result);
				//判断是否存在
				IESnare iesnare = cs.getIESanre(result);
				if(iesnare == null){
					IESnare iesanre = new IESnare(usercode, result, enduserip);
					cs.save(iesanre);
				}
			}
		} catch (HttpException e) {
			log.error("iesnare:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("iesnare:" + e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				is.close();
				reader.close();
				method.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    @Override
	public void run() {
    	getDeviceInfo();
	}

}
