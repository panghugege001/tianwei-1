package dfh.utils;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import dfh.action.vo.DtPotVO;
import dfh.action.vo.DtVO;
import dfh.security.EncryptionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * Dt老虎机
 * 
 * @author
 * 
 */
public class DtUtil {

	private static Logger log = Logger.getLogger(DtUtil.class);
	public static String BUSINESS = "NNTI_FHC_TWYL";
	public static String URL = "http://api-dtservice.com/dtApi.html";
	public static String APIKEY = "OZusgRNOgnxXmsOkojXxbw==";

	public enum ErrorCode {
		C00000("00000", "操作成功!"), C00001("00001", "商户为空!"), C00002("00002", "商户错误!"), C00003("00003", "商户签名为空!"), C00004("00004", "商户签名错误!"), C00005("00005", "商户请求方法为空!"), C00006("00006",
				"商户请求方法不存在!"), C00007("00007", "商户被冻结!"), C00008("00008", "商户IP被拒!"), C00009("00009", "商户玩家账号为空!"), C000010("000010", "商户玩家密码为空!"), C000011("000011", "商户玩家账号已存在!"), C000012("000012",
				"商户玩家账号不存在!"), C000013("000013", "商户玩家账号金额为空!"), C000014("000014", "商户玩家账号金额小于或者等于0!"), C000015("000015", "开始时间为空!"), C000016("000016", "结束时间为空!"), C000017("000017", "输入格式错误!"), C000018(
				"000018", "无投注记录!"), C000019("000019", "额度不足!"), C000020("000020", "玩家密码错误!"), C000021("000021", "商户KEY未设置!"), C000022("000022", "玩家被冻结!"), C000023("000023", "金额格式错误!"), C000024(
				"000024", "操作失败,数据异常!"), C000025("000025", "开始时间格式错误!"), C000026("000026", "结束时间格式错误!"), C000027("000027", "玩家未登陆!"), C000028("000028", "操作失败,系统异常!"), C000029("000029", "时间格式错误!"), C000030(
				"000030", "玩家余额不足!"), C000031("000031", "游戏编码为空!"), C000032("000032", "游戏维护!"), C000033("000033", "游戏不存在!");
		// 成员变量
		private String code;
		private String text;

		// 构造方法
		private ErrorCode(String code, String text) {
			this.code = code;
			this.text = text;
		}

		public String getCode() {
			return code;
		}

		public String getText() {
			return text;
		}

		public static String getText(String code) {
			ErrorCode p[] = values();
			for (int i = 0; i < p.length; i++) {
				ErrorCode type = p[i];
				if (type.getCode().equals(code))
					return type.getText();
			}
			return null;
		}
	}

