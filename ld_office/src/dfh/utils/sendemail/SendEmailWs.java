package dfh.utils.sendemail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.encoding.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.focussend.FocusEmail;
import com.focussend.FocusReceiver;
import com.focussend.FocusSendWebServiceSoapProxy;
import com.focussend.FocusTask;
import com.focussend.FocusUser;

import dfh.model.SystemConfig;
import dfh.model.Yjnr;
import dfh.security.EncryptionUtil;




public class SendEmailWs {
	/**
	 * 
	 */

	private static Log log = LogFactory.getLog(CommonUtils.class);
	private static final String KEY ="586102bf-f8b2-4d12-9fcd-45cdfa662e29";//key

	

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

	/**
	 * 创建文件夹----暂时不用
	 */
	public static String CreateFolder() {
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com/v1/folder/";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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

	/**
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
	 */
	public static Map<String,Object> CreateSendNewsletter(String BodyHTML, String BodyText,
			 String Name, String Subject) {
		String result = "";
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/v1/newsletters/";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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
		/*try {
			Thread.sleep(1500);
		} catch (InterruptedException  e) {
			// FIXME: handle exception
		}*/
		Map<String,Object> map = CommonUtils.json2Map(result);
		
		String newSletterId = map.get("Result")+"";
		Map<String,Object> mapResult=UpdateNewsletter( BodyHTML,  BodyText,  Name,  Subject,newSletterId);
		System.out.println("newSletterId=========================="+newSletterId);
		mapResult.put("Result", newSletterId);
		return mapResult;
	}
	
	/**
	 * 更新郵件模板
	 * 
	 * @param BodyHTML
	 * @param BodyText
	 * @param Name
	 * @param Subject
	 * @return
	 * @throws Exception
	 */
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
				"Basic " + Base64.encode(KEY.getBytes()));
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

