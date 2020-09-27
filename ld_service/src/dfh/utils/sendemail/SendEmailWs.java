package dfh.utils.sendemail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import dfh.utils.HttpUtils;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.utils.sendemail.com.focussend.WebServiceCaller;
import sun.misc.BASE64Encoder;




public class SendEmailWs {
	/**
	 * 
	 *//*

	private static Log log = LogFactory.getLog(CommonUtils.class);
	private static final String KEY ="586102bf-f8b2-4d12-9fcd-45cdfa662e29";//key
	private static final String sendEmail="mk@e68.ph";//发件人地址
	private static final String sendName="E路发娱乐城";//发件人名称
	
	
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

	*//**
	 * 创建文件夹----暂时不用
	 *//*
	public static String CreateFolder() {
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com/v1/folder/";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		method.setRequestBody("{\"Name\": \"test000\",\"Type\":\"Content\"}");
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
		return result;
	}

	*//**
	 * 创建郵件模板  
	 * 
	 * @param BodyHTML
	 * @param BodyText
	 * @param FolderId
	 * @param Name
	 * @param Subject
	 * @param Tracking
	 * @return
	 * @throws Exception
	 *//*
	public static Map<String,Object> CreateSendNewsletter(String BodyHTML, String BodyText,
			 String Name, String Subject) {
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/v1/newsletters/";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		
	//	 method.setRequestBody("{\"BodyHTML\": "+BodyHTML+",\"BodyText\": "+BodyText+",\"FolderId\":0,\"Name\": "+Name+",\"Subject\": "+Subject+",\"Tracking\": "+"all"+"}");
		StringBuffer sbf = new  StringBuffer("{\"BodyHtml\": \"");
		sbf.append(BodyHTML);
		sbf.append("\",\"BodyText\": \"");
		sbf.append(BodyText);
		sbf.append("\",\"FolderId\": 0,\"Name\": \"");
		sbf.append(Name);
		sbf.append("\",\"Subject\": \"");
		sbf.append(Subject);
		sbf.append("\",\"Tracking\": \"all\"}");
		method.setRequestBody(sbf.toString());
		System.out.print("result1===" + result);
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
		log.info("result-->" + result);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException  e) {
			// FIXME: handle exception
		}
		Map<String,Object> map = CommonUtils.json2Map(result);
		
		String newSletterId = map.get("Result")+"";
		Map<String,Object> mapResult=UpdateNewsletter( BodyHTML,  BodyText,  Name,  Subject,newSletterId);
		System.out.println("newSletterId=========================="+newSletterId);
		mapResult.put("Result", newSletterId);
		return mapResult;
	}
	
	*//**
	 * 更新郵件模板
	 * 
	 * @param BodyHTML
	 * @param BodyText
	 * @param Name
	 * @param Subject
	 * @return
	 * @throws Exception
	 *//*
	public static Map<String,Object> UpdateNewsletter(String BodyHTML, String BodyText,
			 String Name, String Subject,String NewsletterID) {
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		StringBuffer u =new StringBuffer("http://se.api.anpasia.com:80/v1/newsletters/");
		u.append(NewsletterID);
		PostMethod method = new PostMethod(u.toString());
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		
	//	 method.setRequestBody("{\"BodyHTML\": "+BodyHTML+",\"BodyText\": "+BodyText+",\"FolderId\":0,\"Name\": "+Name+",\"Subject\": "+Subject+",\"Tracking\": "+"all"+"}");
		StringBuffer sbf = new  StringBuffer("{\"BodyHtml\": \"");
		sbf.append(BodyHTML);
		sbf.append("\",\"BodyText\": \"");
		sbf.append(BodyText);
		sbf.append("\",\"FolderId\": 0,\"Name\": \"");
		sbf.append(Name);
		sbf.append("\",\"Subject\": \"");
		sbf.append(Subject);
		sbf.append("\",\"Tracking\": \"all\"}");
		method.setRequestBody(sbf.toString());
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
		Map<String,Object> map = CommonUtils.json2Map(result);
		try {
			Thread.sleep(1500);//因为邮件服务器接口提供商在更新邮件内容，但是会提前返回一个更新完成的代码，所以此时暂停1.5秒 在继续执行
		} catch (Exception e) {
		}
		return map;
	}

	*//**
	 * 创建邮件maillist
	 * 
	 * @return
	 * @throws Exception
	 *//*
	public static String CreateMailinglist() throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/v1/mailinglists/";
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		Random r = new Random();
		String name = ""+new Date()+r.nextInt(9999);
		StringBuffer sb = new StringBuffer("{\"CharacterSet\": \"utf-8\",\"Description\": \"create by qy\",\"FolderID\": 0,\"FromEmail\": \"");
		sb.append(sendEmail+"\",\"FromName\": \"");
		sb.append(sendName+"\",\"Name\": \"");
		sb.append(name+"\"}");
		method.setRequestBody(sb.toString());
		String result = "";
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
		log.info("result-->" + result);
		return result;
	}

	*//**
	 * 导入客户信息
	 *//*
	@SuppressWarnings("deprecation")
	public static Map<String, String> CreateImportByXml(String[] strs)
			throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		StringBuffer sbf = new StringBuffer(
				"http://se.api.anpasia.com:80/v1/import/mailinglist/");
		String reJson = CreateMailinglist();
		String mailingListId = CommonUtils.json2Map(reJson).get("Result") + "";
		sbf.append(mailingListId + "/demographicmapping/true");
		PostMethod method = new PostMethod(sbf.toString());
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		StringBuffer sb = new StringBuffer("{\"XmlData\":\"  <Subscribers>     ");
		for(int i=0;i<strs.length;i++){
			sb.append("<Subscriber> <Name>");
			sb.append(i+"</Name> <Email>");
			sb.append(strs[i]+"</Email><DemographicData>");
			//sb.append("<Demographic mapTo=\"Age\">29</Demographic><Demographic mapTo=\"Country\">U.S.A</Demographic>");
			sb.append("</DemographicData> </Subscriber>");
		}
		sb.append("</Subscribers> \"}");
		method.setRequestBody(sb.toString());
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			try { Thread.sleep ( 1500 ) ;
			} catch (InterruptedException ie){}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("CreateImportByXml result-->" + result);
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("result", result);
		returnMap.put("mailingListId", mailingListId);
		getCounts(mailingListId);
		return returnMap;
	}
	
	private static String getCounts(String MailinglistID){
		String strs="";
		HttpClient httpClient = HttpUtils.createHttpClient();
		StringBuffer u = new StringBuffer("http://se.api.anpasia.com/v1/mailinglists/");
        u.append(MailinglistID+"/subscriptions/count");
		GetMethod method = new GetMethod(u.toString());
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		String result = "";
		int i=0;
		String counts="0";
		while(counts.equals("0")){
			i++;
			if(i>10){
				log.info("------------此处调用邮件列表接口失败！！！！！！！一直不能返回所需的值");
				break;
			}
		try {
			Thread.sleep(2*1000);
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			if((result.contains("Code")&&result.contains("Message")&&result.contains("Result"))){
			Map<String,Object> resMap = CommonUtils.json2Map(result);
			counts =resMap.get("Result")+"";
			System.out.println("counts================"+counts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("result-->" + result);
		
		}
		return strs;
		
	}

	*//**
	 * 获得所有邮件列表  
	 * 
	 * @return
	 * @throws Exception
	 *//*
	public static List<Yjnr> getAllMailList() {
		List<Yjnr> list = new ArrayList<Yjnr>();
		String result1="";
		try {
			result1 = GetAllMailingLists();
		} catch (Exception e1) {
			// FIXME Auto-generated catch block
			e1.printStackTrace();
		}//获得返回值
		Map<String,Object> resMap1 = CommonUtils.json2Map(result1);
		String dataUrl1 = "";
		if (null != resMap1.get("Result")) {
		JSONObject	json1 = (JSONObject) resMap1.get("Result");//获得_status的URL
	//	Map map1 = CommonUtils.json2Map(str1);
		dataUrl1=(String)json1.getString("PollURL");
		}
		String result2 = "";//访问_status的URL后的返回值   是一个jason字符串
		String dataUrl2 = "";
		String str="";
				if (!StringUtils.isEmpty(dataUrl1)) {
			try {
				result2 = CommonUtils.getReturnDataByUrl(dataUrl1);
			} catch (UnsupportedEncodingException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}//访问_status的URL
			Map<String,Object> resMap2 = CommonUtils.json2Map(result2);
			if (null != resMap2.get("DataUrl")) {
			dataUrl2 =(String)resMap2.get("DataUrl");
			try {
				str = CommonUtils.getReturnDataByUrl(dataUrl2);
			} catch (UnsupportedEncodingException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
			//JSONObject jsob = JSONObject.fromObject(str);
			@SuppressWarnings("unchecked")
			Collection<MorphDynaBean> listColl = JSONArray.toCollection(JSONArray.fromObject(str));
			for(MorphDynaBean obj :listColl){
				Yjnr yjnr = new Yjnr();
				String name = (String)obj.get("Name");
				yjnr.setName(name);
				String id =obj.get("Id")+"";
				yjnr.setId(id);
				String folderId =obj.get("FolderId")+"";
				yjnr.setFolderId(folderId);
				String created =(String)obj.get("Created");
				yjnr.setCreated(created);
				String updated =(String)obj.get("Updated");
				yjnr.setUpdated(updated);
				String subject =(String)obj.get("Subject");
				yjnr.setSubject(subject);
				String content =(String)obj.get("Content");
				yjnr.setContent(content);
				String tracking =(String)obj.get("Tracking");
				yjnr.setTracking(tracking);
				list.add(yjnr);
			}
			}
		}
	
		
		
		return list;
	}


	
	*//**
	 * 根据ID获取邮件内容
	 * @param MailinglistID
	 * @return
	 *//*
	public static Yjnr GetMailingListDetails(String NewsletterId){
		HttpClient httpClient = HttpUtils.createHttpClient();
		StringBuffer u = new StringBuffer("http://se.api.anpasia.com:80/v1/newsletters/");
        u.append(NewsletterId);
		GetMethod method = new GetMethod(u.toString());
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		String result = "";
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
		log.info("result-->" + result);
		
		Map<String,Object> resMap = CommonUtils.json2Map(CommonUtils.json2Map(result).get("Result")+"");
		Yjnr yjnr = new Yjnr();
		if(null!=resMap){
			String str=resMap.get("BodyHtml")+"";
			str=str.replace("\n", "");
			str=str.replace(" ", "");
			str=str.replace("\t", "");
			str=str.replace("\r\n", "");
			yjnr.setBodyHtml(str);
			yjnr.setBodyText(resMap.get("BodyText")+"");
			yjnr.setCreated(resMap.get("Created")+"");
			yjnr.setFolderId(resMap.get("FolderId")+"");
			yjnr.setId(resMap.get("Id")+"");
			yjnr.setName(resMap.get("Bame")+"");
			yjnr.setSubject(resMap.get("Subject")+"");
			yjnr.setTracking(resMap.get("Tracking")+"");
			yjnr.setUpdated(resMap.get("Updated")+"");
		}
		
		
		return yjnr;
	}

	*//**
	 * 
	 * @param MailingListIds 用户列表ID
	 * @param NewsletterId  邮件ID
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("deprecation")
	public static Map<String,Object> SendNewsletter(String[] strs,String NewsletterId) throws Exception {
		Map<String,String> map=CreateImportByXml(strs);
		String MailingListIds =map.get("mailingListId");
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/sendqueues/v2/";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		//String sendDateStr = DateUtil.fmtyyyyMMddHHmmss(new Date());
		StringBuffer sbf = new StringBuffer("{\"CampaignId\": \"B5492142-FE3A-E311-A1AA-005056A85A0A\",\"FilterId\": 0,\"FromEmail\": \"");
		sbf.append(sendEmail+"\",\"FromName\": \"");
		sbf.append(sendName+"\",\"MailingListIds\": [");
		sbf.append(MailingListIds+"],\"NewsletterId\": \"");
		sbf.append(NewsletterId+"\",\"SendDate\":\"\"}");
		method.setRequestBody(sbf.toString());
		String result = "";
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
		log.info("result-->" + result);
		Map<String,Object> mapResult = CommonUtils.json2Map(result);
		return mapResult;
	}

	
	*//**
	 * 获取所有的邮件列表---返回值未解析
	 *//*
	private static String GetAllMailingLists() throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/v1/newsletters/all";
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5581.16299 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		String result = "";
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
		JSONObject json = JSONObject.fromObject(result);
		log.info("result-->" + json);
		return result;
	}
	
	
	public static void main(String[] args) {
		getAllMailList();
	}

		//*************************** 以下为方法一 ***************************

		SendEmailWs impl = new SendEmailWs();
		//impl.CreateSendNewsletter("ceshibodyhtml22","ceshibodytest22", "mytestEmail22222", "測試22");
		//	impl.UpdateNewsletter("ceshibodyhtml333","ceshibodytest33", "mytestEmail333", "測試333","122831");
		try {
		//	impl.getAllMailList();//邮件 id 119107  119305
			String[] strs={"jatisme1989@gmail.com","jatisme20151@gmail.com","jatisme2015@gmail.com"};
			User user = new User();
			strs();
			user.setName("jat");
			list.add(user);
			User user1 = new User();
			user1.setEmail("jatisme20151@gmail.com");
			user1.setName("jat1");
			list.add(user1);
			User user2 = new User();
			user2.setEmail("jatisme2015@gmail.com");
			user2.setName("jat2");
			list.add(user2);
			impl.CreateImportByXml(strs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
 			e.printStackTrace();
		}
		JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
		factoryBean.setAddress("http://localhost:9000/yjfsWebS");
		factoryBean.setServiceClass(YjfsServiceInf.class);
		factoryBean.setServiceBean(impl);
		factoryBean.getInInterceptors().add(new LoggingInInterceptor());
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		factoryBean.create();

		*//*************************** 以下为方法二 ****************************//*

		
		/* * Server impl=new Server(); String
		 * address="http://localhost:9000/hello"; Endpoint.publish(address,
		 * impl);
		 
	}

	
	public static String sendEmails(String sjrs,String yjnr,String zt){
		String str="邮件发送成功！";
		String[] sjr=sjrs.split(","); 
		Map<String,Object> map = new HashMap<String,Object>();
		yjnr=yjnr.replace("\n", "<br/>");
		yjnr=yjnr.replace(" ", "&nbsp;");
		yjnr=yjnr.replace("\t", "&nbsp;&nbsp;");
		yjnr=yjnr.replaceAll("\"", "\\\\\"");
		StringBuffer sbf = new StringBuffer("<html><head><title></title></head><body><div>");
		sbf.append("<div  style='float:right'>如果不能打开邮件，请 "+"<a href='##WebVersion##'>点击这里</a></div><br/>");
		sbf.append("<div style='float:right'><a href='##OptOutAll##'>若要取消訂閱該業務通訊，請按一下此連結</a></div><br/><br/>" );
		sbf.append(yjnr);
		sbf.append("<br/>");
		sbf.append("</div></body></html>");
		map = CreateSendNewsletter(sbf.toString(), "", zt, zt);	
		if(!(map.get("Code")+"").equals("1")){
			str="在邮件服务器创建邮件失败，请联系管理员，或再次尝试！";
			return str;
		}
		String newSletterId = map.get("Result")+"";
		Map<String, Object> mapEmail=new HashMap<String,Object>();
		try {
			mapEmail = SendNewsletter(sjr, newSletterId);
		} catch (Exception e) {
			// FIXME Auto-generated catch block
			str="在邮件服务器发送邮件失败，请联系管理员，或再次尝试！";
			e.printStackTrace();
			return str;
		}
		if(!(mapEmail.get("Code")+"").equals("1")){
			str="在邮件服务器发送邮件失败，请联系管理员，或再次尝试！";
			return str;
		}
		
		return str;
	}
	*/
	
