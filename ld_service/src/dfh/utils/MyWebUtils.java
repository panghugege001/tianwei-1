package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/***
 * WEB工具类
 * wander
 */
public final class MyWebUtils {

    private final static Logger logger = Logger.getLogger(MyWebUtils.class);

    public static String getHttpContentByBtParam(String url) throws Exception {
        return getHttpContentByBtParam(url, null);
    }

    /**
     * 通过BODY传递字节数组请求参数
     *
     * @param url
     * @param param
     * @return
     */
    public static String getHttpContentByBtParam(String url, String param) throws Exception {

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        HttpEntity entity = null;
        if (param != null) {
            entity = new ByteArrayEntity(param.getBytes("UTF-8"));
            httppost.setEntity(entity);
        }
        HttpResponse response = httpclient.execute(httppost);
        entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity, "UTF-8");
        }

        return null;
    }

    /**
     * 访问不同服务器的数据
     *
     * @return
     */
    public static String getHttpContentByParam(String url, List<NameValuePair> formparams) throws Exception {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        if (formparams != null && formparams.size() > 0) {
            httppost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
        }
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity, "UTF-8");
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

        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formparams;
    }

    /**
     * 获得 request的所有参数
     *
     * @param request
     * @author wangwei
     */
    public static Map getRequestParameters(HttpServletRequest request) {
        Map<String, String> model = new HashedMap();
        Map map = request.getParameterMap();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
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

}
