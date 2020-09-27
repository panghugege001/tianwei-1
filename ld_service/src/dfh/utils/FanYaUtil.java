package dfh.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 泛亚电竞类
 * @author Caesar
 *
 */
public class FanYaUtil {
	private static Logger log = Logger.getLogger(FanYaUtil.class);
	public static final String KEY = "79fd6283f6c4453cb8289771a6f3eae9";
	public static final String URL = "https://api.avia-gaming.com/api/";
	/*public static final String KEY = "c58f67217e4a429aaba230b98331e6b1";
	public static final String URL = "http://testapi.bw-gaming.com/";*/
	public static final String PRODUCT = "k";//中间件PlatformConfigUtil fanyaMap的属性值对应
	
	public static String doPost(String methed, Map<String, Object> paramMap) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		String result = "";
		// 创建httpClient实例
		httpClient = HttpClients.createDefault();
		// 创建httpPost远程连接实例
		HttpPost httpPost = new HttpPost(URL + methed);
		// 配置请求参数实例
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
				.setConnectionRequestTimeout(35000)// 设置连接请求超时时间
				.setSocketTimeout(60000)// 设置读取数据连接超时时间
				.build();
		// 为httpPost实例设置配置
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Authorization", KEY);
		// 设置请求头
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// 封装post请求参数
		if (null != paramMap && paramMap.size() > 0) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// 通过map集成entrySet方法获取entity
			Set<Entry<String, Object>> entrySet = paramMap.entrySet();
			// 循环遍历，获取迭代器
			Iterator<Entry<String, Object>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> mapEntry = iterator.next();
				nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
			}

			// 为httpPost设置封装好的请求参数
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			// httpClient对象执行post请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpPost);
			// 从响应对象中获取响应内容
			HttpEntity entity = httpResponse.getEntity();
			/** 读取服务器返回过来的json字符串数据 **/
			result = EntityUtils.toString(entity);
			log.info(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != httpResponse) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	//结果转义
	public static JSONObject getRequestData(String methed){
		JSONObject data = null;
		try{
			JSONObject reqJson = JSONObject.fromObject(methed);
			data = (JSONObject)reqJson.get("info");
		}catch (Exception e) {
			log.warn("参数有误", e); 
		}
		return data;
	}
	/**
	 * 注册
	 * @param  UserName Password
	 * @return
	 */
	public static String register(String name,String pwd){
		//String password = EncryptionUtil.encryptPassword(pwd);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UserName", PRODUCT + name);
		map.put("Password", PRODUCT + name);
		JSONObject json = JSONObject.fromObject(FanYaUtil.doPost("user/register", map));
		String msg = json.getString("msg");
		return msg;
	}
	
	/**
	 * 登录
	 * @param   UserName
	 * @return
	 */
	public static String login(String name){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UserName",PRODUCT + name);
		JSONObject info = FanYaUtil.getRequestData(FanYaUtil.doPost("user/login", map));
		String url =  null;
		if(info != null){
			url = info.getString("Url");
		}
		return url;
	}
	
	/**
	 * 查询转账结果
	 * @param ID 流水号
	 * @return
	 */
	public static String transferinfo(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", id);
		JSONObject info = FanYaUtil.getRequestData(FanYaUtil.doPost("user/transferinfo", map));
		return info.toString();
	}
	
	/**
	 * 查询用户余额
	 * @param  UserName 
	 * @return
	 */
	public static Double balance(String userName){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UserName", PRODUCT + userName);
		JSONObject reqJson = JSONObject.fromObject(FanYaUtil.doPost("user/balance", map));
		if(reqJson != null){
			String str = reqJson.getString("success");
			if("0".equals(str)){
				//自动注册 不用提示
				FanYaUtil.register(userName,null);
			}else{
				JSONObject info = (JSONObject)reqJson.get("info");
				return info.getDouble("Money");
			}
		}
		return 0.0;
	}
	public static void main(String[] args) {
		//FanYaUtil.transfer("ufa","devtest999",50.0,"IN","123456789124");
		FanYaUtil.balance("devtest999");
	
	}
}
