package dfh.model.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wander on 2017/3/11.
 */
public class ReturnVo implements Serializable {

    private static final long serialVersionUID = 6844441067157236620L;

    /*** 1 页面跳转 2.二维码code 3.图片链接 4.成功页面 */
    private String type;
    private String url;
    private String data;
    private Map params = new HashMap();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
}
