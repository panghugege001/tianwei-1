package dfh.utils;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import dfh.model.UpgradeLog;
import dfh.service.interfaces.IAutoUpgradeService;

public class UserUpGradeThread extends Thread{

	private static Logger log = Logger.getLogger(UserUpGradeThread.class);
	
	private IAutoUpgradeService autoUpgradeService;
	private String username;
	private Integer level;
	private Integer warnflag;
	private String startTime;
	private String endTime;
	private int i;
	
	public UserUpGradeThread(IAutoUpgradeService autoUpgradeService, String username, Integer level, Integer warnflag, String startTime, String endTime){
		this.autoUpgradeService = autoUpgradeService;
		this.username = username;
		this.level = level;
		this.warnflag = warnflag;
		this.startTime = startTime;
		this.endTime = endTime;
		this.i = 0;
	}
	
	private void upgrade(){
		UpgradeLog upgradeLog = new UpgradeLog();
		upgradeLog.setUsername(username);
		upgradeLog.setCreateTime(new Date());
		upgradeLog.setOldlevel(level);
		upgradeLog.setOptmonth(DateUtil.startYyyyMM());  //获取上一月
		upgradeLog.setStatus("1");
		
		try {
			Integer newLevel = getNewLevel(upgradeLog);
			if(newLevel != null){
				upgradeLog.setNewlevel(newLevel);
				autoUpgradeService.upgrade(upgradeLog, warnflag);
			}
		} catch (Exception e) {
			log.debug(username + "自动升级异常：" + e.getMessage());
		}
		
	}
	
	/**
	 * 调用接口查询游戏投注额
	 * @return
	 */
	private Map<String, Double> getTotalBetByUser(){
		HttpClient httpClient = HttpUtils.createHttpClientLong();
		PostMethod method = new PostMethod(AutoUpdateLevelUtil.betRecordUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		Map<String, Double> betMap = new HashMap<String, Double>();
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("account", username),
			    new NameValuePair("product", "ld"),
			    new NameValuePair("starttime", startTime),
			    new NameValuePair("endtime", endTime)
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			JSONObject json = JSONObject.fromObject(result);
			if(json.get("code").toString().equalsIgnoreCase("1")){
				/*JSONArray jArr = json.getJSONArray("volist");
				for (int i = 0; i < jArr.size(); i++) {
					JSONObject item = jArr.getJSONObject(i);
					allBet = Arith.add(allBet, Double.parseDouble(item.getString("gamebet")));
					if(item.getString("platform").equalsIgnoreCase("pt")){
						ptBet = Double.parseDouble(item.getString("gamebet"));
					}
				}*/
				betMap.put("all", json.getDouble("bet"));
				betMap.put("pt", json.getDouble("ptBet"));
			}
			return betMap;
		} catch (SocketTimeoutException e) {
			log.error(e.getMessage());
			try {
				//超时,睡眠5秒,重新调用一次
				Thread.sleep(5000);
				if(i==0){
					i++;
					return getTotalBetByUser();
				}
			} catch (InterruptedException e1) {
				log.error(e.getMessage(), e);
			}
		} catch (HttpException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 根据投注额计算新级别
	 * @param username
	 * @param level
	 * @param starttime
	 * @param endtime
	 * @param upgradeLog
	 * @return
	 */
	private Integer getNewLevel(UpgradeLog upgradeLog){
		try {
			Map<String, Double> betMap = getTotalBetByUser();
			if(null == betMap){
				return null;
			}
			Double allBet = betMap.get("all");
			Double ptBet = betMap.get("pt");
			upgradeLog.setBet(allBet);
			upgradeLog.setPtBet(ptBet);
			if(allBet == 0.0){
				if(level==0){
					return level;
				}else{
					return level - 1;
				}
			}
			if(level==6){
				//不处理至尊级别
				return 6;
			}
			if(level==0 || allBet >= AutoUpdateLevelUtil.relegationMap.get(level).get("all")*0.2 || ptBet >= AutoUpdateLevelUtil.relegationMap.get(level).get("pt")*0.15){
				for(int i=5; i>level; i--){
					if(allBet >= AutoUpdateLevelUtil.relegationMap.get(i).get("all") || ptBet >= AutoUpdateLevelUtil.relegationMap.get(i).get("pt")){
						return i;
					}
				}
				return level;
			}else{
				return level - 1;
			}
		} catch (Exception e) {
			log.debug("####################### "+ username + " 计算新等级异常：" + e.getMessage(), e);
			return level;
		}
		
	}
	
	public IAutoUpgradeService getAutoUpgradeService() {
		return autoUpgradeService;
	}

	public void setAutoUpgradeService(IAutoUpgradeService autoUpgradeService) {
		this.autoUpgradeService = autoUpgradeService;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getWarnflag() {
		return warnflag;
	}

	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public void run() {
		upgrade();
	}
}
