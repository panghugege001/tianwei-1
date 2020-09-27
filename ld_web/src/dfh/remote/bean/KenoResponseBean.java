package dfh.remote.bean;

public class KenoResponseBean {
	private String name;
	private String value;
	private Double amount;
	private Integer FundIntegrationId;
	
	public KenoResponseBean(String name, String value,Double amount) {
		super();
		this.name = name;
		this.value = value;
		this.amount = amount;
	}
	
	public KenoResponseBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if(value.equals("NOT ENOUGH CREDIT TO FUND OUT")){
			this.value="KENO额度不足";
		}else if(value.equals("PLAYER NOT FOUND")){
			this.value="用户不存在";
		}else {
			this.value = value;
		}
		
	}
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getFundIntegrationId() {
		return FundIntegrationId;
	}

	public void setFundIntegrationId(Integer fundIntegrationId) {
		FundIntegrationId = fundIntegrationId;
	}

	@Override
	public String toString() {
		return "KenoResponseBean [value=" + value + ", name=" + name+ ",amount="+amount+",FundIntegrationId="+FundIntegrationId+"]";
	}
	
}
