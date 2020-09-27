package dfh.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.SMSSwitch;
import dfh.model.UpgradeLog;
import dfh.model.Users;
import dfh.model.bean.Bet;
import dfh.model.enums.DXWErrorCode;
import dfh.model.enums.SMSContent;
import dfh.service.interfaces.IUpgradeService;

/**
 * 会员升级工具类
 *
 */
public class UpgradeUtil {

	private static Logger log = Logger.getLogger(UpgradeUtil.class);
	//月投注额/月pt投注额
	private static Map<Integer, Map<String, Double>> relegationMap = new HashMap<Integer, Map<String,Double>>();
	//周投注额
	private static Map<Integer, Double> relegationWeekMap = new HashMap<Integer, Double>();
	//存款
	private static Map<Integer, Double> depositMap = new HashMap<Integer, Double>();
	
	
	static{
		Map<String, Double> tmp = new HashMap<String,Double>();
		tmp.put("all", 10*10000.0);
		tmp.put("pt", 10*10000.0);
		relegationMap.put(1, tmp); /// 天将
		
		tmp = new HashMap<String,Double>();
		tmp.put("all", 30*10000.0);
		tmp.put("pt", 60*10000.0);
		relegationMap.put(2, tmp); /// 天王
		
		tmp = new HashMap<String,Double>();
		tmp.put("all", 80*10000.0);
		tmp.put("pt", 120*10000.0);
		relegationMap.put(3, tmp);   ///星君
		
		tmp = new HashMap<String,Double>();
		tmp.put("all", 180*10000.0);
		tmp.put("pt", 200*10000.0);
		relegationMap.put(4, tmp);   ///真君
		
		tmp = new HashMap<String,Double>();
		tmp.put("all", 400*10000.0);
		tmp.put("pt", 500*10000.0);
		relegationMap.put(5, tmp);   ///仙君
		
		tmp = new HashMap<String,Double>();
		tmp.put("all", 800*10000.0);
		tmp.put("pt", 1000*10000.0);
		relegationMap.put(6, tmp);   ///帝君
		
		tmp = new HashMap<String,Double>();
		tmp.put("all", 1500*10000.0);
		tmp.put("pt", 1000*10000.0);
		relegationMap.put(7, tmp);   ///天尊
		
		relegationWeekMap.put(2, 40*10000.0);
		relegationWeekMap.put(3, 100*10000.0);
		relegationWeekMap.put(4, 200*10000.0);
		relegationWeekMap.put(5, 300*10000.0);
		
		depositMap.put(1, 50*10000.0);
		depositMap.put(2, 50*10000.0);
		depositMap.put(3, 50*10000.0);
		depositMap.put(4, 50*10000.0);
		depositMap.put(5, 50*10000.0);
		depositMap.put(6, 50*10000.0);
		depositMap.put(7, 50*10000.0);
		
	}
	
	/**
	 * 获取所有平台的投注额
	 * @param username
	 * @param start
	 * @param end
	 * @return
	 */
	public static LinkedList<Bet> getBetByDate(IUpgradeService upgradeService, String username, String type){
		
		String sql = "select SUM(bettotal), platform from agprofit where loginname=:loginname and createTime>:start and createTime<:end GROUP BY platform";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", username);
		Map<String, String> timeMap = generateTime4Local();
		params.put("start", timeMap.get("start"));
		params.put("end", timeMap.get("end"));
		LinkedList<Bet> betList = upgradeService.getBetsAllPlatform(sql, params);
		Double all = 0.0;
		for (Bet bet : betList) {
			all = Arith.add(all, bet.getBet());
		}
		betList.add(new Bet("合计", all));
		//加入存款总额
		//Double depositAmount = upgradeService.getDepositAmount(username, timeMap.get("start").toString(), timeMap.get("end").toString());
		//betList.add(new Bet("累计存款额", depositAmount));
		return betList;
	}
	
	
	public static String checkUpgrade(IUpgradeService upgradeService, String username, String type){
		if(upgradeService.isUpgradeClosed()>0){
			return "自助晋级暂未开启";
		}
		
		Map<String, String> timeMap = generateTime4Local();
		Map<String, Double> betMap = upgradeService.getTotalBetByUser(username, timeMap.get("start").toString(), timeMap.get("end").toString());
		if(null==betMap){
			return "获取投注额异常";
		}
		Users user = (Users) upgradeService.get(Users.class, username);
		Integer newLevel;
		UpgradeLog upgradeLog = new UpgradeLog();
		upgradeLog.setBet(betMap.get("all"));
		upgradeLog.setPtBet(betMap.get("pt"));
		newLevel = getNewLevel(betMap, user.getLevel());
		 
		if(newLevel > user.getLevel()){
			upgradeLog.setUsername(username);
			upgradeLog.setCreateTime(new Date());
			upgradeLog.setOldlevel(user.getLevel());
			upgradeLog.setNewlevel(newLevel);
			SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");
			upgradeLog.setOptmonth(YYYY_MM.format(new Date()));
			upgradeLog.setStatus("1");
//			upgradeService.upgrade(upgradeLog, user);
			Double money = null;
			try {
				money = upgradeService.upgrade(upgradeLog, user);
				if(money != null){
					UpgradeUtil.sendSMS(user, money, upgradeService);
				}
			} catch (Exception e) {
				log.error("晋级失败：",e);
				return "晋级失败：自助晋级每月只能执行一次，如果您确认是本月第一次操作，请稍后再执行。";
			}
		}else{
			//判断存款
			//Double maxDeposit = upgradeService.getMaxDepoist(username, timeMap.get("start").toString(), timeMap.get("end").toString());
			//newLevel = getNewLevel(maxDeposit, user.getLevel(), depositMap);
			Double depositAmount = upgradeService.getDepositAmount(username, timeMap.get("start").toString(), timeMap.get("end").toString());
			newLevel = getNewLevel(depositAmount, user.getLevel(), depositMap);
			if(newLevel > user.getLevel()){
				upgradeLog.setUsername(username);
				upgradeLog.setCreateTime(new Date());
				upgradeLog.setOldlevel(user.getLevel());
				upgradeLog.setNewlevel(newLevel);
				SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");
				upgradeLog.setOptmonth(YYYY_MM.format(new Date()));
				upgradeLog.setStatus("1");
				upgradeLog.setRemark("当月累计存款：" + depositAmount + ".升级");
				upgradeService.upgrade(upgradeLog, user);
				//upgradeService.save(upgradeLog);  //通过存款达成的升级，需要人工确认
				//return "最大单笔存款已达到升级要求，请等待我们的升级确认";
			}else{
				return "未达到升级要求";
			}
		}
		return null;
	}
	
