package dfh.service.interfaces;

import dfh.model.Users;

public interface ISynMemberInfoService {
	
	public Users getUserObject(String loginname)throws Exception;
	
	public int synMemberInfo(String loginname,String nickname)throws Exception;

}
