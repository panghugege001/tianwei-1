package dfh.utils.transfer;

import dfh.service.interfaces.TransferService;

public class SynchrBbinOutUtil {
	
	private static SynchrBbinOutUtil instance = null;
	
	private SynchrBbinOutUtil(){
		
	}
	
	public static SynchrBbinOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrBbinOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforBbin(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutforBbin(seqId, loginname, remit);
	}
	
	
	
}
