package dfh.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.exception.PostFailedException;
import dfh.exception.RemoteApiException;
import dfh.exception.ResponseFailedException;
import dfh.model.enums.EBetCode;
import dfh.remote.bean.CheckClientResponseBean;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.EBetResp;
import dfh.remote.bean.GetTurnOverResponseBean;
import dfh.remote.bean.KenoResponseBean;
import dfh.remote.bean.Property;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.utils.APInUtils;
import dfh.utils.APUtils;
import dfh.utils.BBinUtils;
import dfh.utils.DateUtil;
import dfh.utils.EBetUtil;
import dfh.utils.HttpUtils;
import dfh.utils.KenoUtil;
import dfh.utils.NumericUtil;
import dfh.utils.SportBookUtils;
import dfh.utils.StringUtil;

public class RemoteCaller {

	private static Log log = LogFactory.getLog(RemoteConstant.class);

	public static String getLoginValidationResponseXml(String id, String userid, String username, String status, String errdesc, String acode) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
//		对新开的会员及旧的会员的昵称前面加个 e68的前缀 
		list.add(new Property(RemoteConstant.P_USERNAME, "e68_"+username));
		list.add(new Property(RemoteConstant.P_STATUS, status));
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		String vendorID = RemoteConstant.getVendorID(userid);
		list.add(new Property(RemoteConstant.P_VENDORID, vendorID));
		list.add(new Property(RemoteConstant.P_CURRENCYID, "3".equals(vendorID)?RemoteConstant.CURRENCYID:RemoteConstant.TESTCURRENCYID));
		list.add(new Property(RemoteConstant.P_ERRDESC, errdesc));

