package com.nnti.common.utils;


import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.model.vo.GameResponseDTO;
import com.nnti.common.model.vo.GameRequestVo;
import com.nnti.common.model.vo.GameVo;
import com.nnti.common.security.AESUtil;
import net.sf.json.JSONObject;

public class SlotUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(SlotUtil.class);
	
	public static final String RESULT_SUCC = "SUCCESS";
	private static final String PLATFORM = "pt";
	private static final String ACCOUNTTYPE = "slot";	 //slot wallet
	
	public static final String GAME_CENTER_URL = "/wallet/getURLParams/";	
	public static final String GAME_CENTER_DEBIT = "/wallet/debit/";	  
	public static final String GAME_CENTER_CREDIT = "/wallet/credit/";	
	public static final String GAME_CENTER_BALANCE = "/wallet/getBalance/";
	public static final String GAME_CENTER_PLAYERBET = "/wallet/getPlayerSlotBetWinByTime/";



	
	/**
	 * 获取老虎机余额，如果用户不存在则创建用户，成功后返回0
	 * @param loginName
	 * @return
	 * */
	public static Double getBalance(String product,String loginName){
		String result = null;
	    ObjectMapper JSON = new ObjectMapper();

		try {
	    HashMap<String, String> map = slotMap.get(product);

		GameRequestVo gameRequestVo=new GameRequestVo();
		gameRequestVo.setProductContract(map.get("PRODUCT"));
		gameRequestVo.setProductKey(map.get("PRODUCTKEY"));
		gameRequestVo.setAccountType(ACCOUNTTYPE);
		gameRequestVo.setOriginalName(loginName);
		
		result = sendHttpByAES(map.get("APIURL")+GAME_CENTER_BALANCE,gameRequestVo);

		GameVo gameVo = JSON.readValue(result, GameVo.class);
	           
	     if (StringUtils.equals(gameVo.getCode(), "10000")) {
	    	
	    	 JSONObject object=JSONObject.fromObject(gameVo.getData());
			
	    	 return object.getDouble("slot") ;
		  }
	     
	     log.info(loginName+"getBalance error --> "+gameVo.getMessage());
	     
	    } catch (Exception e) {
	    	log.error("执行getBalance方法发生异常，异常信息：" + e.getMessage());
		}
		
		return null;
	}
	
	
	
	/**
	 * 转入老虎机账户
	 * @param loginName
	 * @param amount
	 * @return
	 * */
	public static String transferToSlot(String product,String loginName, Double amount){
		String result = null;
	    ObjectMapper JSON = new ObjectMapper();
	    
		try {
			
		HashMap<String, String> map = slotMap.get(product);

		GameRequestVo gameRequestVo=new GameRequestVo();
		gameRequestVo.setProductContract(map.get("PRODUCT"));
		gameRequestVo.setProductKey(map.get("PRODUCTKEY"));
		gameRequestVo.setAccountType(ACCOUNTTYPE);
		gameRequestVo.setPlatform(PLATFORM);
		gameRequestVo.setAmount(amount);
		gameRequestVo.setOriginalName(loginName);
		
		result = sendHttpByAES(map.get("APIURL")+GAME_CENTER_CREDIT,gameRequestVo);
		
		log.info("transferToSlot --> loginName="+loginName+" "+result.toString());
		
		GameVo  gameVo = JSON.readValue(result, GameVo.class);
	     if (StringUtils.equals(gameVo.getCode(), "10000")) {			
		     return RESULT_SUCC ;
		 }
		
	      return gameVo.getMessage();
	      
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("执行transferToSlot方法发生异常，异常信息：" + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * 从老虎机账户转出
	 * @param loginName
	 * @param amount
	 * @return
	 * */
	public static String transferFromSlot(String product,String loginName, Double amount){
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		 
		try {
			HashMap<String, String> map = slotMap.get(product);

			GameRequestVo gameRequestVo=new GameRequestVo();
			gameRequestVo.setProductContract(map.get("PRODUCT"));
			gameRequestVo.setProductKey(map.get("PRODUCTKEY"));
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setPlatform(PLATFORM);
			gameRequestVo.setAmount(amount);
			gameRequestVo.setOriginalName(loginName);
	
		   result = sendHttpByAES(map.get("APIURL")+GAME_CENTER_DEBIT,gameRequestVo);
		   
		   log.info("transferFromSlot --> loginName="+loginName+" "+result.toString());

		   GameVo  gameVo = JSON.readValue(result, GameVo.class);
		   
		  if (StringUtils.equals(gameVo.getCode(), "10000")) {			
			  return RESULT_SUCC ;
		    }
		  
		  return gameVo.getMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("执行transferFromSlot方法发生异常，异常信息：" + e.getMessage());
		}
		
		return null;
	}

	/**
	 * 根据用户某个及其实践获取ptsky流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
	public static Double getPlayerBetsByTime(String product,String loginName,String timeBegin,String timeEnd){
		String result=null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			HashMap<String, String> map = slotMap.get(product);
			
			GameRequestVo gameRequestVo=new GameRequestVo();
			gameRequestVo.setProductContract(map.get("PRODUCT"));
			gameRequestVo.setProductKey(map.get("PRODUCTKEY"));
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setPlatform(PLATFORM);
			gameRequestVo.setTimeBegin(timeBegin);
			gameRequestVo.setTimeEnd(timeEnd);
			gameRequestVo.setOriginalName(loginName);
			
			result = sendHttpByAES(map.get("APIURL")+GAME_CENTER_PLAYERBET,gameRequestVo);
            
 		   log.info("getPlayerBetsByTime --> loginName="+loginName+" "+result.toString());

		    GameVo  gameVo = JSON.readValue(result, GameVo.class);
		
           if (StringUtils.equals(gameVo.getCode(), "10000")) {
        	   JSONObject jsonObject =  JSONObject.fromObject(gameVo.getData());
        	   
        	   JSONObject totalResult =  JSONObject.fromObject(jsonObject.get("slot")); //slot是总输赢
        	   
                return  totalResult==null?0.0:totalResult.getDouble("bet");
             }
		    
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	
	
	  /*** AES加密 支付发起请求 */
    public static String sendHttpByAES(String url, GameRequestVo reqVo) throws Exception {
    	ObjectMapper JSON = new ObjectMapper();
        String responseString = null;
        GameResponseDTO message = null;
        String requestJson = JSON.writeValueAsString(reqVo);
        requestJson = AESUtil.encrypt(requestJson);
        responseString = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair("requestData", requestJson));
        message = JSON.readValue(responseString, GameResponseDTO.class);
        if (message.getResponseData() != null && !"".equals(message.getResponseData())) {
            String decrypt = AESUtil.decrypt(message.getResponseData().toString());
            return decrypt;
        }
        return null;
    }
    

	
	/**
	 * 根据用户及其实践获取老虎机流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
	public static Double getBetsAmount(String loginname, String timeStart, String timeEnd){
		
		return null;
	}
	
	
	
	public static void main(String[] args) {
		
//		System.out.println(mobileGameLogin("test1", "holidayseasonmobile"));
//		System.out.println(getBalance("qy","devtest999"));
//		System.out.println(transferToSlot("ws","devtest999", 100.0));
//		System.out.println(tranferFromSlot("qytest123", 200.5));
		System.out.println(getPlayerBetsByTime("ws","devtest999", "2017-10-18 00:00:00","2017-10-20 12:00:00"));
    }

}
