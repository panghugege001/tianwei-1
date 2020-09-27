package dfh.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import dfh.model.enums.shaba.OddsType;
import dfh.model.enums.shaba.ShaBaReturnEnum;
import dfh.model.shaba.FundTranfer;

public class ShaBaUtils {
	
	private static Logger log = Logger.getLogger(ShaBaUtils.class);
	private static final String PREFIX = "k_";
	private static final String OPERATORID = "ld";
	private static final Integer CURRENCY = 13;//货币类型，测试环境只能用20（美元），正式环境改为13（人民币）
	private static final BigDecimal MAXTRANSFER = new BigDecimal(200000.00);//最大转入额度
	private static final BigDecimal MINTRANSFER = new BigDecimal(1.00);//最小转入额度
	private static final String VENDOR_ID = "zXlqf3OXYX8";
	private static final String URL = "http://api.sbtyapitransit.com/api/";
	//private static final String URL = "http://tsa.ws965.com/api/";//测试环境
	public static String TRANSITURL = "http://sbtyapitransit.com/shaba/sendPost.php";
	//public static String TRANSITURL = "http://127.0.0.1:6080/shaba/sendPost.php";//测试环境
	public static String APIKEY = "2!@%!sdfJaaShj56SV@AWEx67a";

	/**
	 * 发送post请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendPost(String method, String parameters) {

		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod med = new PostMethod(TRANSITURL);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		med.setParameter("url", URL+method);
		med.setParameter("parameters", parameters);
		String signatureKey = DigestUtils.md5Hex(URL+method+ APIKEY);
		med.setParameter("signature", signatureKey);
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
			log.info("请求的url:" + URL+method);
			log.info("请求参数:" + parameters);
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
		return "调用中转异常！";
	}
	
	public static String CreateMember(String loginname){
		return CreateMember(loginname, OddsType.Hong_Kong.getCode(), CURRENCY, "", "", "", "", "");
	}
	
	/**
	 * 创建用户
	 * @param Vendor_Member_ID
	 * @param OperatorId
	 * @param FirstName
	 * @param LastName
	 * @param UserName
	 * @param OddsType 赔率
	 * @param Currency 货币
	 * @param custominfo1
	 * @param custominfo2
	 * @param custominfo3
	 * @param custominfo4
	 * @param custominfo5
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String CreateMember(String loginname, String oddstype,
			Integer currency, String custominfo1, String custominfo2,
			String custominfo3, String custominfo4, String custominfo5){
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_member_id="+ PREFIX + loginname 
				+ "&operatorid="
				+ "&firstname="
				+ "&lastname=" 
				+ "&username="+ PREFIX + loginname 
				+ "&oddstype="+ oddstype
				+ "&currency=" + currency 
				+ "&maxtransfer="+ MAXTRANSFER
				+ "&mintransfer=" + MINTRANSFER 
				+ "&custominfo1="+ OPERATORID
				+ "&custominfo2="+ custominfo2 
				+ "&custominfo3="+ custominfo3 
				+ "&custominfo4="+ custominfo4 
				+ "&custominfo5="+ custominfo5;
		String result = sendPost("CreateMember", parameters);
		try {
			JSONObject a = JSONObject.fromObject(result);
			//log.info("沙巴创建玩家账号：" + loginname + ":" + a);
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
			String lastname, BigDecimal maxtransfer, BigDecimal mintransfer,
			String custominfo1, String custominfo2, String custominfo3,
			String custominfo4, String custominfo5) {
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_member_id="+ PREFIX+loginname 
				+ "&firstname="+ firstname 
				+ "&lastname="+ lastname 
				+ "&maxtransfer="+ maxtransfer 
				+ "&mintransfer=" + mintransfer 
				+ "&custominfo1="+ custominfo1 
				+ "&custominfo2="+ custominfo2 
				+ "&custominfo3="+ custominfo3 
				+ "&custominfo4="+ custominfo4 
				+ "&custominfo5="+ custominfo5;
		String result = sendPost("UpdateMember", parameters);
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
	public static BigDecimal CheckUserBalance(String loginname){
		String vendor_member_ids = PREFIX+loginname;
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_member_ids="+ vendor_member_ids
				+ "&wallet_id="+ 1;
		String result = sendPost("CheckUserBalance", parameters);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code1 = (Integer) a.get("error_code");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code1.intValue()){
				Object obj = a.get("Data");
				JSONArray dataList = JSONArray.fromObject(obj);
				Map map = (Map) dataList.get(0);
				Integer error_code2 = (Integer) map.get("error_code");
				if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code2.intValue()){
					BigDecimal balance = new BigDecimal(map.get("balance").toString());
					return balance == null ? new BigDecimal(0.0) : balance;
				}else if(ShaBaReturnEnum.ERROR_CODE2.getCode().intValue() == error_code2.intValue()){
					//账号不存在,则创建一个
					CreateMember(loginname);
					return new BigDecimal(0.0);
				}else{
					return new BigDecimal(0.0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 获取多个玩家余额
	 * @param loginnames 多个用户名用','隔开
	 * @param wallet_id 1:Sportsbook 5:AG 6:GD
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List CheckUsersBalance(String loginnames){
		StringBuffer ids = new StringBuffer();
		if(loginnames.contains(",")){
			String[] arr = loginnames.split(",");
			for (int i = 0; i < arr.length; i++) {
				ids.append(PREFIX+arr[i]+",");
			}
		}else{
			ids.append(PREFIX+loginnames+",");
		}
		String vendor_member_ids = ids.toString().substring(0,ids.toString().length()-1); 
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_member_ids="+ vendor_member_ids
				+ "&wallet_id="+ 1;
		String result = sendPost("CheckUserBalance", parameters);
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
	/**
	 * 设置玩家下注限制
	 * @param loginname
	 * @return
	 */
	public static String SetMemberBetSetting(String loginname){
		String bet_setting = JsonUtil.readFile("/json/BetSet.json");
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_member_id="+ PREFIX+loginname 
				+ "&bet_setting="+ bet_setting;
		String result = sendPost("SetMemberBetSetting", parameters);
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
	 * 获取玩家下注限制的设定。
	 * @param loginname
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List GetMemberBetSetting(String loginname){
		String parameters = "vendor_id=" + VENDOR_ID + 
				"&vendor_member_id=" + PREFIX + loginname;
		String result = sendPost("GetMemberBetSetting", parameters);
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
	public static Boolean FundTransfer(String vendor_trans_id,String loginname,Double amount,Integer currency,Integer direction,Integer wallet_id){
		if(vendor_trans_id == null){
			return Boolean.FALSE;
		}
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_member_id="+ PREFIX+loginname
				+ "&vendor_trans_id="+ vendor_trans_id 
				+ "&amount="+ amount 
				+ "&currency="+ currency 
				+ "&direction=" + direction 
				+ "&wallet_id=" + wallet_id;
		String result = sendPost("FundTransfer", parameters);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(ShaBaReturnEnum.ERROR_CODE0.getCode().intValue() == error_code.intValue()){
				JSONObject b = JSONObject.fromObject(a.get("Data").toString());
				Integer status = (Integer) b.get("status");
				if(status == 0){
					return Boolean.TRUE;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 检查交易情况
	 * @param vendor_trans_id 交易ID
	 * @param wallet_id 钱包ID
	 * @return
	 */
	public static FundTranfer CheckFundTransfer(String vendor_trans_id,Integer wallet_id){
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&vendor_trans_id="+ vendor_trans_id 
				+ "&wallet_id=" + wallet_id;
		String result = sendPost("CheckFundTransfer", parameters);
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
	 * 获取投注明细
	 * @param version_key
	 * @param options
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map GetBetDetail(Long version_key,String options){
		String parameters = "vendor_id="+ VENDOR_ID 
				+ "&version_key="+ version_key 
				+ "&options=" + options;
		String result = sendPost("GetBetDetail", parameters);
		Map map = new HashMap();
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(0 == error_code.intValue()){
				Object obj = a.get("Data");
				a = JSONObject.fromObject(obj);
				Long last_version_key = Long.parseLong(a.get("last_version_key")+"");
				map.put("last_version_key", last_version_key);
				List list = JSONArray.fromObject(a.get("BetDetails"));
				map.put("BetDetails", list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 锁用户
	 * @param loginname
	 * @return
	 */
	public static String LockMember(String loginname){
		String parameters = "vendor_id=" + VENDOR_ID + 
				"&vendor_member_id=" + PREFIX + loginname;
		String result = sendPost("LockMember", parameters);
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
		}
		return null;
	}
	/**
	 * 解锁用户
	 * @param loginname
	 * @return
	 */
	public static String UnlockMember(String loginname){
		String parameters = "vendor_id=" + VENDOR_ID + 
				"&vendor_member_id=" + PREFIX + loginname;
		String result = sendPost("UnlockMember", parameters);
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
		}
		return null;
	}
	
}
