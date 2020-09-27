package dfh.model.notdb;

import java.io.Serializable;
import java.util.Date;

import dfh.utils.Arith;

public class MGTransferLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;
	private String type;
	private Integer amount;
	private Date transferTime;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getType() {
		return type.equalsIgnoreCase("in")?"转入":"转出";
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAmount() {
		return Arith.div(amount.doubleValue(), 100);
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	
	/*@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		MGTransferLog log = new MGTransferLog();
		log.setLoginName(rs.getString("loginname"));
		log.setType(rs.getString("type"));
		log.setAmount(rs.getInt("amount"));
		log.setTransferTime(rs.getDate("transferTime"));
		return log;
	}*/
}
	
	
