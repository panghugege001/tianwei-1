package dfh.icbc.quart.fetch;

import org.apache.log4j.Logger;

import dfh.service.interfaces.NotifyService;
import dfh.utils.StringUtil;

public class SendPhoneThread extends Thread {
	
	private static Logger log = Logger.getLogger(SendPhoneThread.class);

	private NotifyService notifyService ;
	
	private String content ;
	private String phone ; 
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public SendPhoneThread(NotifyService notifyService , String content , String phone) {
		super();
		this.notifyService = notifyService;
		this.content = content ; 
		this.phone = phone ; 
	}

	@Override
	public void run() {
		notifyService.sendSmsNew(phone.trim(), content);
	}

	
}
