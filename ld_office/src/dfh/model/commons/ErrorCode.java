package dfh.model.commons;

public enum ErrorCode {

    /**
     * 功能对应编码(10000为成功编码/20000为自助编码/30000为支付编码)
     */
    SC_10000("10000", "成功"),
    SC_10001("10001", "系统异常"),
    SC_20000_101("20000_101", "自助存送"),
    SC_20000_102("20000_102", "自助洗码"),

    SC_30000_101("30000_101", "代理不能在线支付!"),
    SC_30000_102("30000_102", "在线支付正在维护!"),
    SC_30000_103("30000_103", "此通道维护升级，请您使用其它支付通道!"),
    SC_30000_104("30000_104", "订单金额不能为空!"),
    SC_30000_105("30000_105", "【最小】充值金额为：%s元!"),
    SC_30000_106("30000_106", "【最大】充值金额为：%s元!"),
    SC_30000_107("30000_107", "用户名：%s不存在!"),
    SC_30000_108("30000_108", "%s，订单用户(%s)和实际用户(%s)不一致!"),
    SC_30000_109("30000_109", "额度不足,当前额度为%s!"),
    SC_30000_110("30000_110", "单号[%s]已经支付过了!"),
    SC_30000_112("30000_112", "新玩家需满%s天 才能支付!"),
    SC_30000_113("30000_113", "不是整数!"),
    SC_30000_114("30000_114", "签名验证失败!"),
    SC_30000_111("30000_111", "支付失败!");

    /**
     * 异常对应编码(10000为成功编码/20000为自助编码/30000为支付编码)
     */

    // 错误编码
    private String code;
    // 错误类型
    private String type;

    private ErrorCode(String code, String type) {

        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}