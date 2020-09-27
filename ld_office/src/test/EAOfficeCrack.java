package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import dfh.security.MySecureProtocolSocketFactory;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;

public class EAOfficeCrack {
	static Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);

	public static void main(String[] args) throws IOException {

		HttpClient client = HttpUtils.createHttpClient();
		// Set Cookie
		client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		HttpState initialState = new HttpState();
		Cookie cookie = new Cookie();
		cookie.setDomain("gms.ea2-mission.com");
		cookie.setPath("/");
		cookie.setName("PHPSESSID");
		cookie.setValue("4bab13fb6efa9f0da45c708a92440c7e");
		// cookie.setExpiryDate(DateUtil.getTomorrow());
		initialState.addCookie(cookie);
		client.setState(initialState);
		client.getHostConfiguration().setHost("gms.ea2-mission.com", 443, myhttps);
		HttpMethod method = getPostMethod();
		// 使用 POST 方式提交数据
		System.out.println("begin to post");
		client.executeMethod(method);
		// 打印服务器返回的状态
		System.out.println(method.getStatusLine());
		// 打印结果页面
		String html = method.getResponseBodyAsString();

		Parser htmlParser = new Parser();

		try {
//			String html = IOUtils.toString(new FileInputStream("d:/elf/data.html"), "UTF-8");
			int begin = html.indexOf("<TABLE width=98% border=0 cellPadding=0 cellSpacing=0 borderColorLight=#007cd0 borderColorDark=#ffffff class=\"result\">");
			String endStr = "</table>";
			int end = html.indexOf(endStr, begin);
			html = html.substring(begin, end + endStr.length());
			System.out.println(html);
			htmlParser.setEncoding("UTF-8");
			htmlParser.setInputHTML(html);
			NodeFilter filter = new NodeClassFilter(TableTag.class);
			NodeList nodeList = htmlParser.parse(filter);
			System.out.println(nodeList.size());
			TableTag table = (TableTag) nodeList.elementAt(0);
			System.err.println(table.toHtml());
			System.out.println(table.getRowCount());

			for (int i = 1; i < table.getRowCount(); i++) {
				TableRow tr = (TableRow) table.getRow(i);
				TableColumn[] tc = tr.getColumns();
				String loginname = tc[1].toPlainTextString();
				String ip = tc[2].toPlainTextString();
				String type = tc[4].toPlainTextString();
				String createtime = tc[5].toPlainTextString();
				System.out.println("loginname:" + loginname + ";ip:" + ip + ";type:" + type + ";createtime:" + createtime);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private static HttpMethod getPostMethod() {
		PostMethod post = new PostMethod("/layout/system/link_log.php");
		// NameValuePair name = new NameValuePair("name", "Sotier_Yan");
		// NameValuePair mphone = new NameValuePair("mphone", "13566667777");
		// NameValuePair agroupid = new NameValuePair("agroupid", "22508551");
		// post.setRequestBody(new NameValuePair[] { name, mphone, agroupid });
		return post;
	}
}
