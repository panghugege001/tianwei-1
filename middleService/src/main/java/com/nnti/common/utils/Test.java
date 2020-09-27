package com.nnti.common.utils;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Test {
	public static void main(String[] args){
		JSONObject json = new JSONObject();
		try {
			json.put("account_ext_ref", "fdfdf");
			json.put("category", "TRANSFER");
			json.put("type", "CREDIT");
			json.put("balance_type", "CREDIT_BALANCE");
			json.put("external_ref", "ext1234");
			
			JSONObject obj = new JSONObject();
			obj.put("description", "账号元");
			obj.put("mypromokey", "promokey:");	
			//JSONArray array1 = new JSONArray();
			//System.out.print("obj meta数据："+obj);
			//array1.put(obj);	
			//System.out.print("array1数据："+obj);
			json.put("meta_data", obj);
			JSONArray array2 = new JSONArray();
			array2.put(json);
			
			
			System.out.print(array2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    
	

}
