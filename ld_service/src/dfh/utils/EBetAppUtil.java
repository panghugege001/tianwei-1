package dfh.utils;

import dfh.action.vo.ebetapp.BetHistory;
import dfh.action.vo.ebetapp.EBetAppHistory;
import dfh.action.vo.ebetapp.EBetAppResponseVo;
import dfh.model.PlatformData;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ebetApp 接口相关
 */
public class EBetAppUtil {

	private static Logger log = Logger.getLogger(EBetAppUtil.class);

	private static final String baseUrl = "http://47.90.23.45:8888";
	private static final String getBalanceUrl = "/api/userinfo";
	private static final String transferUrl = "/api/recharge";
	private static final String transferConfirmUrl = "/api/rechargestatus";
	private static final String getBetHistoryUrl = "/api/userbethistory";

	public static final String DEFAULT_CHANNEL_ID = "89";
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"; //2016-06-18 20:10:10
	static final SimpleDateFormat SDF = new SimpleDateFormat(TIME_PATTERN);

	/**
	 * 获取玩家余额
	 * <p>
	 * Request sample data
	 * channelId:60
	 * username:woodytest
	 * signature:XjnC5Sqg7TUuyIe6Fr5NgUEtuJ9YV4Etqi+t2ohvxtqm9JSckLXAF+zjxcNDkM1L6ab0FKnSE7Zk3CPDfhdqeYYCANYEQsHcws5hWPSnK96gTUAhS2HLSs09l7g/UpGpXNUfjqFxrelLJssx44tk7NJy8fg2FLqRUtoCEEFpcuk=
	 * timestamp:1447722613
	 *
	 * @param loginname
	 * @return
	 */
	public static Double getBalance(String loginname) throws Exception {
		log.info("ebet查询余额：" + loginname);
		EBetAppResponseVo vo = new EBetAppResponseVo();
		try {
			// Request parameters and other properties.
			long timeStamp = System.currentTimeMillis();
			NameValuePair[] paramData = NameValuePairBuilder.anPair()
					.with("channelId", DEFAULT_CHANNEL_ID)
					.with("username", loginname)
					.with("timestamp", timeStamp)
					.with("signature", EBetAppRsaUtil.sign(loginname + timeStamp))
					.build();
			String result = query(getBalanceUrl, paramData);
			vo = EBetAppResponseVo.convertFromJsonString(result);
			if (vo.getStatus() == 200) {
				return vo.getMoney();
			} else {
				throw new IllegalArgumentException(getErrorMessage(vo.getStatus()));
			}
		} catch (Exception e) {
			log.error(loginname + "ebet查询余额异常 , 错误:" + getErrorMessage(vo.getStatus()));
			throw e;
		}
	}

	/**
	 * 转账
	 * <p>
	 * Reuqest 参考
	 * channelId:60
	 * username:woodytest
	 * money:100
	 * rechargeReqId:79e824e6-3139-11e6-b548-0242ac11000
	 * signature:XjnC5Sqg7TUuyIe6Fr5NgUEtuJ9YV4Etqi+t2ohvxtqm9JSckLXAF+zjxcNDkM1L6ab0FKnSE7Zk3CPDfhdqeYYCANYEQsHcws5hWPSnK96gTUAhS2HLSs09l7g/UpGpXNUfjqFxrelLJssx44tk7NJy8fg2FLqRUtoCEEFpcuk=
	 * timestamp:1447722613
	 *
	 * @param loginname
	 * @param transferID Unique transaction id.
	 * @param credit     Member remit. Must be an integer.
	 * @param platform
	 * @param type       IN or OUT
	 * @return
	 */
	public static boolean transfer(String loginname, String transferID, Double credit, String platform, String type) {
		log.info(platform + "转账：" + loginname + "   " + type + "    " + credit);
		if (type != null && type.equalsIgnoreCase("out") && credit >= 0) {
			credit = -credit;
		}
		try {
			// Request parameters and other properties.
			long timeStamp = System.currentTimeMillis();
			NameValuePair[] paramData = NameValuePairBuilder.anPair()
					.with("channelId", DEFAULT_CHANNEL_ID)
					.with("username", loginname)
					.with("money", String.valueOf(credit))
					.with("rechargeReqId", transferID)
					.with("timestamp", timeStamp)
					.with("signature", EBetAppRsaUtil.sign(loginname + timeStamp))
					.build();
			String result = query(transferUrl, paramData);
			EBetAppResponseVo vo = EBetAppResponseVo.convertFromJsonString(result);
			if (vo.getRechargeReqId().equals(transferID) && vo.getStatus() == 200) {
				// 转账完成
				return true;
			} else {
				throw new IllegalArgumentException(getErrorMessage(vo.getStatus()));
			}
		} catch (Exception e) {
			log.error("ebet转账异常 , Message = " + e.getMessage());
			return false;
		}
	}

