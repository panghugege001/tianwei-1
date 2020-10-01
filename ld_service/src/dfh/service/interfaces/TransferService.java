package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.AutoYouHuiVo;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.Transfer;
import dfh.model.Users;

public interface TransferService extends UniversalService{

	String transferIn(String transID,String loginname,Double amount);
	
	String transferInEa(String transID,String loginname);
	
	String transferOut(String transID,String loginname,Double amount);
	
	String transferOutforDsp(String transID,String loginname,Double amount);
	
	String transferInforDsp(String transID,String loginname,Double amount);
	
	String transferOutforkeno(String transID,String loginname,Double amount,String ip);
	
	String transferInforkeno(String transID,String loginname,Double amount,String ip);
	
	String transferInforkenoIn(String transID,String loginname,Double amount,String ip);
	
    String transferOutforkeno2(String transID,String loginname,Double amount,String ip);
	
	String transferInforkeno2(String transID,String loginname,Double amount,String ip);
	
	AutoYouHuiVo transferInforCoupon(String transID,String loginname,String couponType,Double remit,String couponCode,String ip);
	
    String transferInforCouponSb(String transID,String loginname,Double remit,String couponCode,String ip);
    
	String transferOutforBbin(String transID,String loginname,Double amount);
	
	String transferInforBbin(String transID,String loginname,Double amount);
	
	String transferInforBbinIn(String transID,String loginname,Double amount);
	
	/*String transferOutforSky(String transID,String loginname,Double amount);
	
	String transferInforSky(String transID,String loginname,Double amount);*/

	/**
	 * 自助红包奖励转入游戏
	 * @param loginname
	 * @return
	 */
	public String selfConpon4TTG4HB(String transID, String loginname, Double remit, String remark,Integer deposit);

	public String transfer4Pt4HB(String transID, String loginname, Double remit ,String remark,Integer deposit) ;

	public String selfConponNT4HB(String transID, String loginname, Double remit, String remark,Integer deposit);

	public String selfConpon4Qt4HB(String transID, String loginname, Double remit, String remark,Integer deposit);

	public String selfConpon4MG4HB(String transID, String loginname, Double remit, String remark,Integer deposit);

	public String selfConpon4DT4HB(String transID, String loginname, Double remit, String remark,Integer deposit);

	public String selfConpon4Slot4HB(String transID, String loginname, Double remit, String remark,Integer deposit);

	String transferOutforTy(String transID, String loginname, Double remit);

	String transferInforTy(String transID, String loginname, Double remit);
	
    String transferNewPtIn(String transID,String loginname,Double amount);
	
	String transferNewPtOut(String transID,String loginname,Double amount);
	
	String transferInforBok(String transID,String loginname,Double amount);

	String transferOutforBok(String transID,String loginname, Double amount);

	String transferOutforSB(String transID, String loginname, Double remit);

	String transferInforSB(String transID, String loginname, Double remit);

	String transferOutforAginDsp(String transID, String loginname, Double remit);
	
    String selfConpon4MG4RedRain(String transID, String loginname, Double remit, String youhuiType, String remark);
	/*******************************************************红包优惠处理***********************************************************************/
	
	AutoYouHuiVo transferInforRedCouponTtg(String transID,String loginname,String couponType,String couponCode,String ip);
	
	AutoYouHuiVo transferInforRedCouponPt(String transID,String loginname,String couponType,String couponCode,String ip);
	
	AutoYouHuiVo transferInforRedCouponQT(String transID,String loginname,String couponType,String couponCode,String ip);
	
	AutoYouHuiVo transferInforRedCouponNT(String transID,String loginname,String couponType,String couponCode,String ip);
	
    AutoYouHuiVo transferInforRedCouponMG(String transID,String loginname,String couponType,String couponCode,String ip);
		
	AutoYouHuiVo transferInforRedCouponDT(String transID,String loginname,String couponType,String couponCode,String ip);
	/******************************************************************************************************************************************/
	
	AutoYouHuiVo transferInforCouponTtg(String transID,String loginname,String couponType,Double remit,String couponCode,String ip);
	
	AutoYouHuiVo transferInforCouponNT(String transID,String loginname,String couponType,Double remit,String couponCode,String ip);
	
	AutoYouHuiVo transferInforCouponQT(String transID,String loginname,String couponType,Double remit,String couponCode,String ip);
	
	String transferInforCouponGpi(String transID,String loginname,String couponType,Double remit,String couponCode,String ip);

	String transferInforAginDsp(String transID, String loginname, Double remit);
	
	public String transferLimitMethod(String loginname,Double remit);
	
	public AutoYouHuiVo transferInforPt8Coupon(String loginname, String couponCode,
			String ip) ;
	
	public String transferOutforSixLottery(String transID, String loginname, Double remit, String ip);
	
	public String transferInforSixlottery(String transID, String loginname,
			Double remit, String ip) ;
	
	public String transferPtAndSelfYouHuiIn(String transID, String loginname, Double remit ,String remark) ;
	
