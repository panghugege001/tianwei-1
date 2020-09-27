package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 32pay 支付
 */
public class SanErPayVo implements Serializable {

    private static final long serialVersionUID = -5704303451746387318L;
    /*** 商户号*/
    private String P_UserId;
    /*** 订单号*/
    private String P_OrderId;
    
    private String P_CardId;
    
    private String P_CardPass;
    /*** 充值金额*/
    private String P_FaceValue;
    /*** 渠道ID*/
    private String P_ChannelId;
    private String P_Subject;
    /*** 充值金额*/
    private String P_Price;
    /*** 数量*/
    private Integer P_Quantity;
    /*** 描述*/
    private String P_Description;

    private String P_Notic;
    /*** 第三方回掉地址 */
    private String P_Result_URL;
    /*** 商城地址*/
    private String P_Notify_URL;
    /*** 密钥*/
    private String P_PostKey;
    /**自动识别wap*****/
    private String P_IsSmart;
    /***网关api***/
    private  String apiUrl;
    
    /*** 错误码*/
    private String P_ErrCode;
    /*** 错误信息*/
    private String P_ErrMsg;
    
    private String P_PayMoney;
    
    


    public SanErPayVo() {
    }

    /*** 提交接口参数值 */
    public void setWxZfb(String p_UserId, String p_OrderId, String p_FaceValue, String p_Price, Integer p_Quantity,
                         String p_Notic, String p_Result_URL, String p_Notify_URL, String p_PostKey, String apiUrl) {
        this.P_UserId = p_UserId;
        this.P_OrderId = p_OrderId;
        this.P_FaceValue = p_FaceValue;
        this.P_Price = p_Price;
        this.P_Quantity = p_Quantity;
        this.P_Notic = p_Notic;
        this.P_Result_URL = p_Result_URL;
        this.P_Notify_URL = p_Notify_URL;
        this.P_PostKey = p_PostKey;
    }

    public String getP_UserId() {
        return P_UserId;
    }

    public void setP_UserId(String p_UserId) {
        P_UserId = p_UserId;
    }

    public String getP_OrderId() {
        return P_OrderId;
    }

    public void setP_OrderId(String p_OrderId) {
        P_OrderId = p_OrderId;
    }

    public String getP_FaceValue() {
        return P_FaceValue;
    }

    public void setP_FaceValue(String p_FaceValue) {
        P_FaceValue = p_FaceValue;
    }

    public String getP_ChannelId() {
        return P_ChannelId;
    }

    public void setP_ChannelId(String p_ChannelId) {
        P_ChannelId = p_ChannelId;
    }

    public String getP_Price() {
        return P_Price;
    }

    public void setP_Price(String p_Price) {
        P_Price = p_Price;
    }

    public Integer getP_Quantity() {
        return P_Quantity;
    }

    public void setP_Quantity(Integer p_Quantity) {
        P_Quantity = p_Quantity;
    }

    public String getP_Description() {
        return P_Description;
    }

    public void setP_Description(String p_Description) {
        P_Description = p_Description;
    }

    public String getP_Notic() {
        return P_Notic;
    }

    public void setP_Notic(String p_Notic) {
        P_Notic = p_Notic;
    }

    public String getP_Result_URL() {
        return P_Result_URL;
    }

    public void setP_Result_URL(String p_Result_URL) {
        P_Result_URL = p_Result_URL;
    }

    public String getP_Notify_URL() {
        return P_Notify_URL;
    }

    public void setP_Notify_URL(String p_Notify_URL) {
        P_Notify_URL = p_Notify_URL;
    }

    public String getP_PostKey() {
        return P_PostKey;
    }

    public void setP_PostKey(String p_PostKey) {
        P_PostKey = p_PostKey;
    }


    public String getP_CardId() {
        return P_CardId;
    }

    public void setP_CardId(String p_CardId) {
        P_CardId = p_CardId;
    }

    public String getP_CardPass() {
        return P_CardPass;
    }

    public void setP_CardPass(String p_CardPass) {
        P_CardPass = p_CardPass;
    }

	public String getP_Subject() {
		return P_Subject;
	}

	public void setP_Subject(String p_Subject) {
		P_Subject = p_Subject;
	}

	public String getP_IsSmart() {
		return P_IsSmart;
	}

	public void setP_IsSmart(String p_IsSmart) {
		P_IsSmart = p_IsSmart;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getP_ErrCode() {
		return P_ErrCode;
	}

	public void setP_ErrCode(String p_ErrCode) {
		P_ErrCode = p_ErrCode;
	}

	public String getP_ErrMsg() {
		return P_ErrMsg;
	}

	public void setP_ErrMsg(String p_ErrMsg) {
		P_ErrMsg = p_ErrMsg;
	}

	public String getP_PayMoney() {
		return P_PayMoney;
	}

	public void setP_PayMoney(String p_PayMoney) {
		P_PayMoney = p_PayMoney;
	}
    
    

}
