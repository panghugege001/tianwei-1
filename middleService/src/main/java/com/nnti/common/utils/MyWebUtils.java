package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;

/***
 * WEB工具类 wander
 */
public final class MyWebUtils {

	private final static Logger logger = Logger.getLogger(MyWebUtils.class);

	/**
	 * 获取字符串类型的请求参数
	 */
	public static String getStringParameter(HttpServletRequest req, String param) {
		String str = req.getParameter(param);
		if (!MyUtils.isNotEmpty(str)) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 获取字符串类型的请求属性
	 */
	public static String getStringAttribute(HttpServletRequest req, String param) {
		Object str = req.getAttribute(param);
		if (!MyUtils.isNotEmpty(str)) {
			return "";
		}
		return str.toString().trim();
	}

	public static String getHttpContentByBtParam(String url) throws BusinessException {
		return getHttpContentByBtParam(url, null);
	}

	/**
	 * 通过BODY传递字节数组请求参数
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static String getHttpContentByBtParam(String url, String param) throws BusinessException {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			HttpEntity entity = null;
			if (MyUtils.isNotEmpty(param)) {
				httppost.setEntity(new StringEntity(param, ContentType.create("application/x-www-form-urlencoded")));
			}
			HttpResponse response = httpclient.execute(httppost);
			entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			logger.error("请求调用异常：", e);
			throw new BusinessException(ErrorCode.SC_10001.getCode(), "请求调用异常");
		}
		return null;
	}

	/**
	 * 发送xml参数
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static String getHttpContentByXMLParam(String url, String param) throws BusinessException {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			HttpEntity entity = null;

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("requestDomain", param));
			if (MyUtils.isNotEmpty(param)) {
				httppost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
			}
			HttpResponse response = httpclient.execute(httppost);
			entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			logger.error("请求调用异常：", e);
			throw new BusinessException(ErrorCode.SC_10001.getCode(), "请求调用异常");
		}
		return null;
	}

	/**
	 * 访问不同服务器的数据
	 *
	 * @return
	 */
	public static String getHttpContentByParam(String url, List<NameValuePair> formparams) throws BusinessException {
		StringBuffer buf = new StringBuffer();
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		try {
			if (formparams != null && formparams.size() > 0) {
				httppost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			}
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			logger.error("请求调用异常：", e);
			throw new BusinessException(ErrorCode.SC_10001.getCode(), "请求调用异常");
		}
		return null;
	}

	/**
	 * 封装HTTPCLIENT请求参数集合
	 *
	 * @param str
	 * @return
	 */
	public static List<NameValuePair> getListNamevaluepair(String... str) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (str != null && str.length % 2 == 0) {
			for (int i = 0; i < str.length; i++) {
				formparams.add(new BasicNameValuePair(str[i], str[++i]));
			}
		}
		return formparams;
	}

	public static List<NameValuePair> getListNamevaluepair(Map<String, String> map) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry) iterator.next();
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return formparams;
	}

	/**
	 * 提交一个带有隐藏表单的页面
	 */
	public static void submitForm(HttpServletResponse res, Map<String, Object> attrs, String url) throws IOException {
		if (MyUtils.isNotEmpty(attrs) && MyUtils.isNotEmpty(url)) {
			PrintWriter wr = null;
			try {
				wr = res.getWriter();
				StringBuilder sb = new StringBuilder();
				res.setContentType("text/html;charset=utf-8");
				sb.append("<!DOCTYPE html><html><body onLoad='document.fromId.submit();'>")
						.append("<form name='fromId' method='post' action='").append(url).append("'>");
				for (Map.Entry<String, Object> entry : attrs.entrySet()) {
					sb.append("<input type='hidden' name='").append(entry.getKey()).append("' value='")
							.append(entry.getValue()).append("'>");
				}
				sb.append("</form></body></html>").toString();
				wr.print(sb.toString());
				wr.flush();
			} catch (IOException e) {
				throw e;
			} finally {
				if (wr != null) {
					wr.close();
				}
			}
		}
	}

	/**
	 * 获得 request的所有参数
	 *
	 * @param request
	 * @author wangwei
	 */
	public static Map getRequestParameters(HttpServletRequest request) {
		Map<String, Object> model = new HashedMap();
		Map map = request.getParameterMap();
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = entry.getKey().toString();
			String value = entry.getValue() == null ? "" : ((String[]) entry.getValue())[0].trim();
			model.put(key, value);
		}
		return model;
	}

	/**
	 * 读取request流
	 * 
	 * @param request
	 * @return
	 */
	public static String readReqStr(HttpServletRequest request) {
		BufferedReader reader = null;  
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		return sb.toString();
	}
	
   public static String sendFormMsg(Map<String, String> parameters, String url) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            Iterator<String> it = parameters.keySet().iterator();
            List<BasicNameValuePair> nvList = new ArrayList<BasicNameValuePair>();
            while(it.hasNext()) {
                String key = it.next();
                String value = parameters.get(key);
                if(null!=key && null!=value && !"".equals(key) && !"".equals(value)) {
                    BasicNameValuePair nv = new BasicNameValuePair(key, value);
                    nvList.add(nv);
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvList, Charset.forName("utf-8"));
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity repEntity = response.getEntity();
            if (repEntity != null) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(repEntity.getContent()));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                EntityUtils.consume(repEntity);
                String str = builder.toString();
                return str;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    public static String sendFormMsgFastPay(String parameters, String url) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(parameters,"utf-8");//解决中文乱码问题    
            entity.setContentEncoding("UTF-8");    
            entity.setContentType("application/json");    
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity repEntity = response.getEntity();
            if (repEntity != null) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(repEntity.getContent()));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                EntityUtils.consume(repEntity);
                String str = builder.toString();
                return str;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

	
	public static void main(String[] args) throws BusinessException {
		String  skySecretkey="897c9ece-d39e-4840-a398-256644a2c732";
		String  username="dayun_test_user";
		String  password="Dayun@123";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("secretKey",skySecretkey);
		params.put("username",username);
		params.put("password",password);
		
		List<NameValuePair> formparams = MyWebUtils.getListNamevaluepair(params);
		String result = getHttpContentByParam("https://api.gcpstg.m27613.com/v1/login", formparams);
		System.out.println(result);
	}
}
