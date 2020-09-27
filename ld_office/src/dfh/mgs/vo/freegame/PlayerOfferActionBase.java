package dfh.mgs.vo.freegame;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import dfh.mgs.vo.DataStructuresNS;

public class PlayerOfferActionBase implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<PlayerAction> playerActions = new ArrayList<PlayerAction>();
    private String sequence;

    public PlayerOfferActionBase() {
    }

    public PlayerOfferActionBase(List<PlayerAction> playerActions, String sequence) {
           this.playerActions = playerActions;
           this.sequence = sequence;
    }
    
    @XmlElementWrapper(name = "PlayerActions", namespace = DataStructuresNS.FreegameAdminNS)
    @XmlElement(name = "PlayerAction", namespace = DataStructuresNS.FreegameAdminNS)
	public List<PlayerAction> getPlayerActions() {
		return playerActions;
	}

	public void setPlayerActions(List<PlayerAction> playerActions) {
		this.playerActions = playerActions;
	}

	@XmlElement(name = "Sequence", namespace = DataStructuresNS.FreegameAdminNS)
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
    
}