package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Telvisit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "telvisit", catalog = "tianwei")
public class Telvisit implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginname;
	private Timestamp addtime;
	private Integer taskid;
	private Integer islock;
	private Integer execstatus;
	private String locker;

	// Constructors

	/** default constructor */
	public Telvisit() {
	}

	/** minimal constructor */
	public Telvisit(String loginname, Timestamp addtime, Integer islock,
			Integer execstatus) {
		this.loginname = loginname;
		this.addtime = addtime;
		this.islock = islock;
		this.execstatus = execstatus;
	}

	/** full constructor */
	public Telvisit(String loginname, Timestamp addtime,
			Integer taskid, Integer islock, Integer execstatus) {
		this.loginname = loginname;
		this.addtime = addtime;
		this.taskid = taskid;
		this.islock = islock;
		this.execstatus = execstatus;
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

	@Column(name = "loginname", nullable = false, length = 30)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "addtime", nullable = false, length = 19)
	public Timestamp getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	@Column(name = "taskid")
	public Integer getTaskid() {
		return this.taskid;
	}

	public void setTaskid(Integer taskid) {
		this.taskid = taskid;
	}

	@Column(name = "islock", nullable = false)
	public Integer getIslock() {
		return this.islock;
	}

	public void setIslock(Integer islock) {
		this.islock = islock;
	}

	@Column(name = "execstatus", nullable = false)
	public Integer getExecstatus() {
		return this.execstatus;
	}

	public void setExecstatus(Integer execstatus) {
		this.execstatus = execstatus;
	}
	
	@Column(name = "locker", nullable = true, length = 30)
	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}
	

}