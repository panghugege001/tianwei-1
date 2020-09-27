package com.nnti.pay.model.vo;

import java.util.Date;

public class PhoneFeeRecord {

	private String order_no;
	private String loginname;
	private Integer phone_fee_config_id;//话费充值配置表id
	private String phone_fee_config_title;//话费充值配置表title
	private String phone_number;
	private Integer amount;
	private Date create_time;
	private Date modify_time;
	private Integer status;//状态：0-领取 1-使用 2-充值成功 3-充值失败 4-失效 5-补单
	private String sporder_id;//商户订单号
	private Date callback_time;//回调时间
	private String order_no_ref;//补单原单号
	private String operator;//补单人或者手动变充成功人
	private String remark;
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getPhone_fee_config_id() {
		return phone_fee_config_id;
	}
	public void setPhone_fee_config_id(Integer phone_fee_config_id) {
		this.phone_fee_config_id = phone_fee_config_id;
	}
	public String getPhone_fee_config_title() {
		return phone_fee_config_title;
	}
	public void setPhone_fee_config_title(String phone_fee_config_title) {
		this.phone_fee_config_title = phone_fee_config_title;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSporder_id() {
		return sporder_id;
	}
	public void setSporder_id(String sporder_id) {
		this.sporder_id = sporder_id;
	}
	public Date getCallback_time() {
		return callback_time;
	}
	public void setCallback_time(Date callback_time) {
		this.callback_time = callback_time;
	}
	public String getOrder_no_ref() {
		return order_no_ref;
	}
	public void setOrder_no_ref(String order_no_ref) {
		this.order_no_ref = order_no_ref;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}