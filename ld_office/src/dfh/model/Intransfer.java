package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "intransfer", catalog = "tianwei")
public class Intransfer implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -18353058351471422L;
	private String wherefrom;
	private String whereto;
	private String operator;
	private String remark;
	private double amount = 0.00;
	private Double fee=0.00;
	private Integer flag=0;
	private String pno;
	private Timestamp createTime;
	private String fromto;
	private Integer transferflag=0;
	
	@Id
	@Column(name = "pno", unique = true)
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	
	
	@Column(name = "operator")
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Column(name = "wherefrom")
	public String getWherefrom() {
		return wherefrom;
	}
	public void setWherefrom(String wherefrom) {
		this.wherefrom = wherefrom;
	}
	
	@Column(name = "whereto")
	public String getWhereto() {
		return whereto;
	}
	public void setWhereto(String whereto) {
		this.whereto = whereto;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "amount")
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Column(name = "createTime")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	@Column(name = "fromto")
	public String getFromto() {
		return fromto;
	}
	public void setFromto(String fromto) {
		this.fromto = fromto;
	}
	
	@Column(name = "fee")
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	@Column(name = "transferflag")
	public Integer getTransferflag() {
		return transferflag;
	}
	public void setTransferflag(Integer transferflag) {
		this.transferflag = transferflag;
	}
	
	
	
}
