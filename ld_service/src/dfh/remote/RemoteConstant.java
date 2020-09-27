package dfh.remote;

import dfh.utils.Constants;
import dfh.utils.DateUtil;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RemoteConstant {

	 public static String HTTPS_HOST = "mis.ea3-mission.com";// 正式地址
//	 public static String HTTPS_HOST = "testmis.ea3-mission.com";// 测试地址

	public static String CHECKCLIENT_URL = "/configs/external/checkclient/e68/server.php";// 接口地址
	public static String DEPOSIT_URL = "/configs/external/deposit/e68/server.php";// 接口地址
	public static String WITHDRAW_URL = "/configs/external/withdrawal/e68/server.php";// 接口地址
	public static String CHECK_AFFILATE_URL = "/configs/external/checkaffiliate/e68/server.php";// 接口地址
	public static String GET_TURNOVER_URL = "/configs/external/turnover/e68/server.php";// 接口地址

	public final static String P_USERID = "userid";
	public final static String P_PASSWORD = "password";
	public final static String P_USERNAME = "username";
	public final static String P_ACODE = "acode";
	public final static String P_VENDORID = "vendorid";
	public final static String P_MERCHANTPASSCODE = "merchantpasscode"; 
	public final static String P_CURRENCYID = "currencyid";
	public final static String P_STATUS = "status";
	public final static String P_ERRDESC = "errdesc";
	public final static String P_REFNO = "refno";
	public final static String P_Amount = "amount";
	public final static String P_FROM_DATE = "from_date";
	public final static String P_TO_DATE = "to_date";
	public final static String P_TURNOVER = "turnover";
	public final static String P_PAYMENTID = "paymentid";
	public final static String P_BALANCE = "balance";
	public final static String P_LOCATION = "location";
	public final static String P_UUID = "uuid";
	public final static String P_CLIENTIP = "clientip";
	public final static String P_TICKET = "ticket";
	public final static String P_BEGINDATE = "begindate";
	public final static String P_ENDDATE = "enddate";
	public final static String P_TIMEZONE = "timezone";
	public final static String P_DATELIST = "datelist";
	public final static String P_STARTDATE = "startdate";
	public final static String PAGESITEEBETAPP = "ebetapp";

	public final static String A_ID = "id";
	public final static String A_NAME = "name";
	//SPortBooks
	public final static String SPORT_BOOK_RETURNCODE = "ReturnCode";
	public final static String SPORT_BOOK_DESCRIPTION = "Description"; 
	public final static String SPORT_BOOK_LOGINNAME = "LoginName";
	public final static String SPORT_BOOK_AMOUNT = "Amount"; 
	public final static String SPORT_BOOK_REFERENCENO = "ReferenceNo"; 
	public final static String SPORT_BOOK_TRANSACTIONID = "TransactionID"; 
	public final static String SPORT_BOOK_CURRENCYCODE = "CurrencyCode"; 
	public final static String SPORT_BOOK_ODDSTYPEID = "OddsTypeId";
	public final static String SPORT_BOOK_LANGCODE = "LangCode"; 
	public final static String SPORT_BOOK_TIMEZONE = "TimeZone"; 
	public final static String SPORT_BOOK_MEMBERSTATUS = "MemberStatus"; 
	public final static String SPORT_BOOK_CLIENTIP = "ClientIP";
	
	public static String PAGESITEEBET = "ebet";
	public static String PAGESITETT = "ttg"; 

	// 0表示运营环境，1表示测试环境
	public static int isLiveEnv = 0;

	public static final List<String> TEST_ACCOUNTS = Arrays.asList(new String[] { "e68test01", "e68test02", "e68test03", "e68test04", "e68test05"});

	public static String getVendorID(String loginname) {
		boolean contain = TEST_ACCOUNTS.contains(loginname);
		String vendorid = "";
		// 如果是正式帐号
		if (!contain) {
			// 如果是正式环境
			if (isLiveEnv == Constants.FLAG_TRUE.intValue())
				vendorid = "3";
			// 测试环境
			else
				vendorid = "3";
			// 如果是测试帐号
		} else {
			// 如果是正式环境
			if (isLiveEnv == Constants.FLAG_TRUE.intValue())
				vendorid = "2";
			// 测试环境
			else
				vendorid = "2";
		}
		return vendorid;
	}

	public void setHTTPS_HOST(String hTTPSHOST) {
		HTTPS_HOST = hTTPSHOST;
	}

	public void setIsLiveEnv(int isLiveEnv) {
		RemoteConstant.isLiveEnv = isLiveEnv;
	}

	// 人民币
	public static String CURRENCYID = "156";
	public static String TESTCURRENCYID = "1111";
	
	public static String WEBSITE = "tianwei";
	public static String PAGESITE = "ea";
	public static String PAGESITEDSP = "ag";
	public static String PAGESITEAGINDSP = "agin";
	public static String PAGESITEKENO = "keno";
	public static String PAGESITEKENO2 = "keno2";
	public static String PAGESITEBbin = "bbin";
	public static String PAGESITESb = "sb";
	public static String PAGESITESBA = "sba";
	public static String PAGESITEBok = "bok";
	public static String PAGESITEPT = "pt";
	public static String PAGESITEDT = "dt";
	public static String PAGESITEMG = "mg";
	public static String PAGESITEPNG = "png";
	public static String PAGESITENEWPT = "newpt";
	public static String PAGESITESIXLOTTERY = "sixlottery";
	public static String PAGESITEJC = "jc";
	public static String PAGESITENT = "nt";
    public static String PAGESITEGPI = "gpi";
    public static String PAGESITEQT = "qt";
    public static final String AGENT = "agent";
    public static String PAGESITESLOT = "slot";
    public static String PAGESITENTWO = "n2live";
    
	public static Integer HOURS_INTERVEAL_FROM_NOW = 0;

	public static Timestamp getOurDate(Date remoteDate) {
		return DateUtil.getDate(remoteDate, HOURS_INTERVEAL_FROM_NOW.intValue() * -1);
	}

	public static Date getRemoteDate(Date date) {
		return DateUtil.getDate(date, HOURS_INTERVEAL_FROM_NOW.intValue());
	}

	public static String getRemoteDateText() {
		return DateUtil.getDateFormat("yyyyMMdd", HOURS_INTERVEAL_FROM_NOW.intValue());
	}

	public RemoteConstant() {
	}

	public void setHOURS_INTERVEAL_FROM_NOW(Integer interval) {
		HOURS_INTERVEAL_FROM_NOW = interval;
	}

	public static void main(String[] args) {
		System.out.println(getVendorID("elftest01"));
	}
}
