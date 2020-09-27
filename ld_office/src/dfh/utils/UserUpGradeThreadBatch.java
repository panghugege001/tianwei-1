package dfh.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import dfh.model.UpgradeLog;
import dfh.model.notdb.UserUpgradeVO;
import dfh.service.interfaces.IAutoUpgradeService;

public class UserUpGradeThreadBatch extends Thread {

private static Logger log = Logger.getLogger(UserUpGradeThreadBatch.class);
	
	private IAutoUpgradeService autoUpgradeService;
	private String usernameStr;
	private Map<String, UserUpgradeVO> userConditionMap = new HashMap<String, UserUpgradeVO>();
	private String startTime;
	private String endTime;
	private int i;
	
	public UserUpGradeThreadBatch(IAutoUpgradeService autoUpgradeService, String usernameStr, Map<String, UserUpgradeVO> userConditionMap, String startTime, String endTime){
		this.autoUpgradeService = autoUpgradeService;
		this.usernameStr = usernameStr;
		this.userConditionMap = userConditionMap;
		this.startTime = startTime;
		this.endTime = endTime;
		this.i = 0;
	}
	
	private void upgrade(){
		List list = getPartBetsAmount();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			String userName = (String) obj[0];
			Double slotBet = (Double) obj[1];
			Double bet = (Double) obj[2];
			UpgradeLog upgradeLog = new UpgradeLog();
			upgradeLog.setUsername(userName);
			upgradeLog.setBet(bet);
			upgradeLog.setPtBet(slotBet);
			upgradeLog.setCreateTime(new Date());
			Integer oldLevel = userConditionMap.get(userName).getLevel();
			upgradeLog.setOldlevel(oldLevel);
			upgradeLog.setOptmonth(DateUtil.startYyyyMM());  //获取上一月
			upgradeLog.setStatus("1");

			try {
				Integer newLevel = getNewLevel(upgradeLog, oldLevel, bet,slotBet);
				if(newLevel != null){
					upgradeLog.setNewlevel(newLevel);
					autoUpgradeService.upgrade(upgradeLog, userConditionMap.get(userName).getWarnflag());
				}
			} catch (Exception e) {
				log.debug(userName + "自动升级异常：" + e.getMessage());
			}
		}
	}

	private List getPartBetsAmount(){

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String startTime = DateUtil.formatDateForStandard(calendar.getTime());


		Calendar calendare = Calendar.getInstance();
		calendare.add(Calendar.MONTH, 0);
		calendare.set(Calendar.DAY_OF_MONTH, 1);
		calendare.set(Calendar.HOUR_OF_DAY, 23);
		calendare.set(Calendar.MINUTE, 59);
		calendare.set(Calendar.SECOND, 59);
		String endTime = DateUtil.formatDateForStandard(calendare.getTime());

		StringBuffer sbf = new StringBuffer();
		sbf.append("select t3.loginname,SUM(t3.slotbet),SUM(t3.allbet) from ( ");
		sbf.append("SELECT ");
		sbf.append("	t1.loginname, ");
		sbf.append("	CONCAT(SUM(t1.bettotal),'') slotbet, ");
		sbf.append("	'' allbet ");
		sbf.append("FROM ");
		sbf.append("	agprofit t1 ");
		sbf.append("WHERE ");
		sbf.append("	t1.createTime >=:startTime ");
		sbf.append("AND t1.createTime <=:endTime ");
		sbf.append("AND t1.platform IN ('newpt','qt','nt','dt','mg','ttg','png','aginslot', 'sw') ");
		sbf.append("AND t1.loginname IN (" + usernameStr + ") ");
		sbf.append("GROUP BY ");
		sbf.append("	t1.loginname ");
		sbf.append("UNION ");
		sbf.append("	SELECT ");
		sbf.append("		t2.loginname, ");
		sbf.append("		'' slotbet, ");
		sbf.append("		CONCAT(SUM(t2.bettotal),'') allbet ");
		sbf.append("	FROM ");
		sbf.append("		agprofit t2 ");
		sbf.append("	WHERE ");
		sbf.append("		t2.createTime >=:startTime ");
		sbf.append("	AND t2.createTime <=:endTime ");
		sbf.append("AND t2.loginname IN (" + usernameStr + ") ");
		sbf.append("	GROUP BY ");
		sbf.append("		t2.loginname ");
		sbf.append(")t3 ");
		sbf.append("GROUP BY ");
		sbf.append("	t3.loginname ");
		String sql = sbf.toString();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		//上月有投注额的玩家
		List betList = autoUpgradeService.queryList(sql, params);

		return betList;
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
	private Integer getNewLevel(UpgradeLog upgradeLog, Integer level, Double allBet, Double slotBet){
		try {		
			if(allBet == 0.0){
				if(level==0){
					return level;
				}else{
					return level ;
				}
			}
			if(level==8){
				return level;
			}
			//|| slotBet >= AutoUpdateLevelUtil.relegationMap.get(level).get("pt")*0.3
			if(level==0 || allBet >= AutoUpdateLevelUtil.relegationMap.get(level).get("all")*0.2 ){
				for(int i=7; i>level; i--){
					//|| slotBet >= AutoUpdateLevelUtil.relegationMap.get(i).get("pt")
					if(allBet >= AutoUpdateLevelUtil.relegationMap.get(i).get("all") ){
						return i;
					}
				}
				return level;
			}else{
				return level ;
			}
		} catch (Exception e) {
			log.debug("计算新等级异常：" + e.getMessage(), e);
			return level;
		}
		
	}
	
	public IAutoUpgradeService getAutoUpgradeService() {
		return autoUpgradeService;
	}

	public void setAutoUpgradeService(IAutoUpgradeService autoUpgradeService) {
		this.autoUpgradeService = autoUpgradeService;
	}

	public String getUsernameStr() {
		return usernameStr;
	}

	public void setUsernameStr(String usernameStr) {
		this.usernameStr = usernameStr;
	}

	public Map<String, UserUpgradeVO> getUserConditionMap() {
		return userConditionMap;
	}

	public void setUserConditionMap(Map<String, UserUpgradeVO> userConditionMap) {
		this.userConditionMap = userConditionMap;
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
