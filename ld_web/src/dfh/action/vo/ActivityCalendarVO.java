package dfh.action.vo;

import java.io.Serializable;

/**
 * Created by wander on 2016/12/20.
 */
public class ActivityCalendarVO implements Serializable {

    private static final long serialVersionUID = 7956308131674112591L;

    private Integer id;
    /*** 活动名称*/
    private String name;
    /*** 活动内容*/
    private String content;
    /*** 活动日期*/
    private String activityDate;
    /*** 创建时间*/
    private String createtime;
    /*** 创建人*/
    private String creator;
    /*** 是否禁用 1启用 2禁止*/
    private Integer flag;

    public ActivityCalendarVO() {
    }

    public ActivityCalendarVO(Integer id, String name, String content, String activityDate, String createtime, String creator, Integer flag) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.activityDate = activityDate;
        this.createtime = createtime;
        this.creator = creator;
        this.flag = flag;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
