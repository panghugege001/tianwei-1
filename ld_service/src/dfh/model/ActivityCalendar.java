package dfh.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * @author wander
 *         活动日历
 */
@Entity
@Table(name = "activity_calendar", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ActivityCalendar implements java.io.Serializable {

    private Integer id;
    /*** 活动名称*/
    private String name;
    /*** 活动内容*/
    private String content;
    /*** 活动日期*/
    private Date activityDate;
    /*** 创建时间*/
    private Timestamp createtime;
    /*** 创建人*/
    private String creator;
    /*** 是否禁用 1启用 2禁止*/
    private Integer flag;

    public ActivityCalendar() {
    }

    public ActivityCalendar(Integer id, String name, String content, Date activityDate, Timestamp createtime, String creator, Integer flag) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.activityDate = activityDate;
        this.createtime = createtime;
        this.creator = creator;
        this.flag = flag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "content")
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "createtime")
    public Timestamp getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "creator")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "flag")
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Column(name = "activityDate")
    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }
}