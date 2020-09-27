package dfh.utils;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.model.Users;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.GameRequestVo;
import dfh.model.bean.GameVo;
import dfh.model.bean.GameResponseDTO;
import dfh.model.bean.XimaDataVo;
import dfh.model.enums.VipLevel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SlotUtil {

	private static Logger log = Logger.getLogger(SlotUtil.class);
	
	private static final String PRODUCT = "ld";
	private static final String PLATFORM = "pt";
	private static final String PRODUCTKEY = "8f515ef3135e688706fb8f83e1ff72f5";	
	private static final String ACCOUNTTYPE = "slot";	 //slot wallet
	private static final String FISH_ACCOUNTTYPE = "fish";	 //fish wallet
	
	public static final String GAME_CENTER_URL = "http://ldalletcall.com/wallet/getURLParams/";	
	public static final String GAME_CENTER_DEBIT = "http://ldalletcall.com/wallet/debit/";	  
	public static final String GAME_CENTER_CREDIT = "http://ldalletcall.com/wallet/credit/";	
	public static final String GAME_CENTER_BALANCE = "http://ldalletcall.com/wallet/getBalance/";
	public static final String GAME_CENTER_BET = "http://ldalletcall.com/wallet/getBetWinAllByDay/";
	public static final String GAME_CENTER_PLAYERBET = "http://ldalletcall.com/wallet/getPlayerSlotBetWinByTime/";

	
	private static final String DT_PLATFORM = "dt";
	public static final String MG_PLATFORM = "mg";
	public static final String NT_PLATFORM = "nt";
	public static final String PNG_PLATFORM = "png";
	public static final String QT_PLATFORM = "qt";



	/**
	 * 公共查询余额方法
	 * @param loginName 用户名
	 * @param acccountType 类型
	 * @return
	 */
	public static GameVo publicBalance(String loginName,String acccountType){
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameRequestVo gameRequestVo=new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(acccountType);
			gameRequestVo.setOriginalName(loginName);
			result = sendHttpByAES(GAME_CENTER_BALANCE,gameRequestVo);
			GameVo gameVo = JSON.readValue(result, GameVo.class);
			return gameVo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取FISH余额，如果用户不存在则创建用户，成功后返回0
	 * @param loginName
	 * @return
	 * */
	public static Double getFishBalance(String loginName){
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameVo gameVo = publicBalance(loginName,FISH_ACCOUNTTYPE);
			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONObject object=JSONObject.fromObject(gameVo.getData());
				return object.getDouble("fish") ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 获取老虎机账户余额，如果用户不存在则创建用户，成功后返回0
	 * @param loginName
	 * @return
	 * */
	public static Double getBalance(String loginName){
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {

			GameVo gameVo = publicBalance(loginName,ACCOUNTTYPE);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {

				JSONObject object=JSONObject.fromObject(gameVo.getData());

				return object.getDouble("slot") ;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 转入PNG账户
	 * @param loginName
	 * @param amount
	 * @return
	 * */
	public static String transferToSlot(String loginName, Double amount){
		String result = null;
		 ObjectMapper JSON = new ObjectMapper();
		try {
			
		GameRequestVo gameRequestVo=new GameRequestVo();
		gameRequestVo.setProductContract(PRODUCT);
		gameRequestVo.setProductKey(PRODUCTKEY);
		gameRequestVo.setAccountType(ACCOUNTTYPE);
		gameRequestVo.setPlatform(PLATFORM);
		gameRequestVo.setAmount(amount);
		gameRequestVo.setOriginalName(loginName);
	
		
		result = sendHttpByAES(GAME_CENTER_CREDIT,gameRequestVo);
		GameVo  gameVo = JSON.readValue(result, GameVo.class);
	     if (StringUtil.equals(gameVo.getCode(), "10000")) {			
		     return "转账成功" ;
		 }
		
	      return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 从PNG账户转出
	 * @param loginName
	 * @param amount
	 * @return
	 * */
	public static String tranferFromSlot(String loginName, Double amount){
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		 
		try {
			
			GameRequestVo gameRequestVo=new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setPlatform(PLATFORM);
			gameRequestVo.setAmount(amount);
			gameRequestVo.setOriginalName(loginName);
		
		   result = sendHttpByAES(GAME_CENTER_DEBIT,gameRequestVo);
		   GameVo  gameVo = JSON.readValue(result, GameVo.class);
		  if (StringUtil.equals(gameVo.getCode(), "10000")) {			
			return "转账成功" ;
		  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
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
	 * 根据用户及其实践获取ptsky流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
	public static List<Bean4Xima> getBetsAmount(String day){
		String result=null;
		ObjectMapper JSON = new ObjectMapper();
		List<Bean4Xima>list=new ArrayList<>();
		try {
		GameRequestVo gameRequestVo=new GameRequestVo();
		gameRequestVo.setProductContract(PRODUCT);
		gameRequestVo.setProductKey(PRODUCTKEY);
		gameRequestVo.setPlatform(PLATFORM);
		gameRequestVo.setDay(day);
        result = sendHttpByAES(GAME_CENTER_BET,gameRequestVo);
		GameVo  gameVo = JSON.readValue(result, GameVo.class);
		
         if (StringUtil.equals(gameVo.getCode(), "10000")) {
            	JSONArray jsonArray=JSONArray.fromObject(gameVo.getData());
            	 for (Object object : jsonArray) {
                     JSONObject jsonObject = (JSONObject) object;
                     Bean4Xima ximaData = new Bean4Xima(jsonObject.getString("originalName"), 
                    		 jsonObject.getDouble("bet"), jsonObject.getDouble("win"));
                     list.add(ximaData);
                 }
	     }
         
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	/**
	 * 根据用户及其实践获取ptsky流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
	public static Double getPlayerBetsByTime(String loginName,String timeBegin,String timeEnd){
		String result=null;
		ObjectMapper JSON = new ObjectMapper();
		try {
		GameRequestVo gameRequestVo=new GameRequestVo();
		gameRequestVo.setProductContract(PRODUCT);
		gameRequestVo.setProductKey(PRODUCTKEY);
		gameRequestVo.setAccountType(ACCOUNTTYPE);
		gameRequestVo.setPlatform(PLATFORM);
		gameRequestVo.setOriginalName(loginName);
		gameRequestVo.setTimeBegin(timeBegin);
		gameRequestVo.setTimeEnd(timeEnd);
        result = sendHttpByAES(GAME_CENTER_PLAYERBET,gameRequestVo);
		GameVo  gameVo = JSON.readValue(result, GameVo.class);
		
         if (StringUtil.equals(gameVo.getCode(), "10000")) {
        	 JSONObject jsonObject =  JSONObject.fromObject(gameVo.getData());
                return  jsonObject.getDouble("bet");
             }
		    
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * 根据日期跟平台获取该游戏平台流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
	public static List<Bean4Xima> getBetsAmount(String day, String platform) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		List<Bean4Xima> list = new ArrayList<>();
		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setPlatform(platform);
			gameRequestVo.setDay(day);
				result = sendHttpByAES(GAME_CENTER_BET, gameRequestVo);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONArray jsonArray = JSONArray.fromObject(gameVo.getData());
				for (Object object : jsonArray) {
					JSONObject jsonObject = (JSONObject) object;

					Bean4Xima ximaData = new Bean4Xima(jsonObject.getString("originalName"),
							jsonObject.getDouble("bet"), jsonObject.getDouble("win"));
					list.add(ximaData);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return list;
	}
	
	/**
	 * 根据用户及其实践获取dt流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
	public static List<Bean4Xima> getDTBetsAmount(String day) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		List<Bean4Xima> list = new ArrayList<>();
		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setPlatform(DT_PLATFORM);
			gameRequestVo.setDay(day);
				result = sendHttpByAES(GAME_CENTER_BET, gameRequestVo);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONArray jsonArray = JSONArray.fromObject(gameVo.getData());
				for (Object object : jsonArray) {
					JSONObject jsonObject = (JSONObject) object;

					Bean4Xima ximaData = new Bean4Xima(jsonObject.getString("originalName"),
							jsonObject.getDouble("bet"), jsonObject.getDouble("win"));
					list.add(ximaData);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return list;
	}
	
	
	//反水上限
	public static double getRebateLimit(Users user) {
		double rebateLimit = 8888.0;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			rebateLimit = 18888;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			rebateLimit = 18888;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			rebateLimit = 28888;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			rebateLimit = 28888;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			rebateLimit = 88888; 
		}
		return rebateLimit;
	}
	
	//老虎机反水比例
	public static double getSlotRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
	
	//真人反水比例
	public static double getLiveRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
	
	public static void main(String[] args) {
		
//		System.out.println(mobileGameLogin("test1", "holidayseasonmobile"));
//		System.out.println(getBalance("devtest999"));
//		System.out.println(transferToSlot("qytest123", 200.0));
//		System.out.println(flashGameLogin("qytest123","pt","sw_gol","127.0.0.1","127.0.0.1","real"));
//		System.out.println(tranferFromSlot("qytest123", 200.5));
//		System.out.println(getBetsAmount("2017-09-19").size());
		System.out.println(getPlayerBetsByTime(null, "2017-10-16 00:00:00", "2017-10-16 12:00:00"));
    }

}
