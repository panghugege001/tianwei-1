package dfh.skydragon.webservice;

import dfh.model.TokenInfo;
import dfh.service.interfaces.LoginTokenService;

/**
 * Created by mars on 2016/7/4.
 */
public class TokenWebServiceWS {

    private LoginTokenService loginTokenService;

    public void setLoginTokenService(LoginTokenService loginTokenService) {
        this.loginTokenService = loginTokenService;
    }

    public String createLoginToken(String platform, String loginname){
        return loginTokenService.createLoginToken(platform, loginname);
    }

    public TokenInfo getTokenInfo(String token){
        return loginTokenService.getInfo(token);
    }
}
