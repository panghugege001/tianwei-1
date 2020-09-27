package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrQtOutUtil {
	
	private static SynchrQtOutUtil instance = null;
	
	private SynchrQtOutUtil(){
		
	}
	
	public static SynchrQtOutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrQtOutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferQtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferQtAndSelfYouHuiOut(seqId, loginname, remit);
//		}
	}
	
	
	
}
