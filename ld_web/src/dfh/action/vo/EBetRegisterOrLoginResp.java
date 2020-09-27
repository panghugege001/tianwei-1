package dfh.action.vo;

public class EBetRegisterOrLoginResp {

    private int status = 0;
    private int subChannelId = 0;
    private String username = "";
    private String accessToken = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(int subChannelId) {
        this.subChannelId = subChannelId;
    }

}
