package dfh.utils.transfer;

import dfh.service.interfaces.TransferService;

public class SynchrSportOutUtil {
	
	private static SynchrSportOutUtil instance = null;
	
	private SynchrSportOutUtil(){
		
	}
	
	public static SynchrSportOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrSportOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforTy(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutforTy(seqId, loginname, remit);
	}
	
	
	
}
