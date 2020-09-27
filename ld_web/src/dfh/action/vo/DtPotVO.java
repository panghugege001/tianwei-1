package dfh.action.vo;

import java.io.Serializable;

/**
 * Created by Wander on 2017/7/28.
 */
public class DtPotVO implements Serializable {

    private String code;
    private String pot;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPot() {
        return pot;
    }

    public void setPot(String pot) {
        this.pot = pot;
    }
}
