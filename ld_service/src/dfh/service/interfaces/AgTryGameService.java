package dfh.service.interfaces;

import dfh.model.AgTryGame;

public interface AgTryGameService {
	
	public AgTryGame agPhoneVerification(AgTryGame agTryGame);
	
	public AgTryGame agSave(AgTryGame agTryGame);
	
	public AgTryGame agLogin(AgTryGame agTryGame,String ip);
	
	public Integer getIpCount(AgTryGame agTryGame);
	
}
