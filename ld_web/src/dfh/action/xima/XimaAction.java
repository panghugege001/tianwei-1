package dfh.action.xima;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dfh.action.SynchronizedUtil;
import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.model.Users;
import dfh.service.interfaces.IGGameinfoService;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;

public class XimaAction extends ActionSupport implements java.io.Serializable,SessionAware,ServletRequestAware,ServletResponseAware{
	private static final String NAMESPACE="http://webservice.skydragon.dfh";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4973316400846527269L;
	private Double validAmount;	// 有效投注额
	private Double ximaAmount;	// 反水金额
	private Double rate;		// 洗码率
	private String startTime;
	private String endTime;
	private IGGameinfoService gameinfoService;
	private Map<String, Object> session;
	private String errormsg;
	private Logger log=Logger.getLogger(XimaAction.class);
	private List ximaList;
	private int maxRowsno=20;
	private int pageno=1;
	private int totalRowsno=0;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private int totalPageno=0;
	private Double totalValidAmount;// 总有效投注额
	private Double totalXimaAmount;	// 总反水金额
	private String platform; //自助洗码平台
	public HttpSession getHttpSession() {
		return req.getSession();
	}
	
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	

	private Users getUserObject(){
		Users users=(Users)session.get(Constants.SESSION_CUSTOMERID);
		return users;
	}
	
	
	public String execXima(){
		String returnStr="";
		Users user = this.getUserObject();
		if (user==null) {
			GsonUtil.GsonObject("请登录后在继续操作");
			return null;
		}
				
		///***************************begin
		String str = user.getLoginname()+platform ;
		Long nowTiem = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            String keyStr = entry.getKey();
            Long longTime = cacheMap.get(keyStr);
            if (nowTiem - longTime > 1000 * 60) {
                it.remove();
            }
        }
        if (cacheMap.get(str) != null) {
            GsonUtil.GsonObject("60秒内不能重复提交同一平台。");
            return null;
        }
        cacheMap.put(str, nowTiem);
		///***************************end
        returnStr=SynchronizedUtil.getInstance().execXima(user, endTime, startTime, platform, validAmount, ximaAmount, rate);
		GsonUtil.GsonObject(returnStr) ;
		return null;
	}
	public void refreshUserInSession() throws Exception {
		Users customer = getCustomerFromSession();
		Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { customer.getLoginname() }, Users.class);
		getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
		if(customer.getPostcode()==null || user.getPostcode()==null){
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		}
		if(!customer.getPostcode().equals(user.getPostcode())){
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		}
	}
	public Users getCustomerFromSession() throws Exception{
		try {
			return (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}

	/**
	 * 一键反水
	 * @return
	 */
	public  String oneKeyXima(){
		try {
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("清除会话失败", e);
			GsonUtil.GsonObject("请登录后在继续操作");
			return null;
		}
		Users user = this.getUserObject();
		if (user==null) {
			GsonUtil.GsonObject("请登录后在继续操作");
			return null;
		}


		///***************************begin
		String str = user.getLoginname()+"oneKey" ;
		Long nowTiem = System.currentTimeMillis();
		Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Long> entry = it.next();
			String keyStr = entry.getKey();
			Long longTime = cacheMap.get(keyStr);
			if (nowTiem - longTime > 1000 * 60) {
				it.remove();
			}
		}
		if (cacheMap.get(str) != null) {
			GsonUtil.GsonObject("60秒内不能重复提交。");
			return null;
		}
		cacheMap.put(str, nowTiem);

		try {
			String msg = oneKeyXima(user);
			refreshUserInSession();
			JSONObject jsonObj = JSONObject.fromObject(msg);
			GsonUtil.GsonObject(jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("网络繁忙!");
		}


		return null;
	}
	public String oneKeyXima(Users user) throws AxisFault {
		Map<String,Object> resultMap = new HashMap<>();
		String[] plats = {"pttiger","ttg"};
		//反水综合老虎机--execSlotXima
		AutoXima execSlotXima = SynchronizedUtil.getInstance().execSlotXima(user, "slot");
		log.info("slot平台反水:"+execSlotXima.getMessage());
		resultMap.put("slot",execSlotXima.getMessage());

		for (int i = 0; i < plats.length; i++) {
			String resultStr="";
			AutoXima  ximaObject = getXimaObjectByLoginname(user.getLoginname(),plats[i]);
			if(StringUtils.isNotBlank(ximaObject.getMessage())){
				resultStr=ximaObject.getMessage();
			}else {
				String execXimaStr = SynchronizedUtil.getInstance().execXima(user, ximaObject.getEndTimeStr(), ximaObject.getStartTimeStr(), plats[i], ximaObject.getValidAmount(), ximaObject.getXimaAmount(), ximaObject.getRate());
				resultStr=execXimaStr;
			}
			resultMap.put(plats[i]+"",resultStr);
		}

		JSONObject json = JSONObject.fromObject(resultMap);
		return json.toString();

	}
	public    AutoXima getXimaObjectByLoginname(String loginname,String platform) throws AxisFault {
		String endTime = DateUtil.formatDateForStandard(new Date());
		//最后洗码时间和洗码数据
		String	startTime = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
				+ "UserWebService", false), AxisUtil.NAMESPACE, "getXimaEndTime", new Object[] {
				loginname , platform }, String.class);

		AutoXima autoXima = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
				+ "UserWebService", false), AxisUtil.NAMESPACE, "getAutoXimaObject", new Object[] {
				endTime, startTime, loginname , platform }, AutoXima.class);
		log.info(platform+"平台获取反水数据:"+autoXima.getMessage());
		autoXima.setEndTimeStr(endTime);
		autoXima.setStartTimeStr(startTime);

		return autoXima;
	}
	
	
	
	public String getXimaEndTime(){
		try {
			Users user = this.getUserObject();
			if (user==null) {
				GsonUtil.GsonObject("请登录后在继续操作");
				return null;
			}
			String	gameFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "checkGameIsProtect", new Object[] {platform }, String.class);
			if(!gameFlag.equals("1")){
				GsonUtil.GsonObject(platform+"维护中...");
				return null;
			}
			
			String	date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getXimaEndTime", new Object[] {
				user.getLoginname() , platform }, String.class);
			String currentDate = DateUtil.formatDateForStandard(new Date()) ;
			GsonUtil.GsonObject(date+","+currentDate);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("");
			return null;
		}
	}
	
	public String getAutoXimaObjectData(){
		Users user = this.getUserObject();
		if (user==null) {
			GsonUtil.GsonObject(new AutoXima("请登录后在继续操作"));
			return null;
		}
		//return new AutoXima("自助反水程序升级，暂停使用，启用日期，请留意主页公告");
		if (startTime.trim().equals("")||startTime.trim().equals("")||user.getLoginname().trim().equals("")) {
			GsonUtil.GsonObject("所有栏目，都为必填项！\n请重新选择[截止时间]，让系统自动帮您填好必填的栏目内容");
			return null;
		}
		java.text.SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end=null;
		Date start=null;
		try {
			end=sf.parse(endTime);
			start=sf.parse(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject("日期格式有误，请重新输入");
			return null;
		}
		
		if (end.getTime()>new Date().getTime()) {
			end=new Date();
		}
		if(end.getTime()<start.getTime()){
			GsonUtil.GsonObject("截止时间必须大于起始时间");
			return null;
		}
		
//		end=new Date(end.getTime()-60*30*1000); // 得到30分钟前的时间
		AutoXima autoXima = null;
		try {
			autoXima = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getAutoXimaObject", new Object[] { endTime, startTime, user.getLoginname() , platform }, AutoXima.class); 
			GsonUtil.GsonObject(autoXima);
			return null;
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject("本次自助洗码异常，请稍后重试！");
			return null;
		}
	}
	
	
	public String execXimaPt() {
//		this.errormsg="自助反水程序升级，暂停使用，启用日期，请留意主页公告";
//		return "index";
		Users user = this.getUserObject();
		if (user==null) {
			this.errormsg="请登录后在继续操作";
			return "index";
		}
		
		// 检测是否为每天的12点至15点之间，这段时间为系统洗码时间，不能执行自助洗码功能。
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		int hour=c.get(Calendar.HOUR_OF_DAY);
		if (hour>=12&&hour<15) {
			this.errormsg="抱歉，暂不能执行自助洗码操作\\n每天的12点至15点是系统洗码时间";
			return "input";
		}
		
		if (rate==null||startTime==null||endTime==null||ximaAmount==null||validAmount==null) {
			this.errormsg="所有栏目，都为必填项！";
			return "input";
		}
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start=null;
		Date end=null;
		try {
			Date tempDate=yyyy_MM_d.parse(startTime);
			//两种情况：防止页面串改起始时间或者防止提交了自助洗码后，先停留在页面，等自助反水执行后，再重复提交
			String date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getXimaEndPtTime", new Object[] {
				user.getLoginname()}, String.class);
			if(null!=date && !"".equals(date)){
				start=yyyy_MM_d.parse(date);
				if(start.compareTo(tempDate)!=0){
					this.errormsg="不允许的时间间隔";
					return "input";
				}
				end=yyyy_MM_d.parse(endTime);
			}else{
				this.errormsg="本次洗码异常，请重试！";
				return "input";
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.errormsg="日期格式错误，请重新填写";
			return "input";
		}
		
		if (end.getTime()>new Date().getTime()) {
			end=new Date();
		}
//		if (validAmount<=35000) {
//			this.errormsg="有效投注金额小于35000元，无法进行结算";
//			return "input";
//		}
		try {
			AutoXimaReturnVo autoXimaReturnVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "checkSubmitPtXima", new Object[] {
				user.getLoginname()}, AutoXimaReturnVo.class);
			if(null!=autoXimaReturnVo && !autoXimaReturnVo.getB() && null==autoXimaReturnVo.getMsg()){
				String endTime = yyyy_MM_d.format(end);
				String startTime = yyyy_MM_d.format(start);
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), NAMESPACE, "execXimaPt", new Object[] {
					user,endTime,startTime}, String.class);
				if(null!=msg && !"".equals(msg)){
					this.errormsg=msg;
				}else{
					this.errormsg="本次洗码异常，请重试！";
				}
			}else if(null!=autoXimaReturnVo && autoXimaReturnVo.getB() && null!=autoXimaReturnVo.getMsg() 
					&& !"".equals(autoXimaReturnVo.getMsg())){
				this.errormsg=autoXimaReturnVo.getMsg();
			}else{
				this.errormsg="本次洗码异常，请重试！";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("会员执行自助洗码时，出现异常",e);
			this.errormsg="系统异常，请稍候再试或与现在客服取得联系";
		}
		return "input";
	}
	
	public String execAjaxXimaPt() {
//		this.errormsg="自助反水程序升级，暂停使用，启用日期，请留意主页公告";
//		return "index";
		Users user = this.getUserObject();
		if (user==null) {
			this.errormsg="请登录后在继续操作";
			GsonUtil.GsonObject(this.errormsg);
			return null;
		}
		
		// 检测是否为每天的12点至15点之间，这段时间为系统洗码时间，不能执行自助洗码功能。
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		int hour=c.get(Calendar.HOUR_OF_DAY);
		if (hour>=12&&hour<15) {
			this.errormsg="抱歉，暂不能执行自助洗码操作\\n每天的12点至15点是系统洗码时间";
			GsonUtil.GsonObject(this.errormsg);
			return null;
		}
		if (rate==null||startTime==null||endTime==null||ximaAmount==null||validAmount==null) {
			this.errormsg="所有栏目，都为必填项！";
			GsonUtil.GsonObject(this.errormsg);
			return null;
		}
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start=null;
		Date end=null;
		try {
			Date tempDate=yyyy_MM_d.parse(startTime);
			//两种情况：防止页面串改起始时间或者防止提交了自助洗码后，先停留在页面，等自助反水执行后，再重复提交
			String date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getXimaEndPtTime", new Object[] {
				user.getLoginname()}, String.class);
			if(null!=date && !"".equals(date)){
				start=yyyy_MM_d.parse(date);
				if(start.compareTo(tempDate)!=0){
					this.errormsg="不允许的时间间隔";
					GsonUtil.GsonObject(this.errormsg);
					return null;
				}
				end=yyyy_MM_d.parse(endTime);
			}else{
				this.errormsg="本次洗码异常，请重试！";
				GsonUtil.GsonObject(this.errormsg);
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.errormsg="日期格式错误，请重新填写";
			GsonUtil.GsonObject(this.errormsg);
			return null;
		}
		
		if (end.getTime()>new Date().getTime()) {
			end=new Date();
		}
		try {
			AutoXimaReturnVo autoXimaReturnVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "checkSubmitPtXima", new Object[] {
				user.getLoginname()}, AutoXimaReturnVo.class);
			if(null!=autoXimaReturnVo && !autoXimaReturnVo.getB() && null==autoXimaReturnVo.getMsg()){
				String endTime = yyyy_MM_d.format(end);
				String startTime = yyyy_MM_d.format(start);
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), NAMESPACE, "execXimaPt", new Object[] {
					user,endTime,startTime}, String.class);
				if(null!=msg && !"".equals(msg)){
					this.errormsg=msg;
				}else{
					this.errormsg="本次洗码异常，请重试！";
				}
			}else if(null!=autoXimaReturnVo && autoXimaReturnVo.getB() && null!=autoXimaReturnVo.getMsg() 
					&& !"".equals(autoXimaReturnVo.getMsg())){
				this.errormsg=autoXimaReturnVo.getMsg();
			}else{
				this.errormsg="本次洗码异常，请重试！";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("会员执行自助洗码时，出现异常",e);
			this.errormsg="系统异常，请稍候再试或与现在客服取得联系";
		}
		GsonUtil.GsonObject(this.errormsg);
		return null;
	}
	
	public String getXimaEndPtTime() throws Exception{
		Users user = this.getUserObject();
		if (user==null) {
			GsonUtil.GsonObject("loginError");
			return null;
		}
		String date = "";
		try {
			date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getXimaEndPtTime", new Object[] {
				user.getLoginname()}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		if(null!=date && !"".equals(date)){
			GsonUtil.GsonObject(date);
			return null;
		}else{
			GsonUtil.GsonObject("failed");
			return null;
		}
	}
	
	public String searchXimaDetail(){
		Users user = this.getUserObject();
		if (user==null) {
			this.errormsg="请登录后在继续操作";
			return "index";
		}
		
		if (startTime==null||endTime==null) {
			this.errormsg="起始时间和截止时间可不为空";
			return "input";
		}
		
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start=null;
		Date end=null;
		try {
			start=yyyy_MM_d.parse(startTime);
			end=yyyy_MM_d.parse(endTime);
		} catch (Exception e) {
			// TODO: handle exception
			this.errormsg="日期格式错误，请重新填写";
			return "input";
		}
		
		try {
			// 洗码记录汇总
			AutoXima ximaTotalRecord = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getTotalCount", new Object[] {
				user.getLoginname(),startTime,endTime}, AutoXima.class);
			
			if (ximaTotalRecord==null) {
				this.errormsg="您检索的范围内没有反水结算记录，请重新选择检索范围";
				return "input";
			}
			
			// 取得总行数
			totalRowsno=ximaTotalRecord.getTotalCount();
			if (totalRowsno<=0) {
				this.errormsg="抱歉，没有查到任何数据";
			}
			this.totalValidAmount=ximaTotalRecord.getTotalValidAmount();
			this.totalXimaAmount=ximaTotalRecord.getTotalXimaAmount();
			
			// 得到总页数
			totalPageno=(totalRowsno%maxRowsno==0?0:1)+(totalRowsno/maxRowsno);
			
			// 得到展示数据
			ximaList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "searchXimaDetail", new Object[] {
				user.getLoginname(),startTime,endTime,pageno,maxRowsno}, AutoXima.class);
			
			if (ximaList==null||ximaList.size()<=0) {
				this.errormsg="抱歉，没有查到任何数据";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("会员在查询自助反水记录明细时，发生异常", e);
			this.errormsg="系统异常，请稍候再试或与现在客服取得联系";
		}
		return "input";
	}
	
	
	
	 public String getAutoXimaSlotObject(){
			
			Users user = this.getUserObject();
			AutoXima autoXima = null;
			
			if (user==null) {
				GsonUtil.GsonObject(new AutoXima("请登录后在继续操作"));
				return null;
			}
			
			try {
				autoXima = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "getAutoSlotXimaObject", new Object[] { user.getLoginname() , platform }, AutoXima.class);
				GsonUtil.GsonObject(autoXima);
				return null;
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				GsonUtil.GsonObject(new AutoXima("本次自助洗码异常，请稍后重试！"));
				return null;
			}
		}
		
		
		public String execSlotXima(){
			
			Users user = this.getUserObject();
			if (user==null) {
				GsonUtil.GsonObject(new AutoXima("请登录后在继续操作"));
				return null;
			}
			///***************************begin
			String str = user.getLoginname()+platform ;
			Long nowTiem = System.currentTimeMillis();
	        Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String, Long> entry = it.next();
	            String keyStr = entry.getKey();
	            Long longTime = cacheMap.get(keyStr);
	            if (nowTiem - longTime > 1000 * 60) {
	                it.remove();
	            }
	        }
	        if (cacheMap.get(str) != null) {
	            GsonUtil.GsonObject(new AutoXima("60秒内不能重复提交同一平台。"));
	            return null;
	        }
	        cacheMap.put(str, nowTiem);
			///***************************end

	        GsonUtil.GsonObject(SynchronizedUtil.getInstance().execSlotXima(user, platform));
			return null;
		}
		
		
	
	
	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
	}

	public void setXimaAmount(Double ximaAmount) {
		this.ximaAmount = ximaAmount;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}


	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session=arg0;
	}
	
	public void setXimaList(List ximaList) {
		this.ximaList = ximaList;
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getMaxRowsno() {
		return maxRowsno;
	}

	public void setMaxRowsno(int maxRowsno) {
		this.maxRowsno = maxRowsno;
	}

	public List getXimaList() {
		return ximaList;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public IGGameinfoService getGameinfoService() {
		return gameinfoService;
	}

	public void setGameinfoService(IGGameinfoService gameinfoService) {
		this.gameinfoService = gameinfoService;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public int getTotalRowsno() {
		return totalRowsno;
	}

	public void setTotalRowsno(int totalRowsno) {
		this.totalRowsno = totalRowsno;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		req=arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		res=arg0;
	}
	
	public Double getTotalValidAmount() {
		return totalValidAmount;
	}

	public void setTotalValidAmount(Double totalValidAmount) {
		this.totalValidAmount = totalValidAmount;
	}

	public Double getTotalXimaAmount() {
		return totalXimaAmount;
	}

	public void setTotalXimaAmount(Double totalXimaAmount) {
		this.totalXimaAmount = totalXimaAmount;
	}

	public int getTotalPageno() {
		return totalPageno;
	}

	public void setTotalPageno(int totalPageno) {
		this.totalPageno = totalPageno;
	}
	
	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}



}