	/**
	 * 转账确认
	 * Request data 参考
	 * channelId:60
	 * rechargeReqId:79e824e6-3139-11e6-b548-0242ac11000
	 * signature:XjnC5Sqg7TUuyIe6Fr5NgUEtuJ9YV4Etqi+t2ohvxtqm9JSckLXAF+zjxcNDkM1L6ab0FKnSE7Zk3CPDfhdqeYYCANYEQsHcws5hWPSnK96gTUAhS2HLSs09l7g/UpGpXNUfjqFxrelLJssx44tk7NJy8fg2FLqRUtoCEEFpcuk=
	 *
	 * @param loginname
	 * @param transferID
	 * @return
	 */
	public static boolean transferConfirm(String loginname, String transferID) {
		log.info("ebet转账确认：" + loginname + "   订单ID" + transferID);
		try {
			// Request parameters and other properties.
			NameValuePair[] paramData = NameValuePairBuilder.anPair()
					.with("channelId", DEFAULT_CHANNEL_ID)
					.with("username", loginname)
					.with("rechargeReqId", transferID)
					.with("signature", EBetAppRsaUtil.sign(transferID))
					.build();
			String result = query(transferConfirmUrl, paramData);
			EBetAppResponseVo vo = EBetAppResponseVo.convertFromJsonString(result);
			if (vo.getRechargeReqId().equals(transferID) && vo.getStatus() == 200) {
				// 转账完成
				return true;
			} else {
				throw new IllegalArgumentException(getErrorMessage(vo.getStatus()));
			}
		} catch (Exception e) {
			log.error("ebet转账确认异常 , Message = " + e.getMessage());
			return false;
		}
	}

	/**
	 * 查询时间区间全部资料
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static EBetAppHistory getBetHistory(Date startTime, Date endTime) {
		// 空字串代表查询全部
		return getBetHistory("",startTime,endTime);
	}

	public static EBetAppHistory getBetHistory(String loginname, Date startTime, Date endTime) {
		EBetAppHistory betHistory = getBetHistory(loginname, startTime, endTime, 1, 100);
		//对方一次的BetHistory最多100笔 , 需分次查询
		if (betHistory.getCount() > 100) {
			int query = (int) Math.ceil((double) betHistory.getCount() / 100);
			for (int i = 2; i <= query; i++) {
				EBetAppHistory tempHistory = getBetHistory(loginname, startTime, endTime, i, 100);
				betHistory.getBetHistories().addAll(tempHistory.getBetHistories());
			}
		}
		filterCreateTimeNotInRange(betHistory,startTime,endTime);
		return betHistory;
	}

	/**
	 * @param loginname
	 * @param pageNum
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static EBetAppHistory getBetHistory(String loginname, Date startTime, Date endTime, int pageNum) {
		EBetAppHistory betHistory = getBetHistory(loginname, startTime, endTime, pageNum, 100);
		filterCreateTimeNotInRange(betHistory,startTime,endTime);
		return betHistory;
	}

	private static void filterCreateTimeNotInRange(EBetAppHistory history, Date startTime, Date endTime) {
		List<BetHistory> newList = new ArrayList<BetHistory>();
		DateTime startDateTime = new DateTime(startTime);
		DateTime endDateTime = new DateTime(endTime);
		for(BetHistory tempHistory : history.getBetHistories()){
			DateTime createDate = new DateTime(tempHistory.getCreateTime());
			if(createDate.isAfter(startDateTime) && createDate.isBefore(endDateTime)){
				newList.add(tempHistory);
			}
		}
		history.setBetHistories(newList);
	}

	/**
	 * Request Sample data
	 * <p>
	 * username:woodytest
	 * startTimeStr:2016-04-01 10:10:10
	 * endTimeStr:2016-06-14 20:10:10
	 * channelId:60
	 * subChannelId:0
	 * pageNum:1
	 * pageSize:100
	 * timestamp:1447722613
	 * signature:XjnC5Sqg7TUuyIe6Fr5NgUEtuJ9YV4Etqi+t2ohvxtqm9JSckLXAF+zjxcNDkM1L6ab0FKnSE7Zk3CPDfhdqeYYCANYEQsHcws5hWPSnK96gTUAhS2HLSs09l7g/UpGpXNUfjqFxrelLJssx44tk7NJy8fg2FLqRUtoCEEFpcuk=
	 * <p>
	 * 签名 （字符串拼接）username+timestamp
	 *
	 * @param loginname /
	 * @return
	 */
	private static EBetAppHistory getBetHistory(String loginname, Date startTime, Date endTime, int page, int pageSize) {
		String startTimeString = SDF.format(startTime);
		String endTimeString = SDF.format(endTime);
		log.info("ebet 查询投注记录： loginName =" + loginname
				+ " , startTime =" + startTimeString
				+ " , endTime =" + endTimeString
				+ " , page =" + page + " , pageSize =" + pageSize);
		try {
			// Request parameters and other properties.
			long timestamp = System.currentTimeMillis();
			NameValuePair[] paramData = NameValuePairBuilder.anPair()
					.with("channelId", DEFAULT_CHANNEL_ID)
					.with("username", loginname)
					.with("startTimeStr", startTimeString)
					.with("endTimeStr", endTimeString)
					.with("subChannelId", 0)
					.with("pageNum", page)
					.with("pageSize", pageSize)
					.with("timestamp", timestamp)
					.with("signature", EBetAppRsaUtil.sign(loginname + timestamp))
					.build();
			String result = query(getBetHistoryUrl, paramData);
			EBetAppHistory vo = EBetAppHistory.convertFromJson(result);
			if (vo.getStatus() == 200) {
				return vo;
			} else {
				throw new IllegalArgumentException(getErrorMessage(vo.getStatus()));
			}
		} catch (Exception e) {
			log.error("ebet 查询投注记录异常 , Message = " + e.getMessage());
			return null;
		}
	}

