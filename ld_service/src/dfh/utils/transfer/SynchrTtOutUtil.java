package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrTtOutUtil {
	
	private static SynchrTtOutUtil instance = null;
	
	private SynchrTtOutUtil(){
		
	} 
	
	public static SynchrTtOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrTtOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferTtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferTtAndSelfYouHuiOut(seqId, loginname, remit);
//		}
	}
	
	
	
}
