package app.model.vo;

import java.util.List;

import dfh.model.HBConfig;

public class TransferHBBDataVO {

	private String hbMoney;
	private List<TransferHBVO> hbSelects;
	private String msg;
	public String getHbMoney() {
		return hbMoney;
	}
	public void setHbMoney(String hbMoney) {
		this.hbMoney = hbMoney;
	}
	public List<TransferHBVO> getHbSelects() {
		return hbSelects;
	}
	public void setHbSelects(List<TransferHBVO> hbSelects) {
		this.hbSelects = hbSelects;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
