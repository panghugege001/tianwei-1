package com.nnti.common.constants;

public enum FunctionCode {
	
	/**
     * 功能对应编码(10000为系统模块编码/20000为自助模块编码/30000为支付模块编码)
     */
	SC_10000("10000", "操作成功"),
	SC_10001("10001", "系统异常"),
	
	/**自助模块编码**/
    SC_20000_101("20000_101", "自助存送"),
    SC_20000_102("20000_102", "自助洗码"),
    SC_20000_103("20000_103", "自助体验金"),
    SC_20000_104("20000_104", "救援金"),
    SC_20000_105("20000_105", "优惠券"),
    SC_20000_106("20000_106", "自助晋级"),
    SC_20000_107("20000_107", "大爆炸"),
    SC_20000_108("20000_108", "签到"),
    SC_20000_109("20000_109", "积分中心"),
    SC_20000_110("20000_110", "好友分享"),
    SC_20000_111("20000_111", "站内信"),
    SC_20000_112("20000_112", "好友分享"),
	SC_20000_113("20000_113", "提款"),
	SC_20000_114("20000_114", "户内转账");
	
	// 编码
    private String code;
    // 类型
    private String type;
    
    private FunctionCode(String code, String type) {

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