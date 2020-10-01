package dfh.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dfh.exception.LiveException;
import dfh.exception.LiveNoRollbackTransferException;
import dfh.exception.LiveNullBodyException;
import dfh.exception.LiveTimeOutException;
import dfh.model.enums.GameKindEnum;
import dfh.response.BgBetRecordRep;

public class BGUtil {
	private static final Logger logger = LoggerFactory.getLogger(BGUtil.class);
	private static String url = "http://am.bgvip55.com/open-cloud/api";
	private static String bg_prefix = "tw_live_";
	// 代理账号
	private static String agent_account = "test7899";
	// 代理密码
	private static String agent_pwd = "test1234";
	private static String SN = "am00";
	private static String secretKey = "8153503006031672EF300005E5EF6AEF";
	private static int SLEEP_TIME = 5000;
	private static int WHILE_COUNT = 3;
	// 账号不存在
	private static final String USER_NOT_EXISTS = "2213";

	private static <T> T doPost(API api, String id, Map<String, String> params) throws Exception {
		String resp = null;
		Map<String, Object> postData = new HashMap<>();
		postData.put("id", id);
		postData.put("method", api.getMethod());
		postData.put("params", params);
		postData.put("jsonrpc", "2.0");
		resp = HttpClientUtils.doPost(url, NewJsonUtil.toJson(postData), null);
		if (StringUtil.isBlank(resp)) {
			throw new LiveNullBodyException(api.getMethod(), params, resp);
		}
		BgBetRecordRep result = NewJsonUtil.toObject(resp, BgBetRecordRep.class);
		if (result == null) {
			throw new LiveNullBodyException(api.getMethod(), params, resp);
		}
		if (result.getError() != null) {
			Map<String, String> errorMap = NewJsonUtil.toMapObject(NewJsonUtil.toJson(result.getError()));
			throw new LiveException(errorMap.get("code"), errorMap.get("message"), api.getMethod(), params, resp);
		}
		return (T) result;
	}

