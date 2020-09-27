package dfh.model.common;

import java.io.Serializable;

public class Response implements Serializable {

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


    public static void main(String[] args) {
        System.out.println(null + "");
    }

    @Override
    public String toString() {
        return "Response [code=" + code + ", desc=" + desc + ", data=" + data + "]";
    }

    public void setResponseEnum(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.desc = responseEnum.getDesc();
    }
}
