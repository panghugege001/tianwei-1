package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;

import dfh.mgs.vo.DataStructuresNS;

public class PlayerAction extends PlayerBase implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer instanceId;
    private Integer offerId;
    private String sequence;

    public PlayerAction() {
    }

    public PlayerAction(String error, String ISOCurrencyCode, String loginName, String playerStartDate, Boolean success, Integer instanceId,  Integer offerId, String sequence) {
        super(error, ISOCurrencyCode, loginName, playerStartDate, success);
        this.instanceId = instanceId;
        this.offerId = offerId;
        this.sequence = sequence;
    }

    @XmlElement(name = "InstanceId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	@XmlElement(name = "OfferId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	@XmlElement(name = "Sequence", namespace = DataStructuresNS.FreegameAdminNS)
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
    
 }