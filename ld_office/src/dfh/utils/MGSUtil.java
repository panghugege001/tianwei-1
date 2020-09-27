package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.joda.time.DateTime;

import com.alibaba.fastjson.JSONArray;

import dfh.model.bean.Bean4Xima;
import dfh.model.notdb.MGPlaycheckVo;
import dfh.model.notdb.MGTransferLog;
import dfh.service.interfaces.MGSService;
import net.sf.json.JSONObject;

public class MGSUtil {

	private static Logger log = Logger.getLogger(MGSUtil.class);
	
	private static final String MGURL = "https://ag.adminserv88.com";
	private static final String MGMEMBERURL = MGURL+"/member-api-web/member-api";
	private static final String j_username = "dydev";
	private static final String j_password = "QAZqazQAZ";
	private static final Integer ID = 119897644;
	private static final String MA = "ma";
	private static final String PREFIX = "DY8";
	private static final String CURRENCY = "CNY";
	private static final String LANGUAGE = "zh";
	
	public static String HttpSend(String path, String obj, String token,String type) {
		try {
			// 创建连接
			URL url = new URL(path);
			HttpURLConnection connection;
			StringBuffer sbuffer = null;
			// 添加 请求内容
			connection = (HttpURLConnection) url.openConnection();
			// 设置http连接属性
			connection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
			connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
			if("PUT".equals(type)){
				connection.setRequestMethod("PUT"); // 可以根据需要 提交// GET、POST、DELETE、PUT等http提供的功能
				connection.setRequestProperty("Accept-Charset", "utf-8"); // 设置编码语言
				connection.setRequestProperty("X-Requested-With","X-Api-Client");
				connection.setRequestProperty("X-Api-Call", "X-Api-Client");
				connection.setRequestProperty("X-Api-Auth", token);  //设置请求的token
				connection.setRequestProperty("Content-Type", " application/json");// 设定// 请求格式// json，也可以设定xml格式的
				connection.setRequestProperty("Content-Length", obj.getBytes().length + ""); // 设置文件请求的长度
			}
			if("POST".equals(type)){
				connection.setRequestMethod(type);
				connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8"); 
				connection.setRequestProperty("Content-Length", obj.getBytes().length + ""); // 设置文件请求的长度
			}
			if("GET".equals(type)){
				connection.setRequestMethod(type);
				connection.setRequestProperty("X-Requested-With", "X-Api-Client");
				connection.setRequestProperty("X-Api-Call", "X-Api-Client");
				connection.setRequestProperty("X-Api-Auth", token);
				//connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8"); 
			}
			connection.setRequestProperty("Connection", "keep-alive"); // 设置连接的状态

			connection.setReadTimeout(10000);// 设置读取超时时间
			connection.setConnectTimeout(10000);// 设置连接超时时间
			connection.connect();
			if(!"GET".equals(type)){
				OutputStream out = connection.getOutputStream();// 向对象输出流写出数据，这些数据将存到内存缓冲区中
				out.write(obj.getBytes()); // out.write(new String("测试数据").getBytes());
				//刷新对象输出流，将任何字节都写入潜在的流中
				out.flush();
				// 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
				out.close();
			}
			// 读取响应
			if (connection.getResponseCode() == 200) {
				// 从服务器获得一个输入流
				InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());// 调用HttpURLConnection连接对象的getInputStream()函数,
				// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
				BufferedReader reader = new BufferedReader(inputStream);

				String lines;
				sbuffer = new StringBuffer("");

				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sbuffer.append(lines);
				}
				reader.close();
			} else {
				log.info("请求失败,响应代码：" + connection.getResponseCode());
				log.info("请求失败,错误内容："+connection.getResponseMessage());
				return null;
			}
			// 断开连接
			connection.disconnect();
			return sbuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送post请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendPost(String url, Map<String, String> params) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("X-Requested-With", "X-Api-Client"); 
		method.setRequestHeader("X-Api-Call", "X-Api-Client");
		method.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		method.setRequestHeader("Connection", "close");
		method.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		
		NameValuePair[] data = new NameValuePair[params.size()];

