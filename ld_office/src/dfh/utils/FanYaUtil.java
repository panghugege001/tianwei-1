package dfh.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.text.SimpleDateFormat;
import dfh.model.FanyaLog;
import dfh.model.FanyaLogVo;
import net.sf.json.JSONArray;
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
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

public class FanYaUtil {
	private static Logger log = Logger.getLogger(FanYaUtil.class);
	public static final String KEY = "79fd6283f6c4453cb8289771a6f3eae9";
	public static final String URL = "https://api.avia-gaming.com/api/";
	/*public static final String KEY = "f5379d8322f54b078e80a2d41d33b023";
	public static final String URL = "http://api.es.betwin.ph/handler/api/";*/
	public static final String PRODUCT = "k";
	
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
		String msg = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("UserName", PRODUCT + name);
			map.put("Password", PRODUCT + name);
			JSONObject json = JSONObject.fromObject(FanYaUtil.doPost("user/register", map));
			msg = json.getString("msg");
		} catch (Exception e) {
			e.printStackTrace();
			msg = "网络异常，请稍后再试";
		}
		return msg;
	}
	
	/**
	 * 登录
	 * @param   UserName
	 * @return
	 */
	public static String login(String name){
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("UserName",PRODUCT + name);
			JSONObject info = FanYaUtil.getRequestData(FanYaUtil.doPost("user/login", map));
			String url =  null;
			if(info != null){
				url = info.getString("Url");
			}
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return "网络异常，请稍后再试";
		}
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

	//获取游戏记录
	public static List loadrecords(String stime,String etime,String name,int page){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("StartAt", stime);
			map.put("EndAt",etime);
			if(StringUtil.isNotBlank(name)){
				map.put("Type", "UserName");
				map.put("UserName",PRODUCT + name);
			}else{
				//查询派奖时间
				map.put("Type", "RewardAt");
			}
			map.put("PageIndex",page);
			JSONObject info = FanYaUtil.getRequestData(FanYaUtil.doPost("log/get", map));
			JSONArray json =  info.getJSONArray("list");
			String str = json.toString().replaceAll("/", "-");
			Gson gson = new Gson();
			//因为第三方是.NET命名格式所以没办法做个实体转换
			List<FanyaLogVo> listVo = gson.fromJson(str, new TypeToken<List<FanyaLogVo>>(){}.getType());
			List<FanyaLog> list = new ArrayList<FanyaLog>();
			if(!listVo.isEmpty()){
				for (FanyaLogVo vo : listVo) {
					FanyaLog log = new FanyaLog();
					log.setBet(vo.getBet());
					log.setBetAmount(Double.valueOf(vo.getBetMoney()));
					log.setCategory(vo.getCategory());
					log.setContent(vo.getContent());
					log.setCreateAt(DateUtil.toTimestamp(vo.getCreateAt()));
					log.setEndAt(DateUtil.toTimestamp(vo.getEndAt()));
					log.setLeague(vo.getLeague());
					//有符号 数据库报错
					log.setMatch1(vo.getMatch());
					log.setMoney(Double.valueOf(vo.getMoney()));
					log.setOrderId(vo.getOrderID());
					log.setResult(vo.getResult());
					log.setStartAt(DateUtil.toTimestamp(vo.getStartAt()));
					log.setStatus(vo.getStatus());
					log.setUpdateAt(DateUtil.toTimestamp(vo.getUpdateAt()));
					//注意用户名取值
					log.setUserName(vo.getUserName().substring(1,vo.getUserName().length()));
					log.setResultAt(DateUtil.toTimestamp(vo.getResultAt()));
					log.setRewardAt(DateUtil.toTimestamp(vo.getRewardAt()));
					log.setCreateTime(new Date());
					list.add(log);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义要输出日期字符串的格式
		 
		Date startTime = new Date();
		 //Mon Jun 05 21:33:00 CST 2017
		 String startTimeSting  = sdf.format(startTime);
		String a = "2017-6-5 21:33:00";
		System.out.println(sdf.parse(a));
		
	}
}
