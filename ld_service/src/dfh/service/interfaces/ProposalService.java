package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.AlipayAccount;
import dfh.model.Proposal;
import dfh.model.Question;
import dfh.model.QuestionStatus;
import dfh.model.TaskUserRecord;
import dfh.model.Users;
import dfh.model.enums.ProposalType;
import org.hibernate.criterion.DetachedCriteria;

public interface ProposalService extends UniversalService {

	/*
	 * String addCashout(String proposer, String loginname, String title, String from, Double money, String accountName, String accountNo, String
	 * accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip, String remark, String notifyNote,
	 * String notifyPhone) throws GenericDfhRuntimeException;
	 * 
	 * 
	 * 
	 * String addCashout(String proposer, String loginname, String title, String from, Double money, String accountName, String accountNo, String
	 * accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip, String remark) throws
	 * GenericDfhRuntimeException;
	 * 
	 * String addCashout(String proposer, String loginname, String title, String from, Double money, String ip, String remark) throws
	 * GenericDfhRuntimeException;
	 */
	/**
	 * elf
	 */
	public Boolean questionValidateForQuestion(Users users, String accountName,String phone,String email,String password);
	public Question queryQuestionByCondition(String loginname, String questionid);
	String savePayPassWordQuestion(String loginname, Integer questionid, String encryptPassword);

