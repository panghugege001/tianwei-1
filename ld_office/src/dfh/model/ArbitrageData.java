package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "arbitrage_data", catalog = "share")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ArbitrageData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	// Fields
	private Integer id;
	private String loginname;
	private String arbitrage_project;
	private String account_name;
	private String phone;
	private String email;
	private String wechart;
	private String register_ip;
	private Date register_time;
	private String login_ip;
	private Date login_time;
	private String phone_id;
	private String bind_bankcard;
	private String deposit_bankcard;
	private String agent;
	private String operator;
	private Date operate_time;
	private String source;
	private String remark;
	private String arbitrage_project_show;
	@Transient
	public String getArbitrage_project_show() {
		return arbitrage_project_show;
	}
	public void setArbitrage_project_show(String arbitrage_project_show) {
		this.arbitrage_project_show = arbitrage_project_show;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getArbitrage_project() {
		return arbitrage_project;
	}
	public void setArbitrage_project(String arbitrage_project) {
		this.arbitrage_project = arbitrage_project;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWechart() {
		return wechart;
	}
	public void setWechart(String wechart) {
		this.wechart = wechart;
	}
	public String getRegister_ip() {
		return register_ip;
	}
	public void setRegister_ip(String register_ip) {
		this.register_ip = register_ip;
	}
	public Date getRegister_time() {
		return register_time;
	}
	public void setRegister_time(Date register_time) {
		this.register_time = register_time;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
	public Date getLogin_time() {
		return login_time;
	}
	public void setLogin_time(Date login_time) {
		this.login_time = login_time;
	}
	public String getPhone_id() {
		return phone_id;
	}
	public void setPhone_id(String phone_id) {
		this.phone_id = phone_id;
	}
	public String getBind_bankcard() {
		return bind_bankcard;
	}
	public void setBind_bankcard(String bind_bankcard) {
		this.bind_bankcard = bind_bankcard;
	}
	public String getDeposit_bankcard() {
		return deposit_bankcard;
	}
	public void setDeposit_bankcard(String deposit_bankcard) {
		this.deposit_bankcard = deposit_bankcard;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperate_time() {
		return operate_time;
	}
	public void setOperate_time(Date operate_time) {
		this.operate_time = operate_time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}