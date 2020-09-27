package dfh.utils.synch;

import dfh.service.interfaces.ProposalService;

public class SynchrAgentVipUtil {
	
	private static SynchrAgentVipUtil instance = null;
	
	private SynchrAgentVipUtil(){
		
	}
	
	public static SynchrAgentVipUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrAgentVipUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String executeAgentVip(ProposalService proposalService , String operator, String ids , Integer level){
		return proposalService.executeAgentVipService(operator , ids , level);
	}
	
	
}
