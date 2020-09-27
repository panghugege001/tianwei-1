package dfh.service.interfaces;

import java.util.List;

import dfh.model.CmbTransfers;

public interface IBankSecDepositService {

	List<CmbTransfers> getTranferInfos();
	
	public void dealProcess(CmbTransfers cmb) throws Exception;
}
