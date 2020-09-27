package dfh.utils;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class BokUtils {
	/**************博客游戏列表**************/
	public static final  String BACCARAT = "baccarat";//百家樂
	public static final  String DDZ2 = "ddz2";//鬥地主
	public static final  String DDZ3 = "ddz3";//3人鬥地主
	public static final  String DOUNIU = "douniu";//鬥牛
	public static final  String FORESTDANCE = "forestdance";//森林舞會
	public static final  String MJ2 = "mj2";//廣東麻將
	public static final  String SCMJ = "scmj";//四川麻將
	public static final  String TEXAS = "texas";//德州
	public static final  String WINDOUNIU = "windouniu";//鬥牛(莊)
	public static final  String ZHAJINHUA = "zhajinhua";//扎金花
	
	static String  dspurl="http://www.bok88.com";
	
	public static String[] CheckOrCreateGameAccount(String loginname){
		String[]str = new String[3];
		try {
			String params = "?src=abc&op=register&uid=" + loginname
					+ "&nick=e68" + loginname + "&pwd=123456";
			String url=dspurl+"/api.aspx"+params;
			HttpClient httpClient=HttpUtils.createHttpClient();
			PostMethod postMethod=new PostMethod(url);
			httpClient.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			System.out.println("Bok Response:"+result);

			JSONObject json = JSONObject.fromObject(result);
			str[0]=json.getString("isok");
			str[1]=json.getString("err");
			str[2]=json.getString("result");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
	
	public static String[] getBokAmountInfo(String loginname){
		String[]str = new String[4];
		try {
			String params = "?src=abc&op=getinfo&uid=" + loginname;
			String url=dspurl+"/api.aspx"+params;
			HttpClient httpClient=HttpUtils.createHttpClient();
			PostMethod postMethod=new PostMethod(url);
			httpClient.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			System.out.println("Bok Response:"+result);
			JSONObject json = JSONObject.fromObject(result);
			str[0]=json.getString("isok");
			str[1]=json.getString("err");
			str[2]=json.getJSONObject("result").getString("nick");
			str[3]=json.getJSONObject("result").getString("point");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
	
	public static String[] changeBokAmountInfo(String loginname,Integer remit,String type){
		String[]str = new String[3];
		try {
			
			if("IN".equals(type)){
				remit = 1*remit;
			}else if("OUT".equals(type)){
				remit = -1*remit;
			}
			String params = "?src=abc&op=setmoney&uid="+loginname+"&money="+remit;
			String url=dspurl+"/api.aspx"+params;
			HttpClient httpClient=HttpUtils.createHttpClient();
			PostMethod postMethod=new PostMethod(url);
			httpClient.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			System.out.println("Bok Response:"+result);
			JSONObject json = JSONObject.fromObject(result);
			str[0]=json.getString("isok");
			str[1]=json.getString("err");
			str[2]=json.getJSONObject("result").getString("point");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
	
	
	public static void main(String[] args) {
		getBokAmountInfo("woodytest");
	}
}
