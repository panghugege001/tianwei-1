package dfh.action.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wander on 2017/3/11.
 */
public class GameVo implements Serializable {

    private static final long serialVersionUID = 6844441067157236620L;


    private String code;
    private Object data;
    private String message;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GameVo [code=" + code + ", data=" + data + ", message=" + message + "]";
	}

  

}
