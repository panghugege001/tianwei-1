package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrSBOutUtil {
	
	private static SynchrSBOutUtil instance = null;
	
	private SynchrSBOutUtil(){
		
	}
	
	public static SynchrSBOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrSBOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutSB(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferOutforSB(seqId, loginname, remit);
//		}
	}
	
	
	
}
