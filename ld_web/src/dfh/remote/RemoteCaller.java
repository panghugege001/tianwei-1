package dfh.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.exception.PostFailedException;
import dfh.exception.RemoteApiException;
import dfh.exception.ResponseFailedException;
import dfh.remote.bean.CheckClientResponseBean;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.GetTurnOverResponseBean;
import dfh.remote.bean.Property;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.utils.APInUtils;
import dfh.utils.APUtils;
import dfh.utils.BBinUtils;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;
import dfh.utils.NumericUtil;
import dfh.utils.SkyUtils;
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
	
	public static DspResponseBean depositBbinRequest(String loginname, Integer remit,Double localCredit, String refno) throws Exception{
		
		return DocumentParser.parseBBinDspResponseRequest(BBinUtils.TransferCredit(loginname, refno, "IN", remit,localCredit));
	}
	
	public static DspResponseBean depositConfirmDspRequest(String userid, Double amount, String refno,Integer flag) throws Exception {
		
		return DocumentParser.parseDspResponseRequest(APUtils.TransferCreditConfirm(userid, refno, "IN", amount, userid,flag));
	}
	
	public static DspResponseBean withdrawBbinRequest(String loginname, Integer remit,Double localCredit, String refno) throws Exception {
		
		return DocumentParser.parseBBinDspResponseRequest(BBinUtils.TransferCredit(loginname, refno, "OUT", remit,localCredit));
	}
	
	public static DspResponseBean withdrawPrepareDspRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.PrepareTransferCredit(userid, refno, "OUT", amount, userid));
	}
	
	public static DspResponseBean withdrawConfirmDspRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.TransferCreditConfirm(userid, refno, "OUT", amount, userid,flag));
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
		
		return DocumentParser.parseBBinRemitDspResponseRequest(BBinUtils.GetBalance(loginname));
	}
	
	public static DspResponseBean checkSBClient(String loginname) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseSBRemitDspResponseRequest(SportBookUtils.getMemberBalance(loginname));
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
	
	public static String queryPtCredit(Integer id) throws PostFailedException, ResponseFailedException {
		String resultAmout = "";
		String loginString = SkyUtils.getSkyMonery(id);
		JSONObject jsonObj = JSONObject.fromObject(loginString);
		try {
			if (!jsonObj.containsKey("balance")) {
				resultAmout = "获取游戏金额失败!";
			} else {
				Double balance = jsonObj.getDouble("balance");
				if (balance != null) {
					resultAmout = String.valueOf(balance / 100);
				} else {
					resultAmout = "获取游戏金额失败！";
				}
			}
		} catch (Exception e) {
			if (!jsonObj.containsKey("error")) {
				resultAmout = "获取异常失败";
			} else {
				Integer error = jsonObj.getInt("error");
				if (error == 1) {
					resultAmout = "参数错误";
				} else if (error == 2) {
					resultAmout = "货币错误";
				} else if (error == 3) {
					resultAmout = "token或secret key 有误";
				} else if (error == 4) {
					resultAmout = "jackpot group id错误";
				} else if (error == 5) {
					resultAmout = "会员id错误";
				} else if (error == 6) {
					resultAmout = "用户名不能进入游戏";
				} else if (error == 7) {
					resultAmout = "资金不足";
				} else if (error == 8) {
					resultAmout = "日期错误";
				}
			}
		}
		return resultAmout;
	}
	
	public static String querySBCredit(String loginname) throws PostFailedException, ResponseFailedException {
		return checkSBClient(loginname).getInfo();
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
