package dfh.model.pay;

import java.io.Serializable;

/**
 * Created by wander on 2017/2/21.
 * 附言支付 对象
 */
public class PayFyVo implements Serializable {

    private static final long serialVersionUID = -4111365354229005257L;

    /*** 支付宝账号 */
    private String accountNo;

    /*** 附言code */
    private String vpnPassword;
    private String zfbImgCode;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getVpnPassword() {
        return vpnPassword;
    }

    public void setVpnPassword(String vpnPassword) {
        this.vpnPassword = vpnPassword;
    }

    public void setZfbImgCode(String zfbImgCode) {
        this.zfbImgCode = zfbImgCode;
    }

    public String getZfbImgCode() {
        return zfbImgCode;
    }
}
