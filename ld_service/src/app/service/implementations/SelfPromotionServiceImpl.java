package app.service.implementations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import dfh.dao.TaskDao;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.UpgradeLog;
import dfh.model.Users;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.utils.Constants;
import app.dao.BaseDao;
import app.model.vo.SelfPromotionVO;
import app.service.interfaces.IConstService;
import app.service.interfaces.ISelfPromotionService;
import app.service.interfaces.ISequenceService;
import app.util.DateUtil;

public class SelfPromotionServiceImpl implements ISelfPromotionService {

	private static Logger log = Logger.getLogger(SelfPromotionServiceImpl.class);
	
	private static Map<Integer, Map<String, Double>> relegationMap = new HashMap<Integer, Map<String, Double>>();
	private static Map<Integer, Double> upgradeMap = new HashMap<Integer, Double>();
	
	private BaseDao baseDao;
	private TaskDao taskDao;
	private IConstService constService;
	private ISequenceService sequenceService;
	
	static {
	
		// 0=新会员/1=忠实VIP/2=青龙VIP/3=银龙VIP/4=金龙VIP/5=御龙VIP
		
		Map<String, Double> tmp = new HashMap<String, Double>();
		tmp.put("all", 10 * 10000.0);
		relegationMap.put(1, tmp);

		tmp = new HashMap<String, Double>();
		tmp.put("all", 60 * 10000.0);
		relegationMap.put(2, tmp);

		tmp = new HashMap<String, Double>();
		tmp.put("all", 120 * 10000.0);
		relegationMap.put(3, tmp);

		tmp = new HashMap<String, Double>();
		tmp.put("all", 200 * 10000.0);
		relegationMap.put(4, tmp);
		
		// 晋级礼金：青龙/588、银龙/888、金龙/1888、御龙/2888
		
		upgradeMap.put(2, 588.00); 
		upgradeMap.put(3, 888.00); 
		upgradeMap.put(4, 1888.00); 
		upgradeMap.put(5, 2888.00);
	}
	
	@SuppressWarnings("rawtypes")
	public List<SelfPromotionVO> querySelfPromotionList(SelfPromotionVO vo) {
	
		List<SelfPromotionVO> returnList = new LinkedList<SelfPromotionVO>();
		
		String sql = "select SUM(bettotal), platform from agprofit where loginname=:loginname and createTime>:start and createTime<:end GROUP BY platform WITH ROLLUP";
					  
		Map<String, String> timeMap = generateTime();
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("loginname", vo.getLoginName());
		params.put("start", timeMap.get("start"));
		params.put("end", timeMap.get("end"));
		
		List list = baseDao.list(sql, params);
		
		if (null != list && !list.isEmpty()) {
		
			for (int i = 0, len = list.size(); i < len; i++) {
			
				Object[] arr = (Object[]) list.get(i);
				
				if (null != arr[1]) {
				
					returnList.add(new SelfPromotionVO(String.valueOf(arr[1]), Double.valueOf(String.valueOf(arr[0]))));
				} else {
				
					returnList.add(new SelfPromotionVO("合计", Double.valueOf(String.valueOf(arr[0]))));
				}				
			}
		}
		
		return returnList;
	}
	