	Boolean changePresident(int id,int uId,DetachedCriteria dc,DetachedCriteria dc3,SlaveService slaveService);
	Double checkPoints(String userName);
	String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip,
			String remark,String msflag,Double gameTolMoney) throws GenericDfhRuntimeException;
	
	String addCashoutForAgentSlot(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip,
			String remark,String msflag) throws GenericDfhRuntimeException;
	
	String addCashin(String proposer, String loginname, String aliasName, String title, String from, Double money,
			String corpBankName, String remark, String accountNo,String bankaccount,String saveway,String cashintime) throws GenericDfhRuntimeException;

	String addConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark) throws GenericDfhRuntimeException;

	String addBankTransferCons(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark);

	String addNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone, String email,
			String role, String remark);

	String addReBankInfo(String proposer, String loginname, String title, String from, String accountName, String accountNo, String accountType,
			String bank, String accountCity, String bankAddress, String ip, String remark) throws GenericDfhRuntimeException;

	String addXima(String proposer, String loginname, String title, String from, Date startTime, Date endTime, Double firstCash, Double rate,
			String payType, String remark) throws GenericDfhRuntimeException;

	String addPrize(String proposer, String loginname, String title, String from, Double amount, String remark) throws GenericDfhRuntimeException;

	String audit(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;

	String cancle(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;
    void createUnion(String userName, DetachedCriteria dc,Integer level);
    void joinUnion(String userName,Integer level,Integer id);
	public Question queryQuestionForApp(String loginname);

	/**
	 * web使用
	 * 
	 * @author sun
	 */
	String clientCancle(String pno, String loginname, String ip, String remark) throws GenericDfhRuntimeException;

	String excute(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;

	/**
	 * office使用
	 */
	String operatorAddNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone,
			String email, String role, String remark, String ipaddress);

	/**
	 * 前台使用
	 * 
	 * @author sun
	 */
	String addUserConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark, String ipaddress) throws GenericDfhRuntimeException;

	/**
	 * 促销优惠
	 */
	String addOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money, String remark)
			throws GenericDfhRuntimeException;
	
	public String addCashoutNew(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone,
			String ip, String remark,String msflag,Double gameTolMoney) throws GenericDfhRuntimeException ;

	Proposal getLastSuccCashout(String loginname,Date before);
	
	public Integer totalProposals(Date starttime,Date endtime,String loginname,Integer type,String username);
	
	public Integer totalPayorder(Date starttime,Date endtime,String loginname,String username);
	
	public Integer totalCounts(Date starttime,Date endtime,String loginname,String username);
	
	public List<Proposal> searchSubTotal(Date starttime,Date endtime,String loginname,String username,int pageno,int length);
	
	public List<Proposal> searchSubProposalamount(Date starttime,Date endtime,String loginname,Integer type,String username,int pageno,int length);
	
	public List<Proposal> searchSubPayorderamount(Date starttime,Date endtime,String loginname,String username,int pageno,int length);
	
	public Double totalProposalamount(Date starttime,Date endtime,String loginname,Integer type,String username);
	
	public Double totalPayorderamount(Date starttime,Date endtime,String loginname,String username);
	
	public Double totalamount(Date starttime,Date endtime,String loginname,String username);
	
	/*
	 * 玩家自行取消负盈利反赠
	 */
	public String cancelLosePromo(String pno, String ip, String remark); 
	
	/**
	 * 玩家自行领取负盈利反赠
	 * @return
	 */
	public String excuteLosePromo(Proposal proposal, String ip, String remark, String target, String seqId);
	
	/**
	 * 玩家领取周周回馈
	 * @param pno
	 * @param ip
	 * @param remark
	 * @param target
	 * @return
	 */
	public String excuteWeekSent(String pno, String ip, String remark, String target);
	
	/**
	 * 玩家领取PT大爆炸活动礼金
	 * @param id
	 * @param ip
	 * @return
	 */
	public String drawPTBigBangBonus(Integer id, String ip);
	
	/**
	 * 核对玩家的回答是否正确
	 * @param loginname
	 * @param question
	 * @return
	 */
	public Boolean questionValidate(String loginname , String answer, Integer questionid);
	
	/**
	 * 查询玩家绑定的问题数量
	 * @param loginname
	 * @return
	 */
	public Integer questionsNumber(String loginname);
	

	/**
	 * 
	 * @param loginname
	 * @return
	 */
	public Question queryQuestion(String loginname);
	/**
	 * 
	 * @param loginname
	 * @param questionid
	 * @param content
	 * @return
	 */
	public String saveQuestion(String loginname , Integer questionid , String content);
	
	public void saveOrUpdateQuestionStatus(String loginname);
	
	public QuestionStatus queryQuesStatus(String loginname);
	
	public String EnableUser(String userName, boolean isEnable, String operator,String remark);
	
	public String updateTaskAmount(String loginname , Double amount , TaskUserRecord record);
	
	public String saveTask(String loginname , Integer taskId);
	
	public String updateAndAddUsertaskAmount(String loginname);
	
	public String transfertaskAmountInAccount(Users user ,  Double remit);
	
	public AlipayAccount getAlipayAccount(String loginname  , Integer disable);
	
	public String addFriendbonus(String loginname, Double remit,String remark);
	
	/**
	 * 积分兑换奖金
	 * @param user
	 * @param loginname
	 * @param remit
	 * @param transferService
	 * @return
	 */
	public String transferPoint(Users user, Double remit);
	
	public String saveFriendbonusrecordFor8(String loginname, Double remit,String remark);
	
	public List<String> getTranGameType(String loginname , Date starttime); 
	
	//获取游戏额度
	public Double getGameMoney(String loginname , String gameType)  throws Exception ;
	
	//获取存款额度 
	public Double getDepositAmount(String loginname , Date starttime , Date endtime);
	
	//获取提款
	public Double getWithdrawAmount(String loginname, Date starttime, Date endtime);
	
	public Double getGameProfit(String loginname, Date starttime);
	
	//获取优惠额度
	public Double getYouHuiAmount(String loginname);
	
	//获取最后一次提款后的额度
	public Double getLastDrawLocalAmount(String loginname , String pno);
	
	//获取提款获取游戏余额的开关
	public List<String> getGameSwitch();
	
	public boolean existHadFinishedProposal(String loginname, ProposalType type) ;
	
	/***
	 * 新秒提
	 */
	public Integer updateDepositBank(String loginname,String ubankno,String depositId);
	
	/**
	 * 根据玩家账号和附言删除附言订单
	 * @param loginname
	 * @param depositId
	 * @return
	 * */
	public String cancelNewdeposit(String loginname, String depositId);
	
}