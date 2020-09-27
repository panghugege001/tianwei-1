package dfh.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dfh.action.vo.AutoXima;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
  
/**  
 * @author Jalen 
 */  
public class JsonUtil {  
    public static String readFile(String Path){
        BufferedReader reader = null;  
        String laststr = "";  
        try{  
            InputStream inputStream  =  JsonUtil.class.getResourceAsStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");  
            reader = new BufferedReader(inputStreamReader);  
            String tempString = null;  
            while((tempString = reader.readLine()) != null){  
                laststr += tempString;  
            }  
            reader.close();  
        }catch(IOException e){  
            e.printStackTrace();  
        }finally{  
            if(reader != null){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return laststr;  
    } 
    
    /**
     * 老虎机钱包自助洗码
     * @param jsonData
     * @return
     */
   public static List jsonToBean(String jsonData ){
	   JSONObject jsonObject =  JSONObject.fromObject(jsonData);
	   List list=new ArrayList<>();
		
		Iterator iterator = jsonObject.keys();
	    while(iterator.hasNext()){
	    	String key = (String) iterator.next();
	    	
	    	if ("message".equals(key))    //去除没用的key
				continue;
	    	
	    	JSONObject jsonResult =  JSONObject.fromObject(jsonObject.getString(key));
	    	AutoXima bXima =(AutoXima) JSONObject.toBean(jsonResult, AutoXima.class);
	    	bXima.setPlatfrom(key=="pt"?"ptsw":key);
	    	list.add(bXima);
	    }
	   
	   return list;
   }
   
   
    public static List getData(String path){
    	String JsonContext = readFile(path);
    	return JSONArray.fromObject(JsonContext);
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException { 
        String JsonContext = JsonUtil.readFile("/json/BetSet.json");  
        JSONArray jsonArray = JSONArray.fromObject(JsonContext);  
        /*String s= java.net.URLDecoder.decode(JsonContext, "utf-8"); 
        JSONObject jsonArray = new JSONObject();*/  
  
        int size = jsonArray.size();  
        System.out.println("Size: " + size);  
        for(int  i = 0; i < size; i++){  
            JSONObject jsonObject = jsonArray.getJSONObject(i);  
            System.out.println("[" + i + "]sport_type=" + jsonObject.get("sport_type"));  
            System.out.println("[" + i + "]min_bet=" + jsonObject.get("min_bet"));  
            System.out.println("[" + i + "]max_bet=" + jsonObject.get("max_bet"));  
            System.out.println("[" + i + "]max_bet_per_match=" + jsonObject.get("max_bet_per_match"));  
              
        }  
        List<MorphDynaBean> listObject = jsonArray.toList(jsonArray);  
        for(int i = 0, j = listObject.size(); i < j ; i++){  
            System.out.println(listObject.get(i));  
        }  
        for(MorphDynaBean temp: listObject){  
            System.out.println(temp.get("sport_type"));  
        }  
    } 
}  