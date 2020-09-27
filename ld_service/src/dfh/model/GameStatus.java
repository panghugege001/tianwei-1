package dfh.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**好友推荐的积分池
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gamestatus", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class GameStatus implements java.io.Serializable {

	// Fields
	private String userName;//玩家账号
    private String gameList;
    
    @Id
	@Column(name="username")
	public String getUserName() {
		return userName;
	}
    
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="gamelist")
	public String getGameList() {
		return gameList;
	}
	
	public void setGameList(String gameList) {
		this.gameList = gameList;
	}

	
}