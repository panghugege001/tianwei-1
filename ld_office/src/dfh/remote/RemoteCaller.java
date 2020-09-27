package dfh.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.exception.PostFailedException;
import dfh.exception.RemoteApiException;
import dfh.exception.ResponseFailedException;
import dfh.remote.bean.CheckClientResponseBean;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.GetTurnOverResponseBean;
import dfh.remote.bean.KenoResponseBean;
import dfh.remote.bean.Property;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.utils.APInUtils;
import dfh.utils.APUtils;
import dfh.utils.BBinUtils;
import dfh.utils.DateUtil;
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
//		对新开的会员及旧的会员的昵称前面加个 ld的前缀 
		list.add(new Property(RemoteConstant.P_USERNAME, "ld_"+username));
		list.add(new Property(RemoteConstant.P_STATUS, status));
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		list.add(new Property(RemoteConstant.P_VENDORID, RemoteConstant.getVendorID(userid)));
		list.add(new Property(RemoteConstant.P_CURRENCYID, RemoteConstant.CURRENCYID));
		list.add(new Property(RemoteConstant.P_ERRDESC, errdesc));

		return DocumentParser.createRequestXML("clogin", id, list);
	}

	public static DepositPendingResponseBean depositPendingRequest(String userid, Double amount, String refno, String acode) throws PostFailedException, ResponseFailedException {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		list.add(new Property(RemoteConstant.P_VENDORID, RemoteConstant.getVendorID(userid)));
		list.add(new Property(RemoteConstant.P_CURRENCYID, RemoteConstant.CURRENCYID));
		list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.P_REFNO, refno));

		String xml = DocumentParser.createRequestXML("cdeposit", generateDepositID(), list);
		return DocumentParser.parseDepositPendingResponseBean(HttpUtils.postXMLBySSL(RemoteConstant.HTTPS_HOST, RemoteConstant.DEPOSIT_URL, xml));
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
		list.add(new Property(RemoteConstant.P_VENDORID, RemoteConstant.getVendorID(userid)));
		list.add(new Property(RemoteConstant.P_CURRENCYID, RemoteConstant.CURRENCYID));
		list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.P_REFNO, refno));

		String xml = DocumentParser.createRequestXML("cwithdrawal", generateWithdrawID(), list);
		return DocumentParser.parseWithdrawalConfirmationResponseBean(HttpUtils.postXMLBySSL(RemoteConstant.HTTPS_HOST, RemoteConstant.WITHDRAW_URL, xml));
	}
	
	public static DspResponseBean depositPrepareDspRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.PrepareTransferCredit(userid, refno, "IN", amount, userid));
	}
	
	public static DspResponseBean depositConfirmDspRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.TransferCreditConfirm(userid, refno, "IN", amount, userid,flag));
	}
	
	public static DspResponseBean withdrawPrepareDspRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.PrepareTransferCredit(userid, refno, "OUT", amount, userid));
	}
	
	public static DspResponseBean withdrawConfirmDspRequest(String userid, Double amount, String refno,Integer flag) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.TransferCreditConfirm(userid, refno, "OUT", amount, userid,flag));
	}

	public static CheckClientResponseBean checkClient(String userid) throws PostFailedException, ResponseFailedException {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_VENDORID, RemoteConstant.getVendorID(userid)));
		list.add(new Property(RemoteConstant.P_CURRENCYID, RemoteConstant.CURRENCYID));

		String xml = DocumentParser.createRequestXML("ccheckclient", generateCheckClientID(), list);
		return DocumentParser.parseCheckClientResponseBean(HttpUtils.postXMLBySSLShort(RemoteConstant.HTTPS_HOST, RemoteConstant.CHECKCLIENT_URL, xml));
	}
	
	public static DspResponseBean checkBbinClient(String userid) throws PostFailedException, ResponseFailedException {	
		//return DocumentParser.parseBBinRemitDspResponseRequest(BBinUtils.GetBalance(userid));
		return null;
	}
	
	public static KenoResponseBean checkKenoClient(String userid) throws PostFailedException, ResponseFailedException {	
		return DocumentParser.parseKenocheckcreditResponseRequest(KenoUtil.checkcredit(userid));
	}
	
	public static DspResponseBean checkDspClient(String userid) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APUtils.GetBalance(userid, userid));
	}
	
	public static DspResponseBean checkAgInDspClient(String userid) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseDspResponseRequest(APInUtils.GetBalance(userid, userid));
	}
	public static DspResponseBean checkSBClient(String loginname) throws PostFailedException, ResponseFailedException {
		
		return DocumentParser.parseSBRemitDspResponseRequest(SportBookUtils.getMemberBalance(loginname));
	}
	public static Double queryCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkClient(userid).getBalance();
	}
	
	public static String queryBbinCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkBbinClient(userid).getInfo();
	}
	
	public static String queryKenoCredit(String userid) {
		// TODO Auto-generated method stub
		try {
			Double d = checkKenoClient(userid).getAmount();
			if(null!=d){
				return String.valueOf(d);
			}else{
				return null;
			}
		} catch (PostFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResponseFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String queryDspCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkDspClient(userid).getInfo();
	}
	
	public static String queryAgInDspCredit(String userid) throws PostFailedException, ResponseFailedException {
		return checkAgInDspClient(userid).getInfo();
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
		Date d = DateUtil.getDate(2013, 4, 7, 12);
		Date d1 = DateUtil.getDate(2013, 4, 8, 11);
		d1.setMinutes(59);
		d1.setSeconds(59);
		System.out.println(getTurnOverRequest("e68test03",d,d1).getTurnover());
	}

	public static String getSportBookMemberBalanceRequestXml(String loginName) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_LOGINNAME, loginName));
		return DocumentParser.createSportBookRequestXML("GetMemberBalance", list);
	}

}
