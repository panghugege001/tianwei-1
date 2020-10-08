package com.nnti.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nnti.common.exception.LiveException;
import com.nnti.common.exception.LiveNoRollbackTransferException;
import com.nnti.common.exception.LiveNullBodyException;
import com.nnti.common.exception.LiveOrderExecuteException;
import com.nnti.common.exception.LiveTimeOutException;
import com.nnti.common.response.SbRespBody;

public class SbSportUtil {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SbSportUtil.class);
	private static final int SLEEP_TIME = 5000;
	private static final int WHILE_COUNT = 3;
	// 测试环境
	private static final String API_URL = "http://tsa.l0030.ig128.com/api";
	private static final String OPERATORID = "TW";
	private static final String VENDOR_ID = "l5jo1y1pql";
	private static final String PREFIX = "TW" + "_";
	private static final Integer CURRENCY = 20;// 货币类型，测试环境只能用20（美元），正式环境改为13（人民币）
	private static final Integer MIN_AMOUNT = 1;
	private static final Integer MAX_AMOUNT = 100;
	private static final Integer MIN_BET = 1;
	private static final Integer MAX_BET = 10;
	private static final Integer MaxBetPerMatch = 100;
	private static final Integer MaxBetPerBall = 10;
	// 中国盘口
	private static final String ODD_TYPE = "2";

	private static Object doPost(String api, Map<String, String> params) throws Exception {
		String resp = HttpClientUtils.doPost(API_URL + api, params, null);
		if (StringUtils.isEmpty(resp)) {
			throw new LiveNullBodyException(API_URL + api, params, resp);
		}
		SbRespBody body = NewJsonUtil.toObject(resp, SbRespBody.class);
		if ("0".equals(body.getError_code())) {
			return body.getData();
		}
		throw new LiveException(body.getError_code(), body.getMessage(), API_URL + api, params, resp);
	}

	public static Double getBalance(String account) throws Exception {
		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_member_ids", PREFIX + account);
		reqMap.put("wallet_id", "1");
		String api = "/CheckUserBalance";
		int count = 0;
		while (true) {
			try {
				logger.info("[SB]查询余额，请求参数：" + reqMap);
				Object data = doPost(api, reqMap);
				if (data != null) {
					List<Map> dataList = (List<Map>) data;
					Map map = dataList.get(0);
					String errorCode = map.get("error_code") + "";
					if ("0".equals(errorCode)) {
						return (double) map.get("balance");
					}
					if ("2".equals(errorCode)) { // 會員不存在
						checkAndCreateMember(account);
						return 0D;
					}
					if ("6".equals(errorCode)) { // 會員尚未轉帳過
						return 0D;
					}
					throw new LiveException(errorCode, errorCode, api, reqMap, data);
				}
				return 0D;
			} catch (LiveException ex) {
				logger.error("[SB]查询余额，异常：", ex);
				throw ex;
			} catch (Exception ex) {
				count++;
				if (count > WHILE_COUNT) {
					logger.error("[SB]查询余额，参数：" + reqMap, ex);
					throw new LiveTimeOutException(ex.getMessage(), api, reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	/**
	 * 转账查询
	 * 
	 * @param orderNo
	 * @return
	 * @throws Exception
	 *             使用转账成功的订单号查询返回：{"error_code":0,"message":"","Data":{
	 *             "trans_id":3406662,"transfer_date":"2019-12-04T00:53:59.43",
	 *             "vender_member_id":"bugfix17_zhenren2","amount":100.00,
	 *             "currency":13,"before_amount":0.0000,"after_amount":100.0000,
	 *             "status":0}} 使用不存在的订单号查询返回：{"error_code":2,"message":
	 *             "Transaction record does not exist"}
	 */
	public static boolean queryOrderStatus(String orderNo, String account, Double amount) throws Exception {
		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_trans_id", orderNo);
		reqMap.put("wallet_id", "1");
		int count = 0;
		String api = "/CheckFundTransfer";
		while (true) {
			try {
				logger.info("[SB]转账查询，请求参数：" + reqMap);
				Map data = (Map) doPost(api, reqMap);
				String status = data.get("status") + ""; // 0執行成功 ,1系統錯誤
															// 2未知狀態。請呼叫 API
															// “CheckFundTransfer”方法確認
															// 狀態
				if ("0".equals(status)) {
					return true;
				}
				if ("1".equals(status)) {
					return false;
				}
				if ("2".equals(status)) {
					throw new LiveOrderExecuteException("status:" + status, api, reqMap, data);
				}
				throw new LiveException(status, status, api, reqMap, data);
			} catch (LiveException ex) {
				logger.error("[SB]转账查询，异常：", ex);
				if ("2".equals(ex.getCode())) { // 2 交易紀錄不存在 3 等待五分鐘後再次確認
					return false;
				}
				if ("3".equals(ex.getCode())) {
					throw new LiveOrderExecuteException(ex.getCode(), ex.getApi(), ex.getParams(), ex.getResponse());
				}
				throw ex;
			} catch (Exception ex) {
				logger.error("[SB]转账查询，参数：" + reqMap, ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), api, reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	public static Boolean transfer(String account, Double amount, String orderNo) throws Exception {
		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_member_id", PREFIX + account);
		reqMap.put("vendor_trans_id", OPERATORID + "_" +orderNo);
		reqMap.put("amount", Math.abs(amount) + "");
		reqMap.put("currency", CURRENCY.toString());
		// direction=1 存款 0取款
		if (amount > 0) {
			reqMap.put("direction", "1");
		} else {
			reqMap.put("direction", "0");
		}
		reqMap.put("wallet_id", "1");
		int count = 0;
		String api = "/FundTransfer";
		while (true) {
			try {
				logger.info("[SB]额度转换，请求参数：" + reqMap);
				Object dataObj = doPost(api, reqMap);
				Map dataMap = (Map) dataObj;
				String status = dataMap.get("status").toString();

				//System.out.print(dataObj);
				//Map dataMap = (Map) dataObj;
				//String status = dataMap.get("status").toString(); // 0執行成功,1系統錯誤,2未知狀態。請呼叫
																	// API
																	// “CheckFundTransfer”方法確認
																	// 狀態
				if ("0".equals(status)) {
					return true;
				}
				if ("1".equals(status)) {
					throw new LiveException(status, "沙巴转账失败", api, reqMap, dataObj);
				}
				if ("2".equals(status)) {
					boolean bool = queryOrderStatus(orderNo, account, amount);
					if (bool) {
						return true;
					} else {
						throw new LiveException(status, "沙巴转账失败", api, reqMap, dataObj);
					}
				}
				throw new LiveOrderExecuteException(status, api, reqMap, dataObj);
			} catch (LiveException ex) {
				if ("2".equals(ex.getCode())) { // 會員不存在
					checkAndCreateMember(account);
				}
				if ("4".equals(ex.getCode())) { // 比最小或最大限制的轉帳金額 還更少或更多
					updateMember(account);
				} else if ("5".equals(ex.getCode())) {
					// 订单号重复不回滚
					throw new LiveNoRollbackTransferException(ex);
				} else {
					logger.error("[SB]额度转换，异常：", ex);
					throw ex;
				}
			} catch (Exception ex) {
				boolean bool = queryOrderStatus(orderNo, account, amount);
				if (bool) {
					return true;
				}
				logger.error("[SB]额度转换，参数：" + reqMap, ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), api, reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	private static String getToken(String account) throws Exception {
		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_member_id", PREFIX + account);
		String api = "/login";
		int count = 0;
		while (true) {
			try {
				Object data = doPost(api, reqMap);
				return String.valueOf(data);
			} catch (LiveException ex) {
				logger.error("[SB]获取token，异常：", ex);
				throw ex;
			} catch (Exception ex) {
				count++;
				if (count > WHILE_COUNT) {
					logger.error("[SB]获取token，参数：" + reqMap, ex);
					throw new LiveTimeOutException(ex.getMessage(), api, reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	/**
	 * 检查&创建会员
	 * 
	 * @param account
	 * @throws Exception
	 */
	public static void checkAndCreateMember(String account) throws Exception {
		try {
			createMember(account);
		} catch (LiveException ex) {
			if ("6".equals(ex.getCode())) { // 6:廠商會員識別碼重複
				return;
			}
			throw ex;
		} catch (Exception ex) {
			throw ex;
		}
		setMemberBetSetting(account);
	}

	public static void createMember(String account) throws Exception {
		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_member_id", PREFIX + account);
		reqMap.put("operatorId", OPERATORID);
		reqMap.put("firstname", "");
		reqMap.put("lastname", account);
		reqMap.put("username", PREFIX + account);
		reqMap.put("oddstype", ODD_TYPE);
		reqMap.put("currency", CURRENCY.toString());
		reqMap.put("maxtransfer", MAX_AMOUNT + "");
		reqMap.put("mintransfer", MIN_AMOUNT + "");
		int count = 0;
		while (true) {
			try {
				logger.info("[SB]创建会员，请求参数：" + reqMap);
				doPost("/CreateMember", reqMap);
				return;
			} catch (LiveException ex) {
				throw ex;
			} catch (Exception ex) {
				logger.error("[SB]创建会员，响应数据：" + reqMap, ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), "createMember", reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}

	}

	/**
	 * 检查&更新会员
	 * 
	 * @param account
	 * @throws Exception
	 */
	public static void updateMember(String account) throws Exception {
		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_member_id", PREFIX + account);
		reqMap.put("firstname", "");
		reqMap.put("lastname", account);
		reqMap.put("oddstype", ODD_TYPE);
		reqMap.put("maxtransfer", MIN_AMOUNT + "");
		reqMap.put("mintransfer", MAX_AMOUNT + "");
		logger.info("[SB]更新会员，请求参数：" + reqMap);
		int count = 0;
		while (true) {
			try {
				doPost("/UpdateMember", reqMap);
				return;
			} catch (LiveException ex) {
				logger.error("[SB]更新会员，异常：", ex);
				throw ex;
			} catch (Exception ex) {
				logger.error("[SB]更新会员，参数：" + reqMap, ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), "updateMember", reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}

	}

	/**
	 * 检查&创建会员
	 * 
	 * @param account
	 * @throws Exception
	 */
	public static void setMemberBetSetting(String account) throws Exception {
		List<Map> betSettingList = new ArrayList<>();
		Stream.of(1, 2, 3, 5, 8, 10, 11, 99).forEach(s -> {
			Map<String, Object> betMap = new HashMap<>();
			betMap.put("sport_type", s);
			betMap.put("min_bet", MIN_BET);
			betMap.put("max_bet", MAX_BET);
			betMap.put("max_bet_per_match", MaxBetPerMatch);
			// 沙巴技术反馈只有 sportType161有此参数.
			// betMap.put("max_bet_per_ball",sbConfig.getMaxBetPerBall());
			betSettingList.add(betMap);
		});
		Map<String, Object> mpMap = new HashMap<>();
		mpMap.put("sport_type", "99MP");
		mpMap.put("min_bet", MIN_BET);
		mpMap.put("max_bet", MAX_BET);
		mpMap.put("max_bet_per_match", MaxBetPerMatch);
		mpMap.put("max_bet_per_ball", MaxBetPerBall);
		betSettingList.add(mpMap);

		Map<String, String> reqMap = new HashMap<>();
		reqMap.put("vendor_id", VENDOR_ID);
		reqMap.put("vendor_member_id", PREFIX + account);
		reqMap.put("bet_setting", NewJsonUtil.toJson(betSettingList));
		logger.info("[SB]创建会员投注额度，请求参数：" + reqMap);
		int count = 0;
		while (true) {
			try {
				doPost("/SetMemberBetSetting", reqMap);
				return;
			} catch (LiveException ex) {
				logger.error("[SB]创建会员投注额度，异常：", ex);
				throw ex;
			} catch (Exception ex) {
				logger.error("[SB]创建会员投注额度，参数：" + reqMap, ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), "setMemberBetSetting", reqMap);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	public static void main(String[] args) {
		try {
			//System.out.print(transfer("test0002",22.00,"4343434312456"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
