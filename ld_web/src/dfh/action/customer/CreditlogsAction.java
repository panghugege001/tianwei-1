package dfh.action.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Creditlogs;
import dfh.model.Users;
import dfh.service.interfaces.ICreditlogsService;
import dfh.skydragon.webservice.model.RecordInfo;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class CreditlogsAction extends ActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 339959562747727773L;
	private Map<String,Object> session;
	private String errormsg;
	private ICreditlogsService creditlogsService;
	private String starttime;
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}




	private String endtime;
	private int pageno=1;
	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getTotalpageno() {
		return totalpageno;
	}

	public void setTotalpageno(int totalpageno) {
		this.totalpageno = totalpageno;
	}

	public int getTotalrowsno() {
		return totalrowsno;
	}

	public void setTotalrowsno(int totalrowsno) {
		this.totalrowsno = totalrowsno;
	}


	public List<Creditlogs> getList() {
		return list;
	}

	public void setList(List<Creditlogs> list) {
		this.list = list;
	}




	private int totalpageno=0;
	private int totalrowsno=0;
	private int maxpageno=20;
	public int getMaxpageno() {
		return maxpageno;
	}

	public void setMaxpageno(int maxpageno) {
		this.maxpageno = maxpageno;
	}




	private List<Creditlogs> list=new ArrayList<Creditlogs>();
	
	
	

	public ICreditlogsService getCreditlogsService() {
		return creditlogsService;
	}

	public void setCreditlogsService(ICreditlogsService creditlogsService) {
		this.creditlogsService = creditlogsService;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;
	}
	
	
	
	
	public String searchCreditlogs(){
		Users user=(Users) this.session.get(Constants.SESSION_CUSTOMERID);
		if (user==null) {
			this.errormsg="请登录后在进行操作";
			return "index";
		}
		if (starttime==null||endtime==null) {
			this.errormsg="起始时间和截止时间可不为空";
			return "input";
		}
		SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date start=yyyy_MM_d.parse(starttime);
			Date end=yyyy_MM_d.parse(endtime);
		} catch (Exception e) {
			// TODO: handle exception
			this.errormsg="日期格式错误，请重新填写";
			return "input";
		}
		
		
		try {
			RecordInfo recordInfo = AxisUtil.getRecordInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getCreditLogList", new Object[] {
				this.starttime, this.endtime, user.getLoginname(), pageno, maxpageno}, 
				RecordInfo.class,Creditlogs.class);
			if(null==recordInfo){
				throw new AxisFault("系统缓忙，请稍后重试！");
			}else{
				this.totalrowsno=recordInfo.getLength();
				this.totalpageno=(this.totalrowsno%this.maxpageno==0?0:1)+(this.totalrowsno/this.maxpageno);
				this.list = recordInfo.getDataList();
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return INPUT;
	}

}
