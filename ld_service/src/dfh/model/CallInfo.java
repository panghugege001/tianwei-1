package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="call_info",catalog="tianwei"
)
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class CallInfo  implements java.io.Serializable {


    // Fields    
     private Integer id;
     private String caller;
 	private String called;
 	private String begin_time;
 	private String end_time;
 	private String duration;
 	private String record_path;
 	private String call_type;
 	private String trunk;
 	private String uuid;
 	private String hangupcause;
 	private Date createtime;
 	
 	
    public CallInfo(String caller, String called, String begin_time, String end_time, String duration,
			String record_path, String call_type, String trunk, String uuid, String hangupcause, Date createtime) {
		super();
		this.caller = caller;
		this.called = called;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.duration = duration;
		this.record_path = record_path;
		this.call_type = call_type;
		this.trunk = trunk;
		this.uuid = uuid;
		this.hangupcause = hangupcause;
		this.createtime = createtime;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
    @javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getCalled() {
		return called;
	}
	public void setCalled(String called) {
		this.called = called;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRecord_path() {
		return record_path;
	}
	public void setRecord_path(String record_path) {
		this.record_path = record_path;
	}
	public String getCall_type() {
		return call_type;
	}
	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}
	public String getTrunk() {
		return trunk;
	}
	public void setTrunk(String trunk) {
		this.trunk = trunk;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getHangupcause() {
		return hangupcause;
	}
	public void setHangupcause(String hangupcause) {
		this.hangupcause = hangupcause;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
     
}