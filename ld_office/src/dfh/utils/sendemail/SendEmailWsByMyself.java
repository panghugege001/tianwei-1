package dfh.utils.sendemail;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.utils.DateUtil;

public class SendEmailWsByMyself {
	/**
	 * 
	 */

	private static Log log = LogFactory.getLog(CommonUtils.class);
	public static String url = "http://45.34.95.94";

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
	/*	for(int i=0;i<10;i++){
		int x = (int)(Math.random() * (6)); 
		System.out.println(x);
		}*/
		String[] strs = { "2870490746@qq.com","web-kjkfkm@mail-tester.com"};
		SendEmailWsByMyself se = new SendEmailWsByMyself();
		se.sendemail(strs, "测试", "艾尔他二哥是电饭锅电饭锅好多封更好的风格和儿童散热提供商风格，打算放弃爱违反，艾尔发的说法设定的。");

	}

	public static String login() {
		String username = "jat";
		String password = "admin_!QAZ";
		String cookie = "td_cookie=41580540; oempro_admin_loginreminder=c4ca4238a0b923820dcc509a6f75849b; PHPSESSID=0ssqc4q3ekcnsajokmar4bdvb4";
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);
		method.setParameter("Username", username);
		method.setParameter("Password", password);
		method.setParameter("Command", "Login");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			if (result.equals("")) {
				return cookie;
			} else {
				log.info("邮件登录失败");
				return "登录失败";
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

	public static String sendByEdmCmCreateQz(String cookie, int z) {
		String result = "";
		String str = RandomUtils.nextDouble() + "";
		str = str.substring(0, 6);
		String qzmc = "联系人:" + str + new Date() + z;
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/list/create/";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);
		method.setParameter("Command", "Create");
		method.setParameter("Name", qzmc);
		method.setParameter("OptInMode", "Double");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return qzmc;
	}

	public static String getbh(String cookie, String str) {
		String returnStr = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/search/";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);
		method.setParameter("keyword", str);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			String regex = "statistics/(.*)\">" + str;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(result);
			while (matcher.find()) {
				returnStr = matcher.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return returnStr;
	}

	public static String impaddress(String cookie, String lbbh, String str) {
		String returnStr = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/subscribers/add/" + lbbh
				+ "/copyandpaste";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);
		method.setParameter("FormSubmit", "true");
		method.setParameter("ListID", lbbh);
		method.setParameter("CopyAndPasteData", str);

		method.setParameter("FieldTerminator", ",");
		method.setParameter("FieldEncloser", "");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return returnStr;
	}

	public static String makeSureImpaddress(String cookie, String lbbh) {
		String returnStr = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/subscribers/add/" + lbbh
				+ "/copyandpaste";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);
		method.setParameter("FormSubmit", "true");
		method.setParameter("ListID", lbbh);
		method.setParameter("MatchedFields1", "EmailAddress");
		method.setParameter("AddToSuppressionList", "none");
		method.setParameter("UpdateDuplicates", "true");
		method.setParameter("DoNotSendOptInConfirmationEmail", "true");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return returnStr;
	}

	public static String createHd(String cookie, String bt, String bh) {
		String returnstr = "";
		String recipients = bh + ":0";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/campaign/create/";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);

		method.setParameter("Command", "Create");
		method.setParameter("TestSize", "20");
		method.setParameter("CampaignName", bt);
		method.setParameter("Recipients[]", recipients);
		method.setParameter("TestDurationMultiplier", "1");
		method.setParameter("TestDurationBaseSeconds", "3600");
		method.setParameter("Winner", "Highest Open Rate");
		method.setParameter("GoogleAnalyticsDomains", "");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			returnstr = method.getResponseHeader("Location").getValue();
			returnstr = returnstr.replace(url
					+ "/oem/app/index.php?/user/email/create/campaign/", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return returnstr;
	}

	public static String createfjr(String cookie, String hdbh, String fjrmc,
			String fjrdz) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/email/create/campaign/"
				+ hdbh + "/fromscratch";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);
		method.setParameter("FormSubmit", "true");
		method.setParameter("EditEvent", "false");
		method.setParameter("FromName", fjrmc);
		method.setParameter("FromEmail", fjrdz);
		method.setParameter("SameNameEmail", "true");
		method.setParameter("ReplyToName", "");
		method.setParameter("ReplyToEmail", "");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	public static String createNR(String cookie, String hdbh, String yjbt,
			String yjnr) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/email/create/campaign/"
				+ hdbh + "/fromscratch";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);

		method.setParameter("FormSubmit", "true");
		method.setParameter("EditEvent", "false");
		method.setParameter("ContentType", "Both");
		method.setParameter("Subject", yjbt);
		method.setParameter("personalization-select-Subject", "");
		//String boday = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><title></title></head><body>"
		StringBuffer sbf=new StringBuffer("<html><head><title></title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><meta http-equiv=\"Content-Language\" content=\"en-us\"><style type=\"text/css\" media=\"screen\"></style></head><body class=\"element-selected\" style=\"\" marginwidth=\"0\" marginheight=\"0\">");
		sbf.append("<div id=\"BodyNewsletter\" style=\"margin: 0px auto; width: 610px;\"><table id=\"ViewInBrowser\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 610px;\"><tr>");
		sbf.append("<td class=\"iguana iguana-rich-editable\" style=\"font-family: Helvetica,Arial,sans-serif;\">");
