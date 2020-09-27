package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 任务联系人表
 * 
 * @author jalen
 *
 */
@Entity
@Table(name = "task_contact", catalog = "atstar")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TaskContact implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer task_contact_id;// 编号
	private String contact_name;// 联系人名称
	private String phone_number;// 电话号码
	private String dial_state;// 呼叫状态
	private String status;// 联系人状态
	private Double task_id;// 联系人
	private String dial_result;// 呼叫结果
	private String fail_cause;// 错误原因
	private String failer;// 出错对象
	private String agent;// 呼叫坐席
	private Date dial_time;// 呼叫时间
	private Integer try_count;// 呼叫时间
	private Integer duration;//
	private Integer queue_time;//
	private String service_agent;//
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "task_contact_id", unique = true)
	public Integer getTask_contact_id() {
		return task_contact_id;
	}
	public void setTask_contact_id(Integer task_contact_id) {
		this.task_contact_id = task_contact_id;
	}
	@Column(name = "contact_name")
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	@Column(name = "phone_number")
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	@Column(name = "dial_state")
	public String getDial_state() {
		return dial_state;
	}
	public void setDial_state(String dial_state) {
		this.dial_state = dial_state;
	}
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "task_id")
	public Double getTask_id() {
		return task_id;
	}
	public void setTask_id(Double task_id) {
		this.task_id = task_id;
	}
	@Column(name = "dial_result")
	public String getDial_result() {
		return dial_result;
	}
	public void setDial_result(String dial_result) {
		this.dial_result = dial_result;
	}
	@Column(name = "fail_cause")
	public String getFail_cause() {
		return fail_cause;
	}
	public void setFail_cause(String fail_cause) {
		this.fail_cause = fail_cause;
	}
	@Column(name = "failer")
	public String getFailer() {
		return failer;
	}
	public void setFailer(String failer) {
		this.failer = failer;
	}
	@Column(name = "agent")
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	@Column(name = "dial_time")
	public Date getDial_time() {
		return dial_time;
	}
	public void setDial_time(Date dial_time) {
		this.dial_time = dial_time;
	}
	@Column(name = "try_count")
	public Integer getTry_count() {
		return try_count;
	}
	public void setTry_count(Integer try_count) {
		this.try_count = try_count;
	}
	@Column(name = "duration")
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	@Column(name = "queue_time")
	public Integer getQueue_time() {
		return queue_time;
	}
	public void setQueue_time(Integer queue_time) {
		this.queue_time = queue_time;
	}
	@Column(name = "service_agent")
	public String getService_agent() {
		return service_agent;
	}
	public void setService_agent(String service_agent) {
		this.service_agent = service_agent;
	}
}