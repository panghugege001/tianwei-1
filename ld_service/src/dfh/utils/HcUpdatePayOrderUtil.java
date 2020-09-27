package dfh.utils;

import dfh.service.interfaces.AnnouncementService;

public class HcUpdatePayOrderUtil {
	
	private static HcUpdatePayOrderUtil instance = null;
	
	private HcUpdatePayOrderUtil(){
		
	}
	
	public static HcUpdatePayOrderUtil getInstance() {
		if (instance == null) {
			return instance = new HcUpdatePayOrderUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String execute(AnnouncementService announcementService ,String orderNo, Double OrdAmt, String type){
		return announcementService.updatePayorderHC(orderNo, OrdAmt, type) ;
	}
	
}
