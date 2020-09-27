package dfh.mgs.vo.freegame;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import dfh.mgs.vo.DataStructuresNS;

public class AddPlayersToFreegameResult  extends PlayerOfferActionBase implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean hasErrors;

    public AddPlayersToFreegameResult() {
    }

    public AddPlayersToFreegameResult(List<PlayerAction> playerActions, String sequence, Boolean hasErrors) {
        super(playerActions, sequence);
        this.hasErrors = hasErrors;
    }

    @XmlElement(name = "HasErrors", namespace = DataStructuresNS.FreegameAdminNS)
	public java.lang.Boolean getHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(java.lang.Boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
    
}