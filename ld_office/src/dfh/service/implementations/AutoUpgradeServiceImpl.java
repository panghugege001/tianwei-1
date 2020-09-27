package dfh.service.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.UpgradeLog;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.IAutoUpgradeService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class AutoUpgradeServiceImpl implements IAutoUpgradeService {

	//private Logger log = Logger.getLogger(AutoUpgradeServiceImpl.class);
	
	private UserDao userDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	private TradeDao tradeDao;

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	private final String upgradeSql = "update users set level=:newLevel where loginname=:username";
	private final String upgradeGiftProposalSql = "select count(*) from proposal where loginname=:username and type=:pType and amount=:amount and flag<>:pFlag";
	
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
	
	@Override
	public Integer getCountBySql(String sql, Map<String, Object> params) {
		return userDao.getCount(sql, params);
	}
	
	@Override
	public List queryList(String sql, Map<String, Object> params) {
		return userDao.queryList(sql, params);
	}
	
	public void upgrade(UpgradeLog upgradeLog, Integer warnflag){
		if(!upgradeLog.getOldlevel().equals(upgradeLog.getNewlevel())){
			//更新等级
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("newLevel", upgradeLog.getNewlevel());
			params.put("username", upgradeLog.getUsername());

			userDao.excuteSql(upgradeSql, params);
			if(upgradeLog.getNewlevel() > upgradeLog.getOldlevel()){
				//升级礼金
				params.put("pType", ProposalType.LEVELPRIZE.getCode());  //晋级礼金
				params.put("amount", upgradeGiftMap.get(upgradeLog.getNewlevel()));  
				params.put("pFlag", ProposalFlagType.CANCLED.getCode());   //不是取消状态
				if(userDao.getCount(upgradeGiftProposalSql, params) > 0){
					//已领取过改等级的晋级礼金
				}else{
					Users user = (Users) userDao.get(Users.class, upgradeLog.getUsername());
					String pno = seqDao.generateProposalPno(ProposalType.LEVELPRIZE);
					Prize prize = new Prize(pno, "MONEY_CUSTOMER", user.getLoginname(), upgradeGiftMap.get(upgradeLog.getNewlevel()), "自动晋级，晋级礼金");
					Double amount = upgradeGiftMap.get(upgradeLog.getNewlevel());
					Integer flagtype = 0;
					Proposal proposal = null;
					if(amount > 100){
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
				}
			}
		}
		userDao.save(upgradeLog);
	}
	
	@Override
	public void optUpgrade(UpgradeLog upgradeLog, String targetAction) {
		upgradeLog.setStatus(targetAction);
		userDao.update(upgradeLog);
		if(targetAction.equals("1")){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("newLevel", upgradeLog.getNewlevel());
			params.put("username", upgradeLog.getUsername());
			userDao.excuteSql(upgradeSql, params);
			//晋级礼金
			params.put("pType", ProposalType.LEVELPRIZE.getCode());  //晋级礼金
			params.put("amount", upgradeGiftMap.get(upgradeLog.getNewlevel()));  
			params.put("pFlag", ProposalFlagType.CANCLED.getCode());   //不是取消状态
			if(userDao.getCount(upgradeGiftProposalSql, params) > 0){
				//已领取过改等级的晋级礼金
			}else{
				Users user = (Users) userDao.get(Users.class, upgradeLog.getUsername());
				String pno = seqDao.generateProposalPno(ProposalType.LEVELPRIZE);
				Prize prize = new Prize(pno, "MONEY_CUSTOMER", user.getLoginname(), upgradeGiftMap.get(upgradeLog.getNewlevel()), "自动晋级，晋级礼金");
				Proposal proposal = new Proposal(pno,  "system", DateUtil.now(), ProposalType.LEVELPRIZE.getCode(), upgradeLog.getNewlevel(), user.getLoginname(),
						upgradeGiftMap.get(upgradeLog.getNewlevel()), user.getAgent(), ProposalFlagType.SUBMITED.getCode(), Constants.FROM_BACK, "自动晋级，晋级礼金。该条礼金需要先确认该玩家的升级后才能审核执行", null);
				userDao.save(prize);
				userDao.save(proposal);
				taskDao.generateTasks(pno, "system");
			}
		}
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

}
