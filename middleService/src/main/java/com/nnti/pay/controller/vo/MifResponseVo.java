package com.nnti.pay.controller.vo;

/**
 * Created by Addis on 2017/10/12.
 */
public class MifResponseVo {

    private String code;

    private String msg;

    private MifPayVo data;
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public MifPayVo getData() {
		return data;
	}

	public void setData(MifPayVo data) {
		this.data = data;
	}



   
    
}
