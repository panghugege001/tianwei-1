package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrAginOutUtil {
	
	private static SynchrAginOutUtil instance = null;
	
	private SynchrAginOutUtil(){
		
	}
	
	public static SynchrAginOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrAginOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforAginDsp(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferOutforAginDsp(seqId, loginname, remit);
//		}
	}

}
