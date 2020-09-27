package app.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import app.model.vo.AppCustomVersionVO;
import app.util.AESUtil;
import dfh.action.SubActionSupport;
import app.enums.CacheEnums;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.OnlineToken;
import dfh.model.PayOrderValidation;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
/**
 * 品牌APP在线支付页面嵌入token验证和session注入
 * @author lin
 *
 */
public class AppCommonAction extends SubActionSupport {
	private Logger log = Logger.getLogger(AppCommonAction.class);
	
	/**
	 * app进入在线支付页面
	 * @return
	 */
	public String appPayOnline(){
		try {
			String token = getRequest().getParameter("token");
			
			//给前端调试 先把参数写死
			if(token == null){
/*				Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {"woodytest555555"}, Users.class);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				return "appPayindex";*/
				getHttpSession().setAttribute("msg", "身份验证失败");
				return "error" ;
			}else{
				
/*				String params = "";
				
				try {
					
					
					token = token.replaceAll("%5R", "\r").replaceAll("%5N", "\n");
					
					params = AESUtil.getInstance().decrypt(token);  
				} catch (Exception e) {
					
					log.error("appPayOnline方法解密参数发生异常，异常信息为：" + e.getMessage());
					//throw e;
					getHttpSession().setAttribute("msg", "身份验证失败");
					return "error" ;
				}
				
				
				JSONObject json = JSONObject.fromObject(params);
				
				String login = String.valueOf(json.get("login"));
				String ip = String.valueOf(json.get("ip")); 
				
				String ipFrom = getIp();
				
				if(!StringUtils.equals(ip, ipFrom)){
					
					getHttpSession().setAttribute("msg", "身份验证失败");
					return "error" ;
				}
				
				token = login;*/
				
				OnlineToken onlineToken = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getToken", new Object[] { token }, OnlineToken.class);
				if(null != onlineToken && (new Date().getTime() - DateUtil.parseDateForStandard(onlineToken.getTempCreatetime()).getTime()  ) <= 1000*60*3){
					Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {onlineToken.getLoginname()}, Users.class);
					getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
					return "appPayindex";
				}else{
					getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
					getHttpSession().setAttribute("msg", "身份验证失败");
					return "error" ;
				}
				
			}
			
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		getHttpSession().setAttribute("msg", "身份验证失败");
		return "error" ;
	}
	/**
	 * app进入微社区
	 * @return
	 */
	public String appCommunity(){
		try {
			String token = getRequest().getParameter("token");
			OnlineToken onlineToken = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getToken", new Object[] { token }, OnlineToken.class);
			if(onlineToken != null){
				
				Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {onlineToken.getLoginname()}, Users.class);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				
			}else{
				
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, new Users());
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 获取定制版本信息
	 */
	public void getAppVersionCustomInfo() {
		List<AppCustomVersionVO> result = new ArrayList<AppCustomVersionVO>();
		try {
			String baseWebsite = this.getRequest().getScheme();
			if (StringUtils.endsWithIgnoreCase(baseWebsite, "s")) {
				baseWebsite = baseWebsite.substring(0, baseWebsite.length() - 1);
			}
			baseWebsite = baseWebsite + "://" + this.getRequest().getServerName();
			// log.info("当前站点" + baseWebsite);

			AppCustomVersionVO[] versionData = AxisUtil.getArrayObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "AppWebService", false), AxisUtil.APPSERVICE_NAMESPACE,
					"getAppCustomVersionData", baseWebsite, AppCustomVersionVO[].class);

			if (versionData != null && versionData.length > 0) {
				for (AppCustomVersionVO version : versionData)
					result.add(version);
			}

			GsonUtil.GsonObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从缓存中获取定制版本信息
	 */
	/*
	public void getAppVersionCustomInfo() {
		List<AppCustomVersionVO> result = new ArrayList<AppCustomVersionVO>();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		try {
			String appVersionCustomJson = RedisUtil.get(CacheEnums.CACHEENUMS_APPPAK.getCode());
			if (StringUtils.isNotBlank(appVersionCustomJson)) {
				String baseWebsite = this.getRequest().getScheme();
				if(StringUtils.endsWithIgnoreCase(baseWebsite, "s")){
					baseWebsite = baseWebsite.substring(0, baseWebsite.length()-1);
				}
				baseWebsite= baseWebsite + "://" + this.getRequest().getServerName();
				log.info("当前站点" + baseWebsite);
				List<AppCustomVersionVO> AppCustomVersionList = gson.fromJson(appVersionCustomJson,new TypeToken<List<AppCustomVersionVO>>() {}.getType());
				if (AppCustomVersionList != null && !AppCustomVersionList.isEmpty()) {
					log.info("------------------App定制打包存在缓存数据！");
					for (AppCustomVersionVO appCustomVersion : AppCustomVersionList) {
						if(appCustomVersion.getAgentAddresses() != null){
							for (String agentAddress : appCustomVersion.getAgentAddresses()) {
								if (StringUtils.equalsIgnoreCase(agentAddress, baseWebsite)) {
									result.add(appCustomVersion);
									break;
								}
							}
						}
						if (result.size() == 2) {
							break;
						}
					}
				}
			}

			GsonUtil.GsonObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}