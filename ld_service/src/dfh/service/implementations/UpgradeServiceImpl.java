package dfh.service.implementations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import dfh.dao.SeqDao;
import dfh.dao.SlaveDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.UpgradeLog;
import dfh.model.Users;
import dfh.model.bean.Bet;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.IUpgradeService;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class UpgradeServiceImpl implements IUpgradeService {

	//private Logger log = Logger.getLogger(AutoUpgradeServiceImpl.class);
	
	private UserDao userDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	private SlaveDao slaveDao;

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	private TradeDao tradeDao;
	private final String upgradeSql = "update users set level=:newLevel where loginname=:username";
	private final String upgradeGiftProposalSql = "select count(*) from proposal where loginname=:username and type=:pType and amount=:amount and flag<>:pFlag";
	private final String maxDedepositSql = "select MAX(amount) from proposal where loginname=:username and type=:pType and flag=:pStatus and createtime>=:start  and createtime<=:end";
	private final String upgradeSwitchStatus = "select COUNT(*) from const where id=:swithID and value=:swithValue";
	
	private static final Map<Integer, Double> upgradeGiftMap;
	 
	static{
		upgradeGiftMap = new HashMap<Integer, Double>();
	    upgradeGiftMap.put(1, 18.00);
	    upgradeGiftMap.put(2, 88.00); 
	    upgradeGiftMap.put(3, 188.00); 
	    upgradeGiftMap.put(4, 388.00); 
	    upgradeGiftMap.put(5, 888.00); 
	    upgradeGiftMap.put(6, 1888.00); 
	    upgradeGiftMap.put(7, 2888.00); 
	    upgradeGiftMap.put(8, 3888.00); 
	}
	
	public Double upgrade(UpgradeLog upgradeLog, Users user){
		//更新等级
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newLevel", upgradeLog.getNewlevel());
		params.put("username", upgradeLog.getUsername());
		userDao.excuteSql(upgradeSql, params);
		Double money = null;
		if(null != upgradeGiftMap.get(upgradeLog.getNewlevel())){
			//升级礼金
			params.put("pType", ProposalType.LEVELPRIZE.getCode());  //晋级礼金
			params.put("amount", upgradeGiftMap.get(upgradeLog.getNewlevel()));  
			params.put("pFlag", ProposalFlagType.CANCLED.getCode());   //不是取消状态
			if(userDao.getCount(upgradeGiftProposalSql, params) > 0){
				//已领取过改等级的晋级礼金
			}else{

				Double amount = upgradeGiftMap.get(upgradeLog.getNewlevel());
				Integer flagtype = 0;
				Proposal proposal = null;
				String pno = seqDao.generateProposalPno(ProposalType.LEVELPRIZE);
				Prize prize = new Prize(pno, "MONEY_CUSTOMER", user.getLoginname(), upgradeGiftMap.get(upgradeLog.getNewlevel()), "自助晋级，晋级礼金");
				if(amount >= 100){
					flagtype = ProposalFlagType.SUBMITED.getCode();
					 proposal = new Proposal(pno,  "system", DateUtil.now(), ProposalType.LEVELPRIZE.getCode(), upgradeLog.getNewlevel(), user.getLoginname(),
							upgradeGiftMap.get(upgradeLog.getNewlevel()), user.getAgent(), flagtype, Constants.FROM_BACK, "自助晋级，晋级礼金", null);
				}else {
					flagtype = ProposalFlagType.EXCUTED.getCode();
					proposal = new Proposal(pno,  "system", DateUtil.now(), ProposalType.LEVELPRIZE.getCode(), upgradeLog.getNewlevel(), user.getLoginname(),
							upgradeGiftMap.get(upgradeLog.getNewlevel()), user.getAgent(), flagtype, Constants.FROM_BACK, "晋级礼金小于等于100元，系统自动审核执行", null);

					proposal.setExecuteTime(DateUtil.now());

					tradeDao.changeCredit(prize.getLoginname(), Double.valueOf(Math.abs(prize.getTryCredit().doubleValue())),
							CreditChangeType.LEVELPRIZE.getCode(), pno, "晋级礼金小于等于100元，系统自动审核执行");
				}


				userDao.save(prize);
				userDao.save(proposal);
				taskDao.generateTasks(pno, "system");
				money = upgradeGiftMap.get(upgradeLog.getNewlevel());
			}
		}
		
		userDao.save(upgradeLog);
		return money;
	}
	
	@Override
	public Double getMaxDepoist(String username, String startTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("pType", ProposalType.CASHIN.getCode());   //存款
		params.put("pStatus", ProposalFlagType.EXCUTED.getCode());  //已执行
		params.put("start", startTime);
		params.put("end", endTime);
		
		return userDao.getDoubleValue(maxDedepositSql, params);
	}

	@Override
	public void save(Object obj) {
		userDao.save(obj);
	}

	@Override
	public Integer isUpgradeClosed() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("swithID", "自助晋级");
		params.put("swithValue", "0");   //关闭状态
		return userDao.getCount(upgradeSwitchStatus, params);
	}
	
	@Override
	public Double getDepositAmount(String username, String startTime, String endTime) {
		Double amount;
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select sum(money) from payorder where loginname=:loginname and type=:orderType and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit";
		params.put("loginname", username);
		params.put("orderType", 0);  //支付成功的订单
		params.put("startTimeDeposit", startTime);
		params.put("endTimeDeposit", endTime);
		amount = userDao.getDoubleValue(sql, params);
		
		sql = "select sum(amount) from proposal where loginname=:loginname and type=:pType and flag=:optFlag and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit";
		params.put("pType", 502);   //存款提案
		params.put("optFlag", 2);   //已执行
		amount = Arith.add(amount, userDao.getDoubleValue(sql, params));
		
		return amount;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public SlaveDao getSlaveDao() {
		return slaveDao;
	}

	public void setSlaveDao(SlaveDao slaveDao) {
		this.slaveDao = slaveDao;
	}

	@Override
	public Object get(Class clazz, Serializable id) {
		return userDao.get(clazz, id);
	}

	
	@Override
	public LinkedList<Bet> getBetsAllPlatform(String sql, Map<String, Object> params) {
		final LinkedList<Bet> list = new LinkedList<Bet>();
		for(final Object o : slaveDao.queryListBySql(sql, params)){
			Object[] arr = (Object[]) o;
			list.add(new Bet((String)arr[1], (Double)arr[0]));
		}
		return list;
	}

	@Override
	public Map<String, Double> getTotalBetByUser(String username, String start, String end) {
		String totalBet = "select sum(bettotal) from agprofit where loginname=:loginname and createTime>:start and createTime<:end";
		String slotsBet = "select sum(bettotal) from agprofit where loginname=:loginname and createTime>:start and createTime<:end and platform in(:platform)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", username);
		params.put("start", start);
		params.put("end", end);
		params.put("platform", new String[]{"newpt", "ttg", "qt", "nt", "mg", "aginslot", "dt", "png", "sw"});
		Map<String, Double> betsMap = new HashMap<String, Double>();
		betsMap.put("all", slaveDao.getDoubleValue(totalBet, params));
		betsMap.put("pt", slaveDao.getDoubleValue(slotsBet, params));
		return betsMap;
	}
	
	@Override
	public List findByCriteria(DetachedCriteria criteria) {
		return this.userDao.findByCriteria(criteria);
	}
}
