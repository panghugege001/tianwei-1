package dfh.action.customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.action.SubActionSupport;
import dfh.action.vo.MessageVo;
import dfh.action.vo.Messages;
import dfh.model.Guestbook;
import dfh.model.ReadedMsg;
import dfh.model.TopicInfo;
import dfh.model.Users;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.service.interfaces.GuestBookService;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;

public class StationLettersAction extends SubActionSupport {

	private static final long serialVersionUID = 1851783780860503943L;
	
	private static ObjectMapper mapper = new ObjectMapper();

	private GuestBookService guestBookService;

	private Integer unreadCount;

	private Integer countAll;
	
	private Integer countMessage;

	private Integer page;

	private Integer count = 10;

	private List<Guestbook> list = new ArrayList<Guestbook>();

	private Integer countAllTwo;

	private Integer countPage;
	
	private String game;

	Guestbook guestbook = new Guestbook();
	
	
	private String cpuid;
	private String ourDeviceId;
	
	private Integer msgID;
	
	private String platform;

	public String getGuestbookCount() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject(0);
			return null;
		}
		try {
			Integer countBook = guestBookService.getGuestbookCount(user.getLoginname());
			if (countBook != null) {
				GsonUtil.GsonObject(countBook);
			} else {
				GsonUtil.GsonObject(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(0);
		}
		return null;

	}
	
	/**
	 * 未读站内信的数量
	 * @return
	 */
//	public void getGuestbookCountNew(){
//		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//		if (user == null) {
//			GsonUtil.GsonObject(0);
//		}
//		try {
//			Integer agent = user.getRole().equalsIgnoreCase("AGENT")?8:user.getLevel();
//			Integer countBook = guestBookService.getUnReadMessageCount(user.getLoginname(), agent);
//			if (countBook != null) {
//				GsonUtil.GsonObject(countBook);
//			} else {
//				GsonUtil.GsonObject(0);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			GsonUtil.GsonObject(0);
//		}
//	}
	/**
	 * 获取站内信
	 * @throws IOException 
	 */
//	public void getMessageByUser() throws IOException{
//		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//		// 检测用户是否登录
//		if (user == null) {
//			return;
//		}
//		Integer agent = user.getRole().equalsIgnoreCase("AGENT")?8:user.getLevel();
//		Messages msg = new Messages();
//		msg.setPageNo(page);
//		msg.setPageSize(count);
//		guestBookService.getMessagesByUser(user.getLoginname(), agent, msg);
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("application/json; charset=utf-8");
//		response.getWriter().write(JSONObject.fromObject(msg).toString());
//	}
	
	/**
	 * 阅读站内信
	 */
//	public void readMsg(){
//		try {
//			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//			// 检测用户是否登录
//			if (user == null) {
//				return;
//			}
//			
//			Guestbook msg = (Guestbook) guestBookService.get(Guestbook.class, msgID);
//			if(msg == null){return;}
//			
//			if(msg.getUserstatus().intValue()==0 && msg.getUsername() != null && msg.getUsername().equals(user.getLoginname())){
//				//个人未读站内信，标记为已读
//				msg.setUserstatus(1);
//				guestBookService.update(msg);
//			}else if(msg.getUsername() == null){
//				//群发站内信，记录已读
//				if(!guestBookService.isRead4PublicMsg(msgID, user.getLoginname())){
//					ReadedMsg readedMsg = new ReadedMsg(msgID, user.getLoginname(), new Date());
//					guestBookService.save(readedMsg);
//				}
//			}
//			MessageVo msgVo = new MessageVo();
//			msgVo.setId(msgID);
//			msgVo.setTitle(msg.getTitle());
//			msgVo.setContent(msg.getContent());
//			msgVo.setCreateDate(DateUtil.formatDateForStandard(msg.getCreatedate()));
//			
//			HttpServletResponse response = ServletActionContext.getResponse();
//			response.setContentType("application/json; charset=utf-8");
//			response.getWriter().write(JSONObject.fromObject(msgVo).toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 删除站内信
	 * 只能删除本人名下的非群发站内信
	 * @throws IOException 
	 */
//	public void deleteMsg() throws IOException{
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("application/json; charset=utf-8");
//		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//		// 检测用户是否登录
//		if (user == null) {
//			response.getWriter().write("请登录后继续操作");
//		}else{
//			Guestbook msg = (Guestbook) guestBookService.get(Guestbook.class, msgID);
//			if(msg.getUsername() != null){
//				if((msg.getUsername().equals(user.getLoginname()) && guestBookService.getSubMessage(msg.getId()).intValue()==0) 
//						|| (msg.getReferenceId() != null && ((Guestbook) guestBookService.get(Guestbook.class, msg.getReferenceId())).getUsername().equals(user.getLoginname()))){
//					//存在回复的原始扎内信，不能删除
//					guestBookService.delete(Guestbook.class, msgID);
//					response.getWriter().write("站内信已删除");
//				}else{
//					response.getWriter().write("该站内信有回复，不允许删除");
//				}
//			}else{
//				response.getWriter().write("非法操作");
//			}
//		}
//	}

	/**
	 * 前台发送站内信
	 * @throws IOException 
	 */
//	public void saveBookNew() throws IOException {
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("application/json; charset=utf-8");
//		try {
//			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//			// 检测用户是否登录
//			if (user == null) {
//				response.getWriter().write("请登录后继续操作");
//			}else{
//				String ip = getIp();
//				if (ip == null || ip.equals("")) {
//					ip = "127.0.0.1";
//				}
//				guestbook = guestBookService.saveBookDate(user.getLoginname(), guestbook.getId(), guestbook.getTitle(), guestbook.getContent(), ip);
//				response.getWriter().write("站内信已发出");
//			}
//		} catch (Exception e) {
//			response.getWriter().write("发送失败");
//		}
//	}
	
	public String leftBook() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				return null;
			}
			unreadCount = guestBookService.getGuestbookCount(user.getLoginname());
			if (unreadCount == null) {
				unreadCount = 0;
			}
			countAll = guestBookService.getGuestbookCountAll(user.getLoginname());
			if (countAll == null) {
				countAll = 0;
			}
			Integer agent=1;
			if(user.getRole().equals("MONEY_CUSTOMER")){
				agent=user.getLevel();
			}else{
				agent=8;
			}
			countMessage=guestBookService.getGuestbookCountMessage(agent);
			if(countMessage==null){
				countMessage = 0;
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 站内信后台
	 * 
	 * @return
	 */
	public String bookHome() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return ERROR;
		}
		return INPUT;
	}

	// 全部信息
	public String bookfindAll() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return null;
			}
			if (page == null) {
				page = 1;
			}
			countAllTwo = guestBookService.getGuestbookListCount(user.getLoginname(), null);
			if (countAllTwo == null) {
				countAllTwo = 0;
			}
			if (countAllTwo > 0) {
				list = guestBookService.getGuestbookList(user.getLoginname(), page, count, null, null);
			}
			countPage = countAllTwo / count;
			if (countAllTwo % count > 0) {
				countPage++;
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	public String bookfindNoReadAll() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return null;
			}
			if (page == null) {
				page = 1;
			}
			countAllTwo = guestBookService.getGuestbookListCount(user.getLoginname(), 1);
			if (countAllTwo == null) {
				countAllTwo = 0;
			}
			if (countAllTwo > 0) {
				list = guestBookService.getGuestbookList(user.getLoginname(), page, count, 1, null) ;
			}
			countPage = countAllTwo / count;
			if (countAllTwo % count > 0) {
				countPage++;
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}
	
	// 全部信息
	public String bookfindMessage() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return null;
			}
			if (page == null) {
				page = 1;
			}
			Integer agent=1;
			if(user.getRole().equals("MONEY_CUSTOMER")){
				agent=user.getLevel();
			}else{
				agent=8;
			}
			countAllTwo=guestBookService.getGuestbookCountMessage(agent);
			if (countAllTwo == null) {
				countAllTwo = 0;
			}
			if (countAllTwo > 0) {
				list = guestBookService.getGuestbookMessageList(user.getLoginname(), page, count, null, agent);
			}
			countPage = countAllTwo / count;
			if (countAllTwo % count > 0) {
				countPage++;
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	public String bookRead() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return null;
			}
			guestbook = guestBookService.getGuestbook(guestbook.getId());
			if (guestbook != null) {
				list = guestBookService.getGuestbookList(null, null, null, null, guestbook.getId());
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	public String saveBook() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return null;
			}
			String ip = getIp();
			if (ip == null || ip.equals("")) {
				ip = "127.0.0.1";
			}
			
			guestbook = guestBookService.saveGuestbook(user.getLoginname(), guestbook.getId(), guestbook.getContent(), ip); 
			if (guestbook != null) {
				list = guestBookService.getGuestbookList(null, null, null, null, guestbook.getId());
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

//	public String saveBookDate() {
//		try {
//			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
//			// 检测用户是否登录
//			if (user == null) {
//				return null;
//			}
//			String ip = getIp();
//			if (ip == null || ip.equals("")) {
//				ip = "127.0.0.1";
//			}
//			guestbook = guestBookService.saveBookDate(user.getLoginname(), guestbook.getId(), guestbook.getTitle(), guestbook.getContent(), ip);
//			if (guestbook != null) {
//				list = guestBookService.getGuestbookList( null, null, null, null, guestbook.getId());
//			}
//			return INPUT;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return INPUT;
//		}
//	}

	public String saveBookPage() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return null;
		}
		return INPUT;
	}
	
	public String skyPage() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return null;
		}
		if(game==null || game.equals("")){
			return null;
		}
		return INPUT;
	}

	/**
	 * 用户访问网站的时候把cpuid信息放入到session中
	 * @return
	 */
	public String addcpuid(){
		System.out.println("111111");
		/*String deviceID = null;
		if(null != ourDeviceId){
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword("F9jD9GM4tpZVmhVF");
			deviceID = textEncryptor.decrypt(ourDeviceId);
			getHttpSession().setAttribute(Constants.DEVICE4WEB, ourDeviceId);
			getHttpSession().setAttribute(Constants.OURDEVICE, deviceID);
		}*/
		//System.out.println("用户本次访问放入cpuID:"+cpuid+"||||deviceID="+deviceID);
		//getHttpSession().setAttribute(Constants.CPUID, cpuid);
		return null;
	}
	
	/**
	 * 标记移动设备设备ID
	 */
	public void signDevice(){
		if(StringUtils.isNotBlank(cpuid)){
			getHttpSession().setAttribute(Constants.mobileDeviceID, cpuid);
			getHttpSession().setAttribute(Constants.mobilePlatform, platform);
		}
	}
	
	public Integer getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(Integer unreadCount) {
		this.unreadCount = unreadCount;
	}

	public Integer getCountAll() {
		return countAll;
	}

	public void setCountAll(Integer countAll) {
		this.countAll = countAll;
	}

	public List<Guestbook> getList() {
		return list;
	}

	public void setList(List<Guestbook> list) {
		this.list = list;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCountAllTwo() {
		return countAllTwo;
	}

	public void setCountAllTwo(Integer countAllTwo) {
		this.countAllTwo = countAllTwo;
	}

	public Integer getCountPage() {
		return countPage;
	}

	public void setCountPage(Integer countPage) {
		this.countPage = countPage;
	}

	public Guestbook getGuestbook() {
		return guestbook;
	}

	public void setGuestbook(Guestbook guestbook) {
		this.guestbook = guestbook;
	}

	public GuestBookService getGuestBookService() {
		return guestBookService;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Integer getCountMessage() {
		return countMessage;
	}

	public void setCountMessage(Integer countMessage) {
		this.countMessage = countMessage;
	}

	public String getCpuid() {
		return cpuid;
	}

	public void setCpuid(String cpuid) {
		this.cpuid = cpuid;
	}

	public String getOurDeviceId() {
		return ourDeviceId;
	}

	public void setOurDeviceId(String ourDeviceId) {
		this.ourDeviceId = ourDeviceId;
	}

	public Integer getMsgID() {
		return msgID;
	}

	public void setMsgID(Integer msgID) {
		this.msgID = msgID;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	
	public String saveBookDate() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return null;
			} else {

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("loginName", user.getLoginname());
				paramMap.put("title", guestbook.getTitle());
				paramMap.put("content", guestbook.getContent());
				String ip = getIp();
				if (ip == null || ip.equals("")) {
					ip = "127.0.0.1";
				}
				paramMap.put("ipAddress", ip);
				String str = mapper.writeValueAsString(paramMap);

				String requestJson = app.util.AESUtil.getInstance().encrypt(str);

				String url = Configuration.getInstance().getValue("middleservice.url");
				url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

				HttpClient httpClient = new HttpClient();

				HttpClientParams params = new HttpClientParams();
				params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
				params.setParameter("http.protocol.content-charset", "UTF-8");
				params.setParameter("http.socket.timeout", 50000);

				httpClient.setParams(params);

				PostMethod method = new PostMethod(url + "/topic/saveTopic");
				method.setRequestHeader("Connection", "close");
				method.setParameter("requestData", requestJson);

				int statusCode = httpClient.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					return null;
				} else {
					byte[] responseBody = method.getResponseBody();
					String responseString = new String(responseBody);

					Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
					String responseData = resultMap.get("responseData");
					responseData = app.util.AESUtil.getInstance().decrypt(responseData);

					resultMap = mapper.readValue(responseData, Map.class);

					String result = resultMap.get("data");

					if (StringUtils.isBlank(result) || result.contains("成功")) {
						return INPUT;
					} else {
						return INPUT;
					}
				}
			}
		} catch (Exception e) {
			return INPUT;
		}
	}

	/**
	 * 未读站内信的数量
	 * 
	 * @return
	 */
	public void getGuestbookCountNew() {

		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject(0);
		}
		PostMethod method = null;
		try {
			Integer agent = "AGENT".equalsIgnoreCase(user.getRole()) ? Constants.TOPIC_SEND_TYPE_ALL_AGENT : user.getLevel();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("loginName", user.getLoginname());
			paramMap.put("userNameType", agent);

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			method = new PostMethod(url + "/topic/queryUnReadNum");

			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject("系统繁忙");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				String responseData = resultMap.get("responseData");
				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);

				String countBook = resultMap.get("data");
				if (countBook != null) {
					GsonUtil.GsonObject(countBook);
				} else {
					GsonUtil.GsonObject(0);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(0);
		} finally {
			if (null != method) {
				method.releaseConnection();
			}
		}

		/*
		 * Users user = (Users)
		 * getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID); if (user
		 * == null) { GsonUtil.GsonObject(0); } try { Integer agent =
		 * user.getRole().equalsIgnoreCase("AGENT")?8:user.getLevel(); Integer
		 * countBook =
		 * guestBookService.getUnReadMessageCount(user.getLoginname(), agent);
		 * if (countBook != null) { GsonUtil.GsonObject(countBook); } else {
		 * GsonUtil.GsonObject(0); } } catch (Exception e) {
		 * e.printStackTrace(); GsonUtil.GsonObject(0); }
		 */
	}

	/**
	 * 获取站内信
	 * 
	 * @throws Exception
	 */
	public void getMessageByUser() throws Exception {

		Messages msg = new Messages();

		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginName", user.getLoginname());
		paramMap.put("pageNum", page);
		paramMap.put("pageSize", count);

		String str = mapper.writeValueAsString(paramMap);

		String requestJson = app.util.AESUtil.getInstance().encrypt(str);

		String url = Configuration.getInstance().getValue("middleservice.url");
		url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

		HttpClient httpClient = new HttpClient();

		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 50000);

		httpClient.setParams(params);

		PostMethod method = new PostMethod(url + "/topic/queryList");
		try {
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject("系统繁忙");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				String responseData = resultMap.get("responseData");
				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);

				JSONObject jsonObject = JSONObject.fromObject(resultMap.get("data"));
				msg.setCount(
						Integer.parseInt(null == jsonObject.get("total") ? "0" : jsonObject.get("total").toString()));
				JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));

				List<TopicInfo> list = (List) JSONArray.toCollection(jsonArray, TopicInfo.class);
				for (TopicInfo topicInfo : list) {
					MessageVo msgVo = new MessageVo();
					msgVo.setId(topicInfo.getStatusId());
					msgVo.setTitle(topicInfo.getTitle());
					msgVo.setCreateDate(topicInfo.getCreateTimeStr());
					msgVo.setPrivate(topicInfo.getTopicStatus().toString().equals("0") ? true : false);
					msgVo.setRead(topicInfo.getIsUserRead().toString().equals("0") ? false : true);
					msg.getMsgList().add(msgVo);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(JSONObject.fromObject(msg).toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 阅读站内信
	 */
	public void readMsg() {
		Messages msg = new Messages();
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				return;
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("topicId", msgID);
			paramMap.put("loginName", user.getLoginname());
			paramMap.put("pageNum", page);
			paramMap.put("pageSize", count);

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/topic/queryTopicById");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject("系统繁忙");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				String responseData = resultMap.get("responseData");
				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);

				JSONObject jsonObject = JSONObject.fromObject(resultMap.get("data"));
				msg.setCount(
						Integer.parseInt(null == jsonObject.get("total") ? "0" : jsonObject.get("total").toString()));
				JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));

				List<TopicInfo> list = (List) JSONArray.toCollection(jsonArray, TopicInfo.class);
				/*
				 * for (TopicInfo topicInfo : list) { MessageVo msgVo = new
				 * MessageVo(); msgVo.setId(msgID);
				 * msgVo.setTitle(topicInfo.getTitle());
				 * msgVo.setContent(topicInfo.getContent());
				 * msgVo.setCreateDate(topicInfo.getCreateTimeStr());
				 * msg.getMsgList().add(msgVo); }
				 */

				TopicInfo topicInfo = list.get(0);
				MessageVo msgVo = new MessageVo();
				msgVo.setId(msgID);
				msgVo.setTitle(topicInfo.getTitle());
				msgVo.setContent(topicInfo.getContent());
				msgVo.setCreateDate(topicInfo.getCreateTimeStr());

				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().write(JSONObject.fromObject(msgVo).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除站内信 只能删除本人名下的非群发站内信
	 * 
	 * @throws IOException
	 */
	public void deleteMsg() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			response.getWriter().write("请登录后继续操作");
		} else {

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("topicId", msgID);
			paramMap.put("loginName", user.getLoginname());

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/topic/deleteTopicById");
			try {

				method.setRequestHeader("Connection", "close");
				method.setParameter("requestData", requestJson);

				int statusCode = httpClient.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					GsonUtil.GsonObject("系统繁忙");
				} else {
					byte[] responseBody = method.getResponseBody();
					String responseString = new String(responseBody);

					Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
					String responseData = resultMap.get("responseData");
					responseData = app.util.AESUtil.getInstance().decrypt(responseData);

					resultMap = mapper.readValue(responseData, Map.class);

					String result = resultMap.get("data");

					if (StringUtils.isBlank(result) || result.contains("成功")) {
						response.getWriter().write("站内信已删除");
					} else {
						response.getWriter().write(result);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				method.releaseConnection();
			}

		}
	}

	/**
	 * 前台发送站内信
	 * 
	 * @throws IOException
	 */
	public void saveBookNew() throws IOException {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				response.getWriter().write("请登录后继续操作");
			} else {

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("loginName", user.getLoginname());
				paramMap.put("title", guestbook.getTitle());
				paramMap.put("content", guestbook.getContent());
				String ip = getIp();
				if (ip == null || ip.equals("")) {
					ip = "127.0.0.1";
				}
				paramMap.put("ipAddress", ip);
				String str = mapper.writeValueAsString(paramMap);

				String requestJson = app.util.AESUtil.getInstance().encrypt(str);

				String url = Configuration.getInstance().getValue("middleservice.url");
				url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

				HttpClient httpClient = new HttpClient();

				HttpClientParams params = new HttpClientParams();
				params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
				params.setParameter("http.protocol.content-charset", "UTF-8");
				params.setParameter("http.socket.timeout", 50000);

				httpClient.setParams(params);

				PostMethod method = new PostMethod(url + "/topic/saveTopic");
				try {

					method.setRequestHeader("Connection", "close");
					method.setParameter("requestData", requestJson);

					int statusCode = httpClient.executeMethod(method);

					if (statusCode != HttpStatus.SC_OK) {
						GsonUtil.GsonObject("系统繁忙");
					} else {
						byte[] responseBody = method.getResponseBody();
						String responseString = new String(responseBody);

						Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
						String responseData = resultMap.get("responseData");
						responseData = app.util.AESUtil.getInstance().decrypt(responseData);

						resultMap = mapper.readValue(responseData, Map.class);

						String result = resultMap.get("data");

						if (StringUtils.isBlank(result) || result.contains("成功")) {
							response.getWriter().write("站内信已发出");
						} else {
							response.getWriter().write(result);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					method.releaseConnection();
				}
			}
		} catch (Exception e) {
			response.getWriter().write("发送失败");
		}

		/*
		 * HttpServletResponse response = ServletActionContext.getResponse();
		 * response.setContentType("application/json; charset=utf-8"); try {
		 * Users user = (Users)
		 * getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID); //
		 * 检测用户是否登录 if (user == null) { response.getWriter().write("请登录后继续操作");
		 * }else{ String ip = getIp(); if (ip == null || ip.equals("")) { ip =
		 * "127.0.0.1"; } guestbook =
		 * guestBookService.saveBookDate(user.getLoginname(), guestbook.getId(),
		 * guestbook.getTitle(), guestbook.getContent(), ip);
		 * response.getWriter().write("站内信已发出"); } } catch (Exception e) {
		 * response.getWriter().write("发送失败"); }
		 */
	}
	
}
