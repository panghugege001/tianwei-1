package app.model.po;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_package_version_custom", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AppPackageVersionCustom implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id; 
	private Integer versionId;  //版本ID，对应主表app_package_version.id
	private Date createTime; //创建时间
	private Date modifyTime;  //修改时间
	private String versionCode; //版本代码
	private String packageName; //包别名
	private String agentAccount; //代理账号
	private String agentCode; //代理码
	private String plat;  //平台  android or ios
	private boolean isUpgrade;  //是否可升级
	private boolean isForceUpgrade;  //是否强制升级
	private Integer pakStatus;  //打包状态 (0-未打包， 1-打包中 ， 2-打包完成， 11-打包错误)
	private Integer status;  // 状态 (0： 停用或未发行-默认 1：启用或发行 )
	private String packageUrl;  //打包链接
	private String publishUrl;  //发行链接
	private String qrCodeUrl;  //二维码链接
	private String operator;   //操作员

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "versionId")
	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
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

	@Column(name = "packageName")
	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Column(name = "agentAccount")
	public String getAgentAccount() {
		return this.agentAccount;
	}

	public void setAgentAccount(String agentAccount) {
		this.agentAccount = agentAccount;
	}

	@Column(name = "agentCode")
	public String getAgentCode() {
		return this.agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	@Column(name = "plat")
	public String getPlat() {
		return this.plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	@Column(name = "isUpgrade")
	public boolean getIsUpgrade() {
		return this.isUpgrade;
	}

	public void setIsUpgrade(boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	@Column(name = "isForceUpgrade")
	public boolean getIsForceUpgrade() {
		return this.isForceUpgrade;
	}

	public void setIsForceUpgrade(boolean isForceUpgrade) {
		this.isForceUpgrade = isForceUpgrade;
	}

	@Column(name = "pakStatus")
	public Integer getPakStatus() {
		return this.pakStatus;
	}

	public void setPakStatus(Integer pakStatus) {
		this.pakStatus = pakStatus;
	}

	@Column(name = "packageUrl")
	public String getPackageUrl() {
		return this.packageUrl;
	}

	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
	}

	@Column(name = "publishUrl")
	public String getPublishUrl() {
		return this.publishUrl;
	}

	public void setPublishUrl(String publishUrl) {
		this.publishUrl = publishUrl;
	}

	@Column(name = "qrCodeUrl")
	public String getQrCodeUrl() {
		return this.qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
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