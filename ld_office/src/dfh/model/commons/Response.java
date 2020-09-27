package dfh.model.commons;

import java.io.Serializable;

/**
 * common response object
 *
 * @author wander
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 5801847377464255792L;

    private String code;
    private String desc;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setResponseEnum(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getType();
    }

    public void setResponseEnum(ErrorCode errorCode, Exception e) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getType();
        if (e.getMessage() != null) {
            this.desc = this.desc + "," + e.getMessage();
        }
    }
}
