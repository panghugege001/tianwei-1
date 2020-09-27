package dfh.icbc.quart.fetch;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.AlipayTransfers;
import dfh.model.DepositWechat;
import dfh.service.interfaces.IGetdateService;

public class DepositWechatThread extends Thread {
	private static Logger log = Logger.getLogger(DepositWechatThread.class);
	public IGetdateService getdateService;

	public DepositWechatThread(IGetdateService getdateService) {
		super();
		this.getdateService = getdateService;
	}

	public void run() {
		//老的处理方式
//		getdateService.updateAlipayStatus();
		
		//新处理方式
		DetachedCriteria dc = DetachedCriteria.forClass(DepositWechat.class);
		dc.add(Restrictions.eq("state", 0)) ;
		List<DepositWechat> depositWechats = getDepositWechats(getdateService);
		if(null != depositWechats && depositWechats.size()>0){
			for (DepositWechat depositWechat : depositWechats) {
				try {
					getdateService.processWeixinDeposit(depositWechat);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static int flag = 1 ;
	public static Object depositWechatLock = new Object();
	public static List<DepositWechat> getDepositWechats(IGetdateService getservice){
		synchronized (depositWechatLock) {
			DetachedCriteria dc = DetachedCriteria.forClass(DepositWechat.class);
			dc.add(Restrictions.eq("state", 0)) ;
			dc.addOrder(Order.asc("paytime"));
			List<DepositWechat> depositWechat = getservice.findByCriteria(dc);
			log.info("synchronized query depositWechat data , size:"+depositWechat.size());
			return depositWechat ;
		}
	}

}
