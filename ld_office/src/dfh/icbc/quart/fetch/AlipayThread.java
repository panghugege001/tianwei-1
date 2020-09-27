package dfh.icbc.quart.fetch;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.AlipayTransfers;
import dfh.service.interfaces.IGetdateService;

public class AlipayThread extends Thread {
	private static Logger log = Logger.getLogger(AlipayThread.class);
	public IGetdateService getdateService;

	public AlipayThread(IGetdateService getdateService) {
		super();
		this.getdateService = getdateService;
	}

	public void run() {
		//老的处理方式
//		getdateService.updateAlipayStatus();
		
		//新处理方式
		DetachedCriteria dc = DetachedCriteria.forClass(AlipayTransfers.class);
		dc.add(Restrictions.eq("status", 0)) ;
		List<AlipayTransfers> alipays = getAlipays(getdateService);
		if(null != alipays && alipays.size()>0){
			for (AlipayTransfers alipayTransfers : alipays) {
				try {
					getdateService.dealAlipayData(alipayTransfers);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static int flag = 1 ;
	public static Object alipayLock = new Object();
	public static List<AlipayTransfers> getAlipays(IGetdateService getservice){
		synchronized (alipayLock) {
			DetachedCriteria dc = DetachedCriteria.forClass(AlipayTransfers.class);
			dc.add(Restrictions.eq("status", 0)) ;
			dc.addOrder(Order.asc("payDate"));
			List<AlipayTransfers> alipays = getservice.findByCriteria(dc);
			log.info("synchronized query alipay data , size:"+alipays.size());
			return alipays ;
		}
	}

}
