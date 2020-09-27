package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 比邻群呼任务表
 * @author jalen
 *
 */
@Entity
@Table(name = "obtask", catalog = "atstar")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ObTask implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer task_id;// 任务编号
	private String task_type;// 7-呼叫类型
	private String task_name;// 任务名称
	private String dial_type;// 外呼原则：以坐席优先，以中继优先
	private String agent_type;// 7-接听方法，自动IVR，人工坐席
	private String agent_queues;// 接听队列
	private Double play_ivr;// 5-接入IVR
	private Integer delay;// 每次拨号延迟时间
	private Integer retry;// 重试次数
	private String trunks;// 呼出外线
	private Integer trunk_count;// 外线可用通道数
	private Integer idle_trunk_count;// 强制空闲通道数
	private String task_param;// 任务参数
	private Integer contact_count;// 联系人数
	private Integer finish_contact;// 完成数
	private String state;// 状态
	private String comment;// 备注
	private Date create_at;// 创建时间
	private Double create_user;// 创建人
	private Date update_at;// 更新时间
	private Integer dial_rate;// 呼叫比例
	private Integer dial_count;// 已播联系人数量
	private Integer fail_count;// 失败联系人数量
	private Integer reject_count;// 拒接联系人总数
	private Integer lost_count;// 客户挂断联系人总数
	private Integer success_count;// 成功联系人总数
	private String early_media;// 保留字段 默认bridged
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "task_id", unique = true)
	public Integer getTask_id() {
		return task_id;
	}
	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	@Column(name = "task_type")
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	@Column(name = "task_name")
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	@Column(name = "dial_type")
	public String getDial_type() {
		return dial_type;
	}
	public void setDial_type(String dial_type) {
		this.dial_type = dial_type;
	}
	@Column(name = "agent_type")
	public String getAgent_type() {
		return agent_type;
	}
	public void setAgent_type(String agent_type) {
		this.agent_type = agent_type;
	}
	@Column(name = "agent_queues")
	public String getAgent_queues() {
		return agent_queues;
	}
	public void setAgent_queues(String agent_queues) {
		this.agent_queues = agent_queues;
	}
	@Column(name = "play_ivr")
	public Double getPlay_ivr() {
		return play_ivr;
	}
	public void setPlay_ivr(Double play_ivr) {
		this.play_ivr = play_ivr;
	}
	@Column(name = "delay")
	public Integer getDelay() {
		return delay;
	}
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	@Column(name = "retry")
	public Integer getRetry() {
		return retry;
	}
	public void setRetry(Integer retry) {
		this.retry = retry;
	}
	@Column(name = "trunks")
	public String getTrunks() {
		return trunks;
	}
	public void setTrunks(String trunks) {
		this.trunks = trunks;
	}
	@Column(name = "trunk_count")
	public Integer getTrunk_count() {
		return trunk_count;
	}
	public void setTrunk_count(Integer trunk_count) {
		this.trunk_count = trunk_count;
	}
	@Column(name = "idle_trunk_count")
	public Integer getIdle_trunk_count() {
		return idle_trunk_count;
	}
	public void setIdle_trunk_count(Integer idle_trunk_count) {
		this.idle_trunk_count = idle_trunk_count;
	}
	@Column(name = "task_param")
	public String getTask_param() {
		return task_param;
	}
	public void setTask_param(String task_param) {
		this.task_param = task_param;
	}
	@Column(name = "contact_count")
	public Integer getContact_count() {
		return contact_count;
	}
	public void setContact_count(Integer contact_count) {
		this.contact_count = contact_count;
	}
	@Column(name = "finish_contact")
	public Integer getFinish_contact() {
		return finish_contact;
	}
	public void setFinish_contact(Integer finish_contact) {
		this.finish_contact = finish_contact;
	}
	@Column(name = "state")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Column(name = "comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column(name = "create_at")
	public Date getCreate_at() {
		return create_at;
	}
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
	@Column(name = "create_user")
	public Double getCreate_user() {
		return create_user;
	}
	public void setCreate_user(Double create_user) {
		this.create_user = create_user;
	}
	@Column(name = "update_at")
	public Date getUpdate_at() {
		return update_at;
	}
	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}
	@Column(name = "dial_rate")
	public Integer getDial_rate() {
		return dial_rate;
	}
	public void setDial_rate(Integer dial_rate) {
		this.dial_rate = dial_rate;
	}
	@Column(name = "dial_count")
	public Integer getDial_count() {
		return dial_count;
	}
	public void setDial_count(Integer dial_count) {
		this.dial_count = dial_count;
	}
	@Column(name = "fail_count")
	public Integer getFail_count() {
		return fail_count;
	}
	public void setFail_count(Integer fail_count) {
		this.fail_count = fail_count;
	}
	@Column(name = "reject_count")
	public Integer getReject_count() {
		return reject_count;
	}
	public void setReject_count(Integer reject_count) {
		this.reject_count = reject_count;
	}
	@Column(name = "lost_count")
	public Integer getLost_count() {
		return lost_count;
	}
	public void setLost_count(Integer lost_count) {
		this.lost_count = lost_count;
	}
	@Column(name = "success_count")
	public Integer getSuccess_count() {
		return success_count;
	}
	public void setSuccess_count(Integer success_count) {
		this.success_count = success_count;
	}
	@Column(name = "early_media")
	public String getEarly_media() {
		return early_media;
	}
	public void setEarly_media(String early_media) {
		this.early_media = early_media;
	}
	
}