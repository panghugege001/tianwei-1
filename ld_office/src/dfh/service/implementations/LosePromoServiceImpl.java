package dfh.service.implementations;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import dfh.action.vo.ProfitVO;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.model.LosePromo;
import dfh.model.PTBigBang;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.ILosePromoService;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class LosePromoServiceImpl implements ILosePromoService {

	private Logger log = Logger.getLogger(LosePromoServiceImpl.class);
	
	private ProposalDao proposalDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	
	//PT大爆炸各级别最高礼金
	private static Map<Integer, Double> ptBigBangBonusMap = new HashMap<Integer, Double>();
	static {
		ptBigBangBonusMap.put(0, 168.00);
		ptBigBangBonusMap.put(1, 168.00);
		ptBigBangBonusMap.put(2, 288.00);
		ptBigBangBonusMap.put(3, 588.00);
		ptBigBangBonusMap.put(4, 888.00);
		ptBigBangBonusMap.put(5, 1888.00);
		ptBigBangBonusMap.put(6, 5888.00);
	}
	
	@Override
	public void distributeLosePromo(String loginname, Double winlose, Date startTime, Date endTime) {
		//前一天的存款额
		Double depositAmount = proposalDao.getDepositAmountByUserYes(loginname, startTime);
		if(depositAmount < 100){
			log.info("用户：" + loginname + "，前一天存款额不足100元，不予反赠");
			return;
		}
		//扣除所得红利
		Double deduct = 0.00;
		String sql = "select SUM(amount) from proposal where loginname=:username and flag=:pstatus and TO_DAYS(executeTime) = TO_DAYS(:date) and (giftamount is null or giftamount=:amount) and type not in(:depositcode, :withdrawalcode)";
		Map<String, Object> pramas = new HashMap<String, Object>();
		pramas.put("username", loginname);
		pramas.put("pstatus", 2);
		pramas.put("date", startTime);
		pramas.put("amount", 0);
		pramas.put("depositcode", 502);
		pramas.put("withdrawalcode", 503);
		//pramas.put("systemPromo", 507);  //系统反水（前一天的系统反水，第二天才会派发，需要第二天扣除）
		deduct = proposalDao.getDoubleValueBySql(sql, pramas);
		
		sql = "select SUM(giftamount) from proposal where loginname=:username and flag=:pstatus and TO_DAYS(executeTime) = TO_DAYS(:date) and giftamount>:amount";
		deduct = Arith.add(deduct, proposalDao.getDoubleValueBySql(sql, pramas));
		
		if(winlose - deduct < 50){
			log.info("用户：" + loginname + "，扣除红利后负盈利不足50元，不予反赠");
			return;
		}
		//计算反赠
		Users user = (Users) proposalDao.get(Users.class, loginname);
		if(user == null){
			return;
		}
		ProfitVO profitVo = new ProfitVO(user, winlose - deduct);
		String pno = this.getSeqDao().generateProposalPno(ProposalType.PROFIT);
		String profitRemark = "存款：" + depositAmount + ";  (" + winlose + "-" + deduct + ")*" + profitVo.getRate();
		LosePromo losePromo = new LosePromo(loginname, winlose, deduct, profitVo.getRate(), profitVo.getProfitAmouont(), 10, "0", new Date(), DateUtil.fmtyyyyMMdd(startTime), "pttiger", pno, profitRemark);
		String remark = "负盈利反赠";
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.PROFIT.getCode(), user.getLevel(), user.getLoginname(), profitVo.getProfitAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
		//this.getTaskDao().generateTasks(pno, "system");
		proposalDao.save(losePromo);
		proposalDao.save(proposal);	
	}

	@Override
	public void calculatePTBigBangGifgMoney(PTBigBang item) {
		if(Math.abs(item.getNetWinOrLose()) < 10.0){
			item.setRemark("净赢/净输不足10元，不予派发PT疯狂奖金");
			item.setGiftMoney(0.00);
			item.setStatus("3");
		}else{
			//扣除所得红利
			Double deduct = 0.00;
			String sql = "select SUM(amount) from proposal where loginname=:username and flag=:pstatus and TO_DAYS(executeTime) = TO_DAYS(:activityEndTime) and executeTime<=:activityEndTime and (giftamount is null or giftamount=:amount) and type not in(:depositcode, :withdrawalcode)";
			Map<String, Object> pramas = new HashMap<String, Object>();
			pramas.put("username", item.getUsername());
			pramas.put("pstatus", 2);
			//pramas.put("date", new Date());
			pramas.put("activityEndTime", item.getEndTime());
			pramas.put("amount", 0);
			pramas.put("depositcode", 502);
			pramas.put("withdrawalcode", 503);
			deduct = proposalDao.getDoubleValueBySql(sql, pramas);
			
			sql = "select SUM(giftamount) from proposal where loginname=:username and flag=:pstatus and TO_DAYS(executeTime) = TO_DAYS(:activityEndTime) and executeTime<=:activityEndTime and giftamount>:amount";
			deduct = Arith.add(deduct, proposalDao.getDoubleValueBySql(sql, pramas));
			item.setBonus(deduct);
			Double giftMoney = Arith.sub(Math.abs(item.getNetWinOrLose()), deduct);
			if(giftMoney < 10.00){
				item.setRemark("扣除红利后净赢/净输不足10元，不予派发PT疯狂奖金");
				item.setGiftMoney(0.00);
				item.setStatus("3");
			}else{
				Users user = (Users) proposalDao.get(Users.class, item.getUsername());
				item.setGiftMoney(giftMoney > ptBigBangBonusMap.get(user.getLevel())? ptBigBangBonusMap.get(user.getLevel()):giftMoney);
				item.setTimes(item.getNetWinOrLose()>0.00?20:30);
				item.setDistributeTime(new Date());
				item.setStatus("1");
			}
		}
		proposalDao.update(item);
	}
	
	@Override
	public String getIntroCode(String userName) {
		String result;
		String sql = "select intro from users where loginname=:username";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userName);
		result = (String)proposalDao.getOneObjectBySql(sql, params);
		if(result == null){
			//查询代理
			sql = "select agent from users where loginname=:username";
			result = (String)proposalDao.getOneObjectBySql(sql, params);
		}
		return result;
	}
	
	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
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

	@SuppressWarnings("unchecked")
	@Override
	public List getListBySql(String sql, Map<String, Object> params) throws Exception {
		return proposalDao.getListBySql(sql, params);
	}
}
