package dfh.mgs.vo.freegame;

import java.util.List;

public class RemovePlayersFromOfferRequest extends PlayerOfferActionBase implements java.io.Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer serverId;

    public RemovePlayersFromOfferRequest() {
    }

    public RemovePlayersFromOfferRequest(List<PlayerAction> playerActions, String sequence, Integer serverId) {
        super(playerActions, sequence);
        this.serverId = serverId;
    }

	public java.lang.Integer getServerId() {
		return serverId;
	}

	public void setServerId(java.lang.Integer serverId) {
		this.serverId = serverId;
	}

}