package dfh.utils;

import dfh.service.interfaces.AnnouncementService;

public class BfbUpdatePayOrderUtil {
	
	private static BfbUpdatePayOrderUtil instance = null;
	
	private BfbUpdatePayOrderUtil(){
		
	}
	
	public static BfbUpdatePayOrderUtil getInstance() {
		if (instance == null) {
			return instance = new BfbUpdatePayOrderUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String execute(AnnouncementService announcementService ,String orderNo, Double OrdAmt, String type){
		return announcementService.updatePayorderBfb(orderNo, OrdAmt, type) ;
	}
	
}
