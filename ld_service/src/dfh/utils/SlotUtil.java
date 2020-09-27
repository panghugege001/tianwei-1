package dfh.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dfh.action.vo.DtPotVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.action.vo.GameVo;
import dfh.model.Users;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.GameRequestVo;
import dfh.model.bean.GameResponseDTO;
import dfh.model.enums.ProposalType;
import dfh.model.enums.VipLevel;
import dfh.utils.sendemail.AESUtil;
import net.sf.json.JSONObject;

public class SlotUtil {

	private static Logger log = Logger.getLogger(SlotUtil.class);

	public static final Map<String, Integer> PLAFROMVALIDXIMA = new HashMap<String, Integer>(); // 获取加入系统反水的平台和老虎机

	public static final String RESULT_SUCC = "SUCCESS";

	private static final String PRODUCT = "ld";
	private static final String PLATFORM = "pt";
	// private static final String PRODUCTKEY =
	// "8f515ef3135e688706fb8f83e1ff72f5";
	private static final String PRODUCTKEY = "OZusgRNOgnxXmsOkojXxbw==";
	private static final String ACCOUNTTYPE = "slot"; // slot wallet
	private static final String FISH_ACCOUNTTYPE = "fish";// fish wallet
	private static final String MERCHANTCODE = "swnwsnrsld";
	public static final String FUNMODE = "fun";
	public static final String MAINTAIN = "MAINTAIN";
	public static final String ERROR = "ERROR";
	// public static final String GAME_CENTER_URL =
	// "http://ldalletcall.com/wallet/getURLParams/";
	public static final String GAME_CENTER_URL = "http://ldalletcall.com/wallet/getURLParams/";
	public static final String GAME_CENTER_URL4PT = "http://ldalletcall.com/wallet/getPtUrl/";
	public static final String TEST_GAME_CENTER_URL = "http://hy.mgsjiekou.com/wallet/getURLParams/";
	public static final String GAME_CENTER_DEBIT = "http://ldalletcall.com/wallet/debit/";
	public static final String GAME_CENTER_CREDIT = "http://ldalletcall.com/wallet/credit/";
	public static final String GAME_CENTER_BALANCE = "http://ldalletcall.com/wallet/getBalance/";
	public static final String GAME_CENTER_PLAYERBET = "http://ldalletcall.com/wallet/getPlayerSlotBetWinByTime/";
	public static final String GAME_LOGIN_URL = "https://gs.m27613.com/v1/mrch/game?merchantCode={MERCHANT_CODE}&gameCode={GAME_CODE}&ip={IP}&language=zh-cn&playmode={PLAYMODE}&ticket={TICKET}&merch_login_url={MERCH_LOGIN_URL}";
	public static final String GAME_LOGIN_TESTURL = "https://gs.m27613.com/v1/mrch/game?merchantCode=" + MERCHANTCODE
			+ "&gameCode={GAME_CODE}&language=zh-cn&playmode={PLAYMODE}&merch_login_url={MERCH_LOGIN_URL}";

	private static final String DT_PLATFORM = "dt";

	// public static final String DT_GAME_CENTER_URL =
	// "http://at.dtcallback.com/wallet/getURLParams/";

	private static String DT_GAME_URL = "http://kgpqbnmbdnb.com/ddtApi.html";

	private static final String DT_MERCHANTCODE = "NNTI_FHC_TWYL";
	private static final String MG_PLATFORM = "mg";
	private static final String NT_PLATFORM = "nt";
	private static final String DT_FISH_PLATFORM = "hyg";

	private static final String DT_FISH_GAME_API_URL = "http://servicefish.onlinegame.vip";
	private static final String DT_FISH_GAME_URL = "https://fishinggame.onlinegame.vip";
	private static final String DT_FISH_MERCHANTCODE = "DC_SUN_LD";

	static {
		PLAFROMVALIDXIMA.put("dt", 628);
		PLAFROMVALIDXIMA.put("ptsw", 632);
		PLAFROMVALIDXIMA.put(NT_PLATFORM, ProposalType.NTSELFXIMA.getCode());
		PLAFROMVALIDXIMA.put("mg", 629);
		PLAFROMVALIDXIMA.put("png", 630);
		PLAFROMVALIDXIMA.put("qt", 624);
	}
	/**
	 * 查询dt jackpot 奖池信息
	 *
	 * @param loginname
	 * @return
	 */
	private static final String DT_MERCHANTPASSWORD = "OZusgRNOgnxXmsOkojXxbw==";

