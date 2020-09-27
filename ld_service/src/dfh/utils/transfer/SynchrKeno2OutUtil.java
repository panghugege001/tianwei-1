package dfh.utils.transfer;

import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class SynchrKeno2OutUtil {
	
	private static SynchrKeno2OutUtil instance = null;
	
	private SynchrKeno2OutUtil(){
		
	}
	
	public static SynchrKeno2OutUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrKeno2OutUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String transferOutforkeno2(TransferService transferService ,String seqId , String loginname, Double remit, String remoteIp){
		Users userSyn = transferService.getUsers(loginname);
		if(userSyn==null){
			return "用户不存在!";
		}
//		synchronized(userSyn.getId()){
			return transferService.transferOutforkeno2(seqId, loginname, remit, remoteIp);
//		}
	}
	
}
