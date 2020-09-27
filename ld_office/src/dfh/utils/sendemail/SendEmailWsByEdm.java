package dfh.utils.sendemail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import dfh.model.SystemConfig;
import dfh.utils.StringUtil;




public class SendEmailWsByEdm {
	/**
	 * 
	 */

	private static Log log = LogFactory.getLog(CommonUtils.class);
	

	private  String querySystemConfig(HibernateTemplate getHibernateTemplate,String typeNo,String itemNo,String flag){
		String str="";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if(!StringUtils.isEmpty(typeNo)){
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if(!StringUtils.isEmpty(itemNo)){
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if(!StringUtils.isEmpty(flag)){
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list=getHibernateTemplate.findByCriteria(dc);
		if(null!=list&&list.size()>0){
			str=list.get(0).getValue();
		}
		return str;
	}
	
	
	public static String getProperty(String key){
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("init.properties"));
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
	
	//登录EDM
	public static String loginEdm(){
		String username="Ivan88";
		String password="Admin888";
		String cookie="lhc_per={%22vid%22:%22qmh1b391z0eek6ijs6w%22}; onethink_home_think_language=zh-cn; PHPSESSID=7dvkbjbr154qdihdq9s4lgscb2";
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://ui.edm.cm/ajax/dologin.html";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		
		
		
		method.setRequestHeader("Host", "ui.edm.cm");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
		method.setRequestHeader("Accept", "*/*");
		method.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		method.setRequestHeader("Accept-Encoding", "gzip, deflate");
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		method.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		method.setRequestHeader("Referer", "http://ui.edm.cm/login.html");
//		method.setRequestHeader("Content-Length", "47");
		method.setRequestHeader("Cookie", cookie);
		method.setRequestHeader("Connection", "keep-alive");
		method.setRequestHeader("Pragma", "no-cache");
		method.setRequestHeader("Cache-Control", "no-cache");
		
		
		
		method.setParameter("username", username);
		method.setParameter("password", password);
		method.setParameter("rememberme", "on");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			JSONObject json = (JSONObject) JSONSerializer.toJSON(result) ;
			String obj= json.get("info").toString();
			obj= URLDecoder.decode(obj, "utf-8");
		//	System.out.println("登录的obj=="+obj);
			if(obj.contains("登录成功")){
				return cookie;
			}else{
				log.info("邮件edm登录失败");
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
	

	   //新建群组的时候 获取验证码
		public static String getYzm(String cookie){
			HttpClient httpClient = null;
			GetMethod postMethod = null;
			InputStream phpHtml=null;
			String value="";
				try {
				//str= URLEncoder.encode(str, "UTF-8");
				StringBuffer sbf = new StringBuffer("http://ui.edm.cm/contacts/create.html");
				httpClient =HttpUtils.createHttpClient();
				postMethod = new GetMethod(sbf.toString());
				postMethod.setRequestHeader("Cookie", cookie);
				int statusCode = httpClient.executeMethod(postMethod);
				if (statusCode == HttpStatus.SC_OK) {
					 phpHtml = postMethod.getResponseBodyAsStream();
					BufferedReader bf=new BufferedReader(new InputStreamReader(phpHtml,"UTF-8"));  
					String line="";
					String str="";
					while((line=bf.readLine())!=null){  
						if(line.contains("vcode")){
							str=line.trim();
							str=str.replace("<input type=\"hidden\" name=\"vcode\" value=\"", "");
							str=str.replace("\" />", "");
						}
				     }  
					return str;
				
				} else {
					log.info("Response Code: " + statusCode);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Response 消息: " + e.toString());
				return null;
			} finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
				try {
					if (phpHtml != null) {
					phpHtml.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	

	
	//新建联络人群组
	public static String sendByEdmCmCreateQz(String cookie,String yzm,int z){
		String result = "";
		
		String qzmc="联系人:"+new Date()+z;
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://ui.edm.cm/contacts/docreate/l/zh-cn.html";
		httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		
		
		
		method.setRequestHeader("Host", "ui.edm.cm");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
		method.setRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		method.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		method.setRequestHeader("Accept-Encoding", "gzip, deflate");
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		method.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		method.setRequestHeader("Referer", "http://ui.edm.cm/contacts/create.html");
		method.setRequestHeader("Content-Length", "280");
		method.setRequestHeader("Cookie", cookie);
		method.setRequestHeader("Connection", "keep-alive");
		method.setRequestHeader("Pragma", "no-cache");
		method.setRequestHeader("Cache-Control", "no-cache");
		
		
		method.setParameter("vcode", yzm);
		method.setParameter("MailAuth", "122");
		method.setParameter("ListName", qzmc);
		method.setParameter("MailistDesc", "联络人名单描述联络人名单描述");
		method.setParameter("AddType", "Paste");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			//System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return qzmc;
	}
	
	
	
	 //查询刚新建的群组  返回改组的编号
	public static String queryQzByName(String str,String cookie){
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		String value="";
			try {
			str= URLEncoder.encode(str, "UTF-8");
			StringBuffer sbf = new StringBuffer("http://ui.edm.cm/Contacts/getlist.html?sEcho=8&iColumns=7&sColumns=%2C%2C%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=15&mDataProp_0=id&sSearch_0=&bRegex_0=false&bSearchable_0=true&bSortable_0=false&mDataProp_1=Auth&sSearch_1=&bRegex_1=false&bSearchable_1=true&bSortable_1=false&mDataProp_2=MailListName&sSearch_2=&bRegex_2=false&bSearchable_2=true&bSortable_2=true&mDataProp_3=MailListNumber&sSearch_3=&bRegex_3=false&bSearchable_3=true&bSortable_3=true&mDataProp_4=MailListInvalidNumber&sSearch_4=&bRegex_4=false&bSearchable_4=true&bSortable_4=true&mDataProp_5=MailListactiveNumber&sSearch_5=&bRegex_5=false&bSearchable_5=true&bSortable_5=true&mDataProp_6=Operating&sSearch_6=&bRegex_6=false&bSearchable_6=true&bSortable_6=false&sSearch="+str+"&bRegex=false&iSortCol_0=0&sSortDir_0=asc&iSortingCols=1&_=1443686620212");
			//System.out.println(sbf);
			httpClient =HttpUtils.createHttpClient();
			postMethod = new GetMethod(sbf.toString());
			postMethod.setRequestHeader("Cookie", cookie);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String phpHtml = postMethod.getResponseBodyAsString();
				  JSONObject json = (JSONObject) JSONSerializer.toJSON(phpHtml) ;
				 Object obj= json.get("aaData");
				if (phpHtml == null || phpHtml.equals("")) {
					log.info("邮件登录失败");
					return "登录失败";
				}else{
					if(null==obj||StringUtil.isEmpty(obj.toString())){
						return "发送失败";
					}
					obj=obj.toString().replace("[", "");
					obj=obj.toString().replace("]", "");
					JSONObject objJson = (JSONObject) JSONSerializer.toJSON(obj) ;
					value=objJson.get("id").toString();
					org.dom4j.Document document = DocumentHelper.parseText(value);
					Node node = document.selectSingleNode("/div/input");
					value=node.valueOf("@value");
					 /*  if(node.selectSingleNode("error_code").getStringValue().equals("0")){
					    
					   }else{
					   }*/
				//	System.out.println(value);
				}
				return value;
			} else {
				log.info("Response Code: " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 消息: " + e.toString());
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		
	}
	
	
	   //根据返回的标号  导入用户
		public static String createUsers(String[] strs,String bh,String cookie){  
			String result = "";
			StringBuffer sss =new StringBuffer();
			for(String str : strs){
				str=str.replace("@", "%40");
				sss.append(str+"%0A");
			}
			String urlStr="";
			/*try {
				//urlStr = URLEncoder.encode(sss.toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}*/
			StringBuffer sbf = new StringBuffer("http://ui.edm.cm/contacts/dopastecontacts.html?cntselected="+bh+"&rowid="+bh+"&MailAuth=122&orgrowid=62&orgrowid=74&orgrowid=222&orgrowid=245&orgrowid=253&orgrowid=264&orgrowid=278&orgrowid=319&orgrowid=333&orgrowid=389&orgrowid=405&orgrowid=453&orgrowid=499&orgrowid=516&orgrowid=547&orgrowid=552&orgrowid=564&orgrowid=584&orgrowid=600&orgrowid=617&orgrowid=632&orgrowid=634&orgrowid=69&orgrowid=75&orgrowid=78&orgrowid=292&orgrowid=304&orgrowid=329&orgrowid=346&orgrowid=378&orgrowid=388&orgrowid=454&txtContacts="+sss);
		//	System.out.println(sbf);
			 HttpClient client = new HttpClient();  
			 client.getParams().setIntParameter("http.socket.timeout", 1000000);
			GetMethod postMethod = new GetMethod(sbf.toString());
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
					"UTF-8");
			postMethod.setRequestHeader("Host", "ui.edm.cm");
			postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
			postMethod.setRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
			postMethod.setRequestHeader("X-Requested-With", "XMLHttpRequest");
			postMethod.setRequestHeader("Referer", "http://ui.edm.cm/Contacts/Import/type/Paste.html");
			postMethod.setRequestHeader("Cookie",cookie);
			postMethod.setRequestHeader("Connection", "keep-alive");
			try {
				client.executeMethod(postMethod);
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
		
		
		
		
		 //新建邮件---创建邮件标题
		public static String createEmailName(String bt,String bh,String cookie){

			String result = "";
			String urlStr="";
			try {
				bt = URLEncoder.encode(bt, "utf-8");
				String strrr = URLEncoder.encode("确认</a>您同意继续接受经由我们所发送的电子邮件", "utf-8");
			//	System.out.println(strrr);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			StringBuffer sbf = new StringBuffer("http://ui.edm.cm/message/dobasic_settings.html?editor=editor&email_subject="+bt+"&MailAuth=122&MailList%5B%5D="+bh+"&WeiBoShare=2&WeiBoTitle=&WeiBodesc=&WeiBodimg=&Permit=2&Permitdesc=%E6%82%A8%E4%B9%8B%E6%89%80%E4%BB%A5%E5%8F%97%E5%88%B0%E6%AD%A4%E9%82%AE%E4%BB%B6%E2%80%8B%E2%80%8B%E6%98%AF%E5%9B%A0%E4%B8%BA%E6%82%A8%E8%AE%A2%E9%98%85%E4%BA%86%E6%88%91%E4%BB%AC%E7%9A%84%E9%82%AE%E4%BB%B6%E3%80%82%E8%AF%B7%3Ca%2Bstyle%3D%22color%3A%230000ff%3B%22%2Bhref%3D%22%7B%24CONFIRMURL%7D%22%3E%E7%A1%AE%E8%AE%A4%3C%2Fa%3E%E6%82%A8%E5%90%8C%E6%84%8F%E7%BB%A7%E7%BB%AD%E6%8E%A5%E5%8F%97%E7%BB%8F%E7%94%B1%E6%88%91%E4%BB%AC%E6%89%80%E5%8F%91%E9%80%81%E7%9A%84%E7%94%B5%E5%AD%90%E9%82%AE%E4%BB%B6.");
		//	System.out.println(sbf);
			 HttpClient client = new HttpClient();  
			 client.getParams().setIntParameter("http.socket.timeout", 1000000);
			GetMethod postMethod = new GetMethod(sbf.toString());
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
					"UTF-8");
			postMethod.setRequestHeader("Host", "ui.edm.cm");
			postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
			postMethod.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
			postMethod.setRequestHeader("Cookie",cookie);
			postMethod.setRequestHeader("Connection", "keep-alive");
			postMethod.setRequestHeader("Cache-Control", "max-age=0");
			try {
				client.executeMethod(postMethod);
				result = postMethod.getResponseBodyAsString();
				JSONObject json = (JSONObject) JSONSerializer.toJSON(result) ;
			    String obj= json.get("result").toString();
			    String[] strs = obj.split("/");
			    result =strs[strs.length-1];
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
			return result;
		}
		
		
		
		//修改邮件内容
		public static String updateEmailNr(String yjnr,String html,String cookie){
			String result = "";
			HttpClient httpClient = HttpUtils.createHttpClient();
			String u = "http://ui.edm.cm/message/dosavemessage/bs/"+html+"";
			httpClient.getParams().setIntParameter("http.socket.timeout", 1000000);
			PostMethod method = new PostMethod(u);
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
					"UTF-8");
			
			
			
			method.setRequestHeader("Host", "ui.edm.cm");
			method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
			method.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			method.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			method.setRequestHeader("Accept-Encoding", "gzip, deflate");
			method.setRequestHeader("Referer", "http://ui.edm.cm/message/create_message/bs/"+html+"");
			method.setRequestHeader("Cookie", cookie);
			method.setRequestHeader("Connection", "keep-alive");
			
			
			method.setParameter("editor.personalize", "[[email]]");
			method.setParameter("html", "<!DOCTYPE+html><html><head><meta+http-equiv=\"Content-Type\"+content=\"text/html;+charset=UTF-8\"+/><meta+name=\"viewport\"+content=\"width=device-width,+initial-scale=1\"><style+type=\"text/css\"><!--.ReadMsgBody+{width:+100%;background-color:+#ffffff;}.ExternalClass+{width:+100%;background-color:+#ffffff;}html+{width:+100%;padding:+0px;margin:+0px;}body+{width:+100%+!important;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;margin:+0px;padding:+0px;background-color:+#ffffff;transform:+scale(1,+1);zoom:+1;}.wrap+{width:+100%+!important;padding:+0px;margin:+0px;}img+{display:+block;-ms-interpolation-mode:+bicubic;margin:+0+!important;padding:+0+!important;-webkit-box-sizing:+border-box;-moz-box-sizing:+border-box;box-sizing:+border-box;}table+font+{background-color:+inherit;}table+{border-collapse:+collapse;border-spacing:+0px;padding:+0px;empty-cells:+hide;mso-table-lspace:+0pt;mso-table-rspace:+0pt;}tr+{border-collapse:+collapse;border-spacing:+0px;}td+{display:+table-cell+!important;border-collapse:+collapse;border-spacing:+0px;}td.column+{+min-width:+0+!important;+}body[data-getresponse]+td.column+{display:+table-cell+!important;float:+none+!important;min-width:+auto;max-width:+auto;}table.wrapper+{width:+570px+!important;max-width:+570px;font-size:+16px;}@media+all+and+(max-width:+480px)+{body+{-webkit-box-sizing:+border-box;-moz-box-sizing:+border-box;box-sizing:+border-box;}table+{-webkit-box-sizing:+border-box;-moz-box-sizing:+border-box;box-sizing:+border-box;}table+td+{-webkit-box-sizing:+border-box;-moz-box-sizing:+border-box;box-sizing:+border-box;}table.wrapper+{width:+100%+!important;max-width:+100%+!important;}table[data-mobile-width=\"1\"]+{max-width:+100%+!important;width:+100%+!important;}table[data-mobile-width=\"0\"]+{max-width:+100%+!important;}table[data-mobile-width=\"0\"]+td+{padding-left:+10px+!important;padding-right:+10px+!important;}table[data-editable=\"image\"]+img+{width:+100%;max-width:+100%+!important;height:+auto+!important;-webkit-box-sizing:+border-box;-moz-box-sizing:+border-box;box-sizing:+border-box;}table[data-mobile-width=\"1\"]+{width:+100%+!important;}table[data-mobile-width=\"1\"]+img+{width:+100%+!important;max-width:+100%+!important;height:+auto+!important;}body[data-getresponse]+table+td+{display:+block+!important;float:+left;width:+100%+!important;height:+auto+!important;}body[data-getresponse]+td.column+{+display:+block+!important;+}table[data-editable=\"text\"]+table[data-editable=\"button\"]+{float:+none;}table[data-mobile-width=\"1\"]+td+{padding-left:+0+!important;+padding-right:+0+!important;}table[data-editable=\"text\"]+{padding-left:+10px+!important;padding-right:+10px+!important;}table[data-editable=\"text\"]+table[data-editable=\"image\"]+{max-width:+100%+!important;margin:+0px;float:+none;}table[data-editable=\"text\"]+table[data-mobile-width]+td+{padding:+0+!important;padding-bottom:+10px+!important;}table[data-editable=\"socialmedia\"]+table+{text-align:+center+!important;}tr[data-columns=\"no\"]+{+text-align:+center;+}tr[data-columns=\"no\"]+>+td+{display:+inline-block+!important;width:+auto+!important;margin:+auto;padding:+5px;}body[data-getresponse]+table.nowrap+td+{+display:+table-cell+!important;+width:+auto+!important;+}tfoot+.wrapper+table+{+float:+none+!important;+margin:+auto+!important;+width:+100%+!important;+}tfoot+.wrapper+table+td+{+text-align:+center+!important;+}tfoot+.wrapper+table+td+img+{+margin:+auto+!important;+}.pl_image+{+width:+100%+!important;+}body[data-getresponse]+tr[data-columns=\"no\"]+>+td+{display:+inline-block+!important;width:+auto+!important;margin:+auto+!important;padding:+5px;}}.pl_image,+.outline+.editable+.imagemask+div+{vertical-align:+middle;text-align:+center;font-family:+Arial,Helvetica,sans-serif;color:+#ffffff;font-size:+20px;-webkit-border-radius:+6px;-moz-border-radius:+6px;border-radius:+6px;background:+#dedede+url(/images/common/templates/messages/elements/placeholders/image.png)+no-repeat+50%+50%;width:+100%;height:+100%;overflow:+hidden;}--></style><!--[if+gte+mso+9]><style+type=\"text/css\"></style><![endif]--></head><body+data-getresponse=\"true\"+style=\"margin:+0;+padding:+0;\"><div+class=\"wrap\"><style+type=\"text/css\"><!--+table.wrapper+{width:+570px;max-width:+570px+!important;font-size:+16px;}td.column+{+min-width:+0+!important;+}+body[data-getresponse]+td.column+{+float:+none+!important;+}body[xx-iframe]+td.column+{+display:+table-cell+!important;+float:+none+!important;+}--></style><table+style=\"font-size:+16px;+background-color:+rgb(255,+255,+255);\"+data-width=\"570\"+dir=\"ltr\"+data-mobile=\"true\"+border=\"0\"+cellpadding=\"0\"+cellspacing=\"0\"+width=\"100%\">++++<tbody><tr>++++++++<td+style=\"padding:0;margin:0;\"+align=\"center\"+valign=\"top\">++++++++++++<table+style=\"width:+570px;\"+class=\"wrapper\"+align=\"center\"+bgcolor=\"#ffffff\"+border=\"0\"+cellpadding=\"0\"+cellspacing=\"0\"+width=\"570\">++++++++++++++++<tbody>+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<tr>++++++++++++++++++++<td+style=\"margin:0;padding:0;\"+align=\"left\"+valign=\"top\">++++++++++++++++++++++++<table+style=\"margin-right:+auto;+margin-left:+auto;\"+data-editable=\"text\"+align=\"center\"+border=\"0\"+cellpadding=\"0\"+cellspacing=\"0\"+width=\"100%\">++++++++++++++++++++++++++++<tbody><tr>++++++++++++++++++++++++++++++++<td+style=\"padding:+0px;+margin:+0px;+font-family:+Arial,Helvetica,sans-serif;+color:+rgb(0,+0,+0);\">&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+&nbsp;+<font+style=\"font-size:+25px;\"+size=\"25\"><span+style=\"color:+rgb(0,+0,+0);\">&nbsp;</span><span+style=\"color:+rgb(245,+14,+14);\">&gt;&gt;</span></font><font+style=\"font-size:+25px;\"+size=\"25\"><span+style=\"color:+rgb(255,+0,+0);\">&lt;&lt;</span></font><div style=\"font-size: 20px\">"+yjnr+"</div><div></div><div>&nbsp;</div><div><br></div><div><font+style=\"font-size:+25px;+color:+rgb(245,+14,+14);\"+size=\"25\"></font></div><div><br></div><div><br></div></td>++++++++++++++++++++++++++++</tr>++++++++++++++++++++++++</tbody></table>++++++++++++++++++++++++++++++++++++++++++++++++++++++</td>++++++++++++++++</tr><tr>++++++++++++++++++++<td+style=\"margin:0;padding:0;\"+align=\"left\"+valign=\"top\">++++++++++++++++++++++++<table+data-mobile-width=\"1\"+style=\"margin-right:+auto;\"+data-editable=\"image\"+align=\"center\"+border=\"0\"+cellpadding=\"0\"+cellspacing=\"0\"+width=\"59%\">++++++++++++++++++++++++++++<tbody><tr>++++++++++++++++++++++++++++++++<td+style=\"margin:+0px;+padding:+0px+0px+20px;\"+align=\"left\"+valign=\"top\"></td>++++++++++++++++++++++++++++</tr>++++++++++++++++++++++++</tbody></table>+++++++++++++++++++++++++++++++++++++++++++++++++++++++</td>++++++++++++++++</tr></tbody></table>++++++++</td>++++</tr></tbody><tfoot+style=\"\"><tr><td+style=\"margin:+0px;+padding:+15px+0px;\"+align=\"center\"><table+class=\"wrapper\"+style=\"width:+570px;\"+align=\"center\"+border=\"0\"+cellpadding=\"0\"+cellspacing=\"0\"+width=\"570\"><tbody><tr><td+align=\"center\"+valign=\"top\">[[internal_footer]]</td></tr></tbody></table></td></tr></tfoot></table>+</div></body></html>");
			method.setParameter("plain", "");
			method.setParameter("toemail", "");
			method.setParameter("next", "++++++++++++++++++++++++++++++++++++++++++++下一步++++++++++++++++++++++++++++++++++++++++++++");
			method.setParameter("editor_engine", "wysiwyg");
			try {
				httpClient.executeMethod(method);
				result = method.getResponseBodyAsString();
			//	System.out.println(result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (method != null) {
					method.releaseConnection();
				}
			}
			return result;
		}
		
		
		
		
		 //发送邮件
		public static String send(String sendtime,String html,String cookie){
			HttpClient httpClient = null;
			GetMethod postMethod = null;
			String value="";
				try {
				//str= URLEncoder.encode(str, "UTF-8");
				StringBuffer sbf = new StringBuffer("http://ui.edm.cm/message/dosendschedule/bs/"+html+"?hdOffset=+08.00&hdClientZone=AWST&SendMode=2&hdArchiveID=&schOk=&senddate=&sendtime=10:22+AM");
				httpClient =HttpUtils.createHttpClient();
				postMethod = new GetMethod(sbf.toString());
				postMethod.setRequestHeader("Cookie", cookie);
				int statusCode = httpClient.executeMethod(postMethod);
				if (statusCode == HttpStatus.SC_OK) {
					String phpHtml = postMethod.getResponseBodyAsString();
					  JSONObject json = (JSONObject) JSONSerializer.toJSON(phpHtml) ;
					if (phpHtml == null || phpHtml.equals("")) {
						log.info("邮件登录失败");
						return "登录失败";
					}else{
						 String msg=json.get("msg").toString();
						 if(msg.contains("操作成功")){
							 return "发送成功";
						 }else{
							 return "发送失败";
						 }
					}
				} else {
					log.info("Response Code: " + statusCode);
					return "发送失败"+statusCode;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Response 消息: " + e.toString());
				return "发送失败"+e.toString();
			} finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
			
		}
		
		
		public static String sendemail(String[] strs,String bt,String nr){
			//String[] strs ={"jatisme2015@gmail.com","2870490746@qq.com","635757605@qq.com"};
			 int i=0;
				for(String str: strs){
					if(!checkEmail(str)){
						strs[i]="";
					}
					i++;
				}
			//获取cookie
			String cookie=SendEmailWsByEdm.loginEdm();
			String yzm=SendEmailWsByEdm.getYzm(cookie);
			//创建群组 并返回群组名称
			String qzName= SendEmailWsByEdm.sendByEdmCmCreateQz(cookie,yzm,strs.length);
			//根据群组名称 返回群组代码
			String code =SendEmailWsByEdm.queryQzByName(qzName, cookie);
			//根据群组代码 插入邮箱地址
			SendEmailWsByEdm.createUsers(strs, code, cookie);
			//创建邮件  需要先生成群组代码
			String html=SendEmailWsByEdm.createEmailName(bt,code,cookie);
			//修改邮件内容
			SendEmailWsByEdm.updateEmailNr(nr,html, cookie);
			//发送邮件
			Calendar ca = Calendar.getInstance();
			int hour=ca.get(Calendar.HOUR);//小时 
			int minute=ca.get(Calendar.MINUTE);//分 
			String sendtime="";
			if(hour>=13){
			sendtime=hour+":"+minute+"+PM";
			}else{
				sendtime=hour+":"+minute+"+AM";	
			}
			  String result=  SendEmailWsByEdm.send(sendtime,html, cookie);
			//System.out.println(result);
			return result;
		}
		
		
		
		@SuppressWarnings("static-access")
		public static void main(String[] args) {
			String[] strs =new String[]{ "jatisme2015@gmail.com"};
			SendEmailWsByEdm sb = new SendEmailWsByEdm();
			sb.sendemail(strs, "标题标题标题标题标题标题", "内容内容内容内容内容");
		}
		
		/**
		  * 验证邮箱
		  * @param email
		  * @return
		  */
		 public static boolean checkEmail(String email){
		  boolean flag = false;
		  try{
		    String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		    Pattern regex = Pattern.compile(check);
		    Matcher matcher = regex.matcher(email);
		    flag = matcher.matches();
		   }catch(Exception e){
		    flag = false;
		   }
		  return flag;
		 }
	
		 
		 public static String checkemailFlagByexec(final String email){
			  final ExecutorService exec = Executors.newFixedThreadPool(1);  
		        Callable<String> call = new Callable<String>() {  
		            public String call() throws Exception {  
		            	return checkemailFlag(email);
		            }  
		        };  
		        try {  
		            Future<String> future = exec.submit(call);  
		            return future.get(25000 * 1, TimeUnit.MILLISECONDS); //任务处理超时时间设为25 秒  
		        } catch (TimeoutException ex) {  
		            // System.out.println("处理超时啦....");  
		            //ex.printStackTrace();  
		            return null;
		        } catch (Exception e) {  
		            System.out.println("处理失败.");  
		            //e.printStackTrace();  
		            return null;
		        }finally{
		        	 // 关闭线程池  
			        exec.shutdown();  
		        } 
			}
		 
		 
		 	//检查是否是正确有效的邮箱
			public static String checkemailFlag(String email){
				   String str="false";
					if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) { 
					return str; 
					} 
					String log = ""; 
					String host = ""; 
					String hostName = email.split("@")[1];// 去掉@后面的 
			//		System.out.println("hostName:" + hostName); 
					Record[] result = null; 
					SMTPClient client = new SMTPClient(); 
					try { 
					// 查找MX记录 
					Lookup  lookup = new Lookup (hostName, Type.MX); 
					lookup.run(); 
					if (lookup.getResult() != lookup.SUCCESSFUL) { 
					System.out.println("找不到MX记录"); 
					return "false"; 
					} else { 
					result = lookup.getAnswers(); 
					for (int i = 0; i < result.length; i++) { 
			//		System.out.println(result[i].getAdditionalName().toString()); 
			//		System.out.println(result[i]); 
					} 
					} 
					// 连接到邮箱服务器 
					for (int i = 0; i < result.length; i++) { 
					host = result[i].getAdditionalName().toString(); 
					client.connect(host); 
					if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) { 
					client.disconnect(); 
					continue; 
					} else { 
					log += "邮箱mx记录" + hostName + "存在"; 
					log += "成功连接到" + host; 
					break; 
					} 
					} 
			//		System.out.println(client.getReplyString()); 
					client.login("gmail.com"); 
			//		System.out.println(client.getReplyString()); 
					client.setSender("zzz.linuxzzz.com@gmail.com");// 发件人 
					log += "=" + client.getReplyString(); 
					client.addRecipient(email); 
					log += ">RCPT TO: <" + email + ">\n"; 
					log += "=" + client.getReplyString(); 
		   //		System.out.println("log="+log);
					if (250 == client.getReplyCode()) { 
					return "true"; 
					}else{
					return str; 
					}
					} catch (Exception e) { 
					e.printStackTrace(); 
					return null;
					} finally { 
					try { 
					client.disconnect(); 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} // 打印日志 
					} 
}
	}