		int i = 0;

		for (String pk : params.keySet()) {

			data[i] = new NameValuePair(pk, params.get(pk));
			i++;
		}
		method.setRequestBody(data);
		try {
			httpClient.executeMethod(method);

			int responseCode = method.getStatusCode();
			log.info("请求的url:" + url);
			//log.info("请求参数:" + params.toString());
			log.info("响应代码:" + responseCode);
			return method.getResponseBodyAsString();  
		} catch (Exception e) {
			log.error("请求第三方异常:"+e.getMessage());
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return "请求第三方异常";
	}
	
	
	//后台账号登录
	public static String getToken(){
		String url = MGURL+"/lps/j_spring_security_check";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("j_username", j_username);
		params.put("j_password", j_password);
		
		String result = sendPost(url, params);
		
		JSONObject json = JSONObject.fromObject(result);
		String token = json.getString("token");
		return token;
	}
	
	public static String createPlayerName(String loginname,String password){
		String url = MGURL+"/lps/secure/network/"+ID+"/downline";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("crId", ID);
		params.put("crType", MA);
		params.put("neId", ID);
		params.put("neType", MA);
		params.put("tarType", "m");
		params.put("username", PREFIX+loginname);
		params.put("name", PREFIX+loginname);
		params.put("password", password);
		params.put("confirmPassword", password);
		params.put("currency", CURRENCY);
		params.put("language", LANGUAGE);
		params.put("email", "");
		params.put("mobile", "");
		Map<String,Boolean> m1 = new HashMap<String,Boolean>();
		m1.put("enable", true);
		params.put("casino", m1);
		Map<String,Boolean> m2 = new HashMap<String,Boolean>();
		m2.put("enable", false);
		params.put("poker", m2);
		
		JSONObject jsonObject = JSONObject.fromObject(params);
		
		String result = HttpSend(url, jsonObject.toString(), getToken(),"PUT");
		return result;
	}
	
	//玩家登录
	public static String Login(String loginname,String password){
		String xml = "<mbrapi-login-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"username=\""+PREFIX+loginname+"\" "+
						"password=\""+password+"\" "+
						"ipaddress=\"127.0.0.1\" "+
						"partnerId=\"88KIOSK\" "+
						"currencyCode=\""+CURRENCY+"\" "+
					  "/>";
		
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String status = parseXml(result, "/mbrapi-login-resp", "status");
		if("2003".equals(status)){//账号不存在则创建一个
			createPlayerName(loginname, password);
			return Login(loginname, password);
		}
		if("0".equals(status)){
			String token = parseXml(result, "/mbrapi-login-resp", "token");
			return token;
		}
		return null;
	}
	
