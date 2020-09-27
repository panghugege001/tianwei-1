package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Telvisittask entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "telvisittask", catalog = "tianwei")
public class Telvisittask implements java.io.Serializable {

	// Fields

	private Integer id;
	private String taskname;
	private String taskcondition;
	private Integer isdelay;
	private Long delaytime;
	private Timestamp starttime;
	private Timestamp endtime;
	private Timestamp addtime;
	private Integer taskstatus;
	private Timestamp finishtime;

	// Constructors

	
	
	/** default constructor */
	public Telvisittask() {
	}

	/** minimal constructor */
	public Telvisittask(String taskname, String taskcondition,
			Timestamp starttime, Timestamp endtime, Timestamp addtime) {
		this.taskname = taskname;
		this.taskcondition = taskcondition;
		this.starttime = starttime;
		this.endtime = endtime;
		this.addtime = addtime;
	}

	/** full constructor */
	public Telvisittask(String taskname, String taskcondition, Integer isdelay,
			Long delaytime, Timestamp starttime, Timestamp endtime,
			Timestamp addtime,Integer taskstatus) {
		this.taskname = taskname;
		this.taskcondition = taskcondition;
		this.isdelay = isdelay;
		this.delaytime = delaytime;
		this.starttime = starttime;
		this.endtime = endtime;
		this.addtime = addtime;
		this.taskstatus=taskstatus;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "taskname", nullable = false, length = 200)
	public String getTaskname() {
		return this.taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	@Column(name = "taskcondition", nullable = false, length = 1000)
	public String getTaskcondition() {
		return this.taskcondition;
	}

	public void setTaskcondition(String taskcondition) {
		this.taskcondition = taskcondition;
	}

	@Column(name = "isdelay")
	public Integer getIsdelay() {
		return this.isdelay;
	}

	public void setIsdelay(Integer isdelay) {
		this.isdelay = isdelay;
	}

	@Column(name = "delaytime")
	public Long getDelaytime() {
		return this.delaytime;
	}

	public void setDelaytime(Long delaytime) {
		this.delaytime = delaytime;
	}

	@Column(name = "starttime", nullable = false, length = 19)
	public Timestamp getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	@Column(name = "endtime", nullable = false, length = 19)
	public Timestamp getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	@Column(name = "addtime", nullable = false, length = 19)
	public Timestamp getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}
	
	@Column(name = "taskstatus")
	public Integer getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(Integer taskstatus) {
		this.taskstatus = taskstatus;
	}
	
	@Column(name = "finishtime", nullable = true, length = 19)
	public Timestamp getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}


}