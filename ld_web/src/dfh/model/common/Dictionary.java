package dfh.model.common;

import java.io.Serializable;

/**
 * Created by wander on 2017/2/7.
 */
public class Dictionary implements Serializable {

    private static final long serialVersionUID = -7986516055165267863L;

    private String dictName;// '字典项名称',
    private String dictValue;// '字典项值',
    private String dictShow;// '显示项',
    private String dictDesc;// '字典项描述',

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictShow() {
        return dictShow;
    }

    public void setDictShow(String dictShow) {
        this.dictShow = dictShow;
    }

    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }
}
