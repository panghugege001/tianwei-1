package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import dfh.remote.DocumentParser;
import dfh.remote.bean.DspResponseBean;
import dfh.security.DESEncrypt;
import dfh.security.EncryptionUtil;

public class APTRYUtils {

	private static String  encrypt_key="njvs90z1";
	private static String  md5encrypt_key="sdf7&^#gfas";
	private static String  cagent="B20_AGIN";
	private static String  dspurl="http://gi.sunrise88.net:81";
	private static String  gamedspurl="http://gci.sunrise88.net:81";

	private static DESEncrypt  des=new DESEncrypt(encrypt_key);
    private static String PREFIX="kt_";

	public static String getActype(String loginname) {
		return "0";
	}

	public static String isCustomerExist(String loginname) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=ice";
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return result;
	}

	public static String CheckOrCreateGameAccount(String loginname, String password) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=lg/\\\\/actype=0/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return result;
	}

	public static String GetBalance(String loginname, String password) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=gb/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClientShort();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String PrepareTransferCredit(String loginname, String billno, String type, Double credit, String password) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/actype=0/\\\\/method=tc/\\\\/billno=" + cagent + billno.substring(1) + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String TransferCreditConfirm(String loginname, String billno, String type, Double credit, String password, Integer flag) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/actype=0/\\\\/method=tcc/\\\\/billno=" + cagent + billno.substring(1) + "/\\\\/flag=" + flag + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String SearchTransferResult(String loginname, String billno) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=str/\\\\/billno=" + cagent + billno.substring(1);
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String UpdateTransferResultStatus(String loginname, String billno, String operator, Integer flag) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=uts/\\\\/billno=" + billno.substring(1) + "/\\\\/flag=" + flag + "/\\\\/operator=" + operator;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String forwardGame(String loginname, String password, String website, Integer sid) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/dm=" + website + "/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + password + "/\\\\/sid=" + sid;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/forwardGame.do?params=" + targetParams + "&key=" + key;
			httpClient = new HttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String agTryLogin(String loginname,String password,String loginid,String tranferid,String gameType,String domain){
		try {
			String result="";
			String params="";
			String url="";
			if(null!=loginname && !"".equals(loginname.trim()) && null!=password && !"".equals(password)){
				DspResponseBean dspResponseBean =DocumentParser.parseDspResponseRequest(APTRYUtils.isCustomerExist(loginname));

				if(null!=dspResponseBean && null!=dspResponseBean.getInfo() && "0".equals(dspResponseBean.getInfo())){
					//检测是否有帐号，如果帐号不存在，则创建DSP帐号,0表示帐号不存在
					DspResponseBean createaccount=DocumentParser.parseDspResponseRequest(APTRYUtils.CheckOrCreateGameAccount(loginname,loginname));
					if(null!=createaccount && null!=createaccount.getInfo() && "0".equals(createaccount.getInfo())){ //表示创建成功
					    //处理字符串,开始登录游戏
						params="cagent="+cagent+"/\\\\/loginname="+loginname+"/\\\\/dm="+domain+"/\\\\/actype=0/\\\\/password="+loginname+"/\\\\/sid="+cagent+loginid + "/\\\\/gameType="+gameType;
						DESEncrypt  des=new DESEncrypt(encrypt_key);
						String targetParams=des.encrypt(params);
						String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
						url=gamedspurl+"/forwardGame.do?params="+targetParams+"&key="+key;

						return url;
					}else{
						result=createaccount.getInfo();
						return "联系客服，错误信息:"+result;
					}
				}else if(null!=dspResponseBean && null!=dspResponseBean.getInfo() && "1".equals(dspResponseBean.getInfo())){//表示已经存在改帐号
						//处理字符串,开始登录游戏
						params="cagent="+cagent+"/\\\\/loginname="+loginname+"/\\\\/dm="+domain+"/\\\\/actype=0/\\\\/password="+loginname+"/\\\\/sid="+cagent+loginid + "/\\\\/gameType="+gameType;;
						DESEncrypt  des=new DESEncrypt(encrypt_key);
						String targetParams=des.encrypt(params);
						String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
						url=gamedspurl+"/forwardGame.do?params="+targetParams+"&key="+key;
						return url;
				}else{
					result=dspResponseBean.getInfo();
					return "联系客服，错误信息:"+result;
				}
			}else{
				return "登录异常!";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "异常!"+e.toString();
		}
	}

	public static void main(String[] args) throws Exception {
	
	}
}
