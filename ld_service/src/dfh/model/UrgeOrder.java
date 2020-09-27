package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "urge_order", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UrgeOrder implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private Integer id;
	private String loginname;//登录账号
	private String accountName;//存款人姓名
	private String thirdOrder;//第三方订单号
	private String nickname; //昵称
	private Date depositTime;//存款时间
	private Double amount;	//金额
	private Date createtime; //创建时间
	private Date updatetime; //更新时间
	private String remark; //备注
	private Integer status; //状态
	private String type; //存款类型（'1':'在线支付宝扫描','2':'支付宝扫描','3':'支付宝附言','4':'微信扫描','5':'微信额度验证','6':'在线支付','7':'工行附言','8':'招行附言','9':'点卡支付'）
	private String picture;//截图地址
	private String operator;//处理人员
	private Integer cardtype;//点卡类型
	private String cardno;//点卡卡号
	
	
	//用于axis2对date不支持
	private String tempDepositTime;
    private String tempCreateTime;
    private String tempUpdateTime;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	@Column(name = "accountName")
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	@Column(name = "thirdOrder")
	public String getThirdOrder() {
		return thirdOrder;
	}
	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}
	@Column(name = "nickname")
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Column(name = "depositTime")
	public Date getDepositTime() {
		return depositTime;
	}
	public void setDepositTime(Date depositTime) {
		this.depositTime = depositTime;
	}
	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Column(name = "updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "picture")
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	@Column(name = "operator")
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@Column(name = "cardtype")
	public Integer getCardtype() {
		return cardtype;
	}
	public void setCardtype(Integer cardtype) {
		this.cardtype = cardtype;
	}
	@Column(name = "cardno")
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	@Transient
	public String getTempDepositTime() {
		return tempDepositTime;
	}
	public void setTempDepositTime(String tempDepositTime) {
		this.tempDepositTime = tempDepositTime;
	}
	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}
	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	@Transient
	public String getTempUpdateTime() {
		return tempUpdateTime;
	}
	public void setTempUpdateTime(String tempUpdateTime) {
		this.tempUpdateTime = tempUpdateTime;
	}
}