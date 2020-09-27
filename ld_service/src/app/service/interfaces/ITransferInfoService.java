package app.service.interfaces;

public interface ITransferInfoService {

	String transferLimit(String loginName, Double remit);
	
	String transferPT(String transferId, String loginName, Double remit, Double remoteCredit, String remark);
	
	String transferMG(String transferId, String loginName, Double remit, String remark);
	
	String transferDT(String transferId, String loginName, Double remit, String remark);
	
	String transferQT(String transferId, String loginName, Double remit, String remark);
	
	String transferNT(String transferId, String loginName, Double remit, String remark);
	
	String transferTTG(String transferId, String loginName, Double remit, String remark);
	
	void transferRecordInPT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordOutPT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordInMG(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordOutMG(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordInDT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordOutDT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordInQT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordOutQT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordInNT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordOutNT(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordInTTG(String transferId, String loginName, Double localCredit, Double remit);
	
	void transferRecordOutTTG(String transferId, String loginName, Double localCredit, Double remit);
	
}