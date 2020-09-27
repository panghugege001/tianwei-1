package dfh.service.implementations;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import dfh.action.vo.WeekSentVO;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.WeekSent;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.IWeekSentService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class WeekSentServiceImpl implements IWeekSentService {

	private Logger log = Logger.getLogger(WeekSentServiceImpl.class);
	
	private ProposalDao proposalDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public void excWeekSent(String loginname, Double bettotal, Date startTime, Date endTime) {
		//生成提案
		Users user = (Users) proposalDao.get(Users.class, loginname);
		if(user == null){
			return;
		}
		WeekSentVO weekSentVO = new WeekSentVO(user, bettotal);
		if(weekSentVO.getSentAmount() > 0){
			String pno = this.getSeqDao().generateProposalPno(ProposalType.WEEKSENT);
			String wsRemark = "投注额：" + bettotal + ";  (" + weekSentVO.getSentAmount() + ")";
			WeekSent weekSent = new WeekSent(loginname, bettotal, weekSentVO.getSentAmount(), WeekSentVO.times, "0", new Date(), "", 
					startTime, endTime, pno, wsRemark);
			String remark = "周周回馈";
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.WEEKSENT.getCode(), user.getLevel(), user.getLoginname(), 
					weekSentVO.getSentAmount(), user.getAgent(), ProposalFlagType.AUDITED.getCode(), Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			proposalDao.save(weekSent);
			proposalDao.save(proposal);
		}
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