//		sbf.append("<a href=\"%Link:WebBrowser%\">View it on your browser</a>.</p></td></tr></table>");
//				sbf.append("<table id=\"NewsletterHeader\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 610px;\">");
//		sbf.append("<tr><td style=\"color: #ffffff; background: none repeat scroll 0% 0% #4779dc; font-family: Helvetica,Arial,sans-serif; padding: 18px 18px 0pt;\" height=\"90\">");
//		sbf.append("<h1 class=\"iguana iguana-single-editable\" style=\"font-size: 30px; margin: 0px; padding: 0px; text-align: center;\">"+yjbt+"</h1>");
//		sbf.append("</td></tr><tr><td style=\"font-family: Helvetica,Arial,sans-serif; padding: 0px 18px; background-color: #4779dc;\">");
//		sbf.append("<p style=\"font-size: 14px; padding: 9px 18px; background: none repeat scroll 0% 0% #3c5da4; color: #ffffff;\"><span class=\"iguana iguana-single-editable\" id=\"Date\" style=\"float: right;\">December 27, 2007</span> <span class=\"iguana iguana-single-editable\">Brought to you by Abc Company</span></p>");
//		sbf.append("</td></tr></table><table id=\"BorderTable\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 610px;\"><tr>");
//		sbf.append("<td style=\"background: none repeat scroll 0% 0% #4779dc; padding: 0px 18px 18px;\">");
//		sbf.append("<table id=\"NewsletterBody\" style=\"width: 100%; background-color: #ffffff;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
//		sbf.append("<tr><td style=\"font-family: Helvetica,Arial,sans-serif; padding: 9px 18px;\"><div class=\"iguana iguana-duplicatable\">");
//		sbf.append("<h2 class=\"iguana iguana-single-editable\" style=\"text-align: center; font-size: 20px; color: #666666;\">Good News</h2>");
//		sbf.append("<div class=\"iguana iguana-rich-editable\" style=\"font-size: 14px; line-height: 18px; color: #666666; text-align: justify;\">");
		sbf.append("<p>"+yjnr+"</p>");
