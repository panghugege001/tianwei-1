package dfh.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import org.apache.log4j.Logger;

import dfh.model.UpgradeLog;
import dfh.model.enums.VipLevel;
import dfh.model.notdb.UserUpgradeVO;
import dfh.service.interfaces.IAutoUpgradeService;

/**
 * 玩家自动升降级
 * 1.上月没有游戏记录的新会员，维持原级别
 * 2.上月没有游戏记录的忠实会员及以上的会员，降一级
 * 3.上月有游戏记录的，根据规则判断升降级
 */
public class AutoUpdateLevelUtil {
	
	public static Map<Integer, Map<String, Double>> relegationMap = new HashMap<Integer, Map<String,Double>>();
	
	public static String betRecordUrl = "http://69.172.86.107:6789/betRecordBatch.ssl";
	//上月有游戏记录的玩家
	private static final String betOfLastMonth = "select loginname, level, warnflag from users u where EXISTS(select 1 from agprofit a where a.createTime>=:start and a.createTime<=:agEnd and a.loginname=u.loginname) limit :offset, :batchCount";
	private static final String betOfLastMonthCount = "select count(*) from users u where EXISTS(select 1 from agprofit a where a.createTime>=:start and a.createTime<=:agEnd and a.loginname=u.loginname)";
	
	//上月没有游戏记录的忠实以上级别玩家
	private static final String noBetOfLastMonth = "select loginname, level, warnflag from users u where level>:level and not EXISTS(select 1 from agprofit a where a.createTime>=:start and a.createTime<=:agEnd and a.loginname=u.loginname)";
	
