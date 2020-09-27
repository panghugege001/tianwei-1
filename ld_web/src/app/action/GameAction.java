package app.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import app.util.AESUtil;
import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;

public class GameAction extends SubActionSupport {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(GameAction.class);
	
	private String token;
	private String platform;
	private String tips;//由APP端传过来的，判断是否第一次打开大厅，是否需要弹出小提示
	private String gameCode;
	
	
	
	/**
	 * 验证域名
	 */
    public void verifyDomain(){
    	
    	String domain = this.getRequest().getScheme()+"://"+this.getRequest().getServerName();
    	
    	log.info("app验证域名  ：" + domain);

    	Map<String,String> jsonMap = new HashMap<String,String>();
    	
    	jsonMap.put("code","10000");
    	GsonUtil.GsonObject(jsonMap);
    	
    }
	
	
	
	public String enterGameHall() throws Exception {
		
		log.info("enterGameHall方法获取的参数值为：【token=" + token + "】");
		
		String params = "";
			
			if(StringUtils.isEmpty(token)){
				
				getHttpSession().setAttribute("msg", "身份验证失败");
				return "error" ;
			}
		
			try {
				
				token = token.replaceAll("%5R", "\r").replaceAll("%5N", "\n");
				
				params = AESUtil.getInstance().decrypt(token);  
			} catch (Exception e) {
				
				log.error("enterGameHall方法解密参数发生异常，异常信息为：" + e.getMessage());
				//throw e;
				getHttpSession().setAttribute("msg", "身份验证失败");
				return "error" ;
			}
			
			JSONObject json = JSONObject.fromObject(params);
			
			String loginName = String.valueOf(json.get("loginName"));
			String password = String.valueOf(json.get("password")); 
			//String platform = String.valueOf(json.get("platform"));
			String hotGameType = String.valueOf(json.get("hotGameType"));
			String hotGameId = String.valueOf(json.get("hotGameId"));
			String timestamp = String.valueOf(json.get("timestamp"));
			String ip = String.valueOf(json.get("ip"));
			
			if("null".equals(hotGameId) || StringUtils.isEmpty(hotGameId)){
				
				hotGameId = "";
			}
			
			long timestampSub = (new Date().getTime() - Long.parseLong(timestamp));
			
			if( timestampSub >= 1000*60*3){
				
				getHttpSession().setAttribute("msg", "身份验证失败");
				return "error" ;
			}
			
			Users user = null;
			
			try {
				
				user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { loginName }, Users.class);
			} catch (AxisFault e) {
				
				log.error("enterGameHall方法调用webservice方法getUser发生异常，异常信息为：" + e.getMessage());
				throw e;
				
			}
			
			getHttpSession().setAttribute(Constants.PT_SESSION_USER, password);
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			getHttpSession().setAttribute("loginName", user.getLoginname().toUpperCase());
			getHttpSession().setAttribute("platform",platform);
			getHttpSession().setAttribute("tips",tips);
			getHttpSession().setAttribute("hotGameType",hotGameType);
			getHttpSession().setAttribute("hotGameId",hotGameId);
			
			
			log.info("platform = " + platform + ",tips = " + tips + " ,hotGameType = " + hotGameType + " , hotGameId = " + hotGameId);
			
			if(StringUtils.isNotEmpty(hotGameId)){//热门游戏
				
				getRequest().setAttribute("gameCode", hotGameId);
				getRequest().setAttribute("fromApp","app");//原生app进入pt游戏，出错或者返回大厅时返回到原生的大厅
				
				return "pth5Login";
			}
			
			return "view";
			
		}
	
	
	
    public String ptH5Login(){
    	
	  	  try {
	  	   Users customer = getCustomerFromSession();
	  	   if (customer == null || customer.getRole().equals("AGENT")) {
	  	    return ERROR;
	  	   }
	  	   getRequest().setAttribute("gameCode", gameCode);
	       getRequest().setAttribute("fromApp",(String)getRequest().getParameter("fromApp"));//原生app进入pt游戏，出错或者返回大厅时返回到原生的大厅
	       getHttpSession().setAttribute("loginName", customer.getLoginname().toUpperCase());
	  	   return SUCCESS;
	  	  } catch (Exception e) {
	  	   log.error(e.getCause());
	  	   return ERROR;
	  	  }
  }
	
	
	
	/**
	 * 给前端调试用的
	 * @return
	 * @throws Exception
	 */
	public String enterGameHall_test() throws Exception {
		
			//给前端调整页面，先把参数写死
			String	 loginName = "woodytest";
			String	 password = "admin_admin";
			Users user = null;
				
			try {
				
				user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { loginName }, Users.class);
			} catch (AxisFault e) {
				
				log.error("enterGameHall方法调用webservice方法getUser发生异常，异常信息为：" + e.getMessage());
				throw e;
			}
			getHttpSession().setAttribute(Constants.PT_SESSION_USER, password);
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			getHttpSession().setAttribute("platform",platform);
			getHttpSession().setAttribute("tips",tips);
			
			log.info("测试platform = " + platform + ",tips = " + tips);
			return "view";
	
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	
}