package dfh.service.interfaces;

import java.util.List;

import dfh.model.BankStatus;

public interface BankStatusService {
	
	public String getBankStatus(String bankname);
	
	public List<BankStatus> getBankStatusList();
	
	public void updateBankStatus(Integer id,String status);
	
}