	/**
	 * 创建邮件maillist
	 * 
	 * @return
	 * @throws Exception
	 */
	public  String CreateMailinglist(HibernateTemplate getHibernateTemplate) throws Exception {
		String sendEmail=querySystemConfig(getHibernateTemplate,"type002","001","否");//发件人地址
	    String sendName=querySystemConfig(getHibernateTemplate,"type001","001","否");//发件人名称
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/v1/mailinglists/";
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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

	/**
	 * 导入客户信息
	 */
	@SuppressWarnings("deprecation")
	public  Map<String, String> CreateImportByXml(String[] strs,HibernateTemplate getHibernateTemplate)
			throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		StringBuffer sbf = new StringBuffer(
				"http://se.api.anpasia.com:80/v1/import/mailinglist/");
		String reJson = CreateMailinglist(getHibernateTemplate);
		String mailingListId = CommonUtils.json2Map(reJson).get("Result") + "";
		sbf.append(mailingListId + "/demographicmapping/true");
		PostMethod method = new PostMethod(sbf.toString());
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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
			/*try { Thread.sleep ( 1500 ) ;
			} catch (InterruptedException ie){}*/
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
				"Basic " + Base64.encode(KEY.getBytes()));
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
			if(i>1000){
				log.info("------------此处调用邮件列表接口失败！！！！！！！一直不能返回所需的值");
				break;
			}
		try {
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

	/**
	 * 获得所有邮件列表  
	 * 
	 * @return
	 * @throws Exception
	 */
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
				/*String content =(String)obj.get("Content");
				yjnr.setContent(content);*/
				String tracking =(String)obj.get("Tracking");
				yjnr.setTracking(tracking);
				list.add(yjnr);
			}
			}
		}
	
		
		
		return list;
	}


	
	/**
	 * 根据ID获取邮件内容
	 * @param MailinglistID
	 * @return
	 */
	public static Yjnr GetMailingListDetails(String NewsletterId){
		HttpClient httpClient = HttpUtils.createHttpClient();
		StringBuffer u = new StringBuffer("http://se.api.anpasia.com:80/v1/newsletters/");
        u.append(NewsletterId);
		GetMethod method = new GetMethod(u.toString());
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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

	/**
	 * 
	 * @param MailingListIds 用户列表ID
	 * @param NewsletterId  邮件ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public  Map<String,Object> SendNewsletter(String[] strs,String NewsletterId,HibernateTemplate getHibernateTemplate) throws Exception {
		String sendEmail=querySystemConfig(getHibernateTemplate,"type002","001","否");//发件人地址
	    String sendName=querySystemConfig(getHibernateTemplate,"type001","001","否");//发件人名称
		Map<String,String> map=CreateImportByXml(strs,getHibernateTemplate);
		String MailingListIds =map.get("mailingListId");
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/sendqueues/v2/";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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

	
	/**
	 * 获取所有的邮件列表---返回值未解析
	 */
	private static String GetAllMailingLists() throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://se.api.anpasia.com:80/v1/newsletters/all";
		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");
		method.setRequestHeader("Authorization",
				"Basic " + Base64.encode(KEY.getBytes()));
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
	
	/*	//*************************** 以下为方法一 ****************************//*

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
*/
	
	
	/**
	 * Focus邮件发送加密方法
	 * @param bts
	 * @return
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	/**
	 * focus 邮件群发
	 * @param nr 内容
	 * @param zt 标题
	 * @param sjrs 收件人列表
	 * @return
	 */
	public String sendManyEmailsByFocus(String nr,String zt,String[] sjrs){
		// TODO 自动生成方法存根
		FocusSendWebServiceSoapProxy uid = new FocusSendWebServiceSoapProxy();
		FocusUser fUser = new FocusUser();
		// 平台的登陆名
		fUser.setEmail("bolt88@163.com");
		// 平台的登陆密码,为了保证安全,传输过程中用sha1加密
		String pwd = "aa888888";

		byte[] encodedPwdByte = null;
		String encodedPwd = null;
		try {
			MessageDigest alg = java.security.MessageDigest
					.getInstance("SHA-1");
			alg.update(pwd.getBytes());
			encodedPwdByte = alg.digest();
			encodedPwd = bytes2Hex(encodedPwdByte);
			fUser.setPassword(encodedPwd);
		} catch (NoSuchAlgorithmException ex) {
			return "发送失败";
		}

		// 要发送的内容
		FocusEmail fEmail = new FocusEmail();
		fEmail.setBody(nr);
		fEmail.setIsBodyHtml(true);
		// 一次发送作为一个任务,存储一些其他发送信息
		FocusTask fTask = new FocusTask();
		// 主题
		fTask.setSubject(zt);
		// 任务名,建议保存在数据库中,可以任务名为后期查询报告的标示;任务名不得重复
		fTask.setTaskName(new Date()+"");
		// 回复邮箱
		fTask.setReplyEmail("cs@mk.e68668.com");
		// 发件人姓名
		fTask.setSenderName("龙都娱乐城");
		// 发件人邮箱,强烈要求如果没做spf解析的话,用我们提供的邮箱后缀;如果做了spf解析,可以用你们的
		fTask.setSenderEmail("cs@mk.e68668.com");
		// 发送时间,时间设置在将来即为定时发送
		fTask.setSendDate(Calendar.getInstance());
		// 本次发送的收件人,可以为多个
		
		ArrayList list = new ArrayList();
		for(String str : sjrs){
		FocusReceiver receiver = new FocusReceiver();
		receiver.setEmail(str);
		list.add(receiver);
		}
		int size = list.size();

		String result = null;
		try {
			 //批量发送，当然也可以用来发一封，当FocusReceiver[]数组的长度为1时即为发送1封
			 result= uid.batchSend(fUser, fEmail, fTask,(FocusReceiver[])list.toArray(new FocusReceiver[size]));
			 
		} catch (RemoteException exp) {
			return "发送失败";
		}
		return result;	
	}
	
	
	/**
	 * 發送郵件
	 * 
	 * @return
	 */
	public static boolean sendEmailsBySelf(String sjr,String bt,String nr) {
		 System.out.println("邮件开始：---"+sjr);
		  MailSenderInfo mailInfo = new MailSenderInfo();   
		  mailInfo.setMailServerHost("qy09.net");   
		  mailInfo.setMailServerPort("25");   
		  mailInfo.setValidate(true);   
		  mailInfo.setUserName("qy8@qy09.net");   
		  mailInfo.setPassword("Agsmc168");//您的邮箱密码   
		  mailInfo.setFromAddress("qy8@qy09.net");   
		  System.out.println(sjr+"||"+bt+"||"+nr);
		  mailInfo.setToAddress(sjr);   
		  mailInfo.setSubject(bt);   
		  mailInfo.setContent(nr);   
		     //这个类主要来发送邮件  
		  SimpleMailSender sms = new SimpleMailSender();  
		  boolean b =sms.sendTextMail(mailInfo);//发送文体格式   
		  System.out.println("邮件已发送："+b+"---"+sjr);
		  return b;
		}
	/**
	 * 發送郵件
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	public static void main(String args[]) throws InterruptedException {
		for(int i=0;i<1;i++){
		  MailSenderInfo mailInfo = new MailSenderInfo();   
		  mailInfo.setMailServerHost("qy09.net");   
		  mailInfo.setMailServerPort("25");   
		  mailInfo.setValidate(true);    
		  mailInfo.setUserName("qy8@qy09.net");   //cXkwOEBxeTA5Lm5ldA==
		  mailInfo.setPassword("Agsmc168");//您的邮箱密码   QWdzbWMxNjg=
		  mailInfo.setFromAddress("qy8@qy09.net");   
		  mailInfo.setToAddress("2870490746@qq.com");   
		  mailInfo.setSubject("今天晚上一起出去");   
		  mailInfo.setContent("您好：请问请问千瓦时的幸福三份,里的按时发发发这里的标题似,阿斯达爱上大声地阿斯达.标下次V型从v风格下次V型从v乎.");   
		     //这个类主要来发送邮件  
		  SimpleMailSender sms = new SimpleMailSender();  
		  boolean b =sms.sendTextMail(mailInfo);//发送文体格式   
		  System.out.println(i+""+b);
		 // Thread.sleep(8000);
		}
		/*SendEmailWs sw =new SendEmailWs();
		sw.weixinpay();*/
	}
	
	
	
	public void weixinpay(){
		  try {  
	            URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");  
	            URLConnection con = url.openConnection();  
	            con.setDoOutput(true);  
	            con.setRequestProperty("Pragma:", "no-cache");  
	            con.setRequestProperty("Cache-Control", "no-cache");  
	            con.setRequestProperty("Content-Type", "text/xml");  
	  
	            OutputStreamWriter out = new OutputStreamWriter(con  
	                    .getOutputStream());      
	            String xmlInfo = getXmlInfo();  
	            out.write(new String(xmlInfo.getBytes("UTF-8")));  
	            out.flush();  
	            out.close();  
	            BufferedReader br = new BufferedReader(new InputStreamReader(con  
	                    .getInputStream()));  
	            String line = "";  
	            for (line = br.readLine(); line != null; line = br.readLine()) {  
	                System.out.println(line);  
	            }  
	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}
	
	
	private String getXmlInfo() {  
		String stringA="appid=wx11a108b218fca4e4&attach=aaaaaaaaaaaaaaaaa&body=1111&device_info=WEB&mch_id=1298140901&nonce_str=wx2421b1c4370ecqqb&notify_url=xxxxxxasasdaasdfadf.com&out_trade_no=eweixin_1&product_id=19999099&spbill_create_ip=127.0.0.1&total_fee=1&trade_type=NATIVE";
		String stringSignTemp=stringA+"&key=skJsaolsy8qj8sQlzinlQAZQWEfd8s1p";
		System.out.println("stringSignTemp=="+stringSignTemp);
		String sign=EncryptionUtil.encryptPassword(stringSignTemp);
		//String sign=DigestUtils.md5Hex(stringSignTemp);
		StringBuilder sb = new StringBuilder();  
        sb.append("<xml>");  
        sb.append(" <appid>wx11a108b218fca4e4</appid>");//公众账号ID
        sb.append(" <mch_id>1298140901</mch_id>");  //商户号
        sb.append(" <device_info>WEB</device_info>");  //设备号 网页端'web'
        sb.append(" <nonce_str>wx2421b1c4370ecqqb</nonce_str>");  //随机字符串
        sb.append(" <sign>"+sign+"</sign>");  //数字签名
        sb.append(" <body>1111</body>");  
       // sb.append(" <detail>商品名称明细列表</detail>");  
        sb.append(" <attach>aaaaaaaaaaaaaaaaa</attach>");  //回调的时候原样返回
        sb.append(" <out_trade_no>eweixin_1</out_trade_no>");  
     //   sb.append(" <fee_type></fee_type>");  //货币类型
        sb.append(" <total_fee>1</total_fee>");  //这里单位为分 不能有小数点
        sb.append(" <spbill_create_ip>127.0.0.1</spbill_create_ip>");  
       // sb.append(" <time_start></time_start>");  
      //  sb.append(" <time_expire></time_expire>");  
       // sb.append(" <goods_tag></goods_tag>");  
        
        sb.append(" <notify_url>xxxxxxasasdaasdfadf.com</notify_url>");  //返回通知的url
        sb.append(" <trade_type>NATIVE</trade_type>"); //扫码支付
        sb.append(" <product_id>19999099</product_id>");   //商品id 可以自定义
       // sb.append(" <limit_pay></limit_pay>");  
       // sb.append(" <openid></openid>");  
        sb.append("</xml>");  
        System.out.println("sbf=="+sb.toString());
        return sb.toString();  
    }  
	
}