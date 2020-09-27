package dfh.action.vo.builder;

import dfh.action.vo.EBetRegisterOrLoginResp;

/**
 * Created by mars on 2016/6/7.
 */
public final class EBetRegisterOrLoginRespBuilder {
    private int status = 0;
    private int subChannelId = 0;
    private String username = "";
    private String accessToken = "";

    private EBetRegisterOrLoginRespBuilder() {
    }

    public static EBetRegisterOrLoginRespBuilder anResp() {
        return new EBetRegisterOrLoginRespBuilder();
    }

    public EBetRegisterOrLoginRespBuilder withStatus(int status) {
        this.status = status;
        return this;
    }

    public EBetRegisterOrLoginRespBuilder withSubChannelId(int subChannelId) {
        this.subChannelId = subChannelId;
        return this;
    }

    public EBetRegisterOrLoginRespBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public EBetRegisterOrLoginRespBuilder withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public EBetRegisterOrLoginResp build() {
        EBetRegisterOrLoginResp eBetRegisterOrLoginResp = new EBetRegisterOrLoginResp();
        eBetRegisterOrLoginResp.setStatus(status);
        eBetRegisterOrLoginResp.setSubChannelId(subChannelId);
        eBetRegisterOrLoginResp.setUsername(username);
        eBetRegisterOrLoginResp.setAccessToken(accessToken);
        return eBetRegisterOrLoginResp;
    }


}
