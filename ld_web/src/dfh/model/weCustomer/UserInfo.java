package dfh.model.weCustomer;

public class UserInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String role;	//角色
	private String level;	//vip等级
	private String intro;	//推荐码
	private String agent;	//上级代理
	private String partner;	//合伙人
	private String belongto;//上级代理归属
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getBelongto() {
		return belongto;
	}
	public void setBelongto(String belongto) {
		this.belongto = belongto;
	}
}
