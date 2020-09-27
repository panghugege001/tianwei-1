package dfh.model.shaba;
/**
 * 赌注设定类
 * @author Jalen
 *
 */
public class BetSetting implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sport_type;
	private Integer min_bet;
	private Integer max_be;
	private Integer max_bet_per_match;
	private Integer max_bet_per_ball; //当sport_type = 161（Number Game）时，必须设置此值
	public String getSport_type() {
		return sport_type;
	}
	public void setSport_type(String sport_type) {
		this.sport_type = sport_type;
	}
	public Integer getMin_bet() {
		return min_bet;
	}
	public void setMin_bet(Integer min_bet) {
		this.min_bet = min_bet;
	}
	public Integer getMax_be() {
		return max_be;
	}
	public void setMax_be(Integer max_be) {
		this.max_be = max_be;
	}
	public Integer getMax_bet_per_match() {
		return max_bet_per_match;
	}
	public void setMax_bet_per_match(Integer max_bet_per_match) {
		this.max_bet_per_match = max_bet_per_match;
	}
	public Integer getMax_bet_per_ball() {
		return max_bet_per_ball;
	}
	public void setMax_bet_per_ball(Integer max_bet_per_ball) {
		this.max_bet_per_ball = max_bet_per_ball;
	}
	
}