	private static String query(String queryUri, NameValuePair[] paramData) throws IOException {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(baseUrl + queryUri);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestBody(paramData);
		//Execute and get the response.
		httpClient.executeMethod(method);
		String result = method.getResponseBodyAsString();
		return result;
	}

	/**
	 * 根据日期查询当天玩家盈利情况
	 * loginname = 用户名，转空串导出渠道下所有用户记录
	 *
	 * @param queryDate
	 * @return
	 */
	public static List<PlatformData> getDailyProfit(Date queryDate) {
		String loginname = "";
		Date startDay = new DateTime(queryDate).toDateMidnight().toDate();
		Date endDay = new DateTime(queryDate).toDateMidnight().toDateTime().plusDays(1).minusSeconds(1).toDate();
		EBetAppHistory betHistory = getBetHistory(loginname, startDay, endDay);
		Map<String, List<BetHistory>> historyMap = betHistory.splitByUserName();
		return betHistory.convertToProfits(historyMap, startDay, endDay);
	}

	/**
	 * 根据日期查询当天玩家盈利情况
	 *
	 * @param loginname
	 * @param queryDate
	 * @return
	 */
	public static PlatformData getDailyProfit(String loginname, Date queryDate) {
		Date startDay = new DateTime(queryDate).toDateMidnight().toDate();
		Date endDay = new DateTime(queryDate).toDateMidnight().toDateTime().plusDays(1).minusSeconds(1).toDate();
		EBetAppHistory betHistory = getBetHistory(loginname, startDay, endDay);
		PlatformData profit = betHistory.getProfit(startDay, endDay);
		return profit;
	}

	public static String getErrorMessage(int statusCode) {
		switch (statusCode) {
			case 201:
				return "相同的充值请求";
			case 202:
				return "渠道不存在";
			case 4002:
				return "系统繁忙，稍后在试";
			case 4019:
				return "用户余额不足";
			case 4025:
				return "EBetAppServer 维护中";
			case 4026:
				return "验签失败";
			case 4027:
				return "IP 无法问权限";
			case 4028:
				return "不能给游客充值";
			case 4037:
				return "用户不存在";
			default:
				return "未知错误 , statusCode = " + statusCode;
		}
	}

}
