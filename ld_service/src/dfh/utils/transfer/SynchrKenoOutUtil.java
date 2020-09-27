package dfh.utils.transfer;

import dfh.service.interfaces.TransferService;

public class SynchrKenoOutUtil {
	
	private static SynchrKenoOutUtil instance = null;
	
	private SynchrKenoOutUtil(){
		
	}
	
	public static SynchrKenoOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrKenoOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforkeno(TransferService transferService ,String seqId , String loginname, Double remit, String remoteIp){
		return transferService.transferOutforkeno(seqId, loginname, remit, remoteIp);
	}
	
}
