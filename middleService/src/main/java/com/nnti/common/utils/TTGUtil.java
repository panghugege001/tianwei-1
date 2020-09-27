package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.nnti.common.extend.SpecialEnvironmentConfig;

public class TTGUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(TTGUtil.class);

	// 查询玩家余额
	public static String getPlayerAccount(String product, String loginName) {

		HttpClient httpClient = null;
		GetMethod postMethod = null;

		try {

			HashMap<String, String> maps = ttgMap.get(product);
			String PLAYER_URL = maps.get("PLAYER_URL");
			String PALY_START = maps.get("PALY_START");

			String url = PLAYER_URL + PALY_START + loginName + "/balance";
//			log.info("玩家" + loginName + "执行getPlayerAccount方法，url参数值为：" + url);

			httpClient = HttpUtil.createHttpClient();

			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");

			int statusCode = httpClient.executeMethod(postMethod);
			log.info("玩家" + loginName + "执行getPlayerAccount方法，statusCode参数值为：" + statusCode);

			Map<String, String> map = new HashMap<String, String>();

			if (statusCode == HttpStatus.SC_OK) {

				String phpHtml = postMethod.getResponseBodyAsString();
//				log.info("玩家" + loginName + "执行getPlayerAccount方法，phpHtml参数值为：" + phpHtml);

				map = parse_New(phpHtml);

				if (StringUtils.isBlank(phpHtml)) {

					return null;
				}

				if (map.isEmpty()) {

					return null;
				}

				if (StringUtils.isNotBlank(map.get("real"))) {

					return map.get("real");
				} else {

					return "0";
				}
			} else {

				return null;
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行getPlayerAccount方法发生异常，异常信息：" + e.getMessage());

			return null;
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}
	}

	// 转出或者转入金额
	public static Boolean addPlayerAccountPraper(String product, String loginName, double amt) {

		HashMap<String, String> maps = ttgMap.get(product);
		String PALY_START = maps.get("PALY_START");
		
		String transferID = PALY_START+DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);

		if (StringUtils.isEmpty(transferID)) {

			return false;
		}

		String TRANSACTION_URL = maps.get("TRANSACTION_URL");

		URL url = null;
		loginName = PALY_START + loginName;

		try {

			url = new URL(TRANSACTION_URL + transferID);
		} catch (MalformedURLException e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行addPlayerAccountPraper方法发生异常，异常信息：" + e.getMessage());

			return false;
		}

		URLConnection con = null;

		try {

			con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/xml");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());

			String xml = "<transactiondetail uid=\"" + loginName + "\" amount=\"" + amt + "\" />";

			out.write(new String(xml));
			out.flush();
			out.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			StringBuffer sbf = new StringBuffer("");
			String line = "";

			for (line = br.readLine(); line != null; line = br.readLine()) {

				sbf.append(line);
			}

//			log.info("玩家" + loginName + "执行addPlayerAccountPraper方法，sbf参数值为：" + sbf.toString());

			Map<String, String> map = parse_New(sbf.toString());
			String retry = map.get("retry");

			if (StringUtils.isNotBlank(retry) && retry.equals("0")) {

				return true;
			} else {

				return false;
			}
		} catch (IOException e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行addPlayerAccountPraper方法发生异常，异常信息：" + e.getMessage());

			return false;
		}
	}

	public static String createTtgtransferId(String product, Double amount, String loginName) {

		HashMap<String, String> maps = ttgMap.get(product);
		String PALY_START = maps.get("PALY_START");

		String uuid = PALY_START + UUID.randomUUID();

		Connection conn = null;
		Statement stmt = null;

		String datetime = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());

		String sql = "insert into ttgtransfer (id,amount,createtime,loginname) values ('" + uuid.toString() + "'," + amount + ",'" + datetime + "','" + loginName + "')";

		try {

			String driverClassName = JdbcUtil.getValue("datasource.driverClassName");
			driverClassName = SpecialEnvironmentConfig.getPlainText(driverClassName);

			String masterUrl = JdbcUtil.getValue("master.datasource.url");
			masterUrl = SpecialEnvironmentConfig.getPlainText(masterUrl);

			String masterUserName = JdbcUtil.getValue("master.datasource.username");
			masterUserName = SpecialEnvironmentConfig.getPlainText(masterUserName);

			String masterPassword = JdbcUtil.getValue("master.datasource.password");
			masterPassword = SpecialEnvironmentConfig.getPlainText(masterPassword);

			Class.forName(driverClassName);

			conn = DriverManager.getConnection(masterUrl, masterUserName, masterPassword);
			stmt = conn.createStatement();
			stmt.execute(sql);

			return uuid.toString();
		} catch (Exception e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行createTtgtransferId方法发生异常，异常信息：" + e.getMessage());

			return null;
		} finally {

			try {

				if (null != stmt) {

					stmt.close();
				}

				if (null != conn) {

					conn.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> parse_New(String protocolXML) {

		StringReader read = new StringReader(protocolXML);
		InputSource source = new InputSource(read);

		SAXBuilder sb = new SAXBuilder();
		Map<String, String> returnMap = new HashMap<String, String>();

		try {

			Document doc = sb.build(source);
			Element root = doc.getRootElement();
			List jiedian = root.getAttributes();

			for (int i = 0; i < jiedian.size(); i++) {

				Attribute et = (Attribute) jiedian.get(i);
				returnMap.put(et.getName(), et.getValue());
			}
		} catch (JDOMException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		String loginname = "dytest01";
		System.out.println(addPlayerAccountPraper("dy", loginname, -200));
		System.out.println(getPlayerAccount("dy", loginname));
	}
}