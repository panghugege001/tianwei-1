package dfh.service.interfaces;

import dfh.model.TokenInfo;

public interface LoginTokenService  extends UniversalService {
	
	public String createLoginToken(String platformCode, String loginname); 
	boolean isValidToken(String platform, String token, String loginname);
	public TokenInfo getInfo(String token);
	public TokenInfo proccessTokenInfoWithValid(String platformCode, String loginname);
}
