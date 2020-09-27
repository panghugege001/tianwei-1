package dfh.action.vo.ebetapp;

import net.sf.json.JSONObject;

/**
 * Created by mars on 2016/6/15.
 */
public class EBetAppResponseVo {

    private int status = 0;
    private String userId = "";
    private String loginname = "";
    private int channelId = 0;
    private double money = 0.0;
    private int timestamp = 0;
    private String rechargeReqId = "";

    public static EBetAppResponseVo convertFromJsonString(String result) {
        EBetAppResponseVo vo = new EBetAppResponseVo();
        JSONObject jsonObject = JSONObject.fromObject(result);
        vo.setStatus(jsonObject.getInt("status"));
        if (jsonObject.has("userId")) {
            vo.setUserId(jsonObject.getString("userId"));
        }
        if (jsonObject.has("loginname")) {
            vo.setLoginname(jsonObject.getString("loginname"));
        }
        if (jsonObject.has("channelId")) {
            vo.setChannelId(jsonObject.getInt("channelId"));
        }
        if (jsonObject.has("money")) {
            vo.setMoney(jsonObject.getInt("money"));
        }
        if (jsonObject.has("timestamp")) {
            vo.setTimestamp(jsonObject.getInt("timestamp"));
        }
        if (jsonObject.has("rechargeReqId")) {
            vo.setRechargeReqId(jsonObject.getString("rechargeReqId"));
        }
        return vo;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRechargeReqId() {
        return rechargeReqId;
    }

    public void setRechargeReqId(String rechargeReqId) {
        this.rechargeReqId = rechargeReqId;
    }

    @Override
    public String toString() {
        return "EBetAppResponseVo{" +
                "status=" + status +
                ", userId='" + userId + '\'' +
                ", loginname='" + loginname + '\'' +
                ", channelId=" + channelId +
                ", money=" + money +
                ", timestamp=" + timestamp +
                ", rechargeReqId='" + rechargeReqId + '\'' +
                '}';
    }

}
