package dfh.mgs.vo;

import javax.xml.bind.annotation.XmlElement;

public class QueueDataResponse  implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private java.lang.String loginName;
    private java.lang.Integer userId;
    private java.lang.Integer changeAmount;
    private java.lang.String transactionCurrency;
    private java.lang.String status;
    private java.lang.Integer rowId;
    private java.lang.Integer transactionNumber;
    private java.lang.String gameName;
    private java.lang.String dateCreated;
    private java.lang.String mgsReferenceNumber;
    private java.lang.Integer serverId;
    private java.lang.Integer mgsPayoutReferenceNumber;
    private java.lang.Integer payoutAmount;
    private java.lang.Boolean progressiveWin;
    private java.lang.String progressiveWinDesc;
    private java.lang.String freeGameOfferName;
    private java.lang.Integer tournamentId;
    private java.lang.String description;
    private java.lang.String extInfo;
    private java.lang.Long rowIdLong;
    private java.lang.String betTicketId;
    private java.lang.Boolean punchTicketStatus;   
    
    public QueueDataResponse() {
    }

    public QueueDataResponse(String loginName, Integer userId, Integer changeAmount, String transactionCurrency, String status, Integer rowId, Integer transactionNumber,
           String gameName, String dateCreated, String mgsReferenceNumber, Integer serverId, Integer mgsPayoutReferenceNumber, Integer payoutAmount, Boolean progressiveWin,
           String progressiveWinDesc, String freeGameOfferName, Integer tournamentId, String description, String extInfo, Long rowIdLong, String betTicketId, Boolean punchTicketStatus) {
           this.loginName = loginName;
           this.userId = userId;
           this.changeAmount = changeAmount;
           this.transactionCurrency = transactionCurrency;
           this.status = status;
           this.rowId = rowId;
           this.transactionNumber = transactionNumber;
           this.gameName = gameName;
           this.dateCreated = dateCreated;
           this.mgsReferenceNumber = mgsReferenceNumber;
           this.serverId = serverId;
           this.mgsPayoutReferenceNumber = mgsPayoutReferenceNumber;
           this.payoutAmount = payoutAmount;
           this.progressiveWin = progressiveWin;
           this.progressiveWinDesc = progressiveWinDesc;
           this.freeGameOfferName = freeGameOfferName;
           this.tournamentId = tournamentId;
           this.description = description;
           this.extInfo = extInfo;
           this.rowIdLong = rowIdLong;
           this.betTicketId = betTicketId;
           this.punchTicketStatus = punchTicketStatus;
    }

    @XmlElement(name="LoginName", namespace=DataStructuresNS.VanguardAdminNS)   
    public java.lang.String getLoginName() {
        return loginName;
    }

    public void setLoginName(java.lang.String loginName) {
        this.loginName = loginName;
    }

    @XmlElement(name="UserId", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getUserId() {
        return userId;
    }

    public void setUserId(java.lang.Integer userId) {
        this.userId = userId;
    }

    @XmlElement(name="ChangeAmount", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(java.lang.Integer changeAmount) {
        this.changeAmount = changeAmount;
    }

    @XmlElement(name="TransactionCurrency", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(java.lang.String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    @XmlElement(name="Status", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    @XmlElement(name="RowId", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getRowId() {
        return rowId;
    }

    public void setRowId(java.lang.Integer rowId) {
        this.rowId = rowId;
    }

    @XmlElement(name="TransactionNumber", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(java.lang.Integer transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    @XmlElement(name="GameName", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getGameName() {
        return gameName;
    }

    public void setGameName(java.lang.String gameName) {
        this.gameName = gameName;
    }

    @XmlElement(name="DateCreated", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.lang.String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @XmlElement(name="MgsReferenceNumber", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getMgsReferenceNumber() {
        return mgsReferenceNumber;
    }

    public void setMgsReferenceNumber(java.lang.String mgsReferenceNumber) {
        this.mgsReferenceNumber = mgsReferenceNumber;
    }

    @XmlElement(name="ServerId", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getServerId() {
        return serverId;
    }

    public void setServerId(java.lang.Integer serverId) {
        this.serverId = serverId;
    }

    @XmlElement(name="MgsPayoutReferenceNumber", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getMgsPayoutReferenceNumber() {
        return mgsPayoutReferenceNumber;
    }

    public void setMgsPayoutReferenceNumber(java.lang.Integer mgsPayoutReferenceNumber) {
        this.mgsPayoutReferenceNumber = mgsPayoutReferenceNumber;
    }

    @XmlElement(name="PayoutAmount", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(java.lang.Integer payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    @XmlElement(name="ProgressiveWin", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Boolean getProgressiveWin() {
        return progressiveWin;
    }

    public void setProgressiveWin(java.lang.Boolean progressiveWin) {
        this.progressiveWin = progressiveWin;
    }

    @XmlElement(name="ProgressiveWinDesc", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getProgressiveWinDesc() {
        return progressiveWinDesc;
    }

    public void setProgressiveWinDesc(java.lang.String progressiveWinDesc) {
        this.progressiveWinDesc = progressiveWinDesc;
    }

    @XmlElement(name="FreeGameOfferName", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getFreeGameOfferName() {
        return freeGameOfferName;
    }

    public void setFreeGameOfferName(java.lang.String freeGameOfferName) {
        this.freeGameOfferName = freeGameOfferName;
    }

    @XmlElement(name="TournamentId", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(java.lang.Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @XmlElement(name="Description", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    @XmlElement(name="ExtInfo", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(java.lang.String extInfo) {
        this.extInfo = extInfo;
    }
    
    @XmlElement(name="RowIdLong", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Long getRowIdLong() {
        return rowIdLong;
    }

    public void setRowIdLong(java.lang.Long rowIdLong) {
        this.rowIdLong = rowIdLong;
    }

    @XmlElement(name="BetTicketId", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.String getBetTicketId() {
        return betTicketId;
    }

    public void setBetTicketId(java.lang.String betTicketId) {
        this.betTicketId = betTicketId;
    }

    @XmlElement(name="PunchTicketStatus", namespace=DataStructuresNS.VanguardAdminNS)
    public java.lang.Boolean getPunchTicketStatus() {
        return punchTicketStatus;
    }

    public void setPunchTicketStatus(java.lang.Boolean punchTicketStatus) {
        this.punchTicketStatus = punchTicketStatus;
    }
}