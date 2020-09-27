package dfh.utils;

import java.util.Date;

import net.sf.json.JSONObject;

 public class TlyDepositUtil {
	 
	public static String orderUrl ="http://220.228.11.83:11080/authority/place_order.php";     //下单
	public static String revokeOrder ="http://220.228.11.83:11080/authority/revoke_order.php";  //撤销订单
	public static String query_bankcard ="http://220.228.11.83:11080//authority/query_bankcard.php";  //获取额度

    public static String product ="ld";
	  
	public  static JSONObject sendOrder(String order_id,String uaccountname,String ubankname,String ubankno,String bankcard,String meta,String amount,String comment,Date createtime) throws Exception{
	
		JSONObject json =new JSONObject();
		json.put("order_id",product+"_"+order_id);//自己的订单ID
		json.put("bank_flag", ubankname);
		json.put("card_number", bankcard);//收款卡卡号不能为空
		json.put("card_login_name", "");   // 收款卡的网银登录名，可以为空字符串
		json.put("pay_username", uaccountname);//付款人的姓名
		json.put("amount", amount);         // 付的金额， 必须为数字，不能四舍五入，会严格检查金额是否匹配
		json.put("create_time", createtime.getTime());
		json.put("comment", comment);       //存款附言
		json.put("product", product);       //产品标识号
		if(meta.equals("WX")){
			json.put("meta_data", "{\"QuickOrder\": \"WX2Bank\"}");  //微信特殊标记
		}
		
		String returnResult=HttpUtils.postRequest(orderUrl,json.toString());
		JSONObject reJsonObject =JSONObject.fromObject(returnResult);
		return reJsonObject;
	}
	 
	 public  static void  revoke_order(String order_id) throws Exception{
			JSONObject json =new JSONObject();
			json.put("id",order_id);//自己的订单ID
			json.put("product", product);       //产品标识号
			String returnResult=HttpUtils.postRequest(revokeOrder,json.toString());
			System.out.println(returnResult);
	 }

	 
	 public  static JSONObject  query_bankcard(String card_number,String bank_flag) throws Exception{
			JSONObject json =new JSONObject();
			json.put("card_number",card_number);
			json.put("bank_flag",bank_flag);
			json.put("product", product);       //产品标识号
			String returnResult=HttpUtils.postRequest(query_bankcard,json.toString());
			System.out.println("同略云卡号:"+card_number+",result="+returnResult);
			JSONObject reJsonObject =JSONObject.fromObject(returnResult);
			return reJsonObject;
	 }
	 
	 public static void main(String[] args) {
		try {
			System.out.println(TlyDepositUtil.sendOrder("lp0314_7_WX_uia95h", "王丽明", "CCB", "", "6236682290002704928", "WX","200.17", "", new Date()));
//			TlyDepositUtil.revoke_order("89491051");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
