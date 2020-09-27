package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chess_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ChessData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer	id;
	
	private Integer logid;

	private Date ctime;

	private Integer prev;

	private String sessionid;

	private Integer uid;

	private String acc;

	private Integer leftamount;

	private Integer chg;

	private String kind;

	private Integer realput;

	private Integer allput;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLogid(Integer logid) {
		this.logid = logid;
	}

	public Integer getLogid() {
		return this.logid;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public void setPrev(Integer prev) {
		this.prev = prev;
	}

	public Integer getPrev() {
		return this.prev;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getUid() {
		return this.uid;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}

	public String getAcc() {
		return this.acc;
	}

	public void setLeftamount(Integer leftamount) {
		this.leftamount = leftamount;
	}

	public Integer getLeftamount() {
		return this.leftamount;
	}

	public void setChg(Integer chg) {
		this.chg = chg;
	}

	public Integer getChg() {
		return this.chg;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return this.kind;
	}

	public void setRealput(Integer realput) {
		this.realput = realput;
	}

	public Integer getRealput() {
		return this.realput;
	}

	public void setAllput(Integer allput) {
		this.allput = allput;
	}

	public Integer getAllput() {
		return this.allput;
	}
}