	public static DtPotVO getDTjackPot() {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(DT_GAME_URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "GETJACKPOT");
		method.setParameter("BUSINESS", DT_MERCHANTCODE);
		String signatureKey = DigestUtils.md5Hex(DT_MERCHANTCODE + "GETJACKPOT" + DT_MERCHANTPASSWORD);
		method.setParameter("SIGNATURE", signatureKey);
		DtPotVO vo = new DtPotVO();
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();

			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(DtUtil.ErrorCode.C00000.getCode())) {
					JSONObject data = JSONObject.fromObject(a.get("DATA").toString());
					String pot = data.get("JACKPOT0").toString();
					vo.setCode(DtUtil.ErrorCode.C00000.getCode());
					vo.setPot(pot);
					return vo;
				}
			}
			log.error("异常结果:" + result);
			vo.setCode(DtUtil.ErrorCode.C000028.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常：" + e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return vo;
	}

	/**
	 * 公共查询余额方法
	 * 
	 * @param loginName
	 *            用户名
	 * @param acccountType
	 *            类型
	 * @return
	 */
	public static GameVo publicBalance(String loginName, String acccountType) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(acccountType);
			gameRequestVo.setOriginalName(loginName);
			result = sendHttpByAES(GAME_CENTER_BALANCE, gameRequestVo);
			GameVo gameVo = JSON.readValue(result, GameVo.class);
			return gameVo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取FISH余额，如果用户不存在则创建用户，成功后返回0
	 * 
	 * @param loginName
	 * @return
	 */
	public static Double getFishBalance(String loginName) {
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameVo gameVo = publicBalance(loginName, FISH_ACCOUNTTYPE);
			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONObject object = JSONObject.fromObject(gameVo.getData());
				return object.getDouble("fish");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取老虎机账户余额，如果用户不存在则创建用户，成功后返回0
	 * 
	 * @param loginName
	 * @return
	 */
	public static Double getBalanceByCode(String loginName, String gameCode) {
		try {
			Double balance = null;
			// 获取MG老虎机余额
			if (gameCode.equals("MGS")) {
				balance = MGSUtil.getBalance(loginName);
			}
			return balance;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 转入老虎机账户
	 * 
	 * @param loginName
	 * @param amount
	 * @return
	 */
	public static String transferToSlot(String loginName, Double amount) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();

		try {

			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setPlatform(PLATFORM);
			gameRequestVo.setAmount(amount);
			gameRequestVo.setOriginalName(loginName);

			result = sendHttpByAES(GAME_CENTER_CREDIT, gameRequestVo);

			log.info("transferToSlot --> loginName=" + loginName + " " + result.toString());

			GameVo gameVo = JSON.readValue(result, GameVo.class);
			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				return RESULT_SUCC;
			}

			return gameVo.getMessage();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("执行transferToSlot方法发生异常，异常信息：" + e.getMessage());
		}

		return null;
	}

	/**
	 * 获取玩家flash登陆URL
	 * 
	 * @param loginName
	 * @param gid
	 * @return
	 */
	public static String getNTParams(String loginName, String gameCode) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setGameCode(gameCode);
			gameRequestVo.setOriginalName(loginName);
			gameRequestVo.setPlatform(NT_PLATFORM);
			// 去中心钱包拿ticket
			result = sendHttpByAES(GAME_CENTER_URL, gameRequestVo);
			log.info("ticket：" + result);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONObject object = JSONObject.fromObject(gameVo.getData());

				return object.get("ntKey").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取玩家flash登陆URL
	 * 
	 * @param loginName
	 * @param gid
	 * @return
	 */
	public static String getMGTicket(String loginName, String gameCode) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			loginName = loginName.toLowerCase();
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setGameCode(gameCode);
			gameRequestVo.setOriginalName(loginName);
			gameRequestVo.setPlatform(MG_PLATFORM);
			// 去中心钱包拿ticket
			result = sendHttpByAES(GAME_CENTER_URL, gameRequestVo);
			log.info("ticket：" + result);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONObject object = JSONObject.fromObject(gameVo.getData());
				return object.get("ticket").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取玩家flash登陆URL
	 * 
	 * @param loginName
	 * @param gid
	 * @return
	 */
	public static String flashGameLogin(Users users, String gameCode, String redirectUrl, String playmode, String ip) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			// if (StringUtil.equals(playmode, FUNMODE)&&users==null) {
			// result=GAME_LOGIN_TESTURL.replace("{MERCHANT_CODE}",MERCHANTCODE).replace("{GAME_CODE}",
			// gameCode).replace("{MERCH_LOGIN_URL}",redirectUrl).replace("{PLAYMODE}",
			// playmode);
			// return result ;
			// }else if (StringUtil.equals(playmode, FUNMODE)&&users!=null) {
			// String
			// reloadUrl=redirectUrl+"/game/gameLoginPtSky.php?mode=real&gameCode="+gameCode;
			// result=GAME_LOGIN_TESTURL.replace("{MERCHANT_CODE}",MERCHANTCODE).replace("{GAME_CODE}",
			// gameCode).replace("{MERCH_LOGIN_URL}",URLEncoder.encode(reloadUrl,
			// "UTF-8")).replace("{PLAYMODE}", playmode);
			// return result ;
			// }

			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setGameCode(gameCode);
			if (!StringUtil.equals(playmode, FUNMODE)) {
				gameRequestVo.setOriginalName(users.getLoginname());
			}
			gameRequestVo.setPlatform(PLATFORM);
			gameRequestVo.setPlayMode(playmode);

			result = sendHttpByAES(GAME_CENTER_URL4PT, gameRequestVo);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {
				JSONObject object = JSONObject.fromObject(gameVo.getData());
				// result = GAME_LOGIN_URL.replace("{MERCHANT_CODE}",
				// object.get("merchantCode").toString()).replace("{GAME_CODE}",
				// gameCode).replace("{IP}", ip)
				// .replace("{MERCH_LOGIN_URL}",redirectUrl).replace("{PLAYMODE}",
				// playmode).replace("{TICKET}",
				// object.get("ticket").toString()).toString();
				return object.get("gameUrl").toString();
			}
			log.error(gameVo);
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据用户某个及其实践获取ptsky流水,如果返回空说明发生异常。 返回的pt 投注 默认修改为ptsky
	 * 
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public static List<Bean4Xima> getPlayerBetsByTime(String loginName, String timeBegin, String timeEnd) {
		String result = null;

		ObjectMapper JSON = new ObjectMapper();
		List<Bean4Xima> list = new ArrayList<Bean4Xima>();
		try {

			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setPlatform(PLATFORM);
			gameRequestVo.setTimeBegin(timeBegin);
			gameRequestVo.setTimeEnd(timeEnd);
			gameRequestVo.setOriginalName(loginName);

			result = sendHttpByAES(GAME_CENTER_PLAYERBET, gameRequestVo);
			log.info("getPlayerBetsByTime --> loginName=" + loginName + " " + result.toString());

			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtils.equals(gameVo.getCode(), "10000")) {

				JSONObject jsonObject = JSONObject.fromObject(gameVo.getData());
				Iterator iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					JSONObject jsonResult = JSONObject.fromObject(jsonObject.getString(key));
					Bean4Xima bXima = new Bean4Xima(jsonResult.getString("originalName"), jsonResult.getDouble("bet"),
							jsonResult.getDouble("win"), key.equals("pt") ? "ptsw" : key);
					list.add(bXima);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

		return list;
	}

	/*** AES加密 支付发起请求 */
	public static String sendHttpByAES(String url, GameRequestVo reqVo) throws Exception {
		ObjectMapper JSON = new ObjectMapper();
		String responseString = null;
		GameResponseDTO message = null;

		String requestJson = JSON.writeValueAsString(reqVo);
		requestJson = AESUtil.encrypt(requestJson);
		responseString = MyWebUtils.getHttpContentByParam(url,
				MyWebUtils.getListNamevaluepair("requestData", requestJson));
		message = JSON.readValue(responseString, GameResponseDTO.class);
		if (message.getResponseData() != null && !"".equals(message.getResponseData())) {
			String decrypt = AESUtil.decrypt(message.getResponseData().toString());
			return decrypt;
		}
		return null;
	}

	/**
	 * 根据用户及其实践获取ptsky流水,如果返回空说明发生异常。
	 * 
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public static Double getBetsAmount(String loginname, String timeStart, String timeEnd) {

		return null;
	}

	/**
	 * 获取玩家DTflash登陆URL
	 * 
	 * @param loginName
	 * @param gid
	 * @return
	 */
	public static String getDTFlashGameLogin(Users users, String gameCode, String redirectUrl, String playmode,
			String ip) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setGameCode(gameCode);
			gameRequestVo.setOriginalName(users.getLoginname());
			gameRequestVo.setPlatform(DT_PLATFORM);
			// 去中心钱包拿ticket
			result = sendHttpByAES(GAME_CENTER_URL, gameRequestVo);
			log.info("ticket：" + result);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {

				// 发送HttpClient请求去DT那拿数据
				JSONObject json = JSONObject.fromObject(gameVo.getData());
				String ticket = json.getString("ticket");
				Map<String, Object> map = new HashMap<String, Object>();
				HttpPost httpPost = new HttpPost(DT_GAME_URL);
				CloseableHttpClient client = HttpClients.createDefault();
				String respContent = null;
				// json方式
				// METHOD 方法
				// BUSINESS 商户号
				// GAMECODE 游戏编码
				// LANGUAGE 语言类别
				// TOKEN 认证令牌环
				// REMARK 备注信息
				// CLOSEURL 回跳地址
				JSONObject jsonParam = new JSONObject();
				map.put("METHOD", "LOGIN");
				map.put("BUSINESS", DT_MERCHANTCODE);
				map.put("LANGUAGE", "zh_CN");
				map.put("GAMECODE", gameCode);
				map.put("TOKEN", ticket);
				map.put("REMARK", null);
				map.put("CLOSEURL", null);
				jsonParam.put("data", map);
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
				HttpResponse resp = client.execute(httpPost);
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity he = resp.getEntity();
					respContent = EntityUtils.toString(he, "UTF-8");
					JSONObject jsonObj = JSONObject.fromObject(respContent);
					log.info("获取游戏地址" + jsonObj.toString());
					result = jsonObj.getString("data");
				}
				return result;
			}
			log.info("获取中心钱包ticket失败！");
			return null;

		} catch (Exception e) {
			log.error("获取游戏连接失败！");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * png游戏加载时获取ticket。
	 * 
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public static String getPngLoadingTicket(String loginName, String gameCode) {

		ObjectMapper JSON = new ObjectMapper();

		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setGameCode(gameCode);
			gameRequestVo.setPlatform("png");
			gameRequestVo.setOriginalName(loginName);

			// 中心钱包拿ticket
			String result = sendHttpByAES(GAME_CENTER_URL, gameRequestVo);

			return result;

			/*
			 * GameVo gameVo = JSON.readValue(result, GameVo.class);
			 * 
			 * if (StringUtil.equals(gameVo.getCode(), "10000")) {
			 * 
			 * JSONObject json = JSONObject.fromObject(gameVo.getData()); return
			 * json.getString("ticket"); } return null;
			 */
		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

	}

	/**
	 * QT游戏加载时获取ticket。
	 * 
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public static Map<String, String> getQTLoadingTicket(String loginName, String gameCode) {

		ObjectMapper JSON = new ObjectMapper();

		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setAccountType(ACCOUNTTYPE);
			gameRequestVo.setGameCode(gameCode);
			gameRequestVo.setPlatform("qt");
			gameRequestVo.setOriginalName(loginName);

			// 中心钱包拿ticket
			String result = sendHttpByAES(GAME_CENTER_URL, gameRequestVo);

			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {

				Map<String, String> retuenMap = new HashMap<String, String>();

				JSONObject json = JSONObject.fromObject(gameVo.getData());
				retuenMap.put("ticket", json.getString("ticket"));
				retuenMap.put("playerName", json.getString("playerName"));
				return retuenMap;
			}

			log.info("获取QT ticket失败：" + gameVo.getMessage());
			return null;
		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

	}

	/**
	 * 获取玩家DTFish登陆URL
	 * 
	 * @param loginName
	 * @param gid
	 * @return
	 */
	public static String getDTFishGameLogin(Users users) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {
			GameRequestVo gameRequestVo = new GameRequestVo();
			gameRequestVo.setProductContract(PRODUCT);
			gameRequestVo.setProductKey(PRODUCTKEY);
			gameRequestVo.setOriginalName(users.getLoginname());
			gameRequestVo.setGameCode("fish");
			gameRequestVo.setPlatform(DT_FISH_PLATFORM);
			// 去中心钱包拿ticket
			result = sendHttpByAES(GAME_CENTER_URL, gameRequestVo);
			log.info("ticket：" + result);
			GameVo gameVo = JSON.readValue(result, GameVo.class);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {

				// 发送HttpClient请求去DT那拿数据
				JSONObject json = JSONObject.fromObject(gameVo.getData());
				String ticket = json.getString("ticket");
				String url = DT_FISH_GAME_API_URL + "/api/login?business=" + DT_FISH_MERCHANTCODE
						+ "&language=zh_CN&token=" + ticket + "&gameCode=FISH";
				log.info("getDTFishGameLogin=========url" + url);
				HttpPost httpPost = new HttpPost(url);
				CloseableHttpClient client = HttpClients.createDefault();
				String respContent = null;
				HttpResponse resp = client.execute(httpPost);
				if (resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity he = resp.getEntity();
					respContent = EntityUtils.toString(he, "UTF-8");
					JSONObject jsonObj = JSONObject.fromObject(respContent);

					log.info("获取游戏地址" + jsonObj.toString());
					JSONObject jsonObjs = JSONObject.fromObject(jsonObj.get("body"));
					if (null == jsonObjs) {
						log.info("获取游戏路径失败！");
						return null;
					} else {
						String slotKey = jsonObjs.getString("slotKey");
						log.info("slotKey：" + slotKey);
						// https://fishinggame.onlinegame.vip/fishweb/gamestart.php?slotKey=24049b0a8b19fba72d88d93e70205a40&language=zh_CN&gameCode=FISH&isfun=0
						result = DT_FISH_GAME_URL + "/fishweb/gamestart.php?slotKey=" + slotKey
								+ "&language=zh_CN&gameCode=FISH&isfun=0";
					}
				}
				return result;
			}
			log.info("获取中心钱包ticket失败！");
			return null;

		} catch (Exception e) {
			log.error("获取游戏连接失败！");
			e.printStackTrace();
			return null;
		}
	}

	// 老虎机反水比例
	public static double getSlotRate(Users user) {
		double newrate = 0.005;
		if (user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()) {
			newrate = 0.006;
		} else if (user.getLevel() == VipLevel.TIANWANG.getCode().intValue()) {
			newrate = 0.007;
		} else if (user.getLevel() == VipLevel.XINGJUN.getCode().intValue()) {
			newrate = 0.008;
		} else if (user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()) {
			newrate = 0.009;
		} else if (user.getLevel() == VipLevel.XIANJUN.getCode().intValue()) {
			newrate = 0.010;
		} else if (user.getLevel() == VipLevel.DIJUN.getCode().intValue()) {
			newrate = 0.011;
		} else if (user.getLevel() == VipLevel.TIANZUN.getCode().intValue()) {
			newrate = 0.012;
		} else if (user.getLevel() == VipLevel.TIANDI.getCode().intValue()) {
			newrate = 0.015;
		}
		return newrate;
	}

	// 真人反水比例
	public static double getLiveRate(Users user) {
		double newrate = 0.005;
		if (user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()) {
			newrate = 0.006;
		} else if (user.getLevel() == VipLevel.TIANWANG.getCode().intValue()) {
			newrate = 0.007;
		} else if (user.getLevel() == VipLevel.XINGJUN.getCode().intValue()) {
			newrate = 0.008;
		} else if (user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()) {
			newrate = 0.009;
		} else if (user.getLevel() == VipLevel.XIANJUN.getCode().intValue()) {
			newrate = 0.010;
		} else if (user.getLevel() == VipLevel.DIJUN.getCode().intValue()) {
			newrate = 0.011;
		} else if (user.getLevel() == VipLevel.TIANZUN.getCode().intValue()) {
			newrate = 0.012;
		} else if (user.getLevel() == VipLevel.TIANDI.getCode().intValue()) {
			newrate = 0.015;
		}
		return newrate;
	}

	/**
	 * 获取老虎机洗码率
	 */
	public static double getPTOtherRate(Users user) {

		return 0.004;
	}

	/**
	 * 获取体育洗码率
	 */
	public static double getSportRate(Users user) {

		return 0.004;
	}

