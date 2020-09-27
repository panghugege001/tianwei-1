package dfh.utils;

import dfh.service.interfaces.AnnouncementService;

public class HfAddPayOrderUtil {
	
	private static HfAddPayOrderUtil instance = null;
	
	private HfAddPayOrderUtil(){
		
	}
	
	public static HfAddPayOrderUtil getInstance() {
		if (instance == null) {
			return instance = new HfAddPayOrderUtil();
		} else {
			return instance;
		}
	}
	
	/**
	 * 
	 * @param announcementService
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param msg
	 * @param type  区分第三方支付回调与系统补单  0：第三方支付回调   1：系统补单
	 * @return
	 */
	public synchronized String execute(AnnouncementService announcementService ,String orderNo, Double OrdAmt, String loginname, String msg, String type){
		return announcementService.addPayorderHf(orderNo, OrdAmt, loginname, msg, type) ;
	}
	
}
