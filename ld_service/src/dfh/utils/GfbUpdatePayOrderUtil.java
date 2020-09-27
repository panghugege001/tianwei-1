package dfh.utils;

import dfh.service.interfaces.AnnouncementService;

public class GfbUpdatePayOrderUtil {
	
	private static GfbUpdatePayOrderUtil instance = null;
	
	private GfbUpdatePayOrderUtil(){
		
	}
	
	public static GfbUpdatePayOrderUtil getInstance() {
		if (instance == null) {
			return instance = new GfbUpdatePayOrderUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String execute(AnnouncementService announcementService ,String orderNo, Double OrdAmt, String code, String type){
		return announcementService.updatePayorderGfb(orderNo, OrdAmt, code, type);
	}
	
}
