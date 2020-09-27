package app.service.interfaces;

public interface ILogService {

	void addCreditLog(String loginName, String type, Double credit, Double remit, Double newCredit, String remark);
	
}