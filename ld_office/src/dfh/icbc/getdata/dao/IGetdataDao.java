package dfh.icbc.getdata.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.*;


public interface IGetdataDao extends BaseInterfaceDao{
	
	public HibernateTemplate getHibernateTemplate();
	
	public void changeCredit(Users lockedUser, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException;
	
	public void changeCreditBySql(Users lockedUser, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException;
	
	public void changeAgentSlotCreditBySql(Users lockedUser, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException;
	
	public void changeCreditForSecDeposit(Users lockedUser, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException;
	
	public int changeAmountByIdSql(String username, double amount,String pno);
	
	public void changeAmountByName(String username,double amount,String pno);
	
	public void changeAmountByNameSql(String username, double amount,String pno);
	
	public void insertOperationLog(String loginname, String action, String remark);
	
	public void excuteTask(String pno, String operator, String ip);
	
	public String generateProposalPno(String type);
	
	public String generateProposalIcbcPno(String type);
	
	public String generateProposalAlipayPno(String type);
	
	public Date getDatabaseNow();
	/**
	 * 批量操作
	 * @param resultList
	 */
	public void saveFanyaLogAll(List<FanyaLog> resultList);
	
	public void generateTasks(String pno, String operator);
	
	public List<Creditlogs> getCashInToday(String loginname);
	
	public IcbcTransfers getIcbcDataBySql();
	
	public CmbTransfers getCmbDataBySql();
	
	public AbcTransfers getAbcDataBySql();
	
	public AlipayTransfers getAlipayDataBySql();

	public ValidateAmountDeposit getValidateAmountDeposit();
	
	public void processPtProfit(Users ptUser, Double betCredit, Double payout,
			Date time, Date time2);
	
	/**
	 * NT老虎机抓取每日投注额
	 * @param id
	 * @param betCredit
	 * @param payout
	 * @param time
	 * @param time2
	 */
	public void processNTProfit(String loginname, Double betCredit, Double payout,
			Date time, Date time2);
	
	public Boolean processNewPtProfit(String PLAYERNAME,String CODE,String CURRENCYCODE,String ACTIVEPLAYERS,String BALANCECHANGE,String DEPOSITS,String WITHDRAWS,String BONUSES,String COMPS,String PROGRESSIVEBETS,String PROGRESSIVEWINS,String BETS,String WINS,String NETLOSS,String NETPURCHASE,String NETGAMING,String HOUSEEARNINGS,String RNUM,String DATATIME);
	
	public Boolean dealNewPtDataDao(String pLAYERNAME,
			String fULLNAME, String vIPLEVEL, String cOUNTRY, String gAMES,
			String cURRENCYCODE, String bETS, String wINS, String iNCOME,
			String rNUM, String dateTime);
	
	public Boolean updateNewPtData(String playername,String dateTime, Double betsTiger,
			Double winsTiger);
	
	public boolean insertJCData(List<JCProfitData> jclist);
	
	public List<String> queryJCDataByDate(String executeTime);
	
	public boolean updateJCData(JCProfitData jcp);

	public boolean updateSixLotteryPlatForm(PlatformData data) ;
	
	public boolean updateQtPlatForm(PlatformData data);
	
	public List<PlatformData> selectQtData(Date startT, Date endT);
	
	/**
	 * 处理过期未支付的额度验证存款订单
	 */
	public int discardValidateAmountOrder();
	
	public void updateUserIsCashInSql(String loginname);

	public void saveOrUpdateEBetAppProfit(PlatformData profit);
	
	public DepositWechat getValidateWechatDeposit();
	public void changeWechatAmountOnline(String wxh,double amount,String pno);
	
	/**
	 * @author ck
	 * @param ag 获取平台最后时间
	 * @return
	 */
	public String getAgSlotLastTime(String platform);
		
	/**
	 * @author ck
	 * @param ag 存入数据
	 * @return
	 */
	public void processNewAgSlotProfit(Map<String,AgDataRecord>data,Date lastTime,String platformType);
	

	
	
	
	
	
	  /***
	   *  同略云
	    * 更新提案记录		   
	    */
	   public void updateProposalSql(String flag ,String mstype, String bankName, String pno,String overTime );  
			   
	   /****
	    * 更新代理userstatus表
	    */
		public void updateUserstatusSql(String loginname , Double remit );
	   
	   
	   /****
	    * 更新玩家账户额度
	    */
		public void updateUserCreditSql(String loginname , Double remit );
	    /****
	    *  添加额度记录
	    */
		
		public void insertCreditLog(String loginname ,String type,Double credit,Double newCredit, Double remit,String remark );
		 /****
		  *  添加银行额度记录
		  */
		public void insertBankCreditLog(String bankname ,String type,Double credit,Double newCredit, Double remit,String pno );
			
		/****
		 * 
		  * 更新银行
		  */
		public void updateBankinfoSql(Double remit,Double bankamount,String bankuser, String bankname );
		
		/****
		  * 已审核 待执行
		  */
		public void updateProposalFlagSql(String pno );
	
		public String getOverTime(Integer mssflag );
		
		/**
		 * 秒存 过期处理
		 * @return
		 */
		public int discardValidateAmountOrderMc();
		
		public Double getDoubleValue(String sql, Map params);
	
		public String generateProposalCmbPno(String type);
		
	List list(String sql, Map<String, Object> paramsMap);	
	
	public int excuteSql(String sql, Map<String, Object> params);

	public void saveOrUpdateAll(List resultList);
	
	public Boolean dealAllPtData(String pLAYERNAME, String fULLNAME, String vIPLEVEL, String cOUNTRY, String gAMES,
			String cURRENCYCODE, String bETS, String wINS, String iNCOME, String rNUM, String executeTime,
			Double bETS_TIGER, Double wINS_TIGER, Double pROGRESSIVE_BETS, Double pROGRESSIVE_WINS,
			Double pROGRESSIVE_FEE, Date createTime, Date sTARTTIME, Date eNDTIME);
	
	public List excuteQuerySql(String sql, Map<String, Object> params);
}