	/**
	 * 获取余额
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public static Double getBalance(String account) {
		try {
			checkUser(account);
			String random = UUID.randomUUID().toString();
			String loginId = bg_prefix + account;
			String secretCode = HashUtil.sha1Base64(agent_pwd);
			String digest = HashUtil.md5Hex(random + SN + loginId + secretCode);
			Map<String, String> params = new HashMap<>();
			params.put("random", random);
			params.put("digest", digest);
			params.put("sn", SN);
			params.put("loginId", loginId);
			int count = 0;
			while (true) {
				try {
					BgBetRecordRep<Object, Map<String, String>> result = doPost(API.BALANCE_GET, random, params);
					return MathUtil.doubleFormat(Double.parseDouble(String.valueOf(result.getResult())), 2);
				} catch (LiveException ex) {
					logger.error("{}异常：", API.BALANCE_GET.getName(), ex);
					throw ex;
				} catch (Exception ex) {
					logger.error("{}异常：", API.BALANCE_GET.getName(), ex);
					count++;
					if (count > WHILE_COUNT) {
						throw new LiveTimeOutException(ex.getMessage(), API.BALANCE_GET.getMethod(), params);
					}
					Thread.sleep(SLEEP_TIME);
					;
				}
			}
		} catch (Exception e) {
			logger.error("{}异常：", e.getMessage());
		}
		return null;

	}

	/**
	 * 检查会员
	 * 
	 * @param account
	 * @throws Exception
	 */
	public static void checkUser(String account) throws Exception {
		String random = UUID.randomUUID().toString();
		String loginId = bg_prefix + account;
		String secretCode = HashUtil.sha1Base64(agent_pwd);
		String digest = HashUtil.md5Hex(random + SN + loginId + secretCode);
		Map<String, String> params = new HashMap<>();
		params.put("random", random);
		params.put("digest", digest);
		params.put("sn", SN);
		params.put("loginId", loginId);
		int count = 0;
		while (true) {
			try {
				doPost(API.USER_GET, random, params);
				return;
			} catch (LiveException ex) {
				if (Objects.equals(USER_NOT_EXISTS, ex.getCode())) {
					createUser(account);
					return;
				}
				logger.error("{}异常：", API.USER_GET.getName(), ex);
				throw ex;
			} catch (Exception ex) {
				logger.error("{}异常：", API.USER_GET.getName(), ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), API.USER_GET.getMethod(), params);
				}
				Thread.sleep(SLEEP_TIME);
				;
			}
		}
	}

	/**
	 * 创建会员账号
	 * 
	 * @param account
	 * @return
	 */
	public static void createUser(String account) throws Exception {
		String random = UUID.randomUUID().toString();
		String loginId = bg_prefix + account;
		String secretCode = HashUtil.sha1Base64(agent_pwd);
		String digest = HashUtil.md5Hex(random + SN + secretCode);
		Map<String, String> params = new HashMap<>();
		params.put("random", random);
		params.put("digest", digest);
		params.put("sn", SN);
		params.put("loginId", loginId);
		params.put("nickname", loginId);
		params.put("agentLoginId", agent_account);
		int count = 0;
		while (true) {
			try {
				doPost(API.USER_CREATE, random, params);
				return;
			} catch (LiveException ex) {
				logger.error("{}异常：", API.USER_CREATE.getName(), ex);
				throw ex;
			} catch (Exception ex) {
				logger.error("{}异常：", API.USER_CREATE.getName(), ex);
				count++;
				if (count > WHILE_COUNT) {
					throw new LiveTimeOutException(ex.getMessage(), API.USER_CREATE.getMethod(), params);
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	/**
	 * 开始游戏
	 * 
	 * @param account
	 * @param type
	 *            LIVE真人 BY捕鱼
	 * @param gameType
	 *            105:BG捕鱼大师(默认); 411:西游捕鱼; 443:BG棋牌
	 * @param ip
	 * @param isMobile
	 *            true mobile false 其他
	 * @param url
	 * @param isMode
	 *            true试玩 false真钱
	 * @return
	 * @throws Exception
	 */
	public static String play(String account, String type, String gameType, Boolean isMobile, Boolean isMode) {
		try {
			checkUser(account);
			API api;
			String random = UUID.randomUUID().toString();
			String loginId = bg_prefix + account;
			String isMobileUrl = isMobile ? "1" : "0";
			Map<String, String> params = new HashMap<>();
			if (GameKindEnum.LIVE.getCode().equals(type)) {
				api = API.VIDEO_GAME_URL;
				String secretCode = HashUtil.sha1Base64(agent_pwd);
				String digest = HashUtil.md5Hex(random + SN + loginId + secretCode);
				// 试玩
				if (isMode == true) {
					api = API.VIDEO_TRIAL_GAME_URL;
				} else {
					params.put("loginId", loginId);
				}
				params.put("random", random);
				params.put("digest", digest);
				params.put("sn", SN);
				params.put("locale", "zh_CN");
				params.put("isMobileUrl", isMobileUrl);
			} else {
				/**
				 * 目前只接入了视讯和捕鱼
				 */
				api = API.FISH_URL;
				String sign = HashUtil.md5Hex(random + SN + secretKey);
				params.put("random", random);
				params.put("sign", sign);
				params.put("sn", SN);
				// 试玩
				if (isMode == true) {
					api = API.FISH_TRIAL_URL;
				} else {
					params.put("loginId", loginId);
				}
				params.put("isMobileUrl", isMobileUrl);
				params.put("lang", "zh_CN");
				params.put("gameType", gameType);
			}
			int count = 0;
			while (true) {
				try {
					BgBetRecordRep<Object, Map<String, String>> result = doPost(api, random, params);
					return String.valueOf(result.getResult());
				} catch (LiveException ex) {
					logger.error("{}异常：", api.getName(), ex);
					throw ex;
				} catch (Exception ex) {
					logger.error("{}异常：", api.getName(), ex);
					count++;
					if (count > WHILE_COUNT) {
						throw new LiveTimeOutException(ex.getMessage(), api.getMethod(), params);
					}
					Thread.sleep(SLEEP_TIME);
					;
				}
			}
		} catch (Exception e) {
			logger.error("{}异常：", e.getMessage());
		}
		return null;

	}

	/**
	 * 转账(0 BG充值 1 BG转出)
	 * 
	 * @param account
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String transfer(String account, Double amount) {
		try {
			checkUser(account);
			String random = UUID.randomUUID().toString();
			String loginId = bg_prefix + account;
			String secretCode = HashUtil.sha1Base64(agent_pwd);
			String digest = HashUtil.md5Hex(random + SN + loginId + amount + secretCode);
			Map<String, String> params = new HashMap<>();
			params.put("random", random);
			params.put("digest", digest);
			params.put("sn", SN);
			params.put("loginId", loginId);
			params.put("amount", String.valueOf(amount));
			params.put("bizId", String.valueOf(new Date().getTime()));
			params.put("checkBizId", "1");
			int count = 0;
			API api;
			if (amount > 0) {
				api = API.TRANSFER_IN;
			} else {
				api = API.TRANSFER_OUT;
			}
			while (true) {
				try {
					doPost(api, random, params);
					return "success";
				} catch (LiveException ex) {
					if ("2474".equals(ex.getCode())) {
						// 订单号重复不回滚
						throw new LiveNoRollbackTransferException(ex);
					} else {
						logger.error("{}异常：", api.getName(), ex);
						throw ex;
					}
				} catch (Exception ex) {
					logger.error("{}异常：", api.getName(), ex);
					count++;
					if (count > WHILE_COUNT) {
						throw new LiveTimeOutException(ex.getMessage(), api.getMethod(), params);
					}
					Thread.sleep(SLEEP_TIME);
					;
				}
			}
		} catch (Exception e) {
			logger.error("{}异常：", e.getMessage());
		}
		return null;
	}

	private enum API {
		TRANSFER_IN("BG转入", "open.balance.transfer"), TRANSFER_OUT("BG转出", "open.balance.transfer"), BALANCE_GET(
				"BG查询账户余额", "open.balance.get"), USER_GET("BG查询会员是否存在", "open.user.get"), USER_CREATE("BG会员注册",
						"open.user.create"), VIDEO_GAME_URL("BG视讯游戏进入", "open.video.game.url"), VIDEO_TRIAL_GAME_URL(
								"BG视讯游戏试玩进入", "open.video.trial.game.url"), VIDEO_ORDER_QUERY("BG视讯游戏注单查询",
										"open.order.agent.query"), VIDEO_GAME_RESULT("BG视讯查看游戏结果",
												"open.sn.video.order.detail.url.get"), FISH_URL("BG捕鱼进入",
														"open.game.bg.fishing.url"), FISH_TRIAL_URL("BG捕鱼试玩进入",
																"open.game.bg.fishing.trial.url"), FISH_ORDER_QUERY(
																		"BG捕鱼注单查询",
																		"open.order.bg.fishing.agent.query"), FISH_GAME_RESULT(
																				"BG捕鱼查看游戏结果",
																				"open.sn.fish.order.detail.url.get");

		private String name;
		private String method;

		API(String name, String method) {
			this.name = name;
			this.method = method;
		}

		public String getName() {
			return name;
		}

		public String getMethod() {
			return method;
		}
	}

	public static void main(String[] args) {
		System.out.print("start...............");
		try {
			//System.out.print(play("test888", "LIVE", "", true, false));
			//System.out.print(getBalance("test888"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