	public String transferPtAndSelfYouHuiOut(String transID, String loginname, Double remit) ;
	
	public String transferDtAndSelfYouHuiOut(String transID, String loginname, Double remit) ;
	
	public String transferTtAndSelfYouHuiOut(String transID, String loginname, Double remit) ;

	/**
	 * 领取全民闯关奖励
	 * @param loginname
	 * @return
	 */
	public String doEmigrated(String loginname) ;
	
	public String transferPtAndSelfYouHuiInModify(String transID, String loginname, Double remit ,String remark) ;
	
	public String transferJc(String transID, String loginname, Double remit, String toOrFrom);	
	
	String transferEaIn(String transID,String loginname,Double amount);
	
	String transferInforDspIn(String transID,String loginname,Double amount);
	
	String transferInforAginDspIn(String transID,String loginname,Double amount);
	
	String transferInforkenoIn2(String transID, String loginname, Double remit, String ip);
	
	String transferPtAndSelfYouHuiInIn(String transID, String loginname, Double remit ,String remark) ;
	
	String transferTtAndSelfYouHuiInIn(String transID, String loginname, Double remit ,String remark) ;

	/** Ebet自助优惠转入 */
	String transferInforEbetIn(String transID,String loginname,Double amount,String youhuiType,String remark);
	
	String transferInforTtgIn(String transID,String loginname,Double amount,String youhuiType,String remark);
	
	String transferInforDtIn(String transID,String loginname,Double amount,String youhuiType,String remark);
		
	public String selfConpon4TTG(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfCouponNT(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfCouponQT(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfCouponMG(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	String transferInforSixlotteryIn(String transID, String loginname,
			Double remit, String ip) ;
	
	String transferInforTyIn(String transID, String loginname, Double remit);
	
	void updateUserShippingcodeSql(Users users);
	
	void updateUserShippingcodePtSql(Users users);
	
	void updateUserCreditSql(Users user,Double remit);
	
	public Transfer addTransfer(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);
    
	public Transfer addTransferforDsp(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);

	public Transfer addTransferforAginDsp(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);

	public Transfer addTransferforKneo2(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);

	public Transfer addTransferforNewPt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);

	public Transfer addTransferforTt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);
	
	public Transfer addTransferforNT(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);
	
	public Transfer addTransferforSixLottery(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);
	
	public Transfer addTransferforSB(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);
	
	void updateUserShippingcodeTtSql(Users users);
	
	public Boolean updateSbcoupon(String loginname);
	
	public Transfer addTransferforBbin(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);
	
	public String transfer4EbetOutVerifyBet(String loginname, String transferID, Integer credit);

	public Transfer addTransferforKneo(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) ;
	
	public String transfer4EbetIn(String loginname, String transferID, Integer credit, String platform, String catalog, String type);
	
	public String transfer4Ebet(String loginname, String transferID, Integer credit, String platform, String catalog, String type);
	
	public Transfer addTransferforEbet(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) ;

	public Transfer addTransferforEBetApp(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);


	String transferInforQtIn(String transID, String loginname, Double remit, String youhuiType, String remark) ;
	 
	String transferInforQtInQD(String transID,String loginname,Double amount,String youhuiType,String remark);
	 
	public String transferQtAndSelfYouHuiOut(String transID, String loginname, Double remit) ;
	
	public Transfer addTransferforDt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);
	public Transfer addTransferforSlot(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);

	public Transfer addTransferforQt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);
	
	
	public Transfer addTransferforDT(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);
	/**
	 * GPI转入
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInGPI(String seqId, String loginname, Double remit);
	
	/**
	 * GPI转出
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutGPI(String seqId, String loginname, Double remit);
	
	/**
	 * GPI自助优惠
	 * @param transID
	 * @param loginname
	 * @param remit
	 * @param remark
	 * @return
	 */
	public String selfConpon4GPI(String transID, Users customer, Double remit ,String remark) ;
	
	/**
	 * NT转出至E68
	 * @param transferID
	 * @param id
	 * @param loginname
	 * @param credit
	 * @return
	 */
	public String transferFromNTJudge(String transferID, String loginname, Double credit); 
	
	/**
	 * 从E68转入NT
	 * @param transferID
	 * @param id
	 * @param loginname
	 * @param credit
	 * @return
	 */
	public String transferToNTJudge(String transferID, String loginname, Double credit, String youhuiType, String remark);

	/**
	 * EBetApp 转出至 平台
	 * @param transferID
	 * @param loginname
	 * @param credit
	 * @return
	 */
	String transferFromEBetApp(String transferID, String loginname, Double credit);

	public String transferToEBetApp(String transferID, String loginname, Double remit, String youhuiType, String remark);
	String transferToMain(TransferService transferService, Users users, Integer amout);


	/**
	 * 签到操作
	 * @param loginname
	 * @return
	 * @throws Exception 
	 */
	public String doSignRecord(String loginname,String device,String level) throws Exception ;

