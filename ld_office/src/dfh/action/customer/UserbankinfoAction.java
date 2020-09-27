package dfh.action.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.nnti.office.model.auth.Operator;
import com.nnti.office.model.log.Operationlogs;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Userbankinfo;
import dfh.model.enums.OperationLogType;
import dfh.service.interfaces.IUserbankinfoService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.PageList;

public class UserbankinfoAction extends ActionSupport implements SessionAware,ServletRequestAware,ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6337780550839143855L;
	private Map<String, Object> session;
	private IUserbankinfoService userbankinfoService;
	private String errormsg;
	private Logger log=Logger.getLogger(UserbankinfoAction.class);
	private String bankname;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private String r;
	private PageList pagelist=new PageList();
	private String page;
	private String flag;
	private String username;
	private String id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public PageList getPagelist() {
		return pagelist;
	}
	public void setPagelist(PageList pagelist) {
		this.pagelist = pagelist;
	}
	public void setR(String r) {
		this.r = r;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public IUserbankinfoService getUserbankinfoService() {
		return userbankinfoService;
	}
	public void setUserbankinfoService(IUserbankinfoService userbankinfoService) {
		this.userbankinfoService = userbankinfoService;
	}
	
	
	public String unbanding(){
		try {
			req.setCharacterEncoding("utf-8");
			res.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (id==null||id.trim().equals("")) {
			this.println("操作错误");
			return null;
		}
		Operator user=(Operator) session.get(Constants.SESSION_OPERATORID);
		if (user==null) {
			this.println("请登录后，在继续操作");
			return null;
		}
		try {
			boolean unbanding = this.userbankinfoService.unbanding(new Integer(id).intValue());
			if (unbanding) {
				this.println("解绑成功");
				
				Operationlogs log = new Operationlogs();
				log.setLoginname(user.getUsername());
				log.setAction(OperationLogType.BANKS_OPERATING.getCode());
				Userbankinfo ubi = (Userbankinfo) userbankinfoService.get(Userbankinfo.class, Integer.parseInt(id));
				log.setRemark("解绑玩家银行卡:" + ubi.getLoginname() + "-" + ubi.getBankname() + "-" + ubi.getBankno());
				log.setCreatetime(DateUtil.getCurrentTimestamp());
				userbankinfoService.save(log);				
				return null;
			}else{
				this.println("解绑失败，请重新操作");
				return null;
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
			this.println("系统异常，请稍后再试");
			return null;
		}
		
	}
	
	
	public String searchUserbankinfo(){
		Operator user=(Operator) session.get(Constants.SESSION_OPERATORID);
		if (user==null) {
			this.errormsg="请登录后，在继续操作";
			return "index";
		}
		
		if (page!=null&&!page.trim().equals("")) {
			pagelist.setPageNumber(Integer.parseInt(page));
		}
		
		try {
			int totalCount = this.userbankinfoService.getTotalCount(username, bankname, flag==null||flag.equals("")?null:new Integer(flag));
			if (totalCount==0) {
				this.errormsg="没有查到任何资料";
				return "input";
			}
			this.pagelist.setFullListSize(totalCount);
			List<Userbankinfo> userbankinfoList = this.userbankinfoService.getUserbankinfoList(username, bankname, flag==null||flag.equals("")?null:new Integer(flag), pagelist.getPageNumber(), pagelist.getObjectsPerPage());
			pagelist.setList(userbankinfoList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
			this.errormsg="系统异常，请稍后再试";
			return "input";
		}
		
		return "input";
	}
	
	
	
	private void println(String msg){
		PrintWriter out=null;
		try {
			out = res.getWriter();
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (out!=null) {
				out.close();
			}
		}
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;
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

}
