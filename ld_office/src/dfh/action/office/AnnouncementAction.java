package dfh.action.office;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.nnti.office.model.auth.Operator;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Announcement;
import dfh.service.interfaces.AnnouncementService;
import dfh.utils.Constants;
import dfh.utils.PageList;

public class AnnouncementAction extends ActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2600000458007220957L;
	private Logger log=Logger.getLogger(AnnouncementAction.class);
	private AnnouncementService announcementService;
	private PageList pageList=new PageList();
	private int id;
	private String page;
	private String errormsg="";
	private Map session;
	private String title;
	private String content;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public AnnouncementService getAnnouncementService() {
		return announcementService;
	}

	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}
	

	public PageList getPageList() {
		return pageList;
	}

	public void setPageList(PageList pageList) {
		this.pageList = pageList;
	}


	private String getRoleName(){
		Object o=session.get(Constants.SESSION_OPERATORID);
		if (o==null) {
			return null;
		}else{
			Operator operator=(Operator)o;
			return operator.getAuthority();
		}
	}
	
	// methods:
	
	public String queryAll(){
		
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}

		if (page!=null&&page!="") {
			pageList.setPageNumber(Integer.parseInt(page));
		}
		
		List list=null;
		try {
			int totalCount = announcementService.getAnnouncementCount();
			pageList.setFullListSize(totalCount);
			list = announcementService.getAllIndexAnnouncement(pageList.getPageNumber(),pageList.getObjectsPerPage());
		} catch (Exception e) {
			log.error("执行系统公告查询时，发生异常。", e);
			this.setErrormsg("查询公告记录时，系统发生异常；请重新操作或与管理员联系");
			return Action.ERROR;
		}
		if (list==null) {
			this.setErrormsg("没有查找到您要的记录");
			return Action.ERROR;
		}else{
			pageList.setList(list);
		}
		return Action.SUCCESS;
	}
	
	public String editAnnouncement(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		
		Operator operator=(Operator) session.get(Constants.SESSION_OPERATORID);
		try {
			announcementService.modifyAnnouncement(id, type, content, operator.getUsername(), title);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("编辑数据时发生错误",e);
			this.setErrormsg("编辑记录时发生异常："+e.getMessage().toString());
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}
	
	public String delAnnouncement(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		
		Operator operator=(Operator) session.get(Constants.SESSION_OPERATORID);
		announcementService.deleteAnnouncement(id, operator.getUsername());
		//this.setErrormsg("操作成功");
		return Action.SUCCESS;  // redirectAction this;
	}
	
	public String queryAnnouncementById(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		
		Announcement ann=(Announcement) announcementService.queryAnnouncementById(id);
		if(ann==null){
			this.setErrormsg("您要编辑的记录不存在");
			return Action.ERROR;
		}
		this.setId(ann.getId());
		this.setTitle(ann.getTitle());
		this.setContent(ann.getContent());
		this.setType(ann.getType());
		return Action.SUCCESS;
	}
	
	public String addAnnouncement(){
		String manager = this.getRoleName();
		if (manager==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		
		Operator operator=(Operator) session.get(Constants.SESSION_OPERATORID);
		try {
			announcementService.addAnnouncement(type, content, operator.getUsername(), title);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("插入公告记录时，系统发生异常；", e);
			this.setErrormsg("插入记录时，系统发生异常；请重新操作或与管理员联系");
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}

	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		this.session=arg0;
	}

}
