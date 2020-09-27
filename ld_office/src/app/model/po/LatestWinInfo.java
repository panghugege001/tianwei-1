package app.model.po;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_latestWinInfo", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class LatestWinInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 编号
	private Integer id;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date modifyTime;
	// 状态，0:停用  1:启用
	private Integer status;
	// 会员账号
	private String loginName;
	// 投注金额
	private Double betAmount;
	// 中奖金额
	private Double winAmount;
	// 游戏标题
	private String gameTitle;
	// 游戏图标
	private String gameIcon;
	// 论坛地址
	private String forumUrl;
	// 打开方式
	private String openType;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@javax.persistence.Column(name = "modifyTime")
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	@javax.persistence.Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@javax.persistence.Column(name = "loginName")
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	@javax.persistence.Column(name = "betAmount")
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	@javax.persistence.Column(name = "winAmount")
	public Double getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(Double winAmount) {
		this.winAmount = winAmount;
	}
	@javax.persistence.Column(name = "gameTitle")
	public String getGameTitle() {
		return gameTitle;
	}
	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}
	@javax.persistence.Column(name = "gameIcon")
	public String getGameIcon() {
		return gameIcon;
	}
	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}
	@javax.persistence.Column(name = "forumUrl")
	public String getForumUrl() {
		return forumUrl;
	}
	public void setForumUrl(String forumUrl) {
		this.forumUrl = forumUrl;
	}
	@javax.persistence.Column(name = "openType")
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	
}