//		sbf.append("</div></div></td></tr></table></td></tr></table>");
//		sbf.append("<table id=\"NewsletterFooter\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 610px;\"><tr>");
//		sbf.append("<td class=\"iguana iguana-rich-editable\" style=\"font-family: Helvetica,Arial,sans-serif; padding: 9px 18px;\">");
		sbf.append("<p style=\"text-align: center; font-size: 12px; color: #666666;\">You are receiving this email because the email address %Subscriber:EmailAddress% was subscribed to our email list. "); 
		sbf.append("<p style=\"text-align: center; font-size: 12px; color: #666666;\">2016 Abc Company, Street address. <a href=\"%Link:Unsubscribe%\">若不想再收到此邮件，点击这里退订</a>.</p>");
//		sbf.append("</td></tr></table></div></body></html>;");
		sbf.append("</div></body></html>;");
		method.setParameter("HTMLContent", sbf.toString());
		method.setParameter("personalization-select-HTMLContent", "");
		method.setParameter("PlainContent", yjnr);
		method.setParameter("personalization-select-PlainContent", "");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	public static String createqrnr(String cookie, String hdbh) {
		String result = "";

		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/email/create/campaign/"
				+ hdbh + "/fromscratch";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);

		method.setParameter("Command", "");
		method.setParameter("FormSubmit", "true");
		method.setParameter("EditEvent", "false");
		method.setParameter("PreviewType", "browser");
		method.setParameter("PreviewEmailAddress", "");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	public static String sendNow(String cookie, String bt, String hdbh,
			String lbbh) {
		String result = "";

		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = url + "/oem/app/index.php?/user/campaign/edit/" + hdbh;
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Cookie", cookie);

		method.setParameter("Command", "Save");
		method.setParameter("RelEmailID", lbbh);
		method.setParameter("EmailMode", "Empty");
		method.setParameter("SendDate", DateUtil.fmtYYYY_MM_DD(new Date()));
		method.setParameter("CampaignName", bt);
		method.setParameter("Recipients[]", lbbh + ":0");
		method.setParameter("GoogleAnalyticsDomains", "");
		method.setParameter("ScheduleType", "Immediate");
		method.setParameter("SendTimeZone", "(GMT+08:00) Hong Kong");
		method.setParameter("SendTimeHour", "00");
		method.setParameter("SendTimeMinute", "00");
		method.setParameter("SendRepeteadlyDayType", "Every day");
		method.setParameter("SendRepeatedlyMonthDays", "");
		method.setParameter("SendRepeatedlyMonthType", "Every month");
		method.setParameter("ScheduleRecSendMaxInstance", "0");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	public static String sendemail(String[] strs, String bt, String nr) {
		StringBuffer sbf = new StringBuffer("");
		for (String str : strs) {
			if (!checkEmail(str)) {
				str = "";
			} else {
				sbf.append(str + "\n");
			}
		}
		String cookie = login();
		String qzmc = sendByEdmCmCreateQz(cookie, strs.length);
		String lbbh = getbh(cookie, qzmc);
		if (dfh.utils.StringUtil.isEmpty(lbbh)) {
			return "发送失败：lbbh";
		}
		impaddress(cookie, lbbh, sbf.toString());
		makeSureImpaddress(cookie, lbbh);
		String hdbh = createHd(cookie, bt, lbbh);// 拿到活動編號
		if (dfh.utils.StringUtil.isEmpty(hdbh)) {
			return "发送失败：lbbh";
		}
		String chars = "abcdefghijklmnopqrstuvwxyz";
		StringBuffer sbff=new StringBuffer("");
		for(int i=0;i<6;i++){
			sbff.append(chars.charAt((int)(Math.random() * 26)));
		}
		createfjr(cookie, hdbh,sbff.toString(),/*sbff.toString()+*/"service@e6801.com");
		createNR(cookie, hdbh, bt, nr);
		createqrnr(cookie, hdbh);
		sendNow(cookie, bt, hdbh, lbbh);
		return "发送成功";
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
			if(email.contains("@gmail")||email.contains("@sina")||email.contains("@hotmail")||email.contains("@yahoo")){
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	

}