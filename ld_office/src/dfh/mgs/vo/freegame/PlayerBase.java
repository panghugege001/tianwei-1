package dfh.mgs.vo.freegame;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import dfh.mgs.vo.DataStructuresNS;
import dfh.utils.DateUtil;

public class PlayerBase implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String error;
    private String ISOCurrencyCode;
    private String loginName;
    private Date playerStartDate;
    private Boolean success;

    public PlayerBase() {
    }

    public PlayerBase(String error, String ISOCurrencyCode, String loginName, String playerStartDate, Boolean success) {
           this.error = error;
           this.ISOCurrencyCode = ISOCurrencyCode;
           this.loginName = loginName;
           Date startDate = DateUtil.parseDateForStandard(playerStartDate.replace("T", " "));
           this.playerStartDate = startDate;
           this.success = success;
    }

    @XmlElement(name = "Error", namespace = DataStructuresNS.FreegameAdminNS)
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "ISOCurrencyCode", namespace = DataStructuresNS.FreegameAdminNS)
	public String getISOCurrencyCode() {
		return ISOCurrencyCode;
	}

	public void setISOCurrencyCode(String currencyCode) {
		ISOCurrencyCode = currencyCode;
	}

	@XmlElement(name = "LoginName", namespace = DataStructuresNS.FreegameAdminNS)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@XmlElement(name = "PlayerStartDate", namespace = DataStructuresNS.FreegameAdminNS)
	public Date getPlayerStartDate() {
		return playerStartDate;
	}

	public void setPlayerStartDate(Date playerStartDate) {
		this.playerStartDate = playerStartDate;
	}

	@XmlElement(name = "Success", namespace = DataStructuresNS.FreegameAdminNS)
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
    
}