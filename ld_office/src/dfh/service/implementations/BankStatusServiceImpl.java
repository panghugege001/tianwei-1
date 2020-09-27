package dfh.service.implementations;

import java.util.List;

import dfh.dao.BankStatusDao;
import dfh.model.BankStatus;
import dfh.service.interfaces.BankStatusService;

public class BankStatusServiceImpl implements BankStatusService {
	
	private BankStatusDao bankStatusDao;
	
	public BankStatusDao getBankStatusDao() {
		return bankStatusDao;
	}

	public void setBankStatusDao(BankStatusDao bankStatusDao) {
		this.bankStatusDao = bankStatusDao;
	}

	@Override
	public String getBankStatus(String bankname) {
		return bankStatusDao.getBankStatus(bankname);
	}

	@Override
	public List<BankStatus> getBankStatusList() {
		return bankStatusDao.getBankStatusList();
	}

	@Override
	public void updateBankStatus(Integer id, String status) {
		bankStatusDao.updateBankStatus(id, status);
	}

}
