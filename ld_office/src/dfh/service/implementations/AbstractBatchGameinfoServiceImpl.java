package dfh.service.implementations;

import java.io.File;
import java.util.List;

import dfh.dao.LogDao;
import dfh.dao.PtCouponDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.model.Proposal;
import dfh.model.PtCommissions;
import dfh.model.PtProfit;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.service.interfaces.ProposalService;

public abstract class AbstractBatchGameinfoServiceImpl {

	private SeqDao seqDao;
	private TaskDao taskDao;
	private ProposalService proposalService;
	private UserDao userDao;
	protected StringBuffer sf=null;
	private TradeDao tradeDao;
	private LogDao logDao;
    private PtCouponDao ptCouponDao ;
	
	public PtCouponDao getPtCouponDao() {
		return ptCouponDao;
	}

	public void setPtCouponDao(PtCouponDao ptCouponDao) {
		this.ptCouponDao = ptCouponDao;
	}
	
	protected File file = null;
	
	public abstract String addPtCoupon(File file,String loginname) throws Exception ;
	
	public abstract String addAgentPhone(File file) throws Exception;
	
	public abstract String autoAddXimaProposal(File file,Double rate)throws Exception;
	
	public abstract String systemXimaForKg(File file)throws Exception;
	
	public abstract String autoAddXimaPtProposal(List<PtProfit> ptProfit)throws Exception;
	
	public abstract String addPhone(File file) throws Exception;
	
	public abstract String addMail(File file) throws Exception;
	
	public abstract String addAgTry(File file) throws Exception;
	
	public abstract String autoAddCommissions(File file)throws Exception;
	
	public abstract void excuteAutoXimaProposal()throws Exception;
	
	public abstract void excuteCommissions()throws Exception;

	public abstract void  updateXimaStatus() throws Exception;
	
	public abstract String addLiveGameCommissions(String executetime);
	
	public abstract void convertAndSaveCommissions(List<PtCommissions> commLists , String platform , List<Proposal> proposals, List<Object> newPtLists);

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

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
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
	
	public String getMessage() {
		// TODO Auto-generated method stub
		if (sf!=null) {
			return sf.toString();
		}
		return "end";
	}
	public abstract String systemXimaForBBin(File file,String gamekind);

	public abstract List<Bean4Xima> excelToNTwoVo(File file);



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

	public abstract String systemXimaForGf(File file);
	
	public abstract String systemXimaForAgSlot(File file);
	
	/**
	 * excel转VO
	 * @param file excel文件
	 * @return
	 * */
	public abstract List<XimaDataVo> excelToPNGVo(File file);
	
	/**
	 * excel转VO
	 * @param file excel文件
	 * @return  Bean4xima.class
	 * */
	public abstract List<Bean4Xima> excelToPtSkyVo(File file);

	/**
	 * excel转VO
	 * @param file excel文件
	 * @return  Bean4xima.class
	 * */
	public abstract List<Bean4Xima> excelToDTFishVo(File file);

}
