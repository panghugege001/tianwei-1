package dfh.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RoleTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8383308293176261364L;
	private final static Map<String,String> roles=new HashMap<String, String>();
	private final static Map<String,String> departments=new HashMap<String, String>();
	static {
		roles.put("sale_manager", "sale");
		roles.put("finance_manager", "finance");
		roles.put("market_manager", "market");
		roles.put("card_manager", "card");
		
		departments.put("sale_manager", "客服部");
		departments.put("finance_manager", "财务部");
		departments.put("market_manager", "市场部");
		departments.put("card_manager", "卡管组");
		departments.put("admin", "办公室");
		departments.put("sale", "客服部");
		departments.put("finance", "财务部");
		departments.put("market", "市场部");
		departments.put("card", "卡管组");
	}
	
	public static String getDepartmentName(String key){
		return departments.get(key);
	}
	
	public static String getRole(String key){
		return roles.get(key);
	}
}