	//修改玩家密码
	public static Boolean changepassword(String loginname,String oldpassword,String newpassword){
		String token = Login(loginname, oldpassword);
		
		String xml = "<mbrapi-changepassword-call "+
				"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
				"apiusername=\"apiadmin\" "+
				"apipassword=\"apipassword\" "+
				"token=\""+token+"\" "+
				"oldpassword=\""+oldpassword+"\" "+
				"newpassword=\""+newpassword+"\" "+
			  "/>";
		
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		System.out.println(result);
		String status = parseXml(result, "/mbrapi-changepassword-resp", "status");
		if("0".equals(status)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	//demoMode为true是试玩，为false是真钱
	public static String launchGameUrl(String loginname,String password,String gameid,String demoMode){
		String token = Login(loginname, password);
		
		String xml = "<mbrapi-launchurl-call "+
				"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
				"apiusername=\"apiadmin\" "+
				"apipassword=\"apipassword\" "+
				"token=\""+token+"\" "+
				"language=\""+LANGUAGE+"\" "+
				"gameId=\""+gameid+"\" "+
				"bankingUrl=\"www.banking.com\" "+
				"lobbyUrl=\"www.lobby.com\" "+
				"logoutRedirectUrl=\"www.logout.com\" "+
				"demoMode=\""+demoMode+"\" "+
			  "/>";

		System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String gameurl = parseXml(result, "/mbrapi-launchurl-resp", "launchUrl");
		return gameurl;
	}
	
	public static Double getMGBalance(String loginname,String password){
		String token = Login(loginname, password);
		if(token == null){
			return null;
		}
		String xml = "<mbrapi-account-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"token=\""+token+"\" "+
					  "/>";

		//System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String balanceStr = parseXml(result,"/mbrapi-account-resp/wallets/account-wallet","credit-balance");
		if(balanceStr != null){
			return Double.parseDouble(balanceStr);
		}
		return null;
	}
	
	
	public static String transferToMG(String loginname,String password,Double amount,String tx_id){
		String token = Login(loginname, password);
		
		String xml = "<mbrapi-changecredit-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"token=\""+token+"\" "+
						"product=\"casino\" "+
						"operation=\"topup\" "+
						"amount=\""+amount+"\" "+
						"tx-id=\""+tx_id+"\" "+
					  "/>";

		//System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String status = parseXml(result,"/mbrapi-changecredit-resp","status");
		if("0".equals(status)){
			return null;
		}
		return "fail";
	}
	
	public static String tranferFromMG(String loginname,String password,Double amount,String tx_id){
		String token = Login(loginname, password);
		
		String xml = "<mbrapi-changecredit-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"token=\""+token+"\" "+
						"product=\"casino\" "+
						"operation=\"withdraw\" "+
						"amount=\""+amount+"\" "+
						"tx-id=\""+tx_id+"\" "+
					  "/>";

		//System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String status = parseXml(result,"/mbrapi-changecredit-resp","status");
		if("0".equals(status)){
			return null;
		}
		return "fail";
	}
	
	public static String parseXml(String xml,String path,String value) {
		xml = StringUtils.trimToEmpty(xml);
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode(path);
			String balance = element.attributeValue(value);
			return balance;
		} else {
			log.info("document parse failed:" + xml);
		}
		return null;
	}
	
	public static List getbets(String startTime,String endTime){
		String url = MGURL+"/lps/secure/agenttx/"+ID+"?start="+startTime+"&end="+endTime+"&timezone=Asia/Shanghai";
		//String url = MGURL+"/lps/secure/agenttx/"+ID+"?start="+startTime+"&end="+endTime+"&timezone=UTC";
		String result = HttpSend(url, null, getToken(),"GET");
		List list = JSONArray.parseArray(result);
		return list;
	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * 查询MG转账记录
	 */
	public static List<MGTransferLog> getTransfersByLoginName(MGSService mgsService, String loginname, String startTime, String endTime) throws SQLException{
		String sql = "select loginname, type, amount, transferTime from transferlog where loginname = :loginname and transferTime >= :startTime and transferTime <= :endTime order by transferTime desc";		
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
        
		List transferList = mgsService.getListBySql(sql, params);
		List<MGTransferLog> list = new ArrayList<MGTransferLog>();
		for (Object obj : transferList) {
			Object[] item = (Object[]) obj;
			MGTransferLog log = new MGTransferLog();
        	log.setLoginName(item[0].toString());
    		log.setType(item[1].toString());
    		log.setAmount(Integer.parseInt(item[2].toString()));
    		log.setTransferTime((Date)item[3]);
    		list.add(log);
		}
        return list;
	}
	
	/**
	 * 查询MG输赢信息
	 */
	public static List<MGPlaycheckVo> mgPlayChck(MGSService mgsService, String loginname, String startTime, String endTime){
		StringBuilder sql = new StringBuilder("select loginname, SUM(amount) as amount, actiontype from mglog where actiontime>=:startTime and actiontime<:endTime");
		if(StringUtils.isNotBlank(loginname)){
			sql.append(" and loginname=:loginname");
		}
		sql.append(" GROUP BY loginname, actiontype ORDER BY loginname");
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		List playList = mgsService.getListBySql(sql.toString(), params);
        Map<String, MGPlaycheckVo> palyCheckVoMap = new HashMap<String, MGPlaycheckVo>();
        for (Object obj : playList) {
        	Object[] item = (Object[]) obj;
        	MGPlaycheckVo playVo;
        	String userName = item[0].toString();
        	if(palyCheckVoMap.containsKey(userName)){
        		playVo = palyCheckVoMap.get(userName);
        	}else{
        		playVo = new MGPlaycheckVo();
        		playVo.setLoginname(userName);
        		palyCheckVoMap.put(userName, playVo);
        	}
        	
        	String type = item[2].toString();
        	Double amount = Arith.div(Double.parseDouble(item[1].toString()), 100);
        	if(type.equalsIgnoreCase("bet")){
        		playVo.setBet(amount);
        	}else if(type.equalsIgnoreCase("refund")){
        		playVo.setRefund(amount);
        	}else if(type.equalsIgnoreCase("win")){
        		playVo.setWin(amount);
        	}else if(type.equalsIgnoreCase("progressivewin")){
        		playVo.setProgressivewin(amount);
        	}
        	playVo.setNet(Arith.add(playVo.getNet(), amount));
		}
        List<MGPlaycheckVo> list = new ArrayList<MGPlaycheckVo>();
        for (String key: palyCheckVoMap.keySet()) {
        	list.add(palyCheckVoMap.get(key));
		}
        return list;
	}
	
	/**
	 * 查询洗码数据
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public static List<Bean4Xima> getMGXIMAData(MGSService mgsService) throws SQLException{
		String startTime = DateUtil.formatDateForStandard(new DateTime().minusDays(1).withTimeAtStartOfDay().toDate());
		String endTime = DateUtil.formatDateForStandard(new DateTime().withTimeAtStartOfDay().minusSeconds(1).toDate());
		
		String betSql = "select loginname, sum(amount) as betamount from mglog where actiontype = :actiontype and  actiontime >= :startTime and actionTime <= :endTime group by loginname";
		String winSql = "select loginname, sum(amount) as winamount from mglog where (actiontype = :win or actiontype = :progressivewin) and actiontime >= :startTime and actionTime <= :endTime group by loginname ";

		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("actiontype", "bet");
		//params.put("isend", 1);   //投注已结束的记录（因为可能存在投注失败退款的情况）
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		Map<String, Double> betMap = new HashMap<String, Double>();
		List betList = mgsService.getListBySql(betSql, params);
		for (Object obj : betList) {
			Object[] item = (Object[]) obj;
			betMap.put(item[0].toString(), Arith.div(Double.parseDouble(item[1].toString()), 100));
		}
        
		params.put("win", "win");
		params.put("progressivewin", "progressivewin");
		
		Map<String, Double> winMap = new HashMap<String, Double>();
		List winList = mgsService.getListBySql(winSql, params);
		for (Object obj : winList) {
			Object[] item = (Object[]) obj;
			winMap.put(item[0].toString(), Arith.div(Double.parseDouble(item[1].toString()), 100));
		}

        List<Bean4Xima> list = new LinkedList<Bean4Xima>();
        for (String key : betMap.keySet()) {
        	Bean4Xima item = new Bean4Xima(key, -betMap.get(key), Arith.sub(-betMap.get(key), winMap.get(key)==null?0.0:winMap.get(key)));
        	list.add(item);
		}
        return list;
	}
	
	public static void main(String[] args) throws Exception {
		String startTime = "2018:06:02:23:00:00";
		String endTime = "2018:06:02:23:59:59";
		System.out.println(getbets(startTime, endTime));
	}
	
}
