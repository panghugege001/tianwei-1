package dfh.utils;

import dfh.action.vo.ActivityCalendarVO;
import dfh.action.vo.AnnouncementVO;
import dfh.action.vo.QueryDataEuroVO;
import dfh.model.HdImage;
import dfh.model.Users;
import dfh.model.enums.NTErrorCode;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AxisSecurityEncryptUtil {
	private static Logger log = Logger.getLogger(AxisSecurityEncryptUtil.class);
	private static	Date olddate ;
	private static	Date olddate_cal ;
	private	static List<AnnouncementVO> announces ;
	
	private static	Date olddateEuro ;
	private	static List<QueryDataEuroVO> dataEuro ;
	private	static List<ActivityCalendarVO> activityCalendars;

	public static List<ActivityCalendarVO> queryTopActivity(Integer top) {
		Date now = new Date();
		try {
			if (null == activityCalendars) {
				log.info("首次获取活动日历");
				olddate_cal = now ;
				activityCalendars = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "queryTopActivity", new Object[] {top}, ActivityCalendarVO.class);
				return activityCalendars;
			}
			if((now.getTime()-olddate_cal.getTime())<1000*60*5 && null != activityCalendars){
				log.info("10分钟内存获取活动日历");
				return activityCalendars ;
			}
			log.info("数据库获取活动日历");
			olddate_cal = now ;
			activityCalendars = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryTopActivity", new Object[] {top}, ActivityCalendarVO.class);
			return activityCalendars ;
		} catch (Exception e) {
			log.error("获取活动日历异常",e);
		}
		return new ArrayList<ActivityCalendarVO>();
	}
	
	//临时活动 
	public static List<QueryDataEuroVO> queryDataEuro(){
		Date now = new Date();
		try {
			if(null == olddateEuro){
				log.info("首次获取Euro ");
				olddateEuro = now ;
				dataEuro = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryDataEuro", new Object[] {}, QueryDataEuroVO.class);
				System.out.println(dataEuro.size());
				return dataEuro ;
			}
			if((now.getTime()-olddateEuro.getTime())<1000*60*10 && null != dataEuro){
				log.info("10分钟内存获取Euro");
				return dataEuro ;
			}
			log.info("数据库获取Euro");
			olddateEuro = now ;
			dataEuro = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryDataEuro", new Object[] {}, QueryDataEuroVO.class);
			return dataEuro ;
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<AnnouncementVO> queryTopNews(){
		Date now = new Date();
		try {
			if(null == olddate){
				log.info("首次获取公告");
				olddate = now ;
				announces = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryTopNews", new Object[] {}, AnnouncementVO.class);
				return announces ;
			}
			if((now.getTime()-olddate.getTime())<1000*60*10 && null != announces){
				log.info("10分钟内存获取公告");
				return announces ;
			}
			log.info("数据库获取公告");
			olddate = now ;
			announces = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryTopNews", new Object[] {}, AnnouncementVO.class);
			return announces ;
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Users getUser(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {
			 loginname}, Users.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String updateUser(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "updatePtFlagUser", new Object[] {
			 loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static List<AnnouncementVO> query(){
		try {
			return AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "query", new Object[] {}, AnnouncementVO.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String generateLoginID(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "generateLoginID", 
						new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String generateLoginKenoID(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "generateLoginKenoID", 
						new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String addPayorder(String billno,Double amount,String attach,String ipsbillno,String mydate){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "addPayorder", new Object[] {
			 billno,amount,attach,ipsbillno,mydate }, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String generateTransferID(){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "generateTransferID", 
						new Object[] {}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String addPayorderZf(String billno,Double amount,String attach,String ipsbillno,String mydate,String merchant_code){
		try {
			//return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderZf", new Object[] {billno,amount,attach,ipsbillno,mydate,merchant_code }, String.class);
			return "操作错误！";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static Integer getGuestbookCount(String loginname) {
		try {
			Integer countBook = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getGuestbookCount", new Object[] { loginname }, Integer.class);
			if (countBook != null) {
				return countBook;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
	public static String getPayOnLineKey(){
		return AxisUtil.getPAY_ONLINE_KEY();
	}
	
	public static List<HdImage> queryImage(){
		try {
			return AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryImage", new Object[] {}, HdImage.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static HdImage queryImageFlash(){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryImageFlash", new Object[] {}, HdImage.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String addPayorderHf(String OrdId, String OrdAmt, String loginname, String msg) {
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderHf", new Object[] { OrdId, Double.parseDouble(OrdAmt), loginname, msg }, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	//获取游戏ea金额
	public static String getEaGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getEaGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	//获取游戏ag金额
	public static String getAgGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getAgGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	//获取游戏agin金额
	public static String getAginGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getAginGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	//获取游戏agin金额
	public static String getBbinGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getBbinBalance", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	//获取游戏keno金额
	public static String getKenoGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getKenoGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	//获取游戏keno2金额
	public static String getKeno2GameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getKeno2GameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	public static String getSixLotteryGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getSixLotteryGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	public static String getEbetGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getEbetBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	//获取游戏体育金额
	public static String getSbaGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getSbaGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	//获取游戏体育金额
	public static String getMwgGameMoney(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getMwgGameMoney", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	public static String getPtGameMoney(String loginname){
		try {
		Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
				AxisUtil.NAMESPACE, "getPtPlayerMoney", new Object[] { loginname }, Double.class);
		return result.toString();
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	//ag登录验证
	public static String agIsCustomerExist(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "agIsCustomerExist", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	//ag创建用户
	public static String agCheckOrCreateGameAccount(String loginname,String password){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "agCheckOrCreateGameAccount", new Object[] {loginname,password}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	//agin登录验证
	public static String aginIsCustomerExist(String loginname){
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "aginIsCustomerExist", new Object[] {loginname}, String.class); 
			return result;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	//agin创建用户
	public static String aginCheckOrCreateGameAccount(String loginname,String password){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "aginCheckOrCreateGameAccount", new Object[] {loginname,password}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	//BBin登录验证
	public static String bbinCheckOrCreateGameAccount(String loginname){
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "bbinCheckOrCreateGameAccount", new Object[] {loginname}, String.class);
			return result;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	//BBin验证
	public static String bbinForwardGame(String loginname,int gameType){
		try {
			//return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "bbinForwardGame", new Object[] {loginname,gameCode}, String.class);
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "bbinLogin", new Object[] {loginname,gameType,"h5"}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	
	//keno登录验证
	public static String kenoCheckOrCreateGameAccount(String PlayerId,String PlayerRealName,
			String PlayerIP,String VendorRef,String Remarks){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "kenoCheckOrCreateGameAccount", new Object[] {PlayerId,PlayerRealName,PlayerIP,VendorRef,Remarks}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	//keno2登录验证
	public static String keno2CheckOrCreateGameAccount(String PlayerId,String PlayerRealName,
			String PlayerIP,String VendorRef,String Remarks){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "keno2CheckOrCreateGameAccount", new Object[] {PlayerId,PlayerRealName,PlayerIP,VendorRef,Remarks}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String agTryLogin(String loginname,String password,String gameType,String domain) {
		   try {
			  return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "agTryLogin", new Object[] { loginname, password,gameType,domain}, String.class);
		   } catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		   }
		}
	
	public static String getJCBalance(String loginname) {
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getJcBalance", new Object[] { loginname }, String.class);
			GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
			return result;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * gpi余额
	 * @param loginname
	 * @return
	 */
	public static String getGPIBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getGPIBalance", new Object[] {loginname}, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	/**
	 * ttg余额
	 * @param loginname
	 * @return
	 */
	public static String getTTGBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getTtgBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	/**
	 * qt余额
	 * @param loginname
	 * @return
	 */
	public static String getQTBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getQtBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	/**
	 * jc余额
	 * @param loginname
	 * @return
	 */
	public static String getJCBalance2(String loginname) {
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),  AxisUtil.NAMESPACE, "getJcBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	/**
	 * nt余额
	 * @param loginname
	 * @return
	 */
	public static String getNTBalance(String loginname) {
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getNTBalance", new Object[] {loginname }, String.class);
			JSONObject rj = JSONObject.fromObject(result);
			
			return rj.getBoolean("result")?rj.getString("balance"):NTErrorCode.compare(rj.getString("error"));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}
	
	/**
	 * Ntwo余额
	 * @param loginname
	 * @return
	 */
	public static String getNTwoBalance(String loginname) {
		try {
			NTwoCheckClientResponseBean bean = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getNTwoCheckClientResponseBean", new Object[] {loginname}, NTwoCheckClientResponseBean.class);
			return String.valueOf(bean.getBalance().setScale(2, RoundingMode.HALF_UP));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	public static String getEbetAppBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
					AxisUtil.NAMESPACE, "getEbetAppBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getDtAppBalance(String loginname){  
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getDtBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getMgAppBalance(String loginname,String gameCode){  
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getMGBalance", new Object[] { loginname,gameCode }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	
	public static String getCq9AppBalance(String loginname, String gameCode) {
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getCQ9Balance", new Object[] { loginname,gameCode }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getPgAppBalance(String loginname,String gameCode){  
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPGBalance", new Object[] { loginname,gameCode }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getBgAppBalance(String loginname,String gameCode){  
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getBGBalance", new Object[] { loginname,gameCode }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getPngBalance(String loginname){  
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPNGBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getQdBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "querySignAmount", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	
	public static String getSlotBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getSlotBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}

	public static String getFishBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getFishBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getChessBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getChessBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getFanyaBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getFanyaBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}
	public static String getBitGameBalance(String loginname){
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getBitGameBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}

	/**
	 * 获取红包余额
	 * @param loginname
	 * @return
	 */
	public static String getRedWalletBalance(String loginname) {
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getRedWalletBalance", new Object[] { loginname }, String.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			return "系统繁忙"  ;
		}
	}



}
