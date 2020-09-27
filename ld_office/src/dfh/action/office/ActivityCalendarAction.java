package dfh.action.office;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.nnti.office.model.auth.Operator;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.ActivityCalendar;
import dfh.service.interfaces.ActivityCalendarService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.PageList;

/**
 * author: wander
 * date: 2016-12-19
 * 活动列表
 */
public class ActivityCalendarAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = -2701092878588368292L;

    private Logger log = Logger.getLogger(ActivityCalendarAction.class);

    private ActivityCalendarService activityCalendarService;
    private PageList pageList = new PageList();
    private String page;
    private String errormsg = "";
    private Map session;
    private Integer id;
    private String name;
    private String content;
    private String activityDate;
    private Integer flag;
    private String creator;
    private Integer orderBy;

    private String getRoleName() {
        Object o = session.get(Constants.SESSION_OPERATORID);
        if (o == null) {
            return null;
        } else {
            Operator operator = (Operator) o;
            return operator.getAuthority();
        }
    }

    public String queryAll() {

        String manager = this.getRoleName();
        if (manager == null) {
            this.errormsg = "请登录后在尝试操作";
            return "logout";
        }

        if (page != null && page != "") {
            pageList.setPageNumber(Integer.parseInt(page));
        }

        List list = null;
        try {
            int totalCount = activityCalendarService.getActivityCalendarCount();
            pageList.setFullListSize(totalCount);
            list = activityCalendarService.getAllIndexActivityCalendar(pageList.getPageNumber(), pageList.getObjectsPerPage());
        } catch (Exception e) {
            log.error("执行系统活动列表查询时，发生异常。", e);
            this.setErrormsg("查询活动列表记录时，系统发生异常；请重新操作或与管理员联系");
            return Action.ERROR;
        }
        if (list == null) {
            this.setErrormsg("没有查找到您要的记录");
            return Action.ERROR;
        } else {
            pageList.setList(list);
        }
        return Action.SUCCESS;
    }

    public String editActivityCalendar() {
        String manager = this.getRoleName();
        if (manager == null) {
            this.errormsg = "请登录后在尝试操作";
            return "logout";
        }

        Operator operator = (Operator) session.get(Constants.SESSION_OPERATORID);
        try {
        	activityCalendarService.modifyActivityCalendar(new ActivityCalendar(id, name, content,DateUtil.parseDateForYYYYmmDD(activityDate), null, operator.getUsername(), flag,orderBy));
        } catch (Exception e) {
            log.error("编辑数据时发生错误", e);
            this.setErrormsg("编辑记录时发生异常：" + e.getMessage().toString());
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }

    public String switchActivityCalendar() {
        String manager = this.getRoleName();
        if (manager == null) {
            this.errormsg = "请登录后在尝试操作";
            return "logout";
        }

        Operator operator = (Operator) session.get(Constants.SESSION_OPERATORID);
        activityCalendarService.switchActivityCalendar(id, operator.getUsername(), flag);
        return Action.SUCCESS;
    }

    public String add_view() {
        String manager = this.getRoleName();
        if (manager == null) {
            this.errormsg = "请登录后在尝试操作";
            return "logout";
        }
        return Action.INPUT;
    }

    public String queryActivityCalendarById() {
        String manager = this.getRoleName();
        if (manager == null) {
            this.errormsg = "请登录后在尝试操作";
            return "logout";
        }

        ActivityCalendar ann = (ActivityCalendar) activityCalendarService.queryActivityCalendarById(id);
        if (ann == null) {
            this.setErrormsg("您要编辑的记录不存在");
            return Action.ERROR;
        }
        this.setId(ann.getId());
        this.setName(ann.getName());
        this.setContent(ann.getContent());
        this.setFlag(ann.getFlag());
        this.setCreator(ann.getCreator());
        Date activityDate = ann.getActivityDate();
        if (activityDate == null) {
            this.setActivityDate("");
        } else {
            this.setActivityDate(DateUtil.fmtYYYY_MM_DD(activityDate));
        }
        return Action.SUCCESS;
    }

    public String addActivityCalendar() {
        String manager = this.getRoleName();
        if (manager == null) {
            this.errormsg = "请登录后在尝试操作";
            return "logout";
        }

        Operator operator = (Operator) session.get(Constants.SESSION_OPERATORID);
        try {
        	activityCalendarService.addActivityCalendar(new ActivityCalendar(null, name, content,DateUtil.parseDateForYYYYmmDD(activityDate), DateUtil.now(), operator.getUsername(), flag,orderBy));
        } catch (Exception e) {
            log.error("插入活动列表记录时，系统发生异常；", e);
            this.setErrormsg("插入记录时，系统发生异常；请重新操作或与管理员联系");
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }


    public ActivityCalendarService getActivityCalendarService() {
        return activityCalendarService;
    }

    public void setActivityCalendarService(ActivityCalendarService activityCalendarService) {
        this.activityCalendarService = activityCalendarService;
    }

    public PageList getPageList() {
        return pageList;
    }

    public void setPageList(PageList pageList) {
        this.pageList = pageList;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderBy() {
        return orderBy;
    }
}