		return DocumentParser.createRequestXML("clogin", id, list);
	}
	
	public static String getSportBookLoginValidationResponseXml(String loginName) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_RETURNCODE, "000"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_DESCRIPTION, "Success"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_LOGINNAME, loginName));
		list.add(new Property(RemoteConstant.SPORT_BOOK_CURRENCYCODE, "RMB"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_ODDSTYPEID, "1"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_LANGCODE, "CHS"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_TIMEZONE, "GMT+08:00"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_MEMBERSTATUS, "501"));
		list.add(new Property(RemoteConstant.SPORT_BOOK_CLIENTIP, "10.10.0.1"));
		return DocumentParser.createSportBookResponseXML("Login", list);
	}
	
	public static String getSportBookMemberBalanceRequestXml(String loginName) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_LOGINNAME, loginName));
		return DocumentParser.createSportBookRequestXML("GetMemberBalance", list);
	}
	
	public static String getSportBookDepositFundRequestXml(String loginName,Double amount,String ReferenceNo) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_LOGINNAME, loginName));
		list.add(new Property(RemoteConstant.SPORT_BOOK_AMOUNT, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.SPORT_BOOK_REFERENCENO, ReferenceNo));
		return DocumentParser.createSportBookRequestXML("DepositFund", list);
	}
	
	public static String getSportBookWithdrawFundRequestXml(String loginName,Double amount,String ReferenceNo) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_LOGINNAME, loginName));
		list.add(new Property(RemoteConstant.SPORT_BOOK_AMOUNT, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.SPORT_BOOK_REFERENCENO, ReferenceNo));
		return DocumentParser.createSportBookRequestXML("WithdrawFund", list);
	}
	
	public static String getSportBookTransferStatusRequestXml(String ReferenceNo) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_REFERENCENO, ReferenceNo));
		return DocumentParser.createSportBookRequestXML("GetTransferStatus", list);
	}

	public static String getGPIAccessValidateXML(String code, String username, Integer isTest){
		List<Property> list = new ArrayList<Property>();
		list.add(new Property("error_code", code));
		if(code.equals("0")){
			list.add(new Property("cust_id", username));
			list.add(new Property("cust_name", username));
			list.add(new Property("currency_code", "RMB"));
			list.add(new Property("language", "zh-cn"));
			list.add(new Property("test_cust", isTest.equals(0)?"false":"true"));
			list.add(new Property("country", "CN"));
			list.add(new Property("date_of_birth", ""));
			list.add(new Property("ip", ""));
		}else{
			list.add(new Property("error_msg", "Authentication Failed"));
		}
		return DocumentParser.createGPIAccessXML(list);
	}
	
	public static DepositPendingResponseBean depositPendingRequest(String userid, Double amount, String refno, String acode) throws PostFailedException, ResponseFailedException {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		String vendorID = RemoteConstant.getVendorID(userid);
		list.add(new Property(RemoteConstant.P_VENDORID, vendorID));
		list.add(new Property(RemoteConstant.P_CURRENCYID, "3".equals(vendorID)?RemoteConstant.CURRENCYID:RemoteConstant.TESTCURRENCYID));
		list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.P_REFNO, refno));

		String xml = DocumentParser.createRequestXML("cdeposit", generateDepositID(), list);
		return DocumentParser.parseDepositPendingResponseBean(HttpUtils.postXMLBySSL(RemoteConstant.HTTPS_HOST, RemoteConstant.DEPOSIT_URL, xml));
	}

	public static void depositConfirmationResponse(String id, String status, String paymentid, String acode) throws PostFailedException, ResponseFailedException, RemoteApiException {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		list.add(new Property(RemoteConstant.P_STATUS, status));
		list.add(new Property(RemoteConstant.P_PAYMENTID, paymentid));
		list.add(new Property(RemoteConstant.P_ERRDESC, ""));

		String xml = DocumentParser.createRequestXML("cdeposit-confirm", id, list);
		String response = HttpUtils.postXMLBySSL(RemoteConstant.HTTPS_HOST, RemoteConstant.DEPOSIT_URL, xml);
		if (StringUtil.trimToNull(response) != null) {
			throw new RemoteApiException("cdeposit-confirm get response:" + xml);
		}
	}

	public static WithdrawalConfirmationResponseBean withdrawPendingRequest(String userid, Double amount, String refno, String acode) throws PostFailedException, ResponseFailedException {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		String vendorID = RemoteConstant.getVendorID(userid);
		list.add(new Property(RemoteConstant.P_VENDORID, vendorID));
		list.add(new Property(RemoteConstant.P_CURRENCYID, "3".equals(vendorID)?RemoteConstant.CURRENCYID:RemoteConstant.TESTCURRENCYID));
		list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.P_REFNO, refno));

		String xml = DocumentParser.createRequestXML("cwithdrawal", generateWithdrawID(), list);
		return DocumentParser.parseWithdrawalConfirmationResponseBean(HttpUtils.postXMLBySSL(RemoteConstant.HTTPS_HOST, RemoteConstant.WITHDRAW_URL, xml));
	}
	
	public static DspResponseBean depositPrepareDspRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.PrepareTransferCredit(userid, refno, "IN", amount, userid));
	}
	
	public static DspResponseBean depositPrepareDspAginRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APInUtils.PrepareTransferCredit(userid, refno, "IN", amount, userid));
	}
	
	/** 自助存送活动Ebet存款 */
	public static String tradeEbetRequest(String loginname, Integer remit, String inOrOut, String refno) throws Exception{
		EBetResp er = EBetUtil.transfer(loginname, refno, remit, "EBET", "LIVE", inOrOut);
		if (er.getCode().equals(EBetCode.success.getCode())) {
			//转账后调用确认接口
			EBetResp erConfirm = EBetUtil.transferConfirm(loginname, refno);
			if (erConfirm.getCode().equals(EBetCode.success.getCode())){
				return null;
			} else {
				return erConfirm.getMsg();
			}
		} else {
			return er.getMsg();
		}
	}
	
	public static DspResponseBean depositBbinRequest(String loginname, Integer remit,Double localCredit, String refno) throws Exception {
		
		//return DocumentParser.parseBBinDspResponseRequest(BBinUtils.TransferCredit(loginname, refno, "IN", remit,localCredit));
		return null;

	}
	public static DspResponseBean depositSBRequest(String loginname, Double remit, String refno) throws Exception {
		
		return DocumentParser.parseSBDspResponseRequest(SportBookUtils.depositFund(loginname, remit, refno));
	}
	
	public static DspResponseBean getTransferStatusSBRequest(String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseSBDspResponseRequest(SportBookUtils.getTransferStatus(refno));
	}
	
	public static DspResponseBean depositConfirmDspRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.TransferCreditConfirm(userid, refno, "IN", amount, userid,flag));
	}
	
	public static DspResponseBean depositConfirmDspAginRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APInUtils.TransferCreditConfirm(userid, refno, "IN", amount, userid,flag));
	}
	
	public static DspResponseBean withdrawBbinRequest(String loginname, Integer remit,Double localCredit, String refno) throws Exception{
		
		//return DocumentParser.parseBBinDspResponseRequest(BBinUtils.TransferCredit(loginname, refno, "OUT", remit,localCredit));
		return null;
	}
	
	public static DspResponseBean withdrawSBRequest(String loginname, Double remit, String refno) throws Exception {
		
		return DocumentParser.parseSBDspResponseRequest(SportBookUtils.withdrawFund(loginname, remit, refno));
	}
	
	public static DspResponseBean withdrawPrepareDspRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.PrepareTransferCredit(userid, refno, "OUT", amount, userid));
	}
	
	public static DspResponseBean withdrawPrepareDspAginRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APInUtils.PrepareTransferCredit(userid, refno, "OUT", amount, userid));
	}
	
	public static DspResponseBean withdrawConfirmDspRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.TransferCreditConfirm(userid, refno, "OUT", amount, userid,flag));
	}
	public static DspResponseBean withdrawConfirmDspAginRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APInUtils.TransferCreditConfirm(userid, refno, "OUT", amount, userid,flag));
	}
	
	public static GetTurnOverResponseBean getTurnOverRequest(String userid,Date fromDate,Date toDate) throws PostFailedException, ResponseFailedException{
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_VENDORID, RemoteConstant.getVendorID(userid)));
		list.add(new Property(RemoteConstant.P_FROM_DATE, DateUtil.formatDateForStandard(fromDate)));
		list.add(new Property(RemoteConstant.P_TO_DATE, DateUtil.formatDateForStandard(toDate)));

		String xml = DocumentParser.createRequestXML("cturnover", generateGetTurnOverID(), list);
		return DocumentParser.parseGetTurnOverResponseBean(HttpUtils.postXMLBySSL(RemoteConstant.HTTPS_HOST, RemoteConstant.GET_TURNOVER_URL, xml));
	}
	
	public static CheckClientResponseBean checkClient(String userid) throws PostFailedException, ResponseFailedException {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		String vendorID = RemoteConstant.getVendorID(userid);
		list.add(new Property(RemoteConstant.P_VENDORID, vendorID));
		list.add(new Property(RemoteConstant.P_CURRENCYID, "3".equals(vendorID)?RemoteConstant.CURRENCYID:RemoteConstant.TESTCURRENCYID));

		String xml = DocumentParser.createRequestXML("ccheckclient", generateCheckClientID(), list);
		return DocumentParser.parseCheckClientResponseBean(HttpUtils.postXMLBySSLShort(RemoteConstant.HTTPS_HOST, RemoteConstant.CHECKCLIENT_URL, xml));
	}
	
	public static DspResponseBean checkDspClient(String userid) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.GetBalance(userid, userid));
	}
	
	public static DspResponseBean checkDspAginClient(String userid) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APInUtils.GetBalance(userid, userid));
	}
	
	public static DspResponseBean checkBbinClient(String loginname) throws PostFailedException, ResponseFailedException {
		
		//return DocumentParser.parseBBinRemitDspResponseRequest(BBinUtils.GetBalance(loginname));
		return null;
	}
	
	public static DspResponseBean checkSBClient(String loginname) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseSBRemitDspResponseRequest(SportBookUtils.getMemberBalance(loginname));
	}
	
	public static String querySBCredit(String loginname) throws PostFailedException, ResponseFailedException {
		return checkSBClient(loginname).getInfo();
	}
	
	public static KenoResponseBean checkKenoClient(String loginname) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseKenocheckcreditResponseRequest(KenoUtil.checkcredit(loginname));
	}
	public static Double queryCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkClient(userid).getBalance();
	}
	
	public static String queryDspCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkDspClient(userid).getInfo();
	}
	public static String queryDspAginCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkDspAginClient(userid).getInfo();
	}
	public static String queryBbinCredit(String loginname) throws PostFailedException, ResponseFailedException {
		return checkBbinClient(loginname).getInfo();
	}
	
	/** 查询Ebet余额 */
	public static String queryEbetCredit(String loginname) throws PostFailedException, ResponseFailedException {
		return EBetUtil.getBalance(loginname, "EBET")+"";
	}

	public static Double queryKenoCredit(String loginname) throws PostFailedException, ResponseFailedException {
		KenoResponseBean kenoResponseBean = checkKenoClient(loginname);
		if(kenoResponseBean!=null && kenoResponseBean.getAmount()!=null){
			return kenoResponseBean.getAmount();
		}else{
			return -1.0;
		}
	}

	private static String generateDepositID() {
		return "D" + DateUtil.getDateFmtID();
	}
	
	private static String generateGetTurnOverID() {
		return "T" + DateUtil.getDateFmtID();
	}
	
	private static String generateWithdrawID() {
		return "W" + DateUtil.getDateFmtID();
	}

	private static String generateCheckClientID() {
		return "C" + DateUtil.getDateFmtID();
	}

	public static void main(String[] args) throws PostFailedException, ResponseFailedException {
		System.out.println(checkClient("e68test04"));
	}

}
