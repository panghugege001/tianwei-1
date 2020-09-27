package dfh.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import dfh.model.SBAData;
import dfh.model.enums.shaba.OddsType;
import dfh.model.enums.shaba.ShaBaReturnEnum;
import dfh.model.shaba.FundTranfer;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class ShaBaUtils {
	
	private static Logger log = Logger.getLogger(ShaBaUtils.class);
	private static final String PREFIX = "DY8";
	//private static final String OPERATORID = "DY";
	private static final Integer CURRENCY = 13;//货币类型，测试环境只能用20（美元），正式环境改为13（人民币）
	private static final BigDecimal MAXTRANSFER = new BigDecimal(200000.00);//最大转入额度
	private static final BigDecimal MINTRANSFER = new BigDecimal(1.00);//最小转入额度
	private static final String MD5KEY = "SWKMX76CHD5SJK4328";
	private static final String URL = "http://api.prod.ib.gsoft88.net";

	/**
	 * 发送post请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendPost(String url) {

		HttpClient httpClient = HttpUtils.createHttpClient();
		GetMethod med = new GetMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		BufferedReader reader = null;
		try {
			httpClient.executeMethod(med);
			reader = new BufferedReader(new InputStreamReader(med.getResponseBodyAsStream()));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
			   stringBuffer.append(str);  
			}
			String result = stringBuffer.toString();
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (med != null) {
				med.releaseConnection();
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static String hello() {
		String parameters = "OpCode=DYGJCN";
		String result = sendPost(URL+"/api/Hello?"+parameters);
		return result;
	}
	
	private static String getMd5String(String parameters){
		return DigestUtils.md5Hex(MD5KEY+parameters).toUpperCase();
	}
	
	public static String login(String loginname){
		String urlpart = "/api/Login?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname;
		
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			String sessionToken = (String) a.get("sessionToken");
			String message = (String) a.get("message");
			if(error_code==0){
				return sessionToken;
			}else{
				log.info(loginname+"获取sessionToken异常："+message);
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String CreateMember(String loginname){
		return CreateMember(loginname, OddsType.Hong_Kong.getCode(), CURRENCY, "", "", "", "", "");
	}
	
	/**
	 * 创建用户
	 * @param OddsType 赔率
	 * @param Currency 货币
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String CreateMember(String loginname, String oddstype,
			Integer currency, String custominfo1, String custominfo2,
			String custominfo3, String custominfo4, String custominfo5){
		String urlpart = "/api/CreateMember?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&oddstype="+ oddstype
				+ "&currency=" + currency 
				+ "&maxtransfer="+ MAXTRANSFER
				+ "&mintransfer=" + MINTRANSFER;
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			String message = (String) a.get("message");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				return "sucess";
			}else{
				return message;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 更新用户
	 * @param vendor_id
	 * @param loginname
	 * @param firstname
	 * @param lastname
	 * @param maxtransfer
	 * @param mintransfer
	 * @param custominfo1
	 * @param custominfo2
	 * @param custominfo3
	 * @param custominfo4
	 * @param custominfo5
	 * @return
	 */
	public static String UpdateMember(String loginname, String firstname,
			String lastname, BigDecimal maxtransfer, BigDecimal mintransfer) {
		
		String urlpart = "/api/UpdateMember?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&firstname="+ firstname
				+ "&lastname="+ lastname
				+ "&maxtransfer="+ maxtransfer
				+ "&mintransfer=" + mintransfer;
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			String message = (String) a.get("message");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				return "sucess";
			}else{
				return message;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 获取单个玩家余额
	 * @param loginname 玩家账号
	 * @param wallet_id 1:Sportsbook 5:AG 6:GD
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Double CheckUserBalance(String loginname){
		String urlpart = "/api/CheckUserBalance?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&wallet_id=1";
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(error_code == 0){
				Object obj = a.get("Data");
				JSONArray dataList = JSONArray.fromObject(obj);
				Map map = (Map) dataList.get(0);
				Double balance = (Double) map.get("balance");
				return balance;
			}
			if(error_code == 23005){//账号不存在就创建一个
				CreateMember(loginname);
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取玩家下注限制的设定。
	 * @param loginname
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List GetMemberBetSetting(String loginname){
		String urlpart = "/api/GetMemberBetSetting?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&Currency="+ CURRENCY;
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				JSONArray jc = JSONArray.fromObject(a.get("Data").toString());
				return jc;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List LockMember(String loginname){
		String urlpart = "/api/LockMember?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname;
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				JSONArray jc = JSONArray.fromObject(a.get("Data").toString());
				return jc;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static Boolean FundTransfer(String vendor_trans_id,String loginname,Double amount,Integer direction){
		return FundTransfer(vendor_trans_id,loginname, amount, CURRENCY, direction, 1);
	}
	/**
	 * 资金转账
	 * @param loginname
	 * @param vendor_trans_id
	 * @param amount
	 * @param currency
	 * @param direction 资金转移方向  0 转出（Withdraw） 1 转入（Deposit）
	 * @param wallet_id 钱包ID 1:Sportsbook 5:AG 6:GD
	 * @return
	 */
	public static Boolean FundTransfer(String OpTransId,String loginname,Double amount,Integer currency,Integer direction,Integer wallet_id){
		
		String urlpart = "/api/FundTransfer?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&OpTransId="+ OpTransId 
				+ "&amount="+ amount 
				+ "&direction=" + direction;
		
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				JSONObject b = JSONObject.fromObject(a.get("Data").toString());
				Integer status = (Integer) b.get("status");
				if(status == 0){
					return Boolean.TRUE;
				}
				if(status == 2){
					FundTranfer ft = CheckFundTransfer(loginname,OpTransId);
					if(ft.getStatus() == 0){
						return Boolean.TRUE;
					}else{
						return Boolean.FALSE;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			FundTranfer ft = CheckFundTransfer(loginname,OpTransId);
			if(ft.getStatus() == 0){
				return Boolean.TRUE;
			}else{
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 检查交易情况
	 * @param loginname 用户名
	 * @param OpTransId 交易ID
	 * @return
	 */
	public static FundTranfer CheckFundTransfer(String loginname,String OpTransId){
		String urlpart = "/api/CheckFundTransfer?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&OpTransId="+ OpTransId;
		
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				JSONObject b = JSONObject.fromObject(a.get("Data").toString());
				String trans_id = b.get("trans_id") + "";
				String transfer_date = (String) b.get("transfer_date");
				BigDecimal amount = new BigDecimal(b.get("amount") + "");
				Integer currency = (Integer) b.get("currency");
				BigDecimal before_amount = new BigDecimal(b.get("before_amount") + "");
				BigDecimal after_amount = new BigDecimal(b.get("after_amount") + "");
				Integer status = (Integer) b.get("status");
				FundTranfer ft = new FundTranfer();
				ft.setTrans_id(trans_id);
				ft.setTransfer_date(transfer_date);
				ft.setAmount(amount);
				ft.setCurrency(currency);
				ft.setBefore_amount(before_amount);
				ft.setAfter_amount(after_amount);
				ft.setStatus(status);
				return ft;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取投注明细（StartTime开始时间和 EndTime结束时间只允许12小时）
	 * @param version_key
	 * @param options
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map GetSportBetLog(Long version_key){
		String urlpart = "/api/GetSportBetLog?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&LastVersionKey="+ version_key 
				+ "&Lang=zhcn";
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		if(result == null){
			return null;
		}
		Map map = new HashMap();
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			List<SBAData> dataList = new ArrayList<SBAData>();
			if(0 == error_code.intValue()){
				Long LastVersionKey = Long.parseLong(a.get("LastVersionKey")+"");
				map.put("LastVersionKey", LastVersionKey);
				
				List list = (JSONArray) a.get("Data");
				if(list == null){
					map.put("BetDetails", dataList);
					return map;
				}
				
				String[] strArr = {"HALF WON","HALF LOSE","WON","LOSE"};
				if(list != null && !list.isEmpty()){
					for (int j = 0; j < list.size(); j++) {
						JSONObject jobj = (JSONObject) list.get(j);
						if(jobj == null || jobj.isNullObject()){
							continue;
						}
						String TicketStatus = jobj.getString("TicketStatus");
						if(TicketStatus == null){
							continue;
						}
						if(Arrays.asList(strArr).contains(TicketStatus.toUpperCase())){
							SBAData vo = new SBAData();
							vo.setTransId(jobj.getString("TransId"));
							vo.setPlayerName(jobj.getString("PlayerName"));
							vo.setMatchDatetime(DateUtil.getStandardFmtTime(jobj.getString("MatchDatetime")));
							vo.setStake(jobj.getString("Stake"));
							vo.setTransactionTime(DateUtil.getStandardFmtTime(jobj.getString("TransactionTime")));
							vo.setTicketStatus(TicketStatus);
							vo.setWinLoseAmount(jobj.getString("WinLoseAmount"));
							vo.setAfterAmount(jobj.getString("AfterAmount"));
							vo.setWinLostDateTime(DateUtil.getStandardFmtTime(jobj.getString("WinLostDateTime")));
							dataList.add(vo);
						}
					}
				}
			}
			map.put("BetDetails", dataList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static void main(String[] args) throws Exception {
		GetSportBetLog(46543733L);
	}
}
