package dfh.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

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