	public Boolean checkRecord(String loginname,String device,String level) throws Exception ;


	
	/**
	 * 签到奖金转入ttg
	 * @param transID
	 * @param loginname
	 * @param remit
	 * @param youhuiType
	 * @param remark
	 * @return
	 */
	public String selfConpon4TTG4Sign(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String transfer4Pt4Sign(String transID, String loginname, Double remit ,String remark) ;
	
	public String selfConponNT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfConpon4QT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfConpon4MG4Sign(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfConpon4DT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark);
	public String selfConpon4SLOT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	
	/**
	 * 好友推荐奖金转入
	 * @param transID
	 * @param loginname
	 * @param remit
	 * @param youhuiType
	 * @param remark
	 * @return
	 */
	public String selfConpon4TTG4Friend(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String transfer4Pt4Friend(String transID, String loginname, Double remit ,String remark) ;
	
	public String selfConponNT4Friend(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfConpon4Qt4Friend(String transID, String loginname, Double remit, String youhuiType, String remark);
	
	public String selfConpon4Mg4Friend(String transID, String loginname, Double remit, String youhuiType, String remark);
	public String selfConpon4Dt4Friend(String transID, String loginname, Double remit, String youhuiType, String remark);

	/**
	 * 全民闯关奖励转入游戏
	 * @param loginname
	 * @return
	 */
	public String selfConpon4TTG4Emigrated(String transID, String loginname, Double remit, String remark);
	
	public String transfer4Pt4Emigrated(String transID, String loginname, Double remit ,String remark) ;
	
	public String selfConponNT4Emigrated(String transID, String loginname, Double remit, String remark);
	
	public String selfConpon4Qt4Emigrated(String transID, String loginname, Double remit, String remark);

	/**
	 * 代理老虎机金额转入绑定游戏账号
	 * @param transferService
	 * @param loginnameAgent
	 * @param loginnameGame
	 * @param credit
	 * @param transIDAgent
	 * @param transIDGame
	 * @return
	 * */
	public String transferInGameUserFromAgentTiger(String loginnameAgent, Double credit, String transIDAgent, String transIDGame, String password);
	
	public String transferInNTwo(String transID,String loginname,Double amount);
	
	public String transferOutNTwo(String transID,String loginname,Double amount);
	
	public Transfer addTransferNTwo(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark); 
	
	public String transferMgValidateIn(String transID, String loginname, Double remit, String remark) ;
	
	public Transfer addTransferforMg(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);
	
	public String transferMgOut(String transID, String loginname, Double remit) ;

	public Const getConsts(String id);
	
	String transferDTAndSelfYouHuiInModify(String pno, String selfname, String transID, String loginname, Double remit, String remark);

	public AutoYouHuiVo transferInforCouponMG(String seqId, String loginname, String couponType, Double remit, String couponCode, String ip);
	
	public AutoYouHuiVo transferInforCouponDT(String seqId, String loginname, String couponType, Double remit, String couponCode, String ip);
	
	public String transferPngOut(String transID, String loginname, Double remit);
	
	public String transferPngValidateIn(String transID, String loginname, Double remit, String remark);
	
	public Transfer addTransferforPng(Long transID, String loginname,Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark);
	
	public Bankinfo createNewdeposit(String loginname, String banktype, String uaccountname, String ubankname, String ubankno, Double amout, boolean force,Double dpRange,String depositId);
	
	public   Double amountNumber(String amount,String loginname);
	
	/**
	 * 存款奖金操作
	 * @param loginname
	 * @return
	 * @throws Exception 
	 */
	public String doRechargeRecord(String loginname) throws Exception ;
	
	/**
	 * 流水奖金操作
	 * @param loginname
	 * @return
	 * @throws Exception 
	 */
	public String doStreamRecord(String loginname) throws Exception ;
	
	Transfer addTransferforSba(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark);
	//转入沙巴体育
	public String transferInforSbaTyIn(String seqId, String loginname, Double remit);
	//沙巴体育转出
	public String transferOutforSbaTy(String seqId, String loginname, Double remit);

	/**
	 * 微信秒存
	 * @param loginname
	 * @param banktype
	 * @param uaccountname
	 * @param ubankname
	 * @param ubankno
	 * @param amoutboolean
	 * @param force
	 * @return
	 */
	public Bankinfo createWeiXindeposit(String loginname,Double amoutboolean);
	
	public List<Transfer> findTransferList(String loginname, Date startDate);
	//生日礼金转入主账户
	String transferBirthdayToMain(Users users, Double amout);

	//主账户转入红包雨账户
	String transferSelfToRedRain(String seqId, Users user, Double amout, String youhuiType, String remark);

	//红包雨转入DT
	public String selfConpon4DT4RedRain(String transID, String loginname, Double remit, String youhuiType, String remark);

	public Transfer addTransferforPlat(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark,String plat);

	String selfConpon4RedRain(String transID, String loginname, Double remit, String text, String remark, String plat);
}