	// 创建玩家
	public static String createPlayerName(String loginname, String pwd) {
		String playerpassword = EncryptionUtil.encryptPassword(pwd);
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		loginname = (loginname).trim().toUpperCase();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "CREATE");
		method.setParameter("BUSINESS", BUSINESS);
		loginname = (loginname).trim().toUpperCase();
		method.setParameter("PLAYERNAME", loginname);
		method.setParameter("PLAYERPASSWORD", playerpassword);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "CREATE" + loginname + playerpassword + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			log.info("DT创建玩家账号：" + loginname + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					return null;
				} else {
					return a.get("RESPONSECODE").toString();
				}
			} else {
				return a.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return ErrorCode.C000028.getCode();
	}

	// 登录
	public static DtVO login(String loginname, String pwd) {
		String playerpassword = EncryptionUtil.encryptPassword(pwd);
		DtVO dv = new DtVO();
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		loginname = (loginname).toUpperCase();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "LOGIN");
		method.setParameter("BUSINESS", BUSINESS);
		loginname = (loginname).toUpperCase();
		method.setParameter("PLAYERNAME", loginname);
		method.setParameter("PLAYERPASSWORD", playerpassword);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "LOGIN" + loginname + playerpassword + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			log.info("DT登录玩家账号：" + loginname + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					dv.setGameurl(a.get("gameurl").toString());
					dv.setSlotKey(a.get("slotKey").toString());
					return dv;
				} else if(a.get("RESPONSECODE").equals(ErrorCode.C000012.getCode())){
					if(null== createPlayerName(loginname,pwd)){
						return login(loginname,pwd);
					}else{
						return null;
					}
				} else if(a.get("RESPONSECODE").equals(ErrorCode.C000020.getCode())){
					if(null== updateInfo(loginname,pwd)){
						return login(loginname,pwd);
					}else{
						return null;
					}
				}
			} else {
				return 	null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	/**
	 * 转出或者转入金额
	 * 
	 * @param loginname
	 * @param amt
	 * @return
	 */
	public static String withdrawordeposit(String loginname, Double price) {
		String METHOD = null;
		if (price < 0) {
			METHOD = "WITHDRAW";// 转出游戏
		} else if (price > 0) {
			METHOD = "DEPOSIT";// 转入游戏
		}
		price = Math.abs(price);
		String pricestr=price+"";
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", METHOD);
		method.setParameter("BUSINESS", BUSINESS);
		loginname = (loginname).toUpperCase();
		method.setParameter("PLAYERNAME", loginname);
		method.setParameter("PRICE", pricestr);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + METHOD + loginname + pricestr + APIKEY);
		System.out.println("signatureKey=="+signatureKey);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			System.out.println(a.toString());
			log.info("DT" + METHOD + ":" + loginname + ";price:" + price + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					if ("WITHDRAW".equals(METHOD)) {
						String status = a.has("STATUS") == true ? a.get("STATUS")+"" : "0";
						return "success"+status;
					}else if ("DEPOSIT".equals(METHOD)) {
						return "success";
					}
				} else {
					return ErrorCode.getText(a.get("RESPONSECODE").toString());
				}
			} else {
				return a.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return ErrorCode.C000028.getCode();
	}

	/**
	 * 查询余额,如果用户不存在则新增
	 * 
	 * @param loginname
	 * @return
	 */
	public static String getamount(String loginname,String password) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "GETAMOUNT");
		method.setParameter("BUSINESS", BUSINESS);
		loginname = (loginname).toUpperCase();
		method.setParameter("PLAYERNAME", loginname);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "GETAMOUNT" + loginname + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			System.out.println(a.toString());
			log.info("DT查询余额：" + loginname + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					return a.get("AMOUNT").toString();
				}else if(a.get("RESPONSECODE").equals(ErrorCode.C000012.getCode())){
					//如果不存在就创建一个
					createPlayerName(loginname, password);
				}else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	/**
	 * 查询余额
	 * 
	 * @param loginname
	 * @return
	 */
	public static String getamount(String loginname) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "GETAMOUNT");
		method.setParameter("BUSINESS", BUSINESS);
		loginname = (loginname).toUpperCase();
		method.setParameter("PLAYERNAME", loginname);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "GETAMOUNT" + loginname + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			System.out.println(a.toString());
			log.info("DT查询余额：" + loginname + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					return a.get("AMOUNT").toString();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	
	/**
	 * 更新密码
	 * @param loginname
	 * @return
	 */
	public static String updateInfo(String loginname,String pwd) {
		String playerpassword = EncryptionUtil.encryptPassword(pwd);
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		loginname = (loginname).toUpperCase();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "UPDATE");
		method.setParameter("BUSINESS", BUSINESS);
		loginname = (loginname).toUpperCase();
		method.setParameter("PLAYERNAME", loginname);
		method.setParameter("PLAYERPASSWORD", playerpassword);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "UPDATE" + loginname + playerpassword + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			log.info("DT更新玩家密码：" + loginname + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					return null;
				} else {
					return a.get("RESPONSECODE").toString();
				}
			} else {
				return a.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return ErrorCode.C000028.getCode();
	}
	
	/**
	 * description：查询指定时间段内玩家的投注额，该方法查询比较慢，可以精确到秒，但是只能查询最近15天的数据，会定期清理数据
	 * @param：loginname(玩家账号)
	 * @param：starttime(开始时间，格式为yyyy-MM-dd HH:mm:ss)
	 * @param： endtime(结束时间，格式为yyyy-MM-dd HH:mm:ss)
	 */
	@SuppressWarnings("rawtypes")
	public static List getbet(String loginname,String starttime,String endtime) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		String signatureKey=null; 
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "GETBET");
		method.setParameter("BUSINESS", BUSINESS);
		method.setParameter("START_TIME", starttime);
		method.setParameter("END_TIME", endtime);
		if(!StringUtil.isEmpty(loginname)){
			loginname = (loginname).toUpperCase();
			method.setParameter("PLAYERNAME", loginname);
			signatureKey = DigestUtils.md5Hex(BUSINESS + "GETBET" + loginname +starttime+endtime+ APIKEY);
		}else{
			return null;
			//signatureKey = DigestUtils.md5Hex(BUSINESS + "GETBET"  +starttime+endtime+ APIKEY);	
		}
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			JSONObject a = JSONObject.fromObject(result);
			log.info("DT获取投注额：" + loginname + ":" + a);
			if (a.has("RESPONSECODE")) {
				if (!a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					return null;
				} else {
					JSONObject data = JSONObject.fromObject(a.get("DATA").toString());
					JSONArray jc = JSONArray.fromObject(data.get("BETS").toString());
					return jc;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	/**
	 * description：查询指定时间段内玩家的投注额，该方法查询的是报表数据，速度快，只能精确到天，数据永久保留
	 * @param：loginName(玩家账号)
	 * @param：startTime(开始时间，格式为yyyy-MM-dd)
	 * @param： endTime(结束时间，格式为yyyy-MM-dd)
	 */
	@SuppressWarnings("rawtypes")
	public static List getbetByDay(String loginName, String startTime, String endTime) {
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "GETBETBYDAY");
		method.setParameter("BUSINESS", BUSINESS);
		method.setParameter("START_TIME", startTime);
		method.setParameter("END_TIME", endTime);
		
		String signatureKey = null;
		
		if (!StringUtil.isEmpty(loginName)) {
			
			loginName = loginName.toUpperCase();
			method.setParameter("PLAYERNAME", loginName);
			
			signatureKey = DigestUtils.md5Hex(BUSINESS + "GETBETBYDAY" + loginName + startTime + endTime + APIKEY);
		} else {
			
			signatureKey = DigestUtils.md5Hex(BUSINESS + "GETBETBYDAY" + startTime + endTime + APIKEY);
		}
		
		method.setParameter("SIGNATURE", signatureKey);
		
		try {

			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			JSONObject a = JSONObject.fromObject(result);

			log.info("DT获取投注额：" + loginName + "：" + a);

			if (a.has("RESPONSECODE")) {
				
				if (!a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					
					return null;
				} else {
					
					JSONObject data = JSONObject.fromObject(a.get("DATA").toString());
					JSONArray jc = JSONArray.fromObject(data.get("BETS").toString());
					
					return jc;
				}
			} else {
				
				return null;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			if (method != null) {
				
				method.releaseConnection();
			}
		}
		
		return null;
	}

	/**
	 * 查询jackpot 奖池信息
	 *
	 * @param loginname
	 * @return
	 */
	public static DtPotVO getjackpot() {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "GETJACKPOT");
		method.setParameter("BUSINESS", BUSINESS);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "GETJACKPOT" + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		DtPotVO vo = new DtPotVO();
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();

			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			if (a.has("RESPONSECODE")) {
				if (a.get("RESPONSECODE").equals(ErrorCode.C00000.getCode())) {
					JSONObject data = JSONObject.fromObject(a.get("DATA").toString());
					String pot = data.get("JACKPOT0").toString();
					vo.setCode(ErrorCode.C00000.getCode());
					vo.setPot(pot);
					return vo;
				}
			}
			log.error("异常结果:" + result);
			vo.setCode(ErrorCode.C000028.getCode());
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
	

	public static void main(String[] args) {
		String loginname = "twtest01";
		String pwd = "a123a123";
		String starttime = "2018-06-20 08:00:00";
		String endtime = "2018-06-20 20:00:00";
//		System.out.println(updateInfo(loginname, pwd));
		System.out.println(createPlayerName(loginname, pwd));
		System.out.println(login(loginname, pwd));
//		System.out.println(getbet(loginname, starttime,endtime));
	}
	
}