package dfh.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

/**
 * 新的PT工具类
 * @author jad
 *
 */
public class PtUtil1 {

	private static Logger log = Logger.getLogger(PtUtil1.class);
//	public static String PHP_SERVICE ="https://ams-api.ttms.co:8443/sdk/servlet/com.chartwelltechnology.icd.ClientLoginSupportServlet?";//登录
	public static String PALY_START = "LF123_k";
//	public static String PHP_PLAYER_DEPOSIT ="https://ams-api.ttms.co:8443/sdk/servlet/com.chartwelltechnology.icd.CasinoBalanceSupportServlet?";//获取余额,转入，转出	
	  
	public static String PHP_SERVICE_NEW = "https://ams-api.ttms.co:8443/cip/player/";
	public static String PHP_PLAYER_LOGIN = "https://ams-api.ttms.co:8443/cip/gametoken/";
	public static String PHP_PLAYER_DEPOSIT_NEW = "https://ams-api.ttms.co:8443/cip/transaction/longfa123/";
	
	public static void main(String[] args) {
//		String isLogin = createPlayerName("woodytest");
			System.out.println("开始的余额是：" + getPlayerAccount("woodytest"));
//			System.out.println(addPlayerAccountPraper("woodytest", -80));
//			System.out.println("提交后的余额是：" + getPlayerAccount("woodytest"));
		
	}
	
	
	/**
	 * 获取配置文件
	 * 
	 * @param key
	 * @return
	 */
	public static String getPhpPtService(String key) {
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("init.properties"));
			return properties.getProperty(key);
		} catch (Exception e) {
			log.info(e.toString());
			return null;
		}
	}
	// 登录
		public static String createPlayerName(String loginname) {
			loginname = PALY_START + loginname;
			URL url = null;
			String token = "";
			try {
				url = new URL(PHP_PLAYER_LOGIN + loginname);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			URLConnection con = null;
			try {
				con = url.openConnection();
				con.setDoOutput(true);
				con.setRequestProperty("Content-Type", "application/xml");
				OutputStreamWriter out;
				out = new OutputStreamWriter(con.getOutputStream());
				String xml = "<logindetail><player account=\"CNY\" country=\"CN\" firstName=\""
						+ loginname
						+ "\" lastName=\""
						+ loginname
						+ "\" userName=\""
						+ loginname
						+ "\" nickName=\""
						+ loginname
						+ "\" tester=\"0\" partnerId=\"longfa123\" commonWallet=\"0\" /><partners><partner partnerId=\"zero\" partnerType=\"0\" /><partner partnerId=\"gm\" partnerType=\"1\" /><partner partnerId=\"longfa123\" partnerType=\"1\" /></partners></logindetail>";
				out.write(new String(xml));
				out.flush();
				out.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				StringBuffer sbf = new StringBuffer("");
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine()) {
					sbf.append(line);
				}
				Map<String, String> map = new HashMap<String, String>();
				map = parse_New(sbf.toString());
				token = map.get("token");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return token;

		}

		
		/**
		 * 转出或者转入金额
		 * 
		 * @param loginname
		 * @param count
		 * @return
		 */
		public static Boolean addPlayerAccountPraper(String loginname, double amt) {
			String transferID = createTtgtransferId(amt, loginname);
			if (StringUtil.isEmpty(transferID)) {
				log.info("TTG转账失败：" + loginname + ";" + amt);
				return false;
			}
			URL url = null;
			String retry = "";
			loginname = PALY_START + loginname;
			try {
				url = new URL(PHP_PLAYER_DEPOSIT_NEW + transferID);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			URLConnection con = null;
			try {
				con = url.openConnection();
				con.setDoOutput(true);
				con.setRequestProperty("Content-Type", "application/xml");
				OutputStreamWriter out;
				out = new OutputStreamWriter(con.getOutputStream());
				String xml = "<transactiondetail uid=\"" + loginname
						+ "\" amount=\"" + amt + "\" />";
				System.out.println(xml);
				out.write(new String(xml));
				out.flush();
				out.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				StringBuffer sbf = new StringBuffer("");
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine()) {
					sbf.append(line);
				}
				Map<String, String> map = new HashMap<String, String>();
				map = parse_New(sbf.toString());
				retry = map.get("retry");
				log.info("TTG转账：" + loginname + ";金额：" + amt + ";retry=" + retry+";transferID="+transferID);
				if(!StringUtil.isEmpty(retry)&&retry.equals("0")){
					return true;
				}else{
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		

		/**
		 * 查询余额
		 * 
		 * @param loginname
		 * @return
		 */
		public static String getPlayerAccount(String loginname) {
			String url = PHP_SERVICE_NEW + PALY_START + loginname + "/balance";
			HttpClient httpClient = null;
			GetMethod postMethod = null;
			try {
				httpClient = HttpUtils.createHttpClient();
				postMethod = new GetMethod(url);
				postMethod.setRequestHeader("Connection", "close");
				int statusCode = httpClient.executeMethod(postMethod);
				Map<String, String> map = new HashMap<String, String>();
				if (statusCode == HttpStatus.SC_OK) {
					String phpHtml = postMethod.getResponseBodyAsString();
					map = parse_New(phpHtml);
					if (phpHtml == null || phpHtml.equals("")) {
						return null;
					}
					if (map.isEmpty()) {
						return null;
					}
					if (!StringUtil.isEmpty(map.get("real"))) {
						return map.get("real");
					} else {
						return "0";
					}
				} else {
					log.info("新的TT Response Code: " + statusCode);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("新的TT Response 消息: " + e.toString());
				return null;
			} finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
		}

		/**
		 * 转换xml数据2 Map
		 * 
		 * @param protocolXML
		 * @return
		 */
		public static Map<String, String> parse(String protocolXML) {
			// 创建一个新的字符串
			StringReader read = new StringReader(protocolXML);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			Map<String, String> returnMap = null;
			try {
				// 通过输入源构造一个Document
				Document doc = sb.build(source);
				// 取的根元素
				Element root = doc.getRootElement();
				// 得到根元素所有子元素的集合
				List jiedian = root.getChildren();
				// 获得XML中的命名空间（XML中未定义可不写）
				Namespace ns = root.getNamespace();
				Element et = null;
				returnMap = new HashMap<String, String>();
				for (int i = 0; i < jiedian.size(); i++) {
					et = (Element) jiedian.get(i);// 循环依次得到子元素
					returnMap.put(et.getChild("name", ns).getText(),
							et.getChild("value", ns).getText());
				}
				/*
				 * et = (Element) jiedian.get(0); List zjiedian = et.getChildren();
				 * for(int j=0;j<zjiedian.size();j++){ Element xet = (Element)
				 * zjiedian.get(j); System.out.println("aaaa="+xet.getName()); }
				 */
			} catch (JDOMException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

			return returnMap;
		}

		/**
		 * 转换xml数据2 Map
		 * 
		 * @param protocolXML
		 * @return
		 */
		public static Map<String, String> parse_New(String protocolXML) {
			// 创建一个新的字符串
			StringReader read = new StringReader(protocolXML);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			Map<String, String> returnMap = null;
			try {
				// 通过输入源构造一个Document
				Document doc = sb.build(source);
				// 取的根元素
				Element root = doc.getRootElement();
				// 得到根元素所有子元素的集合
				List jiedian = root.getAttributes();
				// 获得XML中的命名空间（XML中未定义可不写）
				Namespace ns = root.getNamespace();
				Attribute et = null;
				returnMap = new HashMap<String, String>();
				for (int i = 0; i < jiedian.size(); i++) {
					et = (Attribute) jiedian.get(i);// 循环依次得到子元素
					returnMap.put(et.getName(), et.getValue());
				}
				/*
				 * et = (Element) jiedian.get(0); List zjiedian = et.getChildren();
				 * for(int j=0;j<zjiedian.size();j++){ Element xet = (Element)
				 * zjiedian.get(j); System.out.println("aaaa="+xet.getName()); }
				 */
			} catch (JDOMException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

			return returnMap;
		}

		// 保存转账ID
		public static String createTtgtransferId(Double amount, String loginname) {
			UUID uuidR = UUID.randomUUID();
			String uuid=PALY_START+uuidR;
			Connection conn = null;
			Statement stmt = null;
			String datetime = DateUtil.fmtyyyyMMddHHmmss(new Date());
			String sql = "insert into ttgtransfer (id,amount,createtime,loginname) values ('"
					+ uuid.toString()
					+ "',"
					+ amount
					+ ",'"
					+ datetime
					+ "','"
					+ loginname + "')";
			try {
				String driverName = "com.mysql.jdbc.Driver";
				Class.forName(driverName);
				String mysqlUrl = getPhpPtService("datasource.url");
				String mysqlname = getPhpPtService("datasource.username");
				String mysqlPassword = getPhpPtService("datasource.password");
				conn = DriverManager.getConnection(mysqlUrl, mysqlname,
						mysqlPassword);
				stmt = conn.createStatement();
				Boolean b = stmt.execute(sql);
				return uuid.toString();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (null != stmt) {
						stmt.close();
						stmt = null;
					}
					if (null != conn) {
						conn.close();
						conn = null;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return null;
		}
	/*	*//**
	 * 创建玩家
	 * 
	 * @return
	 */
	/*
	 * public static String createPlayerName_old(String loginname) {
	 * if(StringUtil.isBlank(loginname)){ log.info("创建玩家时loginname不能为空 ");
	 * return null; } HttpClient httpClient = null; GetMethod postMethod = null;
	 * String uid=PALY_START+""; try { StringBuffer sbf =new
	 * StringBuffer(PHP_SERVICE); sbf.append("method=login");
	 * sbf.append("&uid="+uid+loginname); sbf.append("&account=CNY");
	 * sbf.append("&currency=CNY"); sbf.append("&country=CN");
	 * //sbf.append("&integrationtype=2");
	 * sbf.append("&lsdId=zero,0%7Cgm,1%7Clongfa123,1");
	 * sbf.append("&un="+uid+loginname); sbf.append("&tester=0"); httpClient =
	 * HttpUtils.createHttpClient(); // System.out.println("创建玩家的url==="+sbf);
	 * postMethod = new GetMethod(sbf.toString());
	 * postMethod.setRequestHeader("Connection", "close"); int statusCode =
	 * httpClient.executeMethod(postMethod); Map<String,String> map = new
	 * HashMap<String,String>(); if (statusCode == HttpStatus.SC_OK) { String
	 * phpHtml = postMethod.getResponseBodyAsString(); map=parse(phpHtml); if
	 * (phpHtml == null || phpHtml.equals("")) {
	 * log.info(loginname+"登录新的TT API 接口出现问题a！"); return null; }
	 * if(map.isEmpty()){ log.info(loginname+"登录新的TT API 接口出现问题b！"); return
	 * null; } log.info(loginname+"登录新的TT API 接口成功！"); return
	 * map.get("playerhandle"); } else { log.info("Response Code: " +
	 * statusCode); return null; } } catch (Exception e) { e.printStackTrace();
	 * log.info("Response 消息: " + e.toString()); return null; } finally { if
	 * (postMethod != null) { postMethod.releaseConnection(); } } }
	 *//**
	 * 获取余额
	 * 
	 * @param loginname
	 * @return
	 */
	/*
	 * public static String getPlayerAccount_old(String loginname) { HttpClient
	 * httpClient = null; GetMethod postMethod = null; String uid=PALY_START+"";
	 * try { StringBuffer sbf =new StringBuffer(PHP_PLAYER_DEPOSIT);
	 * sbf.append("method=getbalance"); sbf.append("&account=CNY");
	 * sbf.append("&uid="+uid+loginname); httpClient =
	 * HttpUtils.createHttpClient(); //System.out.println("查询玩家的url==="+sbf);
	 * postMethod = new GetMethod(sbf.toString());
	 * postMethod.setRequestHeader("Connection", "close"); int statusCode =
	 * httpClient.executeMethod(postMethod); Map<String,String> map = new
	 * HashMap<String,String>(); String phpHtml =
	 * postMethod.getResponseBodyAsString();
	 * if("Player Does Not Exist".equals(phpHtml)){ createPlayerName(loginname);
	 * statusCode = httpClient.executeMethod(postMethod); map = new
	 * HashMap<String,String>(); phpHtml = postMethod.getResponseBodyAsString();
	 * } if (statusCode == HttpStatus.SC_OK) { map=parse(phpHtml); if (phpHtml
	 * == null || phpHtml.equals("")) {
	 * log.info(loginname+"新的TT API 接口出现问题a！----获取余额"); return null; }
	 * if(map.isEmpty()){ log.info(loginname+"登录新的TT API 接口出现问题b！----获取余额");
	 * return null; } return map.get("balance"); } else {
	 * log.info("Response Code: " + statusCode); return null; } } catch
	 * (Exception e) { e.printStackTrace(); log.info("Response 消息: " +
	 * e.toString()); return null; } finally { if (postMethod != null) {
	 * postMethod.releaseConnection(); } } }
	 *//**
	 * 转出或者转入金额
	 * 
	 * @param loginname
	 * @param count
	 * @return
	 */
	/*
	 * public static Boolean addPlayerAccountPraper_old(String loginname,double
	 * amt) { HttpClient httpClient = null; GetMethod postMethod = null; String
	 * uid=PALY_START+""; try { StringBuffer sbf =new
	 * StringBuffer(PHP_PLAYER_DEPOSIT); sbf.append("method=prepareaddchips2");
	 * sbf.append("&uid="+uid+loginname); sbf.append("&amt="+amt);
	 * sbf.append("&account=CNY"); sbf.append("&lsdId=longfa123"); httpClient =
	 * HttpUtils.createHttpClient(); //
	 * System.out.println("增加或减少玩家账户金额事务准备sbf==="+sbf.toString()); postMethod =
	 * new GetMethod(sbf.toString()); postMethod.setRequestHeader("Connection",
	 * "close"); int statusCode = httpClient.executeMethod(postMethod);
	 * Map<String,String> map = new HashMap<String,String>(); if (statusCode ==
	 * HttpStatus.SC_OK) { String phpHtml =
	 * postMethod.getResponseBodyAsString(); map=parse(phpHtml); if (phpHtml ==
	 * null || phpHtml.equals("")) {
	 * log.info(loginname+"新的TT  接口出现问题a！----提交金额"); return false; }
	 * if(map.isEmpty()){ log.info(loginname+"登录新的TT  接口出现问题b！----提交金额"); return
	 * false; } return addPlayerAccountCommit(map.get("transferid")); } else {
	 * log.info("新的TT Response Code: " + statusCode); return false; } } catch
	 * (Exception e) { e.printStackTrace(); log.info("新的TT Response 消息: " +
	 * e.toString()); return null; } finally { if (postMethod != null) {
	 * postMethod.releaseConnection(); } } }
	 *//**
	 * 转出或者转入金额提交----要调用上面哪个方法
	 * 
	 * @param loginname
	 * @param count
	 * @return
	 */
	/*
	 * public static Boolean addPlayerAccountCommit_old(String transferid) {
	 * HttpClient httpClient = null; GetMethod postMethod = null; String
	 * uid=PALY_START+""; try { StringBuffer sbf =new
	 * StringBuffer(PHP_PLAYER_DEPOSIT); sbf.append("method=commitaddchips2");
	 * sbf.append("&transferid="+transferid); sbf.append("&xmlresponse=true");
	 * httpClient = HttpUtils.createHttpClient(); //
	 * System.out.println("增加或减少玩家账户金额事务提交sbf==="+sbf.toString()); postMethod =
	 * new GetMethod(sbf.toString()); postMethod.setRequestHeader("Connection",
	 * "close"); int statusCode = httpClient.executeMethod(postMethod);
	 * Map<String,String> map = new HashMap<String,String>(); if (statusCode ==
	 * HttpStatus.SC_OK) { String phpHtml =
	 * postMethod.getResponseBodyAsString(); map=parse(phpHtml); if (phpHtml ==
	 * null || phpHtml.equals("")) {
	 * log.info(transferid+"新的TT API 接口出现问题a！----提交金额"); return false; }
	 * if(map.isEmpty()){ log.info(transferid+"新的TT API 接口出现问题b！----提交金额");
	 * return false; } return true; } else { log.info("新的TT Response Code: " +
	 * statusCode+"----提交金额"); return false; } } catch (Exception e) {
	 * e.printStackTrace(); log.info("新的TT Response 消息: " +
	 * e.toString()+"----提交金额"); return false; } finally { if (postMethod !=
	 * null) { postMethod.releaseConnection(); } } }
	 *//**
	 * 检查转入转出是否成功
	 * 
	 * @param transferid
	 * @return
	 */
	/*
	 * public static String checkAddPlayerAccount_old(String transferid) {
	 * HttpClient httpClient = null; GetMethod postMethod = null; String
	 * uid=PALY_START+""; try { StringBuffer sbf =new
	 * StringBuffer(PHP_PLAYER_DEPOSIT); sbf.append("method=checkAddChips");
	 * sbf.append("&tid="+transferid); sbf.append("&xmlresponse=true");
	 * httpClient = HttpUtils.createHttpClient();
	 * System.out.println("转入或者转入操作是否成功sbf==="+sbf.toString()); postMethod = new
	 * GetMethod(sbf.toString()); postMethod.setRequestHeader("Connection",
	 * "close"); int statusCode = httpClient.executeMethod(postMethod);
	 * Map<String,String> map = new HashMap<String,String>(); if (statusCode ==
	 * HttpStatus.SC_OK) { String phpHtml =
	 * postMethod.getResponseBodyAsString(); map=parse(phpHtml); if (phpHtml ==
	 * null || phpHtml.equals("")) {
	 * log.info(transferid+"新的TT API 接口出现问题a！----提交金额"); return null; }
	 * if(map.isEmpty()){ log.info(transferid+"登录新的TT API 接口出现问题b！----提交金额");
	 * return null; } return map.get("message"); } else {
	 * log.info("新的TT Response Code: " + statusCode); return null; } } catch
	 * (Exception e) { e.printStackTrace(); log.info("新的TT Response 消息: " +
	 * e.toString()); return null; } finally { if (postMethod != null) {
	 * postMethod.releaseConnection(); } } }
	 *//**
	 * 转换xml数据2 Map
	 * 
	 * @param protocolXML
	 * @return
	 */
	/*
	 * public static Map<String,String> parse(String protocolXML) { //创建一个新的字符串
	 * StringReader read = new StringReader(protocolXML); //创建新的输入源SAX 解析器将使用
	 * InputSource 对象来确定如何读取 XML 输入 InputSource source = new InputSource(read);
	 * //创建一个新的SAXBuilder SAXBuilder sb = new SAXBuilder(); Map<String,String>
	 * returnMap=null; try { //通过输入源构造一个Document Document doc =
	 * sb.build(source); //取的根元素 Element root = doc.getRootElement();
	 * System.out.println(root.getName());//输出根元素的名称（测试） //得到根元素所有子元素的集合 List
	 * jiedian = root.getChildren(); //获得XML中的命名空间（XML中未定义可不写） Namespace ns =
	 * root.getNamespace(); Element et = null; returnMap=new
	 * HashMap<String,String>(); for(int i=0;i<jiedian.size();i++){ et =
	 * (Element) jiedian.get(i);//循环依次得到子元素
	 * returnMap.put(et.getChild("name",ns).getText(),
	 * et.getChild("value",ns).getText());
	 * System.out.println("key===="+et.getChild("name",ns).getText());
	 * System.out.println("value===="+et.getChild("value",ns).getText()); } et =
	 * (Element) jiedian.get(0); List zjiedian = et.getChildren(); for(int
	 * j=0;j<zjiedian.size();j++){ Element xet = (Element) zjiedian.get(j);
	 * System.out.println("aaaa="+xet.getName()); } } catch (JDOMException e) {
	 * // TODO 自动生成 catch 块 e.printStackTrace(); } catch (IOException e) { //
	 * TODO 自动生成 catch 块 e.printStackTrace(); }
	 * 
	 * return returnMap; }
	 * 
	 * 
	 * public static String getPhpPtService(String key) { try { Properties
	 * properties = new Properties();
	 * properties.load(Thread.currentThread().getContextClassLoader
	 * ().getResourceAsStream("init.properties")); return
	 * properties.getProperty(key); } catch (Exception e) {
	 * log.info(e.toString()); return null; } }
	 */

}
