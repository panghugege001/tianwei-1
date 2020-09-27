package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="change_line_user"
    ,catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class ChangeLineUser implements java.io.Serializable {
	
	private Date changeLineTime;
	
	public Integer id;
	
	public String userName;
	
	public String codeAfter;
	
	
	public String codeBefore;
	
	
	private Date  createTime;
	
	
	private Double deposit;
	
	private Double  winorlose;

	   public ChangeLineUser() {
	    }

	
	public ChangeLineUser(Date changeLineTime, String userName, String codeAfter, String codeBefore, Date createTime,
			Double deposit, Double winorlose) {
		this.changeLineTime = changeLineTime;
		this.userName = userName;
		this.codeAfter = codeAfter;
		this.codeBefore = codeBefore;
		this.createTime = createTime;
		this.deposit = deposit;
		this.winorlose = winorlose;
	}

	@javax.persistence.Column(name = "changelinetime")
	public Date getChangeLineTime() {
		return changeLineTime;
	}

	public void setChangeLineTime(Date changeLineTime) {
		this.changeLineTime = changeLineTime;
	}

	@javax.persistence.Column(name = "username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



	@javax.persistence.Column(name = "codeafter")
	public String getCodeAfter() {
		return codeAfter;
	}


	public void setCodeAfter(String codeAfter) {
		this.codeAfter = codeAfter;
	}


	@javax.persistence.Column(name = "codebefore")
	public String getCodeBefore() {
		return codeBefore;
	}

	public void setCodeBefore(String codeBefore) {
		this.codeBefore = codeBefore;
	}

	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	
	public Double getWinorlose() {
		return winorlose;
	}

	public void setWinorlose(Double winorlose) {
		this.winorlose = winorlose;
	}

    @Id 
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