	public static void sendSMS(Users user, Double money, IUpgradeService upgradeService) {
		
		try {
			String service = user.getAddress();
			boolean sendflag = false;
			DetachedCriteria dc = DetachedCriteria.forClass(SMSSwitch.class);
			dc.add(Restrictions.eq("disable", "否"));
			dc.add(Restrictions.eq("type", "2"));
			List<SMSSwitch> list = upgradeService.findByCriteria(dc);
			if(list != null && list.size() > 0 && StringUtils.isNotBlank(service)){
				SMSSwitch smsswitch = list.get(0);
				
				String[] ids = service.split(",");
				if(ids != null && ids.length > 0){
					for(int i = 0; i < ids.length; i++){
						
						if(smsswitch.getType().equals(ids[i])){
							sendflag = true;
						}
					}
				}
				
				if(sendflag){
					System.out.println("发送晋级礼金短信start");
					String phoneno = null;
					String content = SMSContent.getText("2").replace("$MONEY", money + "").replace("$XXX", user.getLoginname().substring(0, 3) + "****");
					try {
						phoneno = AESUtil.aesDecrypt(user.getPhone(),AESUtil.KEY);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(StringUtils.isNotBlank(phoneno)){
						String code = SmsDXWUtils.sendSms(phoneno, content);
						if("1".equals(code)){
							System.out.println("发送成功");
						} else {
							String msg = DXWErrorCode.getText(code);
							System.out.println(msg);
						}
					} else {
						System.out.println("用户手机号码为空");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	private static Integer getNewLevel(Map<String, Double> betMap, Integer level){
		try {
			Double allBet = betMap.get("all");
//			Double ptBet = betMap.get("pt");
			if(allBet == 0.0){
				return level;
			}
			for(int i=7; i>level; i--){
				if(allBet >= relegationMap.get(i).get("all")){
					return i;
				}
			}
			return level;
		} catch (Exception e) {
			log.debug("计算新等级异常：" + e.getMessage(), e);
			return level;
		}
	}
	
	/**
	 * 根据周投注额或最大一笔存款计算新级别
	 * @return
	 */
	private static Integer getNewLevel(Double amount, Integer level, Map<Integer, Double> upgradeAmountMap){
		for(int i=7; i>level; i--){
			if(amount >= upgradeAmountMap.get(i)){
				return i;
			}
		}
		return level;
	}
	
	private static Map<String, String> generateTime(String type){
		Map<String, String> timeMap = new HashMap<String, String>();
		Calendar calendar = Calendar.getInstance();
		if(type.equalsIgnoreCase("month")){
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			timeMap.put("start", DateUtil.formatDateForStandard(calendar.getTime()));
			
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			timeMap.put("end", DateUtil.formatDateForStandard(calendar.getTime()));
		}else if(type.equalsIgnoreCase("week")){
			calendar.set(Calendar.DAY_OF_WEEK, 2);  //本周的第一天，写1获取到的是周日，2获取到的是周一
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			timeMap.put("start", DateUtil.formatDateForStandard(calendar.getTime()));
			
			calendar.set(Calendar.DAY_OF_WEEK, 7);                //程序里一周的最后一天是周六，不符合中国人习惯，所以+1
			calendar.add(Calendar.DATE, 1);
	        calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			timeMap.put("end", DateUtil.formatDateForStandard(calendar.getTime()));
		}
		return timeMap;
	}
	
	private static Map<String, String> generateTime4Local(){
		Map<String, String> timeMap = new HashMap<String, String>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		timeMap.put("start", DateUtil.formatDateForStandard(calendar.getTime()));
		
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		timeMap.put("end", DateUtil.formatDateForStandard(calendar.getTime()));
		return timeMap;
	}
}
