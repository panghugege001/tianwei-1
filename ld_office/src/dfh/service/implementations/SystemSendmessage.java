package dfh.service.implementations;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.Proposal;
import dfh.model.Users;
import dfh.service.interfaces.NotifyService;
import dfh.utils.DateUtil;

public class SystemSendmessage extends AbstractGameinfoServiceImpl{
	
	private Logger log=Logger.getLogger(SystemSendmessage.class);
	private NotifyService notifyService;

	@Override
	public void run() {
		try {
			sendMessage();
			this.destory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.setErrorMsg(e.getMessage(),true);
		}
		
	}
	
	@Override
	public void sendMessage() throws Exception{
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc.add(Restrictions.ge("createTime",calendar.getTime() ));
		dc.add(Restrictions.eq("type", 507));
		dc.add(Restrictions.eq("flag", 2));
		List<Proposal> proposals=this.getTaskDao().findByCriteria(dc);
		for (Proposal proposal : proposals) {
			Users user=(Users)this.getTaskDao().get(Users.class, proposal.getLoginname());
			String service=user.getAddress();
			if(null==service){
				service="";
			}
			if(service.indexOf("7")!= -1){//发短信
				if(null != user.getPhone()){
					//notifyService.sendSms(user.getPhone(), "e路发客户"+proposal.getLoginname()+",你昨天的反水"+proposal.getAmount()+"元已经发放完毕,请查收,现帐号余额为"+user.getCredit()+"元,祝多多盈利");
					Thread.sleep(10000); 
				}
				
			}
			if(service.indexOf("8")!= -1){//发邮件
				if(null != user.getEmail()){
					//notifyService.sendEmail(user.getEmail(), "昨日反水已到账,请查收","e路发客户"+proposal.getLoginname()+",你昨天的反水+"+proposal.getAmount()+"元已经发放完毕,请查收,现帐号余额为"+user.getCredit()+"元,祝多多盈利");
					Thread.sleep(10000);
				}
				
			}
			
		}
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	
	

}
