package dfh.action.bbs;

import dfh.service.interfaces.NotifyService;
import dfh.utils.StringUtil;

public class Email extends Thread{
	
	String email;
	String password;
	NotifyService notifyService;
	
	public Email(NotifyService notifyService,String email,String password){
		super();
		this.notifyService=notifyService;
		this.email=email;
		this.password=password;
	}
	public void run(){
		notifyService.sendEmail(email,"密码更新：", StringUtil.trim("新密码是："+password));
	}

}
