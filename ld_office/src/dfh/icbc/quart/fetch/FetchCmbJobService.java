package dfh.icbc.quart.fetch;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.model.CmbTransfers;
import dfh.service.interfaces.IBankSecDepositService;
import dfh.service.interfaces.IGetdateService;

public class FetchCmbJobService {
	
	private static Logger log = Logger.getLogger(FetchCmbJobService.class);
	
	private IGetdateService getdateService;
	
	private IBankSecDepositService bankSecDepositService;
	
	public synchronized  void processStatusData() {
		getdateService.updateoperatorNew("CMB");
		//设置定时器销毁
		discardOrder();
	}
	
	//处理秒存 @Modify by Erick
	public static Object depLock = new Object();
	public void dealBankSecDeposit(){
		List<CmbTransfers> cmbList = null;  
		
		try {
			synchronized(depLock){
				cmbList = bankSecDepositService.getTranferInfos();
				
				if(null != cmbList && cmbList.size()>0){
					log.info("本次任务批量处理"+cmbList.size()+"个单子");
					for (CmbTransfers cmbTransfers : cmbList) {
						try {
							bankSecDepositService.dealProcess(cmbTransfers);
						} catch (Exception e) {
							e.printStackTrace();
							log.error("匹配异常:"+cmbTransfers.getTransfeId()+"|"+cmbTransfers.getUaccountname()+"|"+cmbTransfers.getAmount());
						}
					}
				}
			}
			
			discardOrder();
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>新秒存定时器Exception：", e);
		} catch (Error e) {
			log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>新秒存定时器Error：", e);
		}
		
	}
	
	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
	
	public void discardOrder(){
		getdateService.discardOrderMc();
	}
	
	public IBankSecDepositService getBankSecDepositService() {
		return bankSecDepositService;
	}

	public void setBankSecDepositService(IBankSecDepositService bankSecDepositService) {
		this.bankSecDepositService = bankSecDepositService;
	}
}
