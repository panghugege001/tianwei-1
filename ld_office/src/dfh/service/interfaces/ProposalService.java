package dfh.service.interfaces;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.*;
import dfh.model.bean.AgSlotXima;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.model.enums.ConcertDateType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.SystemLogType;
import dfh.utils.Page;

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
	public String addSwFishBatchXimaProposal(List<Bean4Xima> ptData);


	public List<Proposal> getNotExecProposal(Date starttime,Date endtime)throws Exception;
	
	public List<Proposal> getNotExecProfitProposal(Date starttime,Date endtime)throws Exception;
	
	public List<Proposal> getNotExecPrizeProposal(Date starttime,Date endtime)throws Exception;
	
	String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip,
			String remark) throws GenericDfhRuntimeException;
	
	String addProxyCashOutProposal(String proposer, String loginname, String from, Double money, String accountNo,
			String accountName,String bank,String remark,String ip) throws GenericDfhRuntimeException;
	
	
	public String addPt8CouponProposalEmail(String proposer) ;
	

	String addCashin(String proposer, String loginname, String aliasName, String title, String from, Double money, String corpBankName,
			String remark, String accountNo,String bankaccount,String saveway,Double fee) throws GenericDfhRuntimeException;

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
	
	String addPrizeBirthdayGifts(String proposer, String loginname, String title, String from, Double amount, String remark) throws GenericDfhRuntimeException;
	
	String addPrizeProposalCoupon(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	
	String addPrizeProposalCouponTwo(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	
	String addPrizeProposalCouponPt(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	
	String addRedProposalCoupon(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	
	String addPrizeProposalCouponPtTwo(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent,String usernameType) throws GenericDfhRuntimeException;
	
	String addPrizeProposalCouponSb(String proposer,String from,Integer type,Double amount, String betMultiples, String remark) throws GenericDfhRuntimeException;
	
	String addPrizeProposalCouponSbTwo(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String usernameType) throws GenericDfhRuntimeException;
	
	String addActivity(Integer id,String activityName,Double activityPercent,Date start,Date end,String remark) throws GenericDfhRuntimeException;
	
	Users getUserAgent(String agent) throws GenericDfhRuntimeException;
	
	String addProposalLevelPrize(String proposer, String loginname, String title, String from, Double amount, String remark) throws GenericDfhRuntimeException;

	Proposal getProposalPno(String pno) throws GenericDfhRuntimeException;
	
	String audit(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;
	
	String auditS(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;
	
	String auditMsProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;
	
	String excuteCommission(String loginname, String operator, Integer year,Integer month, String remark) throws GenericDfhRuntimeException;
	
	String auditBusinessProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;
	
	String excuteicbctransfer(Integer id, String loginname,String operator) throws GenericDfhRuntimeException;

	String excutecmbtransfer(Integer id, String loginname,String operator) throws GenericDfhRuntimeException;
	
	public String excuteAlipaytransfer(String id, String loginname,
			String operatorLoginname);
	
	String excuteabctransfer(Integer id, String loginname,String operator) throws GenericDfhRuntimeException;
	
	String supplementPayOrder(Integer id, String loginname, Double amount, String operator) throws GenericDfhRuntimeException;
	
	String cancle(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;
	
	String canclemscashoutproposal(String pno, String operator, String ip, String remark,Integer unknown) throws GenericDfhRuntimeException;
	
	String cancleBusinessProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;

	/**
	 * web使用
	 * 
	 * @author sun
	 */
	String clientCancle(String pno, String loginname, String ip, String remark) throws GenericDfhRuntimeException;

	String excute(String pno, String operator, String ip, String remark,String bankinfoid,Double fee) throws GenericDfhRuntimeException;
	
	String executemscashoutproposal(String pno,String bankinfoid);
	
	String excuteBusinessProposal(String pno, String operator, String ip, String remark,String bankinfoid,Double fee,Double actualmoney,String file) throws GenericDfhRuntimeException;
	
	String addBusiness(String proposer, String depositname,String depositaccount,String depositbank,
			String businessProposalType,Double money,String remark,String file,String belong,Integer bankaccountid)throws GenericDfhRuntimeException;
	
	String cashinExcute(String pno, String operator, String ip) throws GenericDfhRuntimeException;
	
	String cashinCancel(String pno, String operator, String ip,String remark) throws GenericDfhRuntimeException;

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
	String addUserTimesConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark, Integer times,String ipaddress) throws GenericDfhRuntimeException;

	/**
	 * 促销优惠
	 */
	String addOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money, String remark)
			throws GenericDfhRuntimeException;
	String addTimesOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money,Integer times ,String remark)
	throws GenericDfhRuntimeException;
	

	Proposal getLastSuccCashout(String loginname,Date before);

	public String secondProposal(String jobPno, String operatorLoginname,
			String ip, String remark);
	
	String excuteOffer(String pno, String operator, String ip) throws GenericDfhRuntimeException;
	
	public String submitYhjProposal(String pno, String operator, String ip, String loginName);
	
	public String submitYhjCancel(String pno, String operator, String ip, String loginName);
	
    String addPrizeProposalPhone(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	
	String addPrizeProposalEmail(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	
	public String addPrizeProposalEmailAll(String title,String rmrak,Integer batch)throws GenericDfhRuntimeException ;
	
	String addPrizePhone(String phone,String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
	 
	String addPrizeEmail(String email,String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException;
		
	String updateActivityStatus(Object o);

	public String autoAddXimaPtProposal(List<PtProfit> ptProfit);
	
	public String autoAddXimaNewPtProposalNewXXX(List<PtDataNew> ptData);
	
	public String autoAddXimaEaProposal(List<EaData> eaDatas);
	
	public String autoAddXimaAgProposal(List betsList,String platform);
	
	public String autoAddXimaAgSlotProposal(List<AgSlotXima> listXima);
	

	public String autoAddXimaNewPtProposal(List<PtData> ptData);
	
	public String rPayProposal(String jobPno, String operatorLoginname,
			String ip, String loginname);
	
	public String autoAddXimaJCProposal(List<JCProfitData> jcData);
	
	public String autoAddXimaNTProposal(List<NTProfit> ntDatas);
	public String importData(File file, String operator);

	public String autoAddXimaSixLotteryProposal(List<PlatformData> sixData) ;

	public String autoAddXimaEBetAppProposal(List<PlatformData> profits);

	public void dealSelfYouHuiData() ;
	
	public String executePtCommissionsService(String operator , String ids);
	
	public String executeAgentVipService(String operator , String ids , Integer level);
	
	public String xima4MG(List<Bean4Xima> betsList);
	
	/**
	 * 中心钱包NT洗码
	 * */
	public String xima4NT(List<Bean4Xima> ntDataTemp);
	
	public String xima4GPI();
	
	public String xima4QT();
	
	/** pm-start */
	/** 老虎机体验金周赛查询
	 * @param startTime
	 * @param endTime
	 * @param platform
	 * 
	 * @return page
	 */
	public Page queryWeeklySlotsMatch(HttpServletRequest reuqest, String startTime, String endTime,
			Integer currPage, Integer size)  throws Exception;
	/** pm-end */
	
	public String addPrizeProposalCouponTtg(String operatorLoginname,
			String fromBack, Integer typeNew, Double amountNew,
			String betMultiples, String trim);
	
	public String xima4KenoBC();
	
	/**
	 * 执行系统洗码反水
	 * @throws Exception
	 */
	public void excuteAutoXimaProposal(Proposal proposal)throws Exception;
	
	public Double getDoubleValueBySql(String sql, Map<String, Object> params);
	
	public List getListBySql(String sql, Map<String, Object> params) throws Exception;
	
	public Integer getCount(String sql, Map<String, Object> params);
	
	public int excuteSql(String sql, Map<String, Object> params);
	
	public String autoAddXimaDtProposal(List<NTProfit> dtData);
	
	/**
	 *  执行单个批量导入的优惠
	 * @param loginName
	 * @param amount
	 */
	public void exeSinglePrivilege(Integer id, String loginName, Double amount, String remark);
	
	public String autoAddXimaNTwoProposal(List<PlatformData> profits);
	
	public String modifyPlatformRecord(String loginname,String platfrom);
	
	public String modifySinglePlatform(String loginname,String platfrom);
	    
	public String modifyAgSlotPlatform(Date start);
	    
	    /**
	     * AG 单个平台查询
	     */
	public List<AgSlotXima> sqlQueryList(String start,String end,String platform) ;
	    /**
	     * AG 	多个平台查询
	     */
	public List<AgSlotXima> sqlAutoQueryList(String start,String end) ;
	
	

	 // start 演唱会活动 
	public Page queryConcertBet(String loginname,Integer id,Integer pageIndex, Integer size,Integer type);
	
	
	//演唱会数据禁用启用
	public Integer updateConcertDisplay(Integer id);
	
		
	//演唱会修改流水
	public String editConcertDisplay(Integer id,String amount);
	
	public void concertUpdateBet(List<Concert>list,Date now,ConcertDateType concertDateType,Integer type) throws Exception;
	 // end 演唱会活动 
	    
	public String insertSystemLogs(String operator,SystemLogType systemLogType);
	
	public String autoAddXimaSbaProposal(List sbaDatas);
	
	public String autoAddXimaMwgProposal(List mwgData);
	public String autoAddXimaKyqpProposal(List data);

	public String autoAddXimaVRProposal(List data,String gamekind);
	
	public String addNewPtSkyBatchXimaProposal(List<Bean4Xima> ptData);
	public String addQrcode(String agent,String recommendCode,String address,String addQrcode,String remark,String qrcode);
	/**
	 * 平博体育洗码
	 * @param betsList
	 * @return
	 */
	public String PbXiMA(List<Bean4Xima> betsList);
	
	/**
	 * 通用系统洗码执行方法，proposalType为Null时不会扣除自助洗码。
	 * @param tempList
	 * @param starttime
	 * @param endtime
	 * @param plantform 
	 * @param remark 备注
	 * @param proposalType 自助洗码提案枚举
	 * @return
	 * */
	public String systemXima4Common(List<XimaDataVo> tempList, Date starttime, Date endtime, String plantform, String remark, ProposalType proposalType);
	
	Page queryForPagenation(String recordSql, String totalSql, Integer pageIndex, Integer pageSize);
	
	String saveTopicStatus(Map<String, String> params);
	
	public String xima4PNG(List<Bean4Xima> betsList);
	public String xima4QT(List<Bean4Xima> betsList);

	public String autoAddXimaBbinProposal(List sbaData, String gamekind);
	public String addDTFishBatchXimaProposal(List<Bean4Xima> ptData);
	public String addNTwoBatchBaobiaoXimaProposal(List<Bean4Xima> ptData);


	/**
	 * 泛亚电竞洗码
	 * @param data
	 * @return
	 */
	public String autoAddXimaFanyaProposal(List data);

	String concertUpdateBet(Date now, ConcertDateType concertDateType, Integer type) throws Exception;


	public String updateRankingData(Integer round, int type);
	
	public String autoAddXima761Proposal(List data);

	public Boolean selectPBDataByBetId(String betId);

	public void insertPBData(List<PBData> resultList);

	public List selectPBData();

	/**
	 * 比特游戏洗码
	 * @param betsList
	 * @return
	 */
	public String BitXiMA(List<Bean4Xima> betsList);

	public String autoAddXimaSwSProposal(List data);
	
	public String autoAddXimaSwFProposal(List data);
	
	public String xima4TTG();

}