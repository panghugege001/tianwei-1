package dfh.service.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

import dfh.dao.LogDao;
import dfh.model.Actionlogs;
import dfh.security.MySecureProtocolSocketFactory;
import dfh.service.interfaces.EaOfficeFetchService;
import dfh.service.interfaces.SynRecordsService;
import dfh.utils.HttpUtils;

public class EaOfficeFetchServiceImpl extends UniversalServiceImpl implements EaOfficeFetchService {
	private static Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
	private final static String host = "gms.ea2-mission.com";
	
	private final static String loginLogsUrl="/layout/system/link_log.php";

	private static Log log = LogFactory.getLog(EaOfficeFetchServiceImpl.class);
	private LogDao logDao;
	private String phpsessvalue;

	public String getPhpsessvalue() {
		return phpsessvalue;
	}

	public void setPhpsessvalue(String phpsessvalue) {
		this.phpsessvalue = phpsessvalue;
	}

	public EaOfficeFetchServiceImpl() {
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	/**
	 * http请求，返回表单
	 * 
	 * @return
	 * @throws IOException
	 */
	private String httpRequest(PostMethod method) throws IOException {

		HttpClient client = HttpUtils.createHttpClient();
		// Set Cookie
		client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		HttpState initialState = new HttpState();
		Cookie cookie = new Cookie();
		cookie.setDomain(host);
		cookie.setPath("/");
		cookie.setName("PHPSESSID");
		cookie.setValue("");
		// cookie.setExpiryDate(DateUtil.getTomorrow());
		initialState.addCookie(cookie);
		client.setState(initialState);
		client.getHostConfiguration().setHost(host, 443, myhttps);
		// 使用 POST 方式提交数据
		System.out.println("begin to post");
		client.executeMethod(method);
		// 打印服务器返回的状态
		System.out.println(method.getStatusLine());
		// 打印结果页面
		return method.getResponseBodyAsString();
	}

	private List<Actionlogs> parseLoginLogsResponse(String html) {
		List<Actionlogs> list = new ArrayList<Actionlogs>();
		Parser htmlParser = new Parser();
		try {
			// String html = IOUtils.toString(new FileInputStream("d:/elf/data.html"), "UTF-8");
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
		return list;
	}

	private static HttpMethod getLoginlogPostMethod() {
		PostMethod post = new PostMethod("/layout/system/link_log.php");
		// NameValuePair name = new NameValuePair("name", "Sotier_Yan");
		// NameValuePair mphone = new NameValuePair("mphone", "13566667777");
		// NameValuePair agroupid = new NameValuePair("agroupid", "22508551");
		// post.setRequestBody(new NameValuePair[] { name, mphone, agroupid });
		return post;
	}

	public void fetchLoginLogs() {
		log.info("start to fetch login logs");	/*
		Date start = null;
	
		DetachedCriteria dc = DetachedCriteria.forClass(BetRecords.class).setProjection(Projections.max("billTime"));
		List list = getHibernateTemplate().findByCriteria(dc);
		System.out.println(list.toString());
		if (list != null && list.size() > 0)
			start = (Date) list.get(0);
		if (start == null)
			start = new Date(0L); //
		List records = RemoteCaller.getBetRecord(DateUtil.getMontSecond(start, 1), DateUtil.get15MinutesAgo(), null, true);
		if (records == null || records.size() == 0) {
			log.info("no bet records to syn");
			return;
		} else {
			log.info("got " + records.size() + " 条记录");
			final Iterator it = records.listIterator();
			Object result = getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					int rows = 0;
					log.info("start to save the bet records");
					session.connection().setAutoCommit(false);
					try {
						while (it.hasNext()) {
							BetRecords record = (BetRecords) it.next();
							session.save(record);
							rows++;
							if (rows % 500 == 0)
								session.flush();
						}
					} catch (Exception e) {
						session.flush();
						session.clear();
						return rows - rows % 500;
					}
					session.flush();
					session.clear();
					return rows;
				}
			});
			String remark = "以" + DateUtil.formatDateForStandard(start) + "为起点,共获取" + records.size() + "条记录,同步" + result + "条记录";
			logDao.insertOperationLog("system", OperationLogType.SYN_BET_RECORDS, remark);
			log.info("finish syn the bet records");
			return;
		}
		*/

	}

}
