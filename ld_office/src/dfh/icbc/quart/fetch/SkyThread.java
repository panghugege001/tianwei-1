package dfh.icbc.quart.fetch;

import java.util.Date;

import dfh.model.Users;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.MatchDateUtil;

public class SkyThread {

	private IGetdateService getdateService;
	private Users ptUser;
	public SkyThread(IGetdateService getdateService, Users ptUser) {
		// TODO Auto-generated constructor stub
		this.getdateService = getdateService;
		this.ptUser = ptUser;
	}
	
	public SkyThread(IGetdateService getdateService) {
		// TODO Auto-generated constructor stub
		this.getdateService = getdateService;
	}

	public void update() {
		//getdateService.processStatusData(ptUser);
		getdateService.processStatusData(MatchDateUtil.now());
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

	public void setPtUser(Users ptUser) {
		this.ptUser = ptUser;
	}
}
