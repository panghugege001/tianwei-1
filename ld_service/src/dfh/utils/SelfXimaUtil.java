package dfh.utils;

import java.util.Date;

import dfh.model.Users;
import dfh.service.interfaces.IGGameinfoService;

public class SelfXimaUtil {
	
	public synchronized String execute(IGGameinfoService gameinfoService ,Users user, Date endTime, Date startTime , String platform) throws Exception{
		return gameinfoService.execXima(user, endTime, startTime ,platform);
	}

}
