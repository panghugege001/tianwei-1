package dfh.utils.synch;

import dfh.service.interfaces.ProposalService;

public class SynchrPtCommissionsUtil {
	
	private static SynchrPtCommissionsUtil instance = null;
	
	private SynchrPtCommissionsUtil(){
		
	}
	
	public static SynchrPtCommissionsUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrPtCommissionsUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String executePtCommission(ProposalService proposalService , String operator, String ids){
		return proposalService.executePtCommissionsService(operator , ids);
	}
	
	
}
