package dfh.mgs.vo.freegame;

import java.util.List;

public class AddPlayersToOfferRequest extends PlayerOfferActionBase implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer serverId;

    public AddPlayersToOfferRequest() {
    }

    public AddPlayersToOfferRequest(List<PlayerAction> playerActions, String sequence, Integer serverId) {
        super(playerActions, sequence);
        this.serverId = serverId;
    }

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
    
}