	public String checkUpgrade(SelfPromotionVO vo) {
	
		String value = constService.queryConstValue("自助晋级");
		
		if ("0".equals(value)) {
		
			return "自助晋级暂未开启！";
		}
	
		String loginName = vo.getLoginName();
		
		Map<String, String> timeMap = generateTime();
		
		Map<String, Double> betMap = getTotalBetByUser(loginName, timeMap.get("start"), timeMap.get("end"));
		
		if (null == betMap) {
			
			return "获取投注额异常！";
		}
		
		Users user = (Users) baseDao.get(Users.class, loginName);
		
		if (null == user) {
		
			return "获取玩家信息异常！";
		}
		
		log.info("玩家【" + loginName +"】在【" + timeMap.get("start") + "-" + timeMap.get("end") + "】的有效投注额为：" + betMap.get("all"));
		
		Integer level = user.getLevel();
		
		Integer newLevel = getNewLevel(betMap, level);
		log.info("玩家【" + loginName+ "】当前等级为：" + level + ",新的等级为：" + newLevel);
		
		if (newLevel > level) {

			DateFormat format = new SimpleDateFormat("yyyy-MM");

			UpgradeLog upgradeLog = new UpgradeLog();
			upgradeLog.setBet(betMap.get("all"));
			upgradeLog.setPtBet(betMap.get("pt"));
			upgradeLog.setUsername(loginName);
			upgradeLog.setCreateTime(new Date());
			upgradeLog.setOldlevel(level);
			upgradeLog.setNewlevel(newLevel);
			upgradeLog.setOptmonth(format.format(new Date()));
			upgradeLog.setStatus("1");

			String userSql = "update users set level=:newLevel where loginname=:username";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("newLevel", newLevel);
			params.put("username", loginName);
			
			baseDao.executeUpdate(userSql, params);
			
			if (null != upgradeMap.get(newLevel)) {

				// 晋级类别编码
				params.put("pType", ProposalType.LEVELPRIZE.getCode());
				// 晋级礼金
				params.put("amount", upgradeMap.get(newLevel));
				// 不是取消状态
				params.put("pFlag", ProposalFlagType.CANCLED.getCode());
				
				String proposalSql = "select count(1) from proposal where loginname=:username and type=:pType and amount=:amount and flag<>:pFlag";
				
				Object obj = baseDao.uniqueResult(proposalSql, params);
				int num = null == obj ? 0 : Integer.parseInt(obj.toString());
				
				// 如果num不等于0，则代表当前玩家已领取过该等级的晋级礼金
				if (num == 0) {
					
					String pno = sequenceService.generateProposalNo(ProposalType.LEVELPRIZE.getCode());
					
					Prize prize = new Prize(pno, "MONEY_CUSTOMER", user.getLoginname(), upgradeMap.get(newLevel), "自助晋级，晋级礼金");

					Proposal proposal = new Proposal(pno, "system", DateUtil.getCurrentTime(), ProposalType.LEVELPRIZE.getCode(), 
							                         newLevel, user.getLoginname(), upgradeMap.get(newLevel), user.getAgent(), 
							                         ProposalFlagType.SUBMITED.getCode(), Constants.FROM_BACK, "自助晋级，晋级礼金", null);
					
					baseDao.save(prize);
					baseDao.save(proposal);
					taskDao.generateTasks(pno, "system");
					baseDao.save(upgradeLog);
				}
			}
		} else {
		
			return "您本月投注额未达到晋级要求，请再接再厉！";
		}
		
		return null;
	}
	
	private static Map<String, String> generateTime() {
		
		Map<String, String> timeMap = new HashMap<String, String>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		// 设置开始时间，时间值为当年当月的2号00点00分00秒
		timeMap.put("start", DateUtil.getDateFormat(calendar.getTime()));

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		// 设置结束时间，时间值为当年(当月+1)的1号23点59分59秒
		timeMap.put("end", DateUtil.getDateFormat(calendar.getTime()));
		
		return timeMap;
	}
	
	private Map<String, Double> getTotalBetByUser(String loginName, String startTime, String endTime) {
		
		String totalBet = "select sum(bettotal) from agprofit where loginname=:loginname and createTime>:start and createTime<:end";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginName);
		params.put("start", startTime);
		params.put("end", endTime);
		
		Object obj = baseDao.uniqueResult(totalBet, params);
		Double all = null == obj ? 0.00 : Double.parseDouble(obj.toString());
		
		Map<String, Double> betsMap = new HashMap<String, Double>();
		
		betsMap.put("all", all);
		betsMap.put("pt", all);
		
		return betsMap;
	}
	
	// 根据投注额重新计算玩家级别
	private static Integer getNewLevel(Map<String, Double> betMap, Integer level) {
		
		Double allBet = betMap.get("all");
		
		if (allBet == 0.0) {
			
			return level;
		}
		
		// 产品要求：当玩家等级大于3，则从后台手动给玩家升级
		for (int i = 4; i > level; i--) {
			
			if (allBet >= relegationMap.get(i).get("all")) {
				
				return i;
			}
		}
		
		return level;
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public IConstService getConstService() {
		return constService;
	}
	
	public void setConstService(IConstService constService) {
		this.constService = constService;
	}
	
	public ISequenceService getSequenceService() {
		return sequenceService;
	}
	
	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
	
}