	public static void main(String[] args) {
		sendEmailsByApi("3179063436@qq.com","254541");
	}
	
	/**
	 * 邮件商提供的新的发送邮件的接口
	 * @param emailAddress
	 * @param nr
	 * @return
	 */
	public static String sendEmailsNew(String emailAddress,String nr){
		String str="邮件发送成功！";
		try {
		      MailSenderInfo mailInfo = new MailSenderInfo();   
		      mailInfo.setMailServerHost("45.117.146.10");
		      mailInfo.setMailServerPort("6667");   
		      mailInfo.setValidate(true);   
		      mailInfo.setUserName("jat");   
		      mailInfo.setPassword("admin_!QAZ");//您的邮箱密码   
		      mailInfo.setFromAddress("service@e6877.com");   
		      mailInfo.setToAddress(emailAddress);   
		      mailInfo.setSubject("密码找回");   
		      mailInfo.setContent("您好，您在【龙都国际】的最新密码为："+nr+"，请您使用最新密码登陆并进行修改，谢谢！");   
		         //这个类主要来发送邮件  
		      SimpleMailSender sms = new SimpleMailSender();  
		      if(sms.sendTextMail(mailInfo)){	//发送文体格式
		    	  str = "邮件已发送，请查收！";
		      }else{
		    	  str = "邮件发送失败，如有需要请与客服联系！s";
		      }
		} catch (Exception e) {
			// TODO: handle exceptionja
			  return "邮件发送失败，如有需要请与客服联系！";
		}
		
		return str;
		/*String projectId="778";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = " http://se.api.anpasia.com/v1/transactional/projects/"+projectId+"/sendEmail?sendDate=";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5597.27082 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		//String sendDateStr = DateUtil.fmtyyyyMMddHHmmss(new Date());
		StringBuffer sbf = new StringBuffer("{\"Attachments\":[],\"Email\":\"");
		sbf.append(emailAddress+"\",\"Name\": \"\",");
		sbf.append("\"Format\": \"html\",");
		sbf.append(" \"ExternalId\": null,");
		sbf.append(" \"CountryCode\": null,");
		sbf.append(" \"PhoneNumber\": null,");
		sbf.append("\"DemDataFields\": [{\"Key\": \"RandomCode\",");
		sbf.append("\"Value\":\""+nr+"\"}], \"DDHtml\":null}");
	
		method.setRequestBody(sbf.toString());
		String result = "";
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
		log.info("result-->" + result);
		Map<String,Object> mapResult = CommonUtils.json2Map(result);
		if(mapResult.get("Code").toString().equals("1")){
			return str;
		}else{
			return "邮件发送失败";
		}
		*/
	}
	/**
	 * 邮件商提供的新的发送邮件的接口
	 * @param emailAddress
	 * @param nr
	 * @return
	 */
	public static String sendEmailsNewForApp(String emailAddress,String nr){
		
		/*	
		String str="邮件发送成功！";
		try {
			WebServiceCaller ws =new WebServiceCaller();
			str=ws.sendEmail("您好，您在【U乐娱乐城】的最新密码为："+nr+"，请您使用最新密码登陆并进行修改，谢谢！", "密码找回", emailAddress);
		} catch (Exception e) {
			return "邮件发送失败，如有需要请直接联系客服！";
		}
		return str;*/
		
		
		
		String str = null;
		try {
			MailSenderInfo mailInfo = new MailSenderInfo();   
			mailInfo.setMailServerHost("45.117.146.10");
			mailInfo.setMailServerPort("6667");   
			mailInfo.setValidate(true);   
			long a = new Date().getTime();
			if(a%2==0)
			{
				/* mailInfo.setUserName("service@qy015.com"); 
			       mailInfo.setFromAddress("service@qy015.com");   */
				mailInfo.setUserName("jat"); 
				mailInfo.setFromAddress("service@qy015.com");
			}/*else if(a%3 == 1){
	            	mailInfo.setUserName("service@qy771.com");  
	    		    mailInfo.setFromAddress("service@qy771.com");   
	            }*/else{
	            	mailInfo.setUserName("jat"); 
	            	mailInfo.setFromAddress("service@qy015.com");
	            }
			mailInfo.setPassword("admin_!QAZ");//您的邮箱密码   
			mailInfo.setToAddress(emailAddress);   
			mailInfo.setSubject("密码找回");   
			mailInfo.setContent("您好，您在【U乐娱乐城】使用邮箱找回密码功能，你的验证码为："+nr+"");   
			//这个类主要来发送邮件  
			SimpleMailSender sms = new SimpleMailSender();  
			if(sms.sendTextMail(mailInfo)){	//发送文体格式
				//str = "邮件已发送，请查收！";
			}else{
				str = "邮件发送失败，如有需要请与客服联系！s";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "邮件发送失败，如有需要请与客服联系！";
		}
		return str;
		
		/*	String projectId="776";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = " http://se.api.anpasia.com/v1/transactional/projects/"+projectId+"/sendEmail?sendDate=";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + new BASE64Encoder().encode(KEY.getBytes()));
		method.setRequestHeader("Accept", "application/json");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate");
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader(
				"User-Agent",
				"ApsisRestClient- ver1.0.5597.27082 (Microsoft Windows NT 6.1.7601 Service Pack 1) ");
		//String sendDateStr = DateUtil.fmtyyyyMMddHHmmss(new Date());
		StringBuffer sbf = new StringBuffer("{\"Attachments\":[],\"Email\":\"");
		sbf.append(emailAddress+"\",\"Name\": \"\",");
		sbf.append("\"Format\": \"html\",");
		sbf.append(" \"ExternalId\": null,");
		sbf.append(" \"CountryCode\": null,");
		sbf.append(" \"PhoneNumber\": null,");
		sbf.append("\"DemDataFields\": [{\"Key\": \"RandomCode\",");
		sbf.append("\"Value\":\""+nr+"\"}], \"DDHtml\":null}");
	
		method.setRequestBody(sbf.toString());
		String result = "";
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
		log.info("result-->" + result);
		Map<String,Object> mapResult = CommonUtils.json2Map(result);
		if(mapResult.get("Code").toString().equals("1")){
			return str;
		}else{
			return "邮件发送失败";
		}
		 */	
	}

	public static String sendEmailsByApi(String emailAddress,String nr) {
		final String url = "http://api.sendcloud.net/apiv2/mail/send";
		final String apiUser = "PassWordService";
		final String apiKey = "2zvWQhrD5wEs4QbI";
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("apiUser", apiUser);
		method.setParameter("apiKey", apiKey);
		method.setParameter("from", "password@find.servicepasswd.com");
		method.setParameter("fromName", "");
		method.setParameter("to", emailAddress);
		method.setParameter("subject", "密码找回");
		method.setParameter("html", "您好，您的新密码为："+nr+"，请您使用最新密码登陆并进行修改，谢谢");
		String str="邮件发送成功！";
		try {
			httpClient.executeMethod(method);
			method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sendEmailsByApi异常："+e.getMessage());
			return "邮件发送失败，如有需要请与客服联系！";
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return str;
	}
	public static String sendEmailsByApiGetAcc(String emailAddress,String nr) {
		final String url = "http://api.sendcloud.net/apiv2/mail/send";
		final String apiUser = "PassWordService";
		final String apiKey = "2zvWQhrD5wEs4QbI";
		HttpClient httpClient = dfh.utils.sendemail.HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("apiUser", apiUser);
		method.setParameter("apiKey", apiKey);
		method.setParameter("from", "password@find.servicepasswd.com");
		method.setParameter("fromName", "");
		method.setParameter("to", emailAddress);
		method.setParameter("subject", "账号找回");
		method.setParameter("html", nr);
		String str="邮件发送成功！";
		try {
			httpClient.executeMethod(method);
			method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sendEmailsByApi异常："+e.getMessage());
			return "邮件发送失败，如有需要请与客服联系！";
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return str;
	}


}