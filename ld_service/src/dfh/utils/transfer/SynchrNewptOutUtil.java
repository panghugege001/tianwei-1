package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrNewptOutUtil {
	
	private static SynchrNewptOutUtil instance = null;
	
	private SynchrNewptOutUtil(){
		
	}
	
	public static SynchrNewptOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrNewptOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferPtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferPtAndSelfYouHuiOut(seqId, loginname, remit);
//		}
	}
	
}
