package app.model.po;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "app_version", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AppVersion implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String upgrade_context;
	private String force;
	private java.util.Date create_time;
	private String download_url;
	private String plat;
	private String title;
	private String version;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "upgrade_context")
	public String getUpgrade_context() {
		return upgrade_context;
	}
	public void setUpgrade_context(String upgrade_context) {
		this.upgrade_context = upgrade_context;
	}
	@Column(name = "force")
	public String getForce() {
		return force;
	}
	public void setForce(String force) {
		this.force = force;
	}
	@Column(name = "create_time")
	public java.util.Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(java.util.Date create_time) {
		this.create_time = create_time;
	}
	@Column(name = "download_url")
	public String getDownload_url() {
		return download_url;
	}
	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}
	@Column(name = "plat")
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}