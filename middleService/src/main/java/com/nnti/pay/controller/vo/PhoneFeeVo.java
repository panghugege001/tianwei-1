package com.nnti.pay.controller.vo;

/**
 * 话费活动VO
 */
public class PhoneFeeVo {
	//pay.huidiaolong8.com:6336/asp/phoneFeeCallBack.php?sporder_id=J17052214323147600518870&orderid=20170519121111er9f0&sta=1&sign=f5edc1f59f3b7de3f561df953354162a
    private String orderid;

    private Integer sta;

    private String sporder_id;

    private String sign;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Integer getSta() {
		return sta;
	}

	public void setSta(Integer sta) {
		this.sta = sta;
	}

	public String getSporder_id() {
		return sporder_id;
	}

	public void setSporder_id(String sporder_id) {
		this.sporder_id = sporder_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}


}
