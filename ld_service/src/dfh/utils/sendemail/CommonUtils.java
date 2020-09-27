package dfh.utils.sendemail;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;


public class CommonUtils {
	private static Log log = LogFactory.getLog(CommonUtils.class);
	
/**
 * json字符串转化为map
 * @param jsonStr
 * @return
 */
	public static Map<String,Object> json2Map(String jsonStr) {
		
		Map<String, Object> map = new HashMap<String, Object>(); 
		JSONObject json=JSONObject.fromObject(jsonStr);
		for(Object k :json.keySet()){
			Object value = json.get(k);
			map.put(k.toString(),value);
		}
		return map;
	}
	
	/**
	 * json字符串转换为对象
	 * @param jsonStr
	 * @param pojoClass
	 * @return
	 */
	public static Object jason2ObjectPojo(String jsonStr,@SuppressWarnings("rawtypes") Class pojoClass){
		Object obj;
		
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		obj = JSONObject.toBean(jsonObject, pojoClass);
		return obj;
		
	}
	/**
	 * json字符串转化为map 深层解析  json里面包含arrayList的
	 * @param jsonStr
	 * @return
	 */
		public static Map<String,Object> json2MapDeep(String jsonStr) {
			
			Map<String, Object> map = new HashMap<String, Object>(); 
			JSONObject json=JSONObject.fromObject(jsonStr);
			for(Object k :json.keySet()){
				Object value = json.get(k);
				if(value instanceof JSONArray){
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					@SuppressWarnings("unchecked")
					Iterator<JSONObject> it = ((JSONArray)value).iterator();
					while(it.hasNext()){
						JSONObject jsobj =it.next();
						list.add(json2MapDeep(jsobj.toString()));
					}
					map.put(k.toString(), list);
				}
				map.put(k.toString(),value);
			}
			return map;
		}
	
	
	
	/**
	 * 解析URL 返回字符串
	 * @param urlString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getReturnDataByUrl(String urlString) throws UnsupportedEncodingException {  
	     String res = "";   
	        try {   
	            URL url = new URL(urlString);  
	            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();  
	            conn.setDoOutput(true);  
	            conn.setRequestMethod("GET");  
	            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));  
	            String line;  
	           while ((line = in.readLine()) != null) {  
	               res += line+"\n";  
	         }  
	            in.close();  
	        } catch (Exception e) { 
	        	System.out.println(e.getMessage());
	        	log.error("error in wapaction,and e is " + e.getMessage());  
	        }  
	      System.out.println(res);   
	        return res;  

	    }  
	
	/**
	 * 获取随机数字和字母
	 * @param length
	 * @return
	 */
	 public static String getCharAndNumr(int length) {
		  String val = "";
		  Random random = new Random();
		  for (int i = 0; i < length; i++) {
		  /* // 输出字母还是数字
		   String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
		   // 字符串
		   if ("char".equalsIgnoreCase(charOrNum)) {
		    // 取得大写字母还是小写字母
		    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
		    val += (char) (choice + random.nextInt(26));
		   } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
*/		   
			  val += String.valueOf(random.nextInt(10));
		   //}
		  }
		  return val;
		 }
	 
	/* public static void main(String[] args){
		System.out.println( getCharAndNumr(8));
	 }*/
		 
}
