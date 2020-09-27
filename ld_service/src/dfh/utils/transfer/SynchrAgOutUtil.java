package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrAgOutUtil {
	
	private static SynchrAgOutUtil instance = null;
	
	private SynchrAgOutUtil(){
		
	}
	
	public static SynchrAgOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrAgOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforDsp(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferOutforDsp(seqId, loginname, remit);
//		}
	}

}
