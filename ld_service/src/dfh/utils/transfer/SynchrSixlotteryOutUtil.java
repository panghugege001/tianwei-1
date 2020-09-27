package dfh.utils.transfer;

import dfh.service.interfaces.TransferService;

public class SynchrSixlotteryOutUtil {
	
	private static SynchrSixlotteryOutUtil instance = null;
	
	private SynchrSixlotteryOutUtil(){
		
	}
	
	public static SynchrSixlotteryOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrSixlotteryOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforSixLottery(TransferService transferService , String seqId, String loginname, Double remit, String remoteIp){
		return transferService.transferOutforSixLottery(seqId, loginname, remit,remoteIp);
	}
	
	
	
}