	//单次投注额查询最大玩家数量
	private static final Integer batchCount = 100;
	
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
	}
	
	private static Logger log = Logger.getLogger(AutoUpdateLevelUtil.class);	
	
	public static void autoUpgrade(IAutoUpgradeService autoUpgradeService){
		log.info("###############################会员自动升级开始############################");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String startTime = DateUtil.formatDateForStandard(calendar.getTime());
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		String endTime = DateUtil.formatDateForStandard(calendar.getTime());
		
		//因为agprofit的数据拿的是前一天的，为了不漏掉上月最后一天有游戏记录的人，所有设置截至时间为本月1号的23:59:59
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		String agprofitEndTime = DateUtil.formatDateForStandard(calendar.getTime());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", startTime);
		params.put("level", 0);  //新会员以上
		params.put("agEnd", agprofitEndTime);
		params.put("batchCount", 5000);
		
		//线程池，超出部分在队列中等待
		ExecutorService fixedThreadPool;
		//上月有投注额的玩家
		Integer total = autoUpgradeService.getCountBySql(betOfLastMonthCount, params);
		int times = total/Integer.parseInt(params.get("batchCount").toString()) + (total%Integer.parseInt(params.get("batchCount").toString())>0?1:0);
		for (int i = 0; i < times; i++) {
			params.put("offset", i*Integer.parseInt(params.get("batchCount").toString()));
			List list = autoUpgradeService.queryList(betOfLastMonth, params);
			fixedThreadPool = Executors.newFixedThreadPool(5);
			int batch = 0;
			StringBuilder usernameStr = new StringBuilder();
			Map<String, UserUpgradeVO> userConditionMap = new HashMap<String, UserUpgradeVO>();
			for (int j = 0; j < list.size(); j++) {
				Object[] item = (Object[])list.get(j);
				if(!VipLevel.TIANZUN.getCode().equals(Integer.parseInt(item[1].toString()))){
					batch++;
					if(batch < batchCount && j < list.size() -1){
						String userName = item[0].toString();
						usernameStr.append("'"+userName+"',");
						UserUpgradeVO uuVo = new UserUpgradeVO(Integer.parseInt(item[1].toString()), Integer.parseInt(item[2].toString()));
						userConditionMap.put(userName, uuVo);
					}else{
						String userName = item[0].toString();
						usernameStr.append("'"+userName+"'");
						UserUpgradeVO uuVo = new UserUpgradeVO(Integer.parseInt(item[1].toString()), Integer.parseInt(item[2].toString()));
						userConditionMap.put(userName, uuVo);
						try {
							//fixedThreadPool.execute(new UserUpGradeThread(autoUpgradeService, item[0].toString(), Integer.parseInt(item[1].toString()), Integer.parseInt(item[2].toString()), startTime, endTime));
							fixedThreadPool.execute(new UserUpGradeThreadBatch(autoUpgradeService, usernameStr.toString(), userConditionMap, startTime, endTime));
						} catch (RejectedExecutionException e) {
							log.error("------------------------会员自动升级，任务被拒绝：" + item[0].toString());
						} catch (Exception e) {
							log.error("------------------------自动升级线程池异常：" + e.getMessage());
						}
						batch = 0;
						usernameStr = new StringBuilder();
						userConditionMap = new HashMap<String, UserUpgradeVO>();
					}
				}
			}
			fixedThreadPool.shutdown();
			while (true) {
				//检测是否执行完毕
				if (fixedThreadPool.isTerminated()) {
					break;
				}
				try {
					//3秒检测一次
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
			}
		}

		List list = autoUpgradeService.queryList(noBetOfLastMonth, params);
		for (Object obj : list) {
			Object[] item = (Object[])obj;
			UpgradeLog upgradeLog = new UpgradeLog();
			upgradeLog.setUsername(item[0].toString());
			upgradeLog.setBet(0.0);
			upgradeLog.setPtBet(0.0);
			upgradeLog.setCreateTime(new Date());
			upgradeLog.setOldlevel(Integer.parseInt(item[1].toString()));
			upgradeLog.setNewlevel(Integer.parseInt(item[1].toString()));
			upgradeLog.setOptmonth(DateUtil.startYyyyMM());  //获取上一月
			upgradeLog.setStatus("1");
			try {
				autoUpgradeService.upgrade(upgradeLog, Integer.parseInt(item[2].toString()));
			} catch (Exception e) {
				log.error(item[0].toString() + "自动升级异常：" + e.getMessage());
			}

		}

		log.info("###############################会员自动升级结束############################");
	}
		
		/*//上月有投注额的玩家
		Integer total = autoUpgradeService.getCountBySql(betOfLastMonthCount, params);
		int times = total/Integer.parseInt(params.get("batchCount").toString()) + (total%Integer.parseInt(params.get("batchCount").toString())>0?1:0);
		for (int i = 0; i < times; i++) {
			params.put("offset", i*Integer.parseInt(params.get("batchCount").toString()));
			List list = autoUpgradeService.queryList(betOfLastMonth, params);
			fixedThreadPool = Executors.newFixedThreadPool(5);
			int batch = 0;
			StringBuilder usernameStr = new StringBuilder();
			Map<String, UserUpgradeVO> userConditionMap = new HashMap<String, UserUpgradeVO>();
			//for (Object obj : list) {
			for (int j = 0; j < list.size(); j++) {	
				Object[] item = (Object[])list.get(j);
				if(!VipLevel.ZHIZUN.getCode().equals(Integer.parseInt(item[1].toString()))){
					batch++;
					if(batch < batchCount && j < list.size() -1){
						String userName = item[0].toString();
						usernameStr.append(userName).append("#");
						UserUpgradeVO uuVo = new UserUpgradeVO(Integer.parseInt(item[1].toString()), Integer.parseInt(item[2].toString()));
						userConditionMap.put(userName, uuVo);
					}else{
						String userName = item[0].toString();
						usernameStr.append(userName);
						UserUpgradeVO uuVo = new UserUpgradeVO(Integer.parseInt(item[1].toString()), Integer.parseInt(item[2].toString()));
						userConditionMap.put(userName, uuVo);
						try {
							//fixedThreadPool.execute(new UserUpGradeThread(autoUpgradeService, item[0].toString(), Integer.parseInt(item[1].toString()), Integer.parseInt(item[2].toString()), startTime, endTime));
							fixedThreadPool.execute(new UserUpGradeThreadBatch(autoUpgradeService, usernameStr.toString(), userConditionMap, startTime, endTime));
						} catch (RejectedExecutionException e) {
							log.error("------------------------会员自动升级，任务被拒绝：" + item[0].toString());
						} catch (Exception e) {
							log.error("------------------------自动升级线程池异常：" + e.getMessage());
						}
						batch = 0;
						usernameStr = new StringBuilder();
						userConditionMap = new HashMap<String, UserUpgradeVO>();
					}
				}
			}
			fixedThreadPool.shutdown();  
		    while (true) {
		    	//检测是否执行完毕
				if (fixedThreadPool.isTerminated()) {
					break;
				}
				try {
					//3秒检测一次
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
			}  
		}
		
		// 上一月没有投注额的忠实以上的玩家
		List list = autoUpgradeService.queryList(noBetOfLastMonth, params);
		for (Object obj : list) {
			Object[] item = (Object[])obj;
			UpgradeLog upgradeLog = new UpgradeLog();
			upgradeLog.setUsername(item[0].toString());
			upgradeLog.setBet(0.0);
			upgradeLog.setPtBet(0.0);
			upgradeLog.setCreateTime(new Date());
			upgradeLog.setOldlevel(Integer.parseInt(item[1].toString()));
			upgradeLog.setNewlevel(Integer.parseInt(item[1].toString()) - 1);
			upgradeLog.setOptmonth(DateUtil.startYyyyMM());  //获取上一月
			upgradeLog.setStatus("1");
			try {
				autoUpgradeService.upgrade(upgradeLog, Integer.parseInt(item[2].toString()));
			} catch (Exception e) {
				log.error(item[0].toString() + "自动升级异常：" + e.getMessage());
			}
			
		}

		log.info("###############################会员自动升级结束############################");
	}*/
	
}
