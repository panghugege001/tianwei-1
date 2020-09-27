package dfh.service.implementations;

import dfh.dao.BankStatusDao;
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

}
