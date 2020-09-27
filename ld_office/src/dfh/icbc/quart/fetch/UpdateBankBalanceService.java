package dfh.icbc.quart.fetch;


import dfh.service.interfaces.IBankinfoService;

public class UpdateBankBalanceService {

	private IBankinfoService bankinfoService;

	
	public IBankinfoService getBankinfoService() {
		return bankinfoService;
	}


	public void setBankinfoService(IBankinfoService bankinfoService) {
		this.bankinfoService = bankinfoService;
	}
	
	public void execute(){
		System.out.println("更新远程银行账户额度开始...."+"，时间："+ System.currentTimeMillis());
		bankinfoService.updateBankBalance();
		System.out.println("更新远程银行账户额度结束...."+"，时间："+ System.currentTimeMillis());
		
		System.out.println("更新同略云远程银行账户额度开始...."+"，时间："+ System.currentTimeMillis());
		bankinfoService.updateTlyBankBalance();
		System.out.println("更新同略云远程银行账户额度结束...."+"，时间："+ System.currentTimeMillis());
		
	}
	
	

}