/*	*//**
	 * 获取老虎机账户余额，如果用户不存在则创建用户，成功后返回0
	 * 
	 * @param loginName
	 * @return
	 *//*
	public static Double getBalance(String loginName, String gameCode) {
		String result = null;
		ObjectMapper JSON = new ObjectMapper();
		try {

			if (gameCode.equals("mg")) {
				Double mgBalance = MGSUtil.getBalance(loginname);
			}
			GameVo gameVo = publicBalance(loginName, ACCOUNTTYPE);

			if (StringUtil.equals(gameVo.getCode(), "10000")) {

				JSONObject object = JSONObject.fromObject(gameVo.getData());

				return object.getDouble("slot");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}*/

	public static void main(String[] args) {

		// System.out.println(mobileGameLogin("test1", "holidayseasonmobile"));
		//
		//System.out.println(getBalance("devtest999", "mg"));
		// System.out.println(transferToSlot("pttest01", 2000.0));
		/*
		 * Users user =new Users(); user.setLoginname("pttest01");
		 * System.out.println(flashGameLogin(user,"sw_gol","127.0.0.1","real",
		 * "127.0.0.1"));
		 */
		// System.out.println(tranferFromSlot("qytest123", 200.5));

		// System.out.println(getPngLoadingTicket("devtest999",
		// "prosperitypalace"));

	}

}
