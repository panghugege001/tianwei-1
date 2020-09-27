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
@Table(name = "app_package_version", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AppPackageVersion implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date createTime;  //创建时间
	private Date modifyTime;  //修改时间
	private String versionCode;  //版本代码
	private String versionTitle;  //版本标题
	private String upgradeLog;  //升级日志
	private String plat;  //平台 android or ios
	private String operator;  //操作员
	private Integer status;  // 状态，0:停用  1:启用
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modifyTime")
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "versionCode")
	public String getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	@Column(name = "versionTitle")
	public String getVersionTitle() {
		return this.versionTitle;
	}

	public void setVersionTitle(String versionTitle) {
		this.versionTitle = versionTitle;
	}

	@Column(name = "upgradeLog")
	public String getUpgradeLog() {
		return this.upgradeLog;
	}

	public void setUpgradeLog(String upgradeLog) {
		this.upgradeLog = upgradeLog;
	}

	@Column(name = "plat")
	public String getPlat() {
		return this.plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	@Column(name = "operator")
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}