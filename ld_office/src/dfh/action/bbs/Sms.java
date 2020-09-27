package dfh.action.bbs;

import dfh.service.interfaces.NotifyService;
import dfh.utils.StringUtil;

public class Sms extends Thread{
	
	String phone;
	String password;
	NotifyService notifyService;
	
	public Sms(NotifyService notifyService,String phone,String password){
		super();
		this.notifyService=notifyService;
		this.phone=phone;
		this.password=password;
	}
	public void run(){
		notifyService.sendSms(phone, StringUtil.trim("新密码是："+password));
	}

}
