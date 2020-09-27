package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;

import dfh.action.vo.XimaVO;
import dfh.dao.GameinfoDao;
import dfh.dao.LogDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.IGameinfoService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public abstract class AbstractGameinfoServiceImpl implements IGameinfoService {
	
	private GameinfoDao gameinfoDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	private ProposalService proposalService;
	private UserDao userDao;
	protected StringBuffer sf=null;
	private TradeDao tradeDao;
	private LogDao logDao;
	

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public GameinfoDao getGameinfoDao() {
		return gameinfoDao;
	}

	public void setGameinfoDao(GameinfoDao gameinfoDao) {
		this.gameinfoDao = gameinfoDao;
	}

	protected void destory(){
		try {
			Thread.sleep(3000);
			this.sf=null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	protected List<XimaVO> getXimaObject(Date starttime,Date endtime) throws Exception {
		// TODO Auto-generated method stub
		List<Object[]> ximaObjectList = gameinfoDao.getXimaObject(starttime, endtime);
		if (ximaObjectList==null) {
			return null;
		}
		List<XimaVO> ximaList=new ArrayList<XimaVO>();
		for (Object[] objects : ximaObjectList) {
			ximaList.add(new XimaVO((Double)objects[1], String.valueOf(objects[0])));
		}
		return ximaList;
	}

	
	@Override
	public void updateXimaStatus() throws Exception {
		// TODO Auto-generated method stub
		// 由子类重写
	}

	@Override
	public void autoAddXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		//由子类重写
	}
	
	@Override
	public void excuteAutoXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		//由子类重写
	}
	
	@Override
	public void sendMessage() throws Exception {
		// TODO Auto-generated method stub
		//由子类重写
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		if (sf!=null) {
			return sf.toString();
		}
		return "end";
	}
	
	
	// 收集消息
	protected void setMsg(String s){
		sf.append(s);
	}
	protected void setMsg(String s ,boolean isEnd){
		sf.append(s+"<br/>");
	}
	protected void setErrorMsg(String s){
		sf.append("<font color='red'>"+s+"</font>");
	}
	protected void setErrorMsg(String s,boolean isEnd){
		sf.append("<font color='red'>"+s+"</font><br/>");
	}
	
	

}
