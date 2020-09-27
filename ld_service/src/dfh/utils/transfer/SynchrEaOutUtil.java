package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrEaOutUtil {
	
	private static SynchrEaOutUtil instance = null;
	
	private SynchrEaOutUtil(){
		
	}
	
	public static SynchrEaOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrEaOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOut(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferOut(seqId, loginname, remit);
//		}
	}
	
	
	
}
