package dfh.action.vo;

import dfh.utils.EBetAppRsaUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class EBetRegisterOrLoginReq {

    private String cmd = "";
    private int eventType = 0;
    private int channelId = 0;
    private String username = "";
    private String password = "";
    private String signature = "";
    private int timestamp = 0;
    private String ip = "";
    private String accessToken = "";

    public boolean isValidRequest() throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        if (getSignature() == null && getSignature().isEmpty() && getTimestamp() == 0 && getChannelId() == 0) {
            return false;
        }
        // event type 4 using timestamp + accessToken
        if (getEventType() == 4) {
            return getAccessToken() != null
                    && !getAccessToken().isEmpty();
        }
        // eventType =3 的AccessToken 有可能为空 , signature 会经过加密 , 需解密
        if (getEventType() == 4 || getEventType() == 3) {
            return EBetAppRsaUtil.sign(getTimestamp() + getAccessToken()).equals(getSignature());
        }
        // 其他的都要userName
        if (getUsername() == null && getUsername().isEmpty()) {
            return false;
        }
        if ("UserInfo".equals(getCmd())) {
            return EBetAppRsaUtil.sign(getUsername() + getChannelId() + getTimestamp()).equals(getSignature());
        }
        // event type 1 or whatever ,  using user name + timestamp
        return EBetAppRsaUtil.sign(getUsername() + getTimestamp()).equals(getSignature());
    }

    public boolean isUserInfo() {
        return !getCmd().isEmpty()
                && "UserInfo".equalsIgnoreCase(getCmd());
    }

    public boolean isRegisterOrLoginReq() {
        return !getCmd().isEmpty()
                && "RegisterOrLoginReq".equalsIgnoreCase(getCmd());
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        if (cmd != null) {
            this.cmd = cmd;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDecodeToken() throws UnsupportedEncodingException {
        return URLDecoder.decode(accessToken, "UTF-8");
    }

}
