package app.service.interfaces;

import dfh.model.Userstatus;

public interface IUserStatusService {

	Userstatus queryUserStatus(String loginName);
	
}