package dfh.service.implementations;

import dfh.service.interfaces.TransferService;

public class SignamountJobService {
	private TransferService transferService;
	public void execute(){
		System.out.println("月底清出签到金额开始...."+"，时间："+ System.currentTimeMillis());
		transferService.updateSignamount();
		System.out.println("月底清出签到金额结束...."+"，时间："+ System.currentTimeMillis());
	}
	
	
	
	
	
	
	public TransferService getTransferService() {
		return transferService;
	}
	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}
}
