package dfh.utils;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import dfh.model.enums.EBetCode;
import dfh.remote.bean.EBetResp;

/**
 * ebet接口相关
 * 2015/03/18
 */
public class EBetUtil {

	private static Logger log = Logger.getLogger(EBetUtil.class);
	
	private static final String baseUrl = "http://ngameapi-e68.gameapi.me/";
	private static final String addUserUrl = "/ebet/api.aspx?method=addUser";
	private static final String loginUrl = "/ebet/api.aspx?method=doLogin";
	private static final String getBalanceUrl = "/ebet/api.aspx?method=getBalance";
	private static final String transferUrl = "/ebet/api.aspx?method=doTransfer";
	private static final String transferConfirmUrl = "/ebet/api.aspx?method=doTransferConfirm";
	private static final String getBetRecordUrl = "/ebet/api.aspx?method=doGetBetRecord";
	
	static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding"; 
	private static final String hostLang = "zh_CN";
	private static final String agent = "e68";
	private static final String key = "tZXGUxL6";	
	
	
	/**
	 * 用户注册
	 * @param loginname
	 * @param userType
	 * @return
	 */
    public static Integer addUser(String loginname, String userType){
    	log.info("ebet注册：" + loginname);
    	HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(baseUrl + addUserUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("loginname", loginname),
			    new NameValuePair("key", getkey(loginname + userType + agent)),
			    new NameValuePair("userType", userType),
			    new NameValuePair("agent", agent)
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/apiResponse/result");
			return Integer.valueOf(node.valueOf("@code"));
		} catch (HttpException e) {
			log.error("ebet注册用户: HttpException");
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error("ebet注册用户: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("ebet注册用户: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("ebet注册用户: Exception");
			log.error(e.getMessage());
		}
    	return null;
    }
    
    /**
     * 登入ebet
     * @param loginname   玩家账号
     * @param platfrom    游戏平台
     * @param catalog     游戏
     * @param hostUrl     退出游戏时跳转的地址
     * @param type        1：玩家登录   2：登录返回账号不存在时，注册后系统自动发起再次登录
     * @return
     */
    public static EBetResp login(String loginname, String platform, String catalog, String hostUrl, String type){
    	log.info("ebet登入：" + loginname);
    	HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(baseUrl + loginUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("loginname", loginname),
			    new NameValuePair("key", getkey(loginname + hostUrl + platform + catalog + agent)),
			    new NameValuePair("hostUrl", hostUrl),
			    new NameValuePair("hostLang", hostLang),
			    new NameValuePair("platform", platform),
			    new NameValuePair("catalog", catalog),
			    new NameValuePair("agent", agent)		
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/apiResponse/result");
			Integer code = Integer.valueOf(node.valueOf("@code"));
			if(EBetCode.success.getCode().equals(code)){
				return new EBetResp(code, node.selectSingleNode("gameUrl").getText());
			}else if(code.equals(EBetCode.noAccount.getCode()) && type.equals("1")){
				//账号不存在
				if(addUser(loginname, "NORMAL").equals(EBetCode.success.getCode())){
					return login(loginname, platform, catalog, hostUrl, "2");
				}
			}else{
				return new EBetResp(code, EBetCode.getText(code));
			}
		} catch (HttpException e) {
			log.error("ebet登录失败: HttpException");
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error("ebet登录失败: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("ebet登录失败: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("ebet登录失败");
			log.error(e.getMessage());
		}
    	return null;
    }
    
    /**
     * 获取玩家余额
     * @param loginname
     * @param platform
     * @return
     */
    public static Double getBalance(String loginname, String platform){
    	log.info("ebet查询余额：" + loginname);
    	HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(baseUrl + getBalanceUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("loginname", loginname),
			    new NameValuePair("key", getkey(loginname + agent + platform)),
			    new NameValuePair("platform", platform),
			    new NameValuePair("agent", agent)		
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/apiResponse/result");
			Integer code = Integer.valueOf(node.valueOf("@code"));
			if(EBetCode.success.getCode().equals(code)){
				return  Double.valueOf(node.selectSingleNode("balance").getText());
			}else{
				log.error(loginname + "查询ebet余额" + EBetCode.getText(code));
				return null;
			}
		} catch (Exception e) {
			log.error("ebet查询余额异常");
			log.error(e.getMessage());
		}
    	return null;
    }
    
    /**
     * 转账
     * @param loginname
     * @param transferID  Unique transaction id.
     * @param credit   Member remit. Must be an integer.
     * @param platform     
     * @param catalog  
     * @param type  IN or OUT
     * @return
     */
    public static EBetResp transfer(String loginname, String transferID, Integer credit, String platform, String catalog, String type){
    	log.info("ebet转账：" + loginname + "   " + type + "    " + credit);
    	HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(baseUrl + transferUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("loginname", loginname),
			    new NameValuePair("key", getkey(loginname + transferID + credit + platform + catalog + type + agent)),
			    new NameValuePair("transferId", transferID),
			    new NameValuePair("credit", String.valueOf(credit)),
			    new NameValuePair("platform", platform),
			    new NameValuePair("catalog", catalog), 
			    new NameValuePair("type", type),
			    new NameValuePair("agent", agent)	
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/apiResponse/result");
			Integer code = Integer.valueOf(node.valueOf("@code"));
			
			return new EBetResp(code, EBetCode.getText(code));
		} catch (Exception e) {
			log.error("ebet转账异常");
			log.error(e.getMessage());
		}
    	return null;
    }
    
    /**
     * 转账确认
     * @param loginname
     * @param transferID
     * @return
     */
    public static EBetResp transferConfirm(String loginname, String transferID){
    	log.info("ebet转账确认：" + loginname + "   订单ID" + transferID);
    	HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(baseUrl + transferConfirmUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		try {
			// Request parameters and other properties.
			NameValuePair[] data = {
			    new NameValuePair("loginname", loginname),
			    new NameValuePair("key", getkey(loginname + transferID + agent)),
			    new NameValuePair("transferId", transferID),
			    new NameValuePair("agent", agent)	
			};
			method.setRequestBody(data);
			//Execute and get the response.
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/apiResponse/result");
			Integer code = Integer.valueOf(node.valueOf("@code"));
			return new EBetResp(code, EBetCode.getText(code));
		} catch (Exception e) {
			log.error("ebet转账确认异常");
			log.error(e.getMessage());
		}
    	return null;
    }
    
    /**
     * 计算签名，并加密
     * @param str
     * @return
     * @throws Exception 
     */
    private static String getkey(String str) throws Exception{
    	return aesEncrypt(key + DigestUtils.md5Hex(str));
    }
    
    public static String aesEncrypt(String text) throws Exception{
    	Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
    	byte[] keyBytes= new byte[16];
    	byte[] b= key.getBytes("UTF-8");
    	int len= b.length;
    	if (len > keyBytes.length) len = keyBytes.length;
    	System.arraycopy(b, 0, keyBytes, 0, len);
    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    	IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    	cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
    	byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
    	Base64 encoder = new Base64();
    	return new String(encoder.encode(results),"UTF-8");
    }
    
    public static void test(){
    	try {
    		String xml = "<apiResponse method='login' time='2014-07-03 09:10:23'><result code='10000' msg='success'><gameUrl>http://www.google.com</gameUrl></result></apiResponse>";
    		Document document = DocumentHelper.parseText(xml);
    		Node node = document.selectSingleNode( "/apiResponse/result");
    		System.out.println(node.valueOf("@code"));
    		System.out.println(node.selectSingleNode("gameUrl").getText());
            //Element node1 = document.getRootElement();
            //readNode(node1);
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*private static void readNode(Element node){
    	//List<Node> nodes = document.selectNodes("/class/student" );
    	System.out.println("当前节点名称：" + node.getName());//当前节点名称
		System.out.println("当前节点的内容：" + node.getTextTrim());//当前节点名称
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			System.out.println("属性名称："+name+"属性值："+value);
		}
		
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			readNode(e);//递归
		}
    }*/
    
    public static void main(String[] args) throws DocumentException {
    	//EBetResp ebet = EBetUtil.login("woodytest", "EBET", "LIVE", "www.example.com", "1");
    	//System.out.println(ebet.getCode() + ":" + ebet.getMsg());
    	//System.out.println(EBetUtil.getBalance("test", "EBET"));
    	EBetResp ebet = EBetUtil.transfer("woodytest", "200000001044511", 100, "EBET", "LIVE", "IN");
    	//System.out.println(ebet.getCode() + ":" + ebet.getMsg());
    	System.out.println(transferConfirm("woodytest", "200000001044511"